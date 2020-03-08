package org.jmqtt.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MonitorConfig {
    private static final String PROPERTIES_DEFAULT = "monitorConfig.properties";
    public static String monitorCpuUrl;
    public static String monitorMemUrl;
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
            monitorCpuUrl = properties.getProperty("monitorCpuUrl");
            monitorMemUrl = properties.getProperty("monitorMemUrl");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
