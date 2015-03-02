package com.lbsp.promotion.coreplatform.job;

import com.lbsp.promotion.coreplatform.job.factory.QuartzJobFactory;
import com.lbsp.promotion.entity.model.TaskQueue;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hp on 2014/12/8.
 */
public class SchedulerUtil {

    private Scheduler scheduler;

    public SchedulerUtil(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    /**
     * 获取计划运行的任务
     *
     * @return
     * @throws org.quartz.SchedulerException
     */
    public List<TaskQueue> normalScheduleJobTask() throws SchedulerException{
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<TaskQueue> jobList = new ArrayList<TaskQueue>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                TaskQueue tq = new TaskQueue();
                tq.setTask_name(jobKey.getName());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                tq.setTask_status(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    tq.setCron_expression(cronExpression);
                }
                jobList.add(tq);
            }
        }
        return jobList;
    }

    /**
     * 获取正在运行的任务
     *
     * @return
     * @throws org.quartz.SchedulerException
     */
    public List<TaskQueue> runningScheduleJobTask() throws SchedulerException{
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<TaskQueue> jobList = new ArrayList<TaskQueue>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            TaskQueue job = new TaskQueue();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setTask_name(jobKey.getName());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setTask_status(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCron_expression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 添加
     *
     * @param job
     * @throws org.quartz.SchedulerException
     */
    public void add(ScheduleJobTask job) throws SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTaskQueue().getTask_name(), job.getTaskQueue().getTask_class()+"");

        //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        //不存在，创建一个
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getTaskQueue().getTask_name(), job.getTaskQueue().getTask_class()+"").build();
            jobDetail.getJobDataMap().put("scheduleJobTask", job);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getTaskQueue().getCron_expression());

            //按新的cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskQueue().getTask_name(), job.getTaskQueue().getTask_class()+"").withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getTaskQueue().getCron_expression());

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    /**
     * 暂停
     *
     * @param tq
     * @throws org.quartz.SchedulerException
     */
    public void pause(TaskQueue tq) throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(tq.getTask_name(), tq.getTask_class()+"");
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复
     *
     * @param tq
     * @throws org.quartz.SchedulerException
     */
    public void restore(TaskQueue tq) throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(tq.getTask_name(), tq.getTask_class()+"");
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除
     *
     * @param tq
     * @throws org.quartz.SchedulerException
     */
    public void delete(TaskQueue tq) throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(tq.getTask_name(), tq.getTask_class()+"");
        scheduler.deleteJob(jobKey);
    }

    /**
     * 只运行一次
     *
     * @param tq
     * @throws org.quartz.SchedulerException
     */
    public void onlyRunOnce(TaskQueue tq) throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(tq.getTask_name(), tq.getTask_class()+"");
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新时间表达式
     *
     * @param tq
     * @throws org.quartz.SchedulerException
     */
    public void updateCronExpression(TaskQueue tq) throws SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(tq.getTask_name(), tq.getTask_class()+"");
        //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(tq.getCron_expression());
        //按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        //按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

}
