package org.example;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    // ANSI 转义码 - 红色
    private static final String RED = "\033[31m";
    // ANSI 转义码 - 绿色
    private static final String GREEN = "\033[32m";

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        CreateDatabaseWithCheck CWithCheck = new CreateDatabaseWithCheck();
        Email Email = new Email();

        // 创建数据库
        CWithCheck.DatabaseNamecreated("QuickCode");

        // 创建数据库表 SQL 语句
        String createTableSQL = "CREATE TABLE IF NOT EXISTS mailcode (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "email VARCHAR(255) NOT NULL, " +
                "verification_code VARCHAR(6) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "expires_at TIMESTAMP)";

        // 执行延迟的操作，延迟2秒
        try {
            delayExecution(2000); // 延迟 2 秒
        } catch (InterruptedException e) {
            System.err.println(RED + "警告: 延迟操作失败: " + e.getMessage());
        }

        // 延迟 3 秒后执行创建表任务
        CWithCheck.MySQLTableCreator("quickcode", "mailcode", createTableSQL);

        // 执行延迟的操作，延迟2秒
        try {
            delayExecution(2000); // 延迟 2 秒
        } catch (InterruptedException e) {
            System.err.println(RED + "警告: 延迟操作失败: " + e.getMessage());
        }

        Email.EmailMain();
    }

    // 延迟执行的方法，单位为毫秒
    public static void delayExecution(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);  // 让当前线程休眠指定的时间
    }
}
