package com.lbsp.promotion.core.service.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lbsp.promotion.core.dao.TaskQueueDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.model.TaskQueue;
import com.lbsp.promotion.entity.response.TaskQueueRsp;

@Service
public class TaskQueueServiceImpl extends BaseServiceImpl<TaskQueue> implements
		TaskQueueService<TaskQueue> {

	@Autowired
	private TaskQueueDao taskQueueDao;

	/**
	 * 查询任务队列集合
	 * 
	 * @param rsp
	 * @return
	 */
	public List<TaskQueueRsp> getTaskQueueList(TaskQueueRsp rsp) {
		return taskQueueDao.getTaskQueueList(rsp);
	}

	/**
	 * 查询任务队列集合总数
	 * 
	 * @param rsp
	 * @return
	 */
	public int getTaskQueueListCount(TaskQueueRsp rsp) {
		return taskQueueDao.getTaskQueueListCount(rsp);
	}

	/**
	 * 更改状态
	 * 
	 * @param status
	 * @param id
	 * @param userId
	 * @return
	 */
	@Transactional
	public boolean updateStatus(String status, Integer id, Integer userId) {
		TaskQueue tq = new TaskQueueRsp();
		tq.setId(id);
		tq.setUpdate_time(System.currentTimeMillis());
		tq.setUpdate_user(userId);
		tq.setTask_status(status);
		List<String> filter = new ArrayList<String>();
		filter.add("create_time");
		filter.add("create_user");
		filter.add("task_name");
		filter.add("task_class");
		filter.add("corn_expression");
		filter.add("corn_text");
		filter.add("desc");
		return this.update(tq, filter) > 0;
	}

	/**
	 * 更新时间策略
	 * 
	 * @param expression
	 * @param text
	 * @param id
	 * @param userId
	 * @return
	 */
	@Transactional
	public boolean updateCornExpression(String expression, String text,
			Integer id, Integer userId) {
		TaskQueue tq = new TaskQueueRsp();
		tq.setId(id);
		tq.setUpdate_time(System.currentTimeMillis());
		tq.setUpdate_user(userId);
		tq.setCron_expression(expression);
		if (StringUtils.isNotBlank(text)) {
			tq.setCron_text(text);
		}
		List<String> filter = new ArrayList<String>();
		if (StringUtils.isBlank(text)) {
			filter.add("corn_text");
		}
		filter.add("create_time");
		filter.add("create_user");
		filter.add("task_name");
		filter.add("status");
		filter.add("task_class");
		filter.add("desc");
		return this.update(tq, filter) > 0;
	}

	/**
	 * 插入任务队列
	 * 
	 * @param queue
	 * @return
	 */
	@Transactional
	public boolean insertTaskQueue(TaskQueue queue) {
		queue.setUpdate_time(System.currentTimeMillis());
		queue.setCreate_time(System.currentTimeMillis());
		return this.insert(queue) > 0;
	}

	/**
	 * 删除任务
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public boolean deleteTask(String id) {
		return this.delete(id) > 0;
	}

}
