package org.example;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Email {
    // ANSI 转义码 - 红色
    private static final String RED = "\033[31m";
    // ANSI 转义码 - 绿色
    private static final String GREEN = "\033[32m";
    static Scanner scanner = new Scanner(System.in);

    public void EmailMain() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        EmailSender Email = new EmailSender();
        // 询问用户输入邮箱地址
        System.out.print("请输入邮箱地址: ");
        String email = scanner.nextLine();

        // 验证邮箱格式
        if (EmailSender.isValidEmail(email)) {
            System.out.println(GREEN + " 正在处理请求！");
            // 延迟 3 秒后执行发送邮件任务
            scheduler.schedule(() -> {
                Email.sendEmail(email);  // 发送邮件
            }, 3, TimeUnit.SECONDS);
        } else {
            System.out.println(RED + email + " 不是一个有效的邮箱地址.");
        }
    }
}
