package com.varsql.core.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.configuration.Configuration;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.utils.StringUtils;

/**
 * ConnectionInfo Manager - Web-safe (non-blocking) - Serial execution -
 * Reload-safe - Graceful shutdown
 */
public final class ConnectionInfoManager {

    private final int TIMEOUT;
    private final Logger logger = LoggerFactory.getLogger(ConnectionInfoManager.class);

    /** 캐시 */
    private final ConcurrentHashMap<String, ConnectionInfo> connectionConfig = new ConcurrentHashMap<>();

    /** connid별 lock 객체 */
    private final ConcurrentHashMap<String, Object> poolLocks = new ConcurrentHashMap<>();

    /** 순차 실행용 executor (shutdown 용도) */
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("connection-info-executor");
        t.setDaemon(false);
        return t;
    });

    private static final AtomicBoolean shutdownHookAdded = new AtomicBoolean(false);

    private ConnectionInfoDao connectionInfoDao;

    private ConnectionInfoManager() {
        init();
        addShutdownHook();
        int dbNetworkTimeout = Configuration.getInstance().getDbNetworkTimeout();
        TIMEOUT = dbNetworkTimeout > 0 ? dbNetworkTimeout : 60;
    }

    /** connid별 lock 생성/조회 */
    private Object getLock(String connid) {
        return poolLocks.computeIfAbsent(connid, k -> new Object());
    }

    /*
     * =========================================================
     * Public API
     * =========================================================
     */
    public ConnectionInfo getConnectionInfo(String connid) throws ConnectionFactoryException {
        return getConnectionInfo(connid, false);
    }

    public ConnectionInfo getConnectionInfo(String connid, boolean reloadFlag) throws ConnectionFactoryException {
        synchronized (getLock(connid)) {
            if (reloadFlag) {
                connectionConfig.remove(connid);
            }

            ConnectionInfo cached = connectionConfig.get(connid);
            if (cached != null) {
                return cached;
            }

            return loadConnectionInfo(connid);
        }
    }

    /*
     * =========================================================
     * Internal Logic
     * =========================================================
     */
    private ConnectionInfo loadConnectionInfo(String connid) {
        try {
            ConnectionInfo connInfo = connectionInfoDao.getConnectionInfo(connid);

            if (connInfo == null) {
                logger.error("connection info not found : {}", connid);
                throw new ConnectionFactoryException("connection info not found : " + connid);
            }

            DataSource dataSource = JdbcUtils.getDataSource(connInfo);

            try (Connection conn = dataSource.getConnection()) {

                JdbcUtils.setNetworkTimeout(conn, connInfo.getType(), TIMEOUT - 5);

                // catalog
                try {
                    connInfo.setDatabaseName(conn.getCatalog());
                } catch (SQLException e) {
                    logger.warn("getCatalog error: {}", e.getMessage());
                }

                // schema
                String schema = "";
                try {
                    schema = conn.getSchema();
                    if (StringUtils.isBlank(schema)) {
                        schema = conn.getMetaData().getUserName();
                    }
                } catch (SQLException e) {
                    try {
                        schema = conn.getMetaData().getUserName();
                    } catch (SQLException ex) {
                        logger.warn("getUserName error: {}", ex.getMessage());
                    }
                }

                connInfo.setSchema(schema);

                if (DBVenderType.getDBType(connInfo.getType()).isUseDatabaseName()) {
                    connInfo.setSchema(connInfo.getDatabaseName());
                }
            }

            connectionConfig.put(connid, connInfo);
            return connInfo;

        } catch (Exception e) {
            throw new ConnectionFactoryException(e);
        }
    }

    /*
     * =========================================================
     * Cache API
     * =========================================================
     */
    public boolean exists(String connid) {
        return connectionConfig.containsKey(connid);
    }

    public void remove(String connid) {
        connectionConfig.remove(connid);
    }

    public int size() {
        return connectionConfig.size();
    }

    public Set<Entry<String, ConnectionInfo>> entrySet() {
        return connectionConfig.entrySet();
    }

    /*
     * =========================================================
     * Singleton
     * =========================================================
     */
    private static class FactoryHolder {
        private static final ConnectionInfoManager instance = new ConnectionInfoManager();
    }

    public static ConnectionInfoManager getInstance() {
        return FactoryHolder.instance;
    }

    /*
     * =========================================================
     * Init
     * =========================================================
     */
    private void init() {
        logger.debug("connection info config scan package : {}", Configuration.getInstance().getConnectiondaoPackage());

        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(Configuration.getInstance().getConnectiondaoPackage()))
                    .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));

            Set<Class<?>> types = reflections.getTypesAnnotatedWith(ConnectionInfoConfig.class);

            StringBuilder sb = new StringBuilder();

            for (Class<?> type : types) {
                ConnectionInfoConfig anno = type.getAnnotation(ConnectionInfoConfig.class);

                sb.append("beanType: [").append(anno.beanType()).append("] primary: [").append(anno.primary())
                        .append("] beanName: [").append(anno.beanName()).append("]");

                connectionInfoDao = (ConnectionInfoDao) type.getDeclaredConstructor().newInstance();

                if (anno.primary()) {
                    break;
                }
            }

            if (connectionInfoDao == null) {
                throw new ConnectionFactoryException(VarsqlAppCode.EC_FACTORY_CONNECTION_INFO,
                        "ConnectionInfoDao bean null; " + sb.toString());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionFactoryException("ConnectionInfoDao bean: " + e.getMessage());
        }
    }

    /*
     * =========================================================
     * Shutdown
     * =========================================================
     */
    private void addShutdownHook() {
        if (shutdownHookAdded.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("ConnectionInfoManager shutdown start");
                shutdownExecutor(executor, "connectionInfoExecutor");
                logger.info("ConnectionInfoManager shutdown end");
            }));
        }
    }

    private void shutdownExecutor(ExecutorService executor, String name) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warn("{} not terminated, forcing shutdown", name);
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        logger.info("ConnectionInfoManager executor shutdown");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
