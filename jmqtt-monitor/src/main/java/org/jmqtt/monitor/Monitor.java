package org.jmqtt.monitor;

import com.alibaba.fastjson.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.*;
import java.math.BigDecimal;
import java.util.Map;


@Component
public class Monitor {

    @Scheduled(cron = "0 */1 * * * ?")
    public void monitorAll(){

        double cpuusage     = monitorCpuusge();
        double availablemem = monitorMem();
        double diskio       = monitorIO();
        try {
            if(cpuusage > 0.01) {
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：CPU使用率已超过0.01：" + cpuusage);
            }
            if(availablemem/1024 > 100){
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：剩余内存不足100MB：" + availablemem + "KB");
            }
            //if(diskio > 1){
               // MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：磁盘io写入速度过高：" + diskio + "KB/S");
            //}
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

    public double monitorIO(){
        InputStream in      = null;
        Process     pro     = null;
        String      tmp     = null;
        String[]    result  = new String[5];
        String[]    data    = null;
        double      diskIO  = 0;
        int         num     = 0;

        try {
            pro = Runtime.getRuntime().exec("iostat -d -k 1 1");
            pro.waitFor();
            in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            while((tmp = read.readLine()) != null){
                result[num++] = tmp;
            }//总共5行，只需要其中的4行,第4行为数据
            data = result[3].split(" ");
            diskIO = Double.valueOf(data[3].toString());
            MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：磁盘io写入速度过高：" + result[3] + "*" + diskIO + "KB/S");
            return diskIO;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            return diskIO;
        }
    }
}
