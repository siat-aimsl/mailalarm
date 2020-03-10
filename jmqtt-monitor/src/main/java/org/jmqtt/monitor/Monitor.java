package org.jmqtt.monitor;

import org.jmqtt.client.IHttpClient;
import org.jmqtt.model.response.ActuatorResponse;
import org.jmqtt.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${SendInterval_5}")
    private int SendInterval_5;
    @Value("${SendInterval_35}")
    private int SendInterval_35;
    private int cpuUsageAlarmNum  = 0;
    private int cpuUsageAlarmSendInterval = 0;
    private int cpuUsageAlarmSendTimeCount = 0;
    private double cpuUsage = 0;
    private double cpuUsageBefore = -1;
    private int memAlarmNum   = 0;
    private int memAlarmSendInterval = 0;
    private int memAlarmSendTimeCount = 0;
    private double availableMem = 0;
    private double availableMemBefore = -1;
    private int diskIOAlarmNum   = 0;
    private int diskIOAlarmSendInterval = 0;
    private int diskIOAlarmSendTimeCount = 0;
    private int cpuUsageChangeAlarmNum  = 0;
    private int cpuUsageChangeAlarmSendInterval = 0;
    private int cpuUsageChangeAlarmSendTimeCount = 0;
    private int memChangeAlarmNum   = 0;
    private int memChangeAlarmSendInterval = 0;
    private int memChangeAlarmSendTimeCount = 0;



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
            }
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

    public void monitorCpuUsage(double cpuUsage){

        if(cpuUsage > cpuUsageThreshold) {//阈值判断
            cpuUsageAlarmNum++;
        } else {
            cpuUsageAlarmNum = 0;
        }
        cpuUsageAlarmSendInterval = controlMailSendTime(cpuUsageAlarmNum);
        if(cpuUsageAlarmNum > 0 && cpuUsageAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + cpuUsageAlarmNum, "警告：CPU使用率已超过0.01：" + cpuUsage);
            }
          cpuUsageAlarmSendTimeCount = cpuUsageAlarmSendInterval;
        } else {
            cpuUsageAlarmSendTimeCount--;
        }
    }

    public void monitorMem(double availableMem){

        if(availableMem/1024 > memThreshold){//阈值判断
            memAlarmNum++;
        } else {
            memAlarmNum = 0;
        }
        memAlarmSendInterval = controlMailSendTime(memAlarmNum);
        if(memAlarmNum > 0 && memAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + memAlarmNum, "警告：剩余内存不足100MB：" + availableMem + "KB");
            }
           memAlarmSendTimeCount = memAlarmSendInterval;
        } else {
            memAlarmSendTimeCount--;
        }
    }

    public void monitorDiskIO(){
        double diskIo = getDiskIO();

        if(diskIo > diskIOThreshold) {//阈值判断
            diskIOAlarmNum++;
        } else {
            diskIOAlarmNum = 0;
        }
        diskIOAlarmSendInterval = controlMailSendTime(diskIOAlarmNum);
        if(diskIOAlarmNum > 0 && diskIOAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告"+ diskIOAlarmNum, "警告：磁盘io写入速度过高：" + diskIo + "KB/S");
            }
            diskIOAlarmSendTimeCount = diskIOAlarmSendInterval;
        } else {
            diskIOAlarmSendTimeCount--;
        }
    }

    public void monitorCpuusgeChange(double cpuUsageBehind){
        if(cpuUsageBefore == -1){
            cpuUsageBefore = cpuUsageBehind;
        }
        double cpuUsageChange = cpuUsageBehind - cpuUsageBefore;
        cpuUsageBefore = cpuUsageBehind;

        if(cpuUsageChange > cpuUsageChangeThreshold) {//阈值判断
            cpuUsageChangeAlarmNum++;
        } else {
            cpuUsageChangeAlarmNum = 0;
        }
        cpuUsageChangeAlarmSendInterval = controlMailSendTime(cpuUsageChangeAlarmNum);
        if(cpuUsageChangeAlarmNum > 0 && cpuUsageChangeAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + cpuUsageChangeAlarmNum, "警告：CPU使用率在一分钟内升高了0.5以上：" + cpuUsageChange);
            }
            cpuUsageChangeAlarmSendTimeCount = cpuUsageChangeAlarmSendInterval;
        } else {
            cpuUsageChangeAlarmSendTimeCount--;
        }
    }

    public void monitorMemChange(double availableMemBehind){
        if(availableMemBefore == -1){
            availableMemBefore = availableMemBehind;
        }
        double availableMemChange = availableMemBefore - availableMemBehind;
        availableMemBefore = availableMemBehind;

        if(availableMemChange/1024 > memChangeThreshold){//阈值判断
            memChangeAlarmNum++;
        } else {
            memChangeAlarmNum = 0;
        }
        memChangeAlarmSendInterval = controlMailSendTime(memChangeAlarmNum);
        if(memChangeAlarmNum > 0 && memChangeAlarmSendTimeCount == 0){//计数产生间隔时间
            for (String to: emailTo) {
                mailService.sendMail(to, "主题：服务警告" + memChangeAlarmNum, "警告：内存在一分钟内使用超过100MB以上：" + availableMemChange + "MB");
            }
            memChangeAlarmSendTimeCount = memChangeAlarmSendInterval;
        } else {
            memChangeAlarmSendTimeCount--;
        }
    }

    public int controlMailSendTime(double alarmNum) {
        int SendInterval = 0;

        if(alarmNum <= 4) {//根据连续超过阈值次数设定邮件发送间隔时间
            SendInterval = 0;
        } else if(alarmNum <= 25){
            SendInterval = SendInterval_5 -1;
        } else {
            SendInterval = SendInterval_35 -1;
        }
        return SendInterval;
    }

}
