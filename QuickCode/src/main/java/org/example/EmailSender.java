package org.example;

import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    LoadProfile LoadProfile = new LoadProfile();
    CreateDatabaseWithCheck CWithCheck = new CreateDatabaseWithCheck();
    // ANSI 转义码 - 红色
    private static final String RED = "\033[31m";
    // ANSI 转义码 - 绿色
    private static final String GREEN = "\033[32m";
    // 正则表达式，用于验证电子邮件地址格式
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public void sendEmail(String recipient) {
        LoadProfile.ConfigurationFile();

        if (LoadProfile.getEmailHost() == null || LoadProfile.getEmailPost() == null ||
                LoadProfile.getEmailauth() == null || LoadProfile.getEmailEnable() == null) {
            System.out.println(RED + "警告: 电子邮件属性为空!");
            return;
        }


        Properties properties = new Properties();
        properties.put("mail.smtp.host", LoadProfile.getEmailHost());
        properties.put("mail.smtp.port", LoadProfile.getEmailPost());
        properties.put("mail.smtp.auth", LoadProfile.getEmailauth());
        properties.put("mail.smtp.starttls.enable", LoadProfile.getEmailEnable());

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(LoadProfile.getFromEmail(), LoadProfile.getEmaliPasswd());
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(LoadProfile.getFromEmail()));  // 发件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));  // 收件人
            message.setSubject("验证码");  // 邮件主题
            String verificationCode = generateCode();
            // 动态生成 HTML 内容
            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; padding: 20px;">
                        <div style="color: #4CAF50;font-size: 16px">您好,%s</div>
                        <p style="font-size: 16px; color: #333;margin: 10px 0 40px 0;">您的验证码如下：</p>
                        <div style="font-size: 24px; font-weight: bold; color: #000; margin: 10px 0;text-align: center;">
                            %s
                        </div>
                        <p style="font-size: 14px; color: #999;text-align: center;">此验证码有效期为10分钟，请勿泄露！</p>
                        <p style="font-size: 14px; color: #777;margin: 40px 0 0 0;">如有问题，请联系我们：<a href="mailto:%s" style="color: #4CAF50;">IBS客服</a></p>
                    </div>
                    """.formatted(recipient, verificationCode, LoadProfile.getFromEmail());

            // 设置邮件内容为 HTML
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 发送邮件
            Transport.send(message);
            System.out.println(GREEN + "提示: 电子邮件发送成功.");
            //存储数据
            storeVerificationCode(recipient, verificationCode);
        } catch (MessagingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));  // 生成0-9的数字
        }

        return code.toString();
    }

    // 将验证码和过期时间存储到数据库
    public void storeVerificationCode(String email, String code) throws SQLException {
        String databaseName = "quickcode";
        try (Connection conn = DriverManager.getConnection(LoadProfile.getDburl() + databaseName, LoadProfile.getDbuser(), LoadProfile.getDbpasswd())) {
            // 插入验证码和过期时间（10分钟后过期）
            String sql = "INSERT INTO mailcode (email, verification_code, expires_at) VALUES (?, ?, NOW() + INTERVAL 10 MINUTE)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, code);
                stmt.executeUpdate();
                System.out.println(GREEN + "提示: 验证码存储成功.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(RED + "警告: 存储验证码失败!", e);
        }
    }

    // 验证邮箱格式
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}