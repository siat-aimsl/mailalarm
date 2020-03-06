package org.jmqtt.start;

import org.jmqtt.broker.BrokerStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"org.jmqtt.monitor"})
@EnableScheduling
public class JmqttStartApplication {

    public static void main(String[] args) {

        SpringApplication.run(JmqttStartApplication.class, args);

        try {
            BrokerStartup.start(args);
        } catch (Exception e) {
            System.out.println("Jmqtt start failure,cause = " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
