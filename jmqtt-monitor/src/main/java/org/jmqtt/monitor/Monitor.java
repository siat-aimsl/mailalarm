package org.jmqtt.monitor;

import com.alibaba.fastjson.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;


@Component
public class Monitor {

    @Scheduled(cron = "0/5 * * * * ?")
    public void monitorAll(){

        double cpuusage     = monitorCpuusge();
        double availablemem = monitorMem();

        try {
            if(cpuusage > 0.01) {
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：CPU使用率已超过0.01：" + cpuusage);
            }
            if(availablemem/1024 > 100){
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：剩余内存不足100MB：" + availablemem);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public double monitorCpuusge(){
        Map map = IHttpClient.get("http://127.0.0.1:9002/monitor/metrics/process.cpu.usage");

        JSONArray array = (JSONArray)map.get("measurements");
        Map value = (Map)array.get(0);

        double cpuusage = new BigDecimal(value.get("value").toString()).doubleValue();

        return cpuusage;
    }

    public double monitorMem(){
        Map map = IHttpClient.get("http://127.0.0.1:9002/monitor/metrics/jvm.memory.committed");
        JSONArray array = (JSONArray)map.get("measurements");
        Map value = (Map)array.get(0);

        double availablemem = new BigDecimal(value.get("value").toString()).doubleValue();

        return availablemem;
    }

    public void monitorIO(){

    }
}
