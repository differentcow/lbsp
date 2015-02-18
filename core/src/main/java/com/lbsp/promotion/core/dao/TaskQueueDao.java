/**
 * 
 */
package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.response.TaskQueueRsp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Barry
 *
 */
public interface TaskQueueDao {

    /**
     * 查询任务队列集合
     *
     * @param rsp
     * @return
     */
    List<TaskQueueRsp> getTaskQueueList(@Param("tq") TaskQueueRsp rsp);

    /**
     * 查询任务队列集合总数
     *
     * @param rsp
     * @return
     */
    int getTaskQueueListCount(@Param("tq") TaskQueueRsp rsp);

}
