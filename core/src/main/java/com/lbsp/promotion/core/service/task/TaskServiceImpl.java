package com.lbsp.promotion.core.service.task;

import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.Task;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.util.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TaskServiceImpl extends BaseServiceImpl<Task> implements
        TaskService<Task> {


    /**
     * 保存任务
     *
     * @param className
     * @param name
     * @return
     */
    @Transactional
    public boolean saveTask(String className,String name){
        Date date = new Date();
        Task task = new Task();
        task.setId(Security.generateUUIDStr());
        task.setClass_name(className);
        task.setName(name);
        task.setCreate_user(GenericConstants.LBSP_ADMINISTRATOR_ID);
        task.setUpdate_user(GenericConstants.LBSP_ADMINISTRATOR_ID);
        task.setCreate_date(date);
        task.setLast_update_date(date);
        return this.insert(task) > 0;
    }

    /**
     * 删除任务
     *
     * @param task
     * @return
     */
    @Transactional
    public boolean deleteTask(Task task){
        if(task == null)
            return false;
        GenericQueryParam param = new GenericQueryParam();
        if(StringUtils.isNotBlank(task.getId())){
            param.put(new QueryKey("id", QueryKey.Operators.EQ),task.getId());
        }else{
            if(StringUtils.isNotBlank(task.getName())){
                param.put(new QueryKey("name", QueryKey.Operators.EQ),task.getName());
            }
            if(StringUtils.isNotBlank(task.getClass_name())){
                param.put(new QueryKey("class_name", QueryKey.Operators.EQ),task.getClass_name());
            }
        }
        return this.delete(param) > 0;
    }

}
