package org.jmqtt.monitor;

//@Service
//public class ScheduledForDynamicCron implements SchedulingConfigurer {
    //private String cpucron  = "0 */1 * * * ?";
    //private String memcron  = "0 */1 * * * ?";
    //private String diskcron = "0 */1 * * * ?";


/*
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
*/

/*    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            Monitor monitor = new Monitor();
            @Override
            public void run() {
                try {
                    monitor.monitorCpuusge();
                    int cpusendnum = monitor.cpusendnum;
                    if(cpusendnum < 4){*/
                        //setCpuCron("0 */1 * * * ?");
                    //} else if(cpusendnum < 10){
                     //   setCpuCron("0 */10 * * * ?");
                   // } else {
                   //     setCpuCron("0 0 */1 * * ?");
                   // }
/*                } catch (Exception e) {
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
            Monitor monitor = new Monitor();
            @Override
            public void run() {
                try {
                    monitor.monitorMem();
                    int memsendnum = monitor.memsendnum;
                    if(memsendnum < 4){*/
                   //     setMemCron("0 */1 * * * ?");
                   // } else if(memsendnum < 10){
                  //      setMemCron("0 */10 * * * ?");
                   // } else {
                   //     setMemCron("0 0 */1 * * ?");
/*                    }
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
        });*/
/*
        taskRegistrar.addTriggerTask(new Runnable() {
            Monitor monitor = new Monitor();
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

 */


   // }
//}
