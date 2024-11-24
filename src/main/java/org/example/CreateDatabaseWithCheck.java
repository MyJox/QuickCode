package org.example;

import java.sql.*;

public class CreateDatabaseWithCheck {
    // ANSI 转义码 - 红色
    private static final String RED = "\033[31m";
    // ANSI 转义码 - 绿色
    private static final String GREEN = "\033[32m";
    static LoadProfile LoadProfile = new LoadProfile();

    boolean NOT = false;

    public void DatabaseNamecreated(String databaseName) {
        LoadProfile.ConfigurationFile();

        if (databaseName == null || databaseName.trim().isEmpty()) {
            throw new IllegalArgumentException(RED + "警告: 数据库名称不能为null或空!");
        }

        try (Connection conn = DriverManager.getConnection(LoadProfile.getDburl(), LoadProfile.getDbuser(), LoadProfile.getDbpasswd())) {
            // 检测数据库是否已存在
            if (databaseExists(conn, databaseName)) {
                System.out.println(GREEN + "提示: 数据库 '" + databaseName + "' 已存在.");
            } else {
                System.out.println(RED + "警告: 数据库 '" + databaseName + "' 不存在, 自动创建中！");
                // 执行延迟的操作，延迟2秒
                try {
                    delayExecution(2000); // 延迟 2 秒
                } catch (InterruptedException e) {
                    System.err.println(RED + "警告: 延迟操作失败: " + e.getMessage());
                }
                // 创建新数据库
                createDatabase(conn, databaseName);
                System.out.println(GREEN + "提示: 数据库 '" + databaseName + "' 创建成功.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 检查数据库是否存在
    private static boolean databaseExists(Connection conn, String databaseName) throws SQLException {
        String checkSQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkSQL)) {
            stmt.setString(1, databaseName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // 如果有结果集，表示数据库已存在
            }
        }
    }

    // 创建数据库
    private static void createDatabase(Connection conn, String databaseName) throws SQLException {
        String createDatabaseSQL = "CREATE DATABASE " + databaseName;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createDatabaseSQL);
        }
    }

    // 检查并创建 MySQL 表
    public Boolean MySQLTableCreator(String databaseName, String tableName, String createTableSQL) {
        // 创建数据库连接
        try (Connection conn = DriverManager.getConnection(LoadProfile.getDburl() + databaseName, LoadProfile.getDbuser(), LoadProfile.getDbpasswd())) {
            if (isTableExists(conn, tableName)) {
                System.out.println(GREEN + "提示: 表 " + tableName + " 已存在.");
                NOT = true;
            }else{
                System.out.println(RED + "警告: 表 " + tableName + " 不存在, 自动创建中！");
                // 执行延迟的操作，延迟2秒
                try {
                    delayExecution(2000); // 延迟 2 秒
                } catch (InterruptedException e) {
                    System.err.println(RED + "警告: 延迟操作失败: " + e.getMessage());
                }
                // 如果表不存在，创建表
                createTable(conn, createTableSQL, tableName);
                NOT = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(RED + "警告: 连接到数据库时出错.");
        }
        return NOT;
    }

    // 检查表是否存在
    public static boolean isTableExists(Connection conn, String tableName) throws SQLException {
        String query = "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conn.getCatalog());  // 使用当前数据库的 schema
            stmt.setString(2, tableName);  // 设置表名参数
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 如果查询结果存在，表存在
        }
    }

    // 创建表
    public static void createTable(Connection conn, String createTableSQL, String tableName) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println(GREEN + "提示: 表 '" + tableName + "' 创建成功.");
        }
    }

    // 延迟执行的方法，单位为毫秒
    public static void delayExecution(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);  // 让当前线程休眠指定的时间
    }
}
