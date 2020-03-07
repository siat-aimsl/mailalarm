package org.jmqtt.monitor;

import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.*;
import java.math.BigDecimal;
import java.util.Map;


@Component
public class Monitor {

    ScheduledForDynamicCron scheduledfordynamiccron = new ScheduledForDynamicCron();
/*
    public void monitorAll(){

        int     cpusendnum      = 0;
        int     memsendnum      = 0;
        int     diskIOsendnum   = 0;
        double  cpuusage        = monitorCpuusge();
        double  availablemem    = monitorMem();
        double  diskio          = monitorIO();

        try {
            if(cpuusage > 0.01) {
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：CPU使用率已超过0.01：" + cpuusage);
                cpusendnum++;
            } else {
                cpusendnum = 0;
            }
            if(availablemem/1024 > 100){
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：剩余内存不足100MB：" + availablemem + "KB");
                memsendnum++;
            } else {
                memsendnum = 0;
            }
            if(diskio > 1){
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：磁盘io写入速度过高：" + diskio + "KB/S");
                diskIOsendnum++;
            } else {
                diskIOsendnum = 0;
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
*/
    public double getCpuusge(){
        Map map = IHttpClient.get("http://127.0.0.1:9002/monitor/metrics/process.cpu.usage");

        JSONArray array = (JSONArray)map.get("measurements");
        Map value = (Map)array.get(0);

        double cpuusage = new BigDecimal(value.get("value").toString()).doubleValue();

        return cpuusage;
    }

    public double getMem(){
        Map map = IHttpClient.get("http://127.0.0.1:9002/monitor/metrics/jvm.memory.committed");
        JSONArray array = (JSONArray)map.get("measurements");
        Map value = (Map)array.get(0);

        double availablemem = new BigDecimal(value.get("value").toString()).doubleValue();

        return availablemem;
    }

    public double getDiskIO(){
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
            data = result[3].split("\\s+");
            diskIO = Double.valueOf(data[3]);

            return diskIO;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            return diskIO;
        }
    }

    public void monitorCpuusge(){
        int     cpusendnum  = 0;
        double  cpuusage    = getCpuusge();

        try {
            if(cpuusage > 0.01) {
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：CPU使用率已超过0.01：" + cpuusage);
                cpusendnum++;
            } else {
                cpusendnum = 0;
            }
            System.out.println("cpusendnum:" + cpusendnum);
            if(cpusendnum < 4){
                scheduledfordynamiccron.setCpuCron("* */1 * * * ?");
            } else if(cpusendnum < 10){
                scheduledfordynamiccron.setCpuCron("* */10 * * * ?");
            } else {
                scheduledfordynamiccron.setCpuCron("* * */1 * * ?");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void monitorMem(){
        int     memsendnum   = 0;
        double  availablemem = getMem();

        try {
            if(availablemem/1024 > 100){
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：剩余内存不足100MB：" + availablemem + "KB");
                memsendnum++;
            } else {
                memsendnum = 0;
            }
            System.out.println("memsendnum:" + memsendnum);
            if(memsendnum < 4){
                scheduledfordynamiccron.setMemCron("* */1 * * * ?");
            } else if(memsendnum < 10){
                scheduledfordynamiccron.setMemCron("* */10 * * * ?");
            } else {
                scheduledfordynamiccron.setMemCron("* * */1 * * ?");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void monitorDiskIO(){
        int     diskIOsendnum   = 0;
        double  diskio          = getDiskIO();

        try {
            if(diskio > 1){
                MailService.sendMail("2953197839@qq.com", "主题：服务警告", "警告：磁盘io写入速度过高：" + diskio + "KB/S");
                diskIOsendnum++;
            } else {
                diskIOsendnum = 0;
            }
            System.out.println("diskIOsendnum:" + diskIOsendnum);
            if(diskIOsendnum < 4){
                scheduledfordynamiccron.setDiskCron("* */1 * * * ?");
            } else if(diskIOsendnum < 10){
                scheduledfordynamiccron.setDiskCron("* */10 * * * ?");
            } else {
                scheduledfordynamiccron.setDiskCron("* * */1 * * ?");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
