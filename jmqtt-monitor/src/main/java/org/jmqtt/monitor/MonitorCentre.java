package org.jmqtt.monitor;

import org.jmqtt.async.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorCentre {
    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private Monitor monitor;
    double cpuUsage = 0;
    double availableMem = 0;

    @Scheduled(cron = "0 */1 * * * ?")
    public void monitorAll(){
        cpuUsage = monitor.getCpuUsage();
        availableMem = monitor.getMem();

        asyncTask.monitorCpuUsage(cpuUsage);
        asyncTask.monitorMem(availableMem);
        asyncTask.monitorDiskIO();
        asyncTask.monitorCpuusgeChange(cpuUsage);
        asyncTask.monitorMemChange(availableMem);
    }
}
