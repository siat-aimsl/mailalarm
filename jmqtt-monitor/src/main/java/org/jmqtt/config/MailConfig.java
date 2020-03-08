package org.jmqtt.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class MailConfig {
    private static final String PROPERTIES_DEFAULT = "mailConfig.properties";
    public static String host;
    public static Integer port;
    public static String userName;
    public static String passWord;
    public static String emailForm;
    public static List<String> emailTo = new ArrayList<String>();
    public static String timeout;
    public static Properties properties;

    static{
        init();
    }

    private static void init() {
        properties = new Properties();
        try{
            InputStream inputStream = MailConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_DEFAULT);
            properties.load(inputStream);
            inputStream.close();
            host = properties.getProperty("mailHost");
            port = Integer.parseInt(properties.getProperty("mailPort"));
            userName = properties.getProperty("mailUsername");
            passWord = properties.getProperty("mailPassword");
            emailForm = properties.getProperty("mailFrom");
            timeout = properties.getProperty("mailTimeout");
            Iterator<String> it=properties.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                if(key.startsWith("mailTo")){
                    emailTo.add(properties.getProperty(key));
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
