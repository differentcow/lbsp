package com.lbsp.promotion.coreplatform.job.factory;

import com.lbsp.promotion.core.service.task.TaskQueueService;
import com.lbsp.promotion.coreplatform.job.ScheduleJobTask;
import com.lbsp.promotion.coreplatform.listener.LBSPromotionInit;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.TaskQueue;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * Created by Barry on 2014/12/6.
 */
public class QuartzJobFactory implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext ctx = LBSPromotionInit.getApplicationContext();
        TaskQueueService<TaskQueue> taskQueueService = (TaskQueueService<TaskQueue>)ctx.getBean("taskQueueServiceImpl");
        //执行任务时，更改任务状态
        ScheduleJobTask scheduleJob = (ScheduleJobTask)context.getMergedJobDataMap().get("scheduleJobTask");
        taskQueueService.updateStatus(TaskQueueService.TASK_STATUS_RUN,scheduleJob.getTaskQueue().getId(), GenericConstants.LBSP_ADMINISTRATOR_ID);
        //任务执行
        scheduleJob.execute();
        //恢复正常状态
        taskQueueService.updateStatus(TaskQueueService.TASK_STATUS_NORMAL,scheduleJob.getTaskQueue().getId(), GenericConstants.LBSP_ADMINISTRATOR_ID);
    }
}
