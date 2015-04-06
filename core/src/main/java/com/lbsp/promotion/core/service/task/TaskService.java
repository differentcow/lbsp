package com.lbsp.promotion.core.service.task;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.Task;

public interface TaskService<T> extends BaseService<T> {

	/**
	 * 保存任务
	 * 
	 * @param className
	 * @param name
	 * @return
	 */
	boolean saveTask(String className, String name);

	/**
	 * 删除任务
	 * 
	 * @param task
	 * @return
	 */
	boolean deleteTask(Task task);
}
