package org.jmqtt.monitor;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledForDynamicCron implements SchedulingConfigurer {
    private String cpucron  = "0 */1 * * * ?";
    private String memcron  = "0 */1 * * * ?";
    private String diskcron = "0 */1 * * * ?";
    private Monitor monitor = new Monitor();

    public String getCpuCron() {
        return cpucron;
    }

    public void setCpuCron(String cron) {
        this.cpucron = cron;
    }

    public String getMemCron() {
        return memcron;
    }

    public void setMemCron(String cron) {
        this.memcron = cron;
    }

    public String getDiskCron() {
        return diskcron;
    }

    public void setDiskCron(String cron) {
        this.diskcron = cron;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    monitor.monitorCpuusge();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(cpucron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });

        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    monitor.monitorMem();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(memcron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });

        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    monitor.monitorDiskIO();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(diskcron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }
}
