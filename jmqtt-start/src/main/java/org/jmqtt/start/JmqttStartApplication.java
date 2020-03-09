package org.jmqtt.start;

import org.jmqtt.broker.BrokerStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"org.jmqtt.monitor","org.jmqtt.service"})
@EnableScheduling
public class JmqttStartApplication {
    private static final Logger LOG = LoggerFactory.getLogger(JmqttStartApplication.class);
    public static void main(String[] args) {

        SpringApplication.run(JmqttStartApplication.class, args);

        try {
            BrokerStartup.start(args);
        } catch (Exception e) {
           LOG.info("Jmqtt start failure,cause = " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
