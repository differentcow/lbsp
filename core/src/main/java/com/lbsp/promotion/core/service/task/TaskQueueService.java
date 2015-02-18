package com.lbsp.promotion.core.service.task;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.TaskQueue;
import com.lbsp.promotion.entity.response.TaskQueueRsp;

import java.util.List;

public interface TaskQueueService<T> extends BaseService<T> {

    public static final String TASK_STATUS_RUN = "RUNNING";
    public static final String TASK_STATUS_PAUSE = "PAUSE";
    public static final String TASK_STATUS_NORMAL = "NORMAL";
    public static final String TASK_OPERATE_RUN = "run";
    public static final String TASK_OPERATE_PAUSE = "pause";
    public static final String TASK_OPERATE_RESTORE = "restore";

    /**
     * 查询任务队列集合
     *
     * @param rsp
     * @return
     */
    List<TaskQueueRsp> getTaskQueueList(TaskQueueRsp rsp);

    /**
     * 查询任务队列集合总数
     *
     * @param rsp
     * @return
     */
    int getTaskQueueListCount(TaskQueueRsp rsp);

    /**
     * 更改状态
     *
     * @param status
     * @param id
     * @param userId
     * @return
     */
    boolean updateStatus(String status, String id, String userId);


    /**
     * 更新时间策略
     *
     * @param expression
     * @param text
     * @param id
     * @param userId
     * @return
     */
    boolean updateCornExpression(String expression, String text, String id, String userId);


    /**
     * 插入任务队列
     *
     * @param queue
     * @return
     */
    boolean insertTaskQueue(TaskQueue queue);

    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    boolean deleteTask(String id);

}
