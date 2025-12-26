package com.varsql.core.sql.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.varsql.core.db.DBVenderType;

public class JdbcNetworkChecker {

    public static boolean check(String jdbcUrl, DBVenderType dbType, int timeoutMs) {
    	
    	List<HostPort> hostPorts = parseHosts(jdbcUrl, dbType);
        if (hostPorts.isEmpty()) {
            return true; 
        }
        
    	for (HostPort hp : hostPorts) {
            try {
                tcpCheckOrThrow(hp.host, hp.port, timeoutMs);
                return true; // 하나라도 성공하면 OK
            } catch (IllegalStateException e) {
                // 다음 호스트로 넘어가면서 마지막 실패 메시지 저장
                if (hp == hostPorts.get(hostPorts.size() - 1)) {
                    throw e; // 마지막까지 실패 시 예외 던짐
                }
            }
        }
    	
    	return true; 
    }

    private static List<HostPort> parseHosts(String url, DBVenderType dbType) {
        List<HostPort> result = new ArrayList<>();
        if (url == null) return result;

        switch (dbType) {
            case ORACLE:
                result.addAll(parseOracleHosts(url, dbType));
                break;
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
            case SQLSERVER:
            case H2:
            case DERBY:
            case HSQLDB:
            case DB2:
            case TIBERO:
            case CUBRID:
                result.addAll(parseCommaOrSingleHosts(url, dbType));
                break;
            default:
                HostPort hp = parseSingleHost(url, dbType);
                if (hp != null) result.add(hp);
        }

        return result;
    }

    // Oracle RAC 및 단일 호스트
    private static List<HostPort> parseOracleHosts(String url, DBVenderType dbType) {
        List<HostPort> list = new ArrayList<>();
        try {
            // RAC DESCRIPTION
            Matcher m = Pattern.compile("\\(HOST=([^\\)]+)\\)\\(PORT=(\\d+)\\)").matcher(url);
            while (m.find()) {
                list.add(new HostPort(m.group(1), Integer.parseInt(m.group(2))));
            }
            // 단일 호스트 포맷: @host:port/service
            if (list.isEmpty()) {
                m = Pattern.compile("@(?://)?([^:/]+):(\\d+)").matcher(url);
                if (m.find()) {
                    list.add(new HostPort(m.group(1), Integer.parseInt(m.group(2))));
                } else {
                    String hostOnly = regexHost(url);
                    if (hostOnly != null) list.add(new HostPort(hostOnly, dbType.getDefaultPort()));
                }
            }
        } catch (Exception e) {
            // 파싱 실패시 OK 처리
        }
        return list;
    }

    // 쉼표로 여러 호스트 가능 + 단일 호스트
    private static List<HostPort> parseCommaOrSingleHosts(String url, DBVenderType dbType) {
        List<HostPort> list = new ArrayList<>();
        try {
            Matcher m = Pattern.compile("jdbc:[^:]+://([^/;]+)").matcher(url);
            if (m.find()) {
                String hostsPart = m.group(1);
                String[] hosts = hostsPart.split(",");
                for (String h : hosts) {
                    String[] hp = h.split(":");
                    String host = hp[0];
                    int port = hp.length > 1 ? Integer.parseInt(hp[1]) : dbType.getDefaultPort();
                    list.add(new HostPort(host, port));
                }
            } else {
                // 단일 호스트 fallback
                HostPort hp = parseSingleHost(url, dbType);
                if (hp != null) list.add(hp);
            }
        } catch (Exception e) {
            // 파싱 실패시 OK 처리
        }
        return list;
    }

    private static HostPort parseSingleHost(String url, DBVenderType dbType) {
        try {
            Matcher m = Pattern.compile("jdbc:[^:]+://([^:/;]+)(?::(\\d+))?").matcher(url);
            if (m.find()) {
                String host = m.group(1);
                int port = m.group(2) != null ? Integer.parseInt(m.group(2)) : dbType.getDefaultPort();
                return new HostPort(host, port);
            }
        } catch (Exception e) {
            // 파싱 실패시 OK 처리
        }
        return null;
    }

    private static void tcpCheckOrThrow(String host, int port, int timeoutMs) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
        } catch (Exception e) {
            throw new IllegalStateException(
                "DB network unreachable. Host: " + host + ", Port: " + port +" msg : "+ e.getMessage(), e
            );
        }
    }

    private static String regexHost(String url) {
        Matcher m = Pattern.compile("jdbc:[^:]+://([^:/;]+)").matcher(url);
        if (m.find()) return m.group(1);
        return null;
    }

    private static class HostPort {
        final String host;
        final int port;
        HostPort(String h, int p) { this.host = h; this.port = p; }
    }
}
