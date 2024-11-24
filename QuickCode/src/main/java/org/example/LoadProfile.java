package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProfile {
    private String dburl;
    private String dbuser;
    private String dbpasswd;
    private String fromEmail;
    private String EmaliPasswd;
    private String EmailHost;
    private String EmailPost;
    private String Emailauth;
    private String EmailEnable;

    // 配置文件路径
    private static final String PROPERTIES_FILE_PATH = "src/config/properties.properties";

    public void ConfigurationFile() {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH)) {
            // 加载配置文件
            props.load(fis);

            // 属性值
            setDburl(props.getProperty("db.url"));
            setDbuser(props.getProperty("db.user"));
            setDbpasswd(props.getProperty("db.passwd"));

            setFromEmail(props.getProperty("Email.fromEmail"));
            setEmaliPasswd(props.getProperty("Email.Passwd"));
            setEmailHost(props.getProperty("Email.host"));
            setEmailPost(props.getProperty("Email.port"));
            setEmailauth(props.getProperty("Email.auth"));
            setEmailEnable(props.getProperty("Email.enable"));
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
        }
    }

    public String getDburl() {
        return dburl;
    }

    public void setDburl(String dburl) {
        this.dburl = dburl;
    }

    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getDbpasswd() {
        return dbpasswd;
    }

    public void setDbpasswd(String dbpasswd) {
        this.dbpasswd = dbpasswd;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getEmaliPasswd() {
        return EmaliPasswd;
    }

    public void setEmaliPasswd(String EmaliPasswd) {
        this.EmaliPasswd = EmaliPasswd;
    }

    public String getEmailHost() {
        return EmailHost;
    }

    public void setEmailHost(String emailHost) {
        EmailHost = emailHost;
    }

    public String getEmailPost() {
        return EmailPost;
    }

    public void setEmailPost(String emailPost) {
        EmailPost = emailPost;
    }

    public String getEmailauth() {
        return Emailauth;
    }

    public void setEmailauth(String emailauth) {
        Emailauth = emailauth;
    }

    public String getEmailEnable() {
        return EmailEnable;
    }

    public void setEmailEnable(String emailEnable) {
        EmailEnable = emailEnable;
    }
}
