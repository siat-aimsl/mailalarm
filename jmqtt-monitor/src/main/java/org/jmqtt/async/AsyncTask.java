package org.jmqtt.async;

import org.jmqtt.monitor.Monitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

@Configuration
public class AsyncTask {
    @Autowired
    private Monitor monitor;

    @Async("mailTaskAsyncPool")
    public void monitorCpuUsage(double cpuUsage){

        monitor.monitorCpuUsage(cpuUsage);
    }

    @Async("mailTaskAsyncPool")
    public void monitorMem(double availableMem){

        monitor.monitorMem(availableMem);
    }

    @Async("mailTaskAsyncPool")
    public void monitorDiskIO(){

        monitor.monitorDiskIO();
    }

    @Async("mailTaskAsyncPool")
    public void monitorCpuusgeChange(double cpuUsage){

        monitor.monitorCpuusgeChange(cpuUsage);
    }

    @Async("mailTaskAsyncPool")
    public void monitorMemChange(double availableMem){

        monitor.monitorMemChange(availableMem);
    }

}

