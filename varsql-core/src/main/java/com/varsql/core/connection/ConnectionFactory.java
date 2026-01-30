package com.varsql.core.connection;

import java.sql.Connection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.pool.ConnectionPoolInterface;
import com.varsql.core.connection.pool.PoolStatus;
import com.varsql.core.connection.pool.PoolType;
import com.varsql.core.db.mybatis.SQLManager;
import com.varsql.core.exception.ConnectionFactoryException;

/**
 *
 * @FileName  : ConnectionFactory.java
 * @프로그램 설명 : Connection Factory
 * @Date      : 2018. 2. 13.
 * @작성자      : ytkim
 */
public final class ConnectionFactory implements ConnectionContext {

    private final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    private PoolType connectionPoolType = PoolType.DBCP2;

    /** shutdown 상태인 connid */
    private final Set<String> shutdownConnIds = ConcurrentHashMap.newKeySet();

    /** connid별 lock 객체 */
    private final ConcurrentHashMap<String, Object> poolLocks = new ConcurrentHashMap<>();

    /** 전체 shutdown 여부 */
    private volatile boolean shuttingDown = false;

    private ConnectionFactory() {
        addShutdownHook();
    }

    /* =========================================================
     * Lock
     * ========================================================= */
    private Object getLock(String connid) {
        return poolLocks.computeIfAbsent(connid, k -> new Object());
    }

    /* =========================================================
     * Connection
     * ========================================================= */
    @Override
    public Connection getConnection(String connid) throws ConnectionFactoryException {

        if (shuttingDown) {
            throw new ConnectionFactoryException(
                VarsqlAppCode.EC_DB_POOL_CLOSE,
                "ConnectionFactory is shutting down"
            );
        }

        ConnectionInfo connInfo = createPool(connid);

        if (isShutdown(connid)) {
            throw new ConnectionFactoryException(
                VarsqlAppCode.EC_DB_POOL_CLOSE,
                "db connection refused"
            );
        }

        return connectionPoolType.getPoolBean().getConnection(connInfo);
    }

    /* =========================================================
     * Pool Create
     * ========================================================= */
    private ConnectionInfo createPool(String connid) throws ConnectionFactoryException {
        synchronized (getLock(connid)) {
            try {
                ConnectionInfo connInfo = ConnectionInfoManager.getInstance().getConnectionInfo(connid);

                if (connInfo.isCreateConnectionPool()) {
                    return connInfo;
                }

                getPoolBean().createDataSource(connInfo);

                try (Connection conn = getPoolBean().getConnection(connInfo)) {
                    if (conn == null) {
                        throw new ConnectionFactoryException(
                            VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR,
                            "createConnectionInfo error : [" + connInfo.getConnid() + "]"
                        );
                    }
                }

                connInfo.setCreateConnectionPool(true);
                return connInfo;

            } catch (Exception e) {
                logger.error("createConnectionInfo error for connid: {}", connid, e);
                if (e instanceof ConnectionFactoryException) {
                    throw (ConnectionFactoryException) e;
                }
                throw new ConnectionFactoryException(
                    VarsqlAppCode.EC_FACTORY_CONNECTION_ERROR,
                    "createConnectionInfo error : [" + connid + "]",
                    e
                );
            }
        }
    }

    /* =========================================================
     * Pool Shutdown
     * ========================================================= */
    public void poolShutdown(String connid) {
        synchronized (getLock(connid)) {
            try {
                if (shutdownConnIds.contains(connid)) {
                    return;
                }

                logger.info("db pool shutdown start: {}", connid);

                if (ConnectionInfoManager.getInstance().exists(connid)) {
                    getPoolBean().poolShutdown(
                        ConnectionInfoManager.getInstance().getConnectionInfo(connid)
                    );
                }
                
                logger.info("db pool shutdown end: {}", connid);

                closeMetaDataSource(connid);
                shutdownConnIds.add(connid);

            } catch (Exception e) {
                logger.warn("poolShutdown error for connid {}: {}", connid, e.getMessage(), e);
            } finally {
                poolLocks.remove(connid); // 🔥 lock 메모리 릭 방지
            }
        }
    }

    /* =========================================================
     * All Pool Shutdown (Tomcat-safe)
     * ========================================================= */
    public void allPoolShutdown() {

        if (shuttingDown) return;
        shuttingDown = true;

        logger.info("db pool allPoolShutdown start");

        for (var entry : ConnectionInfoManager.getInstance().entrySet()) {
            poolShutdown(entry.getKey());
        }

        logger.info("db pool allPoolShutdown end");
    }

    /* =========================================================
     * MetaData
     * ========================================================= */
    public void closeMetaDataSource(String connid) {
        try {
            SQLManager.getInstance().close(connid);
            ConnectionInfoManager.getInstance().remove(connid);
        } catch (Exception e) {
            logger.warn("closeMetaDataSource error for connid {}: {}", connid, e.getMessage());
        }
    }

    /* =========================================================
     * Status
     * ========================================================= */
    @Override
    public boolean isShutdown(String connid) {
        return shutdownConnIds.contains(connid);
    }

    @Override
    public PoolStatus getStatus(String connid) {
        if (isShutdown(connid)) return PoolStatus.SHUTDOWN;

        if (ConnectionInfoManager.getInstance().exists(connid)) {
            ConnectionInfo ci = ConnectionInfoManager.getInstance().getConnectionInfo(connid);
            return ci.isCreateConnectionPool() ? PoolStatus.START : PoolStatus.STOP;
        }
        return PoolStatus.STOP;
    }

    public ConnectionPoolInterface getPoolBean() {
        return connectionPoolType.getPoolBean();
    }

    /* =========================================================
     * Reset
     * ========================================================= */
    public void resetConnectionPool(String connid) throws ConnectionFactoryException {

        if (shuttingDown) {
            throw new ConnectionFactoryException(
                VarsqlAppCode.EC_DB_POOL_CLOSE,
                "reset not allowed during shutdown"
            );
        }

        synchronized (getLock(connid)) {
            poolShutdown(connid);
            shutdownConnIds.remove(connid);
            createPool(connid);
        }
    }

    /* =========================================================
     * Singleton
     * ========================================================= */
    private static class FactoryHolder {
        private static final ConnectionFactory instance = new ConnectionFactory();
    }

    public static ConnectionFactory getInstance() {
        return FactoryHolder.instance;
    }

    /* =========================================================
     * JVM Shutdown Hook
     * ========================================================= */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("JVM shutdown hook: closing all connection pools...");
            allPoolShutdown();
        }));
    }
}
