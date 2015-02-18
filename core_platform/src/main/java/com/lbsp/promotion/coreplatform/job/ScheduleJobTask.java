package com.lbsp.promotion.coreplatform.job;

import com.lbsp.promotion.entity.model.TaskQueue;

/**
 * Created by barry on 2014/12/6.
 */
public class ScheduleJobTask {

    /**
     * 任务执行方法
     *
     */
    public void execute(){}

    public String groupId(){
        return "ScheduleTask.DefaultGroupID";
    }

    protected TaskQueue taskQueue;

    public void setTaskQueue(TaskQueue queue){
        this.taskQueue = queue;
    }

    public TaskQueue getTaskQueue(){
        return taskQueue;
    }

}
