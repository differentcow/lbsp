package com.lbsp.promotion.entity.model;

import com.lbsp.promotion.entity.table.annotation.MyTable;

@MyTable("task_queue")
public class TaskQueue extends BaseModel{
    private String task_name;

    private String cron_expression;

    private String cron_text;

    private String task_status;

    private String desc;

    private String task_class;

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getTask_class() {
        return task_class;
    }

    public void setTask_class(String task_class) {
        this.task_class = task_class;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }

    public String getCron_text() {
        return cron_text;
    }

    public void setCron_text(String cron_text) {
        this.cron_text = cron_text;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}