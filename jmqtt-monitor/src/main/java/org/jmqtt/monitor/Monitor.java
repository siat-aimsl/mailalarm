package org.jmqtt.monitor;

import org.jmqtt.client.IHttpClient;
import org.jmqtt.model.response.ActuatorResponse;
import org.jmqtt.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@Component
public class Monitor {

    @Value("${monitorCpuUrl}")
    private String  monitorCpuUrl;
    @Value("${monitorMemUrl}")
    private String  monitorMemUrl;
    @Value("${mailTo}")
    private List<String> emailTo;
    @Autowired
    private MailService mailService;

    @Value("${cpuUsageThreshold}")
    private double cpuUsageThreshold;
    @Value("${memThreshold}")
    private double memThreshold;
    @Value("${diskIOThreshold}")
    private double diskIOThreshold;
    @Value("${cpuUsageChangeThreshold}")
    private double cpuUsageChangeThreshold;
    @Value("${memChangeThreshold}")
    private double memChangeThreshold;



    int     cpuUsageAlarmNum  = 0;
    int     cpuUsageAlarmSendInterval = 0; //cpuusagealarmsendinterval
    int     cpuUsageAlarmSendTimeCount = 0;
    double  cpuUsage = 0;

    int     memAlarmNum   = 0;
    int     memAlarmSendInterval = 0;
    int     memAlarmSendTimeCount = 0;
    double  availableMem = 0;

    int     diskIOAlarmNum   = 0;
    int     diskIOAlarmSendInterval = 0;
    int     diskIOAlarmSendTimeCount = 0;

    int     cpuUsageChangeAlarmNum  = 0;
    int     cpuUsageChangeAlarmSendInterval = 0; //cpuusagealarmsendinterval
    int     cpuUsageChangeAlarmSendTimeCount = 0;

    int     memChangeAlarmNum   = 0;
    int     memChangeAlarmSendInterval = 0;
    int     memChangeAlarmSendTimeCount = 0;

    @Scheduled(cron = "0 */1 * * * ?")
    public void monitorAll(){
        //---------cpu-------
        monitorCpuUsage();
        //---------mem-------
        monitorMem();
        //---------diskIO-------
        //monitorDiskIO();
        monitorCpuusgeChange();
        monitorMemChange();
    }

    public double getCpuUsage(){
        ActuatorResponse actuatorResponse = IHttpClient.get(monitorCpuUrl);
        double cpuusage = actuatorResponse.getMeasurements().get(0).getValue();

        return cpuusage;
    }

    public double getMem(){
        ActuatorResponse actuatorResponse = IHttpClient.get(monitorMemUrl);
        double availablemem = actuatorResponse.getMeasurements().get(0).getValue();

        return availablemem;
    }

    public double getDiskIO(){
        Process     pro     = null;
        String      tmp     = null;
        String[]    result  = new String[5];
        String[]    data    = null;
        double      diskIO  = 0;
        int         num     = 0;

        InputStream in      = null;
        BufferedReader read = null;

        try {
            pro = Runtime.getRuntime().exec("iostat -d -k 1 1");
            pro.waitFor();
            in = pro.getInputStream();
            read = new BufferedReader(new InputStreamReader(in));
            while((tmp = read.readLine()) != null){
                result[num++] = tmp;
            }//总共5行，只需要其中的4行,第4行为数据

            data = result[3].split("\\s+");
            diskIO = Double.valueOf(data[3]);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            if(read != null){
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return diskIO;
    }

    @Async("mailTaskAsyncPool")
    public void monitorCpuUsage(){
        cpuUsage = getCpuUsage();

        if(cpuUsage > cpuUsageThreshold) {//阈值判断
            cpuUsageAlarmNum++;
        } else {
            cpuUsageAlarmNum = 0;
        }
        if(cpuUsageAlarmNum <= 4) {//根据连续超过阈值次数设定邮件发送间隔时间
            cpuUsageAlarmSendInterval = 0;
        } else if(cpuUsageAlarmNum <= 25){
            cpuUsageAlarmSendInterval = 9;
        } else {
            cpuUsageAlarmSendInterval = 59;
        }
        if(cpuUsageAlarmNum > 0 && cpuUsageAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + cpuUsageAlarmNum, "警告：CPU使用率已超过0.01：" + cpuUsage);
            }
          cpuUsageAlarmSendTimeCount = cpuUsageAlarmSendInterval;
        } else {
            cpuUsageAlarmSendTimeCount--;
        }
    }

    @Async("mailTaskAsyncPool")
    public void monitorMem(){
        availableMem = getMem();

        if(availableMem/1024 > memThreshold){//阈值判断
            memAlarmNum++;
        } else {
            memAlarmNum = 0;
        }
        if(memAlarmNum <= 4) {//根据连续超过阈值次数设定邮件发送间隔时间
            memAlarmSendInterval = 0;
        } else if(memAlarmNum <= 25){
            memAlarmSendInterval = 9;
        } else {
            memAlarmSendInterval = 59;
        }
        if(memAlarmNum > 0 && memAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + memAlarmNum, "警告：剩余内存不足100MB：" + availableMem + "KB");
            }
           memAlarmSendTimeCount = memAlarmSendInterval;
        } else {
            memAlarmSendTimeCount--;
        }
    }

    @Async("mailTaskAsyncPool")
    public void monitorDiskIO(){
        double diskIo = getDiskIO();

        if(diskIo > diskIOThreshold) {//阈值判断
            diskIOAlarmNum++;
        } else {
            diskIOAlarmNum = 0;
        }
        if(diskIOAlarmNum <= 4) {//根据连续超过阈值次数设定邮件发送间隔时间
            diskIOAlarmSendInterval = 0;
        } else if(diskIOAlarmNum <= 25){
            diskIOAlarmSendInterval = 9;
        } else {
            diskIOAlarmSendInterval = 59;
        }
        if(diskIOAlarmNum > 0 && diskIOAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告"+ diskIOAlarmNum, "警告：磁盘io写入速度过高：" + diskIo + "KB/S");
            }
            diskIOAlarmSendTimeCount = diskIOAlarmSendInterval;
        } else {
            diskIOAlarmSendTimeCount--;
        }
    }

    @Async("mailTaskAsyncPool")
    public void monitorCpuusgeChange(){
        double cpuUsageBehind = getCpuUsage();
        double cpuUsageChange = cpuUsageBehind - cpuUsage;

        if(cpuUsageChange > cpuUsageChangeThreshold) {//阈值判断
            cpuUsageChangeAlarmNum++;
        } else {
            cpuUsageChangeAlarmNum = 0;
        }
        if(cpuUsageChangeAlarmNum <= 4) {//根据连续超过阈值次数设定邮件发送间隔时间
            cpuUsageChangeAlarmSendInterval = 0;
        } else if(cpuUsageChangeAlarmNum <= 25){
            cpuUsageChangeAlarmSendInterval = 9;
        } else {
            cpuUsageChangeAlarmSendInterval = 59;
        }
        if(cpuUsageChangeAlarmNum > 0 && cpuUsageChangeAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + cpuUsageChangeAlarmNum, "警告：CPU使用率在一分钟内升高了0.5以上：" + cpuUsageChange);
            }
            cpuUsageChangeAlarmSendTimeCount = cpuUsageChangeAlarmSendInterval;
        } else {
            cpuUsageChangeAlarmSendTimeCount--;
        }
    }

    @Async("mailTaskAsyncPool")
    public void monitorMemChange(){
        double availableMemBehind = getMem();
        double availableMemChange = availableMem - availableMemBehind;

        if(availableMemChange/1024 > memChangeThreshold){//阈值判断
            memChangeAlarmNum++;
        } else {
            memChangeAlarmNum = 0;
        }
        if(memChangeAlarmNum <= 4) {//根据连续超过阈值次数设定邮件发送间隔时间
            memChangeAlarmSendInterval = 0;
        } else if(memChangeAlarmNum <= 25){
            memChangeAlarmSendInterval = 9;
        } else {
            memChangeAlarmSendInterval = 59;
        }
        if(memChangeAlarmNum > 0 && memChangeAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + memChangeAlarmNum, "警告：内存在一分钟内使用超过100MB以上：" + availableMemChange + "MB");
            }
            memChangeAlarmSendTimeCount = memChangeAlarmSendInterval;
        } else {
            memChangeAlarmSendTimeCount--;
        }
    }

}
