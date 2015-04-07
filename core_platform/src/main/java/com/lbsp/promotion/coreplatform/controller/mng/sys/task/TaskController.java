package com.lbsp.promotion.coreplatform.controller.mng.sys.task;

import com.lbsp.promotion.core.service.task.TaskQueueService;
import com.lbsp.promotion.core.service.task.TaskService;
import com.lbsp.promotion.coreplatform.controller.base.BaseController;
import com.lbsp.promotion.coreplatform.job.ScheduleJobTask;
import com.lbsp.promotion.coreplatform.job.SchedulerUtil;
import com.lbsp.promotion.entity.base.PageInfoRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.model.Task;
import com.lbsp.promotion.entity.model.TaskQueue;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;
import com.lbsp.promotion.entity.response.TaskQueueRsp;
import com.lbsp.promotion.entity.response.UserRsp;
import com.lbsp.promotion.util.validation.Validation;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Barry
 *
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Autowired
    private TaskQueueService<TaskQueue> taskQueueService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private TaskService<Task> taskService;

    /**
     * 获取所有任务内容(任务方法)
     *
     * @return
     */
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    @ResponseBody
    public Object findTaskContents() {
        GenericQueryParam param = new GenericQueryParam();
        param.addSortCond(new SortCond("last_update_date", SortCond.Order.DESC));
        List<Task> list = taskService.findAll(param);
        return this.createBaseResult("查询成功", list);
    }

    /**
     * 操作任务（只运行一次）
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/status/run", method = RequestMethod.GET)
    @ResponseBody
    public Object updateStatus(HttpServletRequest request,@RequestParam(value = "id",required = true)Integer id) {
        if(isRunning(id)){
            return this.createBaseResult(GenericConstants.LBSP_STATUS_SUCCESS,"任务正在运行中", false);
        }
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        TaskQueue tq = taskQueueService.findOne(param);

        if(tq == null){
            return this.createBaseResult(GenericConstants.LBSP_STATUS_FAILURE,"未知错误.", false);
        }
        String taskStatus = null;
        try {
            SchedulerUtil util = new SchedulerUtil(schedulerFactoryBean.getScheduler());
            util.onlyRunOnce(tq);
        }catch (SchedulerException e){
            e.printStackTrace();
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"系统异常.", false);
        }
        return this.createBaseResult("操作成功", true);
    }

    /**
     * 操作任务（暂停/恢复）
     *
     * @param request
     * @param status
     * @param id
     * @return
     */
    @RequestMapping(value = "/status/{status}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateStatus(HttpServletRequest request,@PathVariable("status") String status,@RequestParam(value = "id",required = true)Integer id) {
        if(isRunning(id)){
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"任务正在运行中", false);
        }
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        TaskQueue tq = taskQueueService.findOne(param);

        if(tq == null){
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"未知错误.", false);
        }
        String taskStatus = null;
        try {
            SchedulerUtil util = new SchedulerUtil(schedulerFactoryBean.getScheduler());
            if (TaskQueueService.TASK_OPERATE_PAUSE.equals(status)){
                util.pause(tq);
                taskStatus = TaskQueueService.TASK_STATUS_PAUSE;
            }
            if (TaskQueueService.TASK_OPERATE_RESTORE.equals(status)){
                util.restore(tq);
                taskStatus = TaskQueueService.TASK_STATUS_NORMAL;
            }
        }catch (SchedulerException e){
            e.printStackTrace();
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"系统异常.", false);
        }
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        boolean flag = taskQueueService.updateStatus(taskStatus,id,userId);
        if(flag){
            return this.createBaseResult("操作成功", true);
        }else{
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"操作失败.", false);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object findTaskById(@PathVariable(value = "id") String id) {
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        TaskQueue tq = taskQueueService.findOne(param);
        param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),tq.getTask_class());
        Task task = taskService.findOne(param);
        TaskQueueRsp rsp = new TaskQueueRsp();
        rsp.setTask_name(tq.getTask_name());
        rsp.setTask_status(tq.getTask_status());
        rsp.setId(tq.getId());
        rsp.setTask_content(task.getName());
        rsp.setCron_expression(tq.getCron_expression());
        rsp.setCron_text(tq.getCron_text());
        rsp.setTask_class(tq.getTask_class());
        return this.createBaseResult("查询成功", rsp);
    }

    /**
     * 新增任务信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delTask(HttpServletRequest request) {
        String id = request.getParameter("id");
        if(isRunning(Integer.parseInt(id))){
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"任务正在运行中", false);
        }
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        TaskQueue tq = taskQueueService.findOne(param);
        try {
            SchedulerUtil util = new SchedulerUtil(schedulerFactoryBean.getScheduler());
            util.delete(tq);
        }catch (SchedulerException e){
            e.printStackTrace();
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"系统异常.", false);
        }
        if(taskQueueService.deleteTask(id)){
            return this.createBaseResult("删除任务成功", true);
        }else{
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"操作失败.", false);
        }

    }

    /**
     * 新增任务信息
     *
     * @param tq
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object addTaskQueue(
            HttpServletRequest request,
            @RequestBody TaskQueue tq) {
        //判断参数是否为空
        if(StringUtils.isBlank(tq.getTask_name()) || StringUtils.isBlank(tq.getCron_expression()) || tq.getTask_class() == null){
            return this.createBaseResult(GenericConstants.HTTP_STATUS_SUCCESS,"参数缺失.", false);
        }
        //判断是否已存在同名任务
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("task_name", QueryKey.Operators.EQ),tq.getTask_name());
        TaskQueue temp = taskQueueService.findOne(param);
        if(temp != null){
            return this.createBaseResult(GenericConstants.HTTP_STATUS_SUCCESS,"任务:["+tq.getTask_name()+"]已存在.", false);
        }
        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();
        tq.setCreate_user(userId);
        tq.setUpdate_user(userId);
        tq.setTask_status(TaskQueueService.TASK_STATUS_NORMAL);
        try {
            param = new GenericQueryParam();
            param.put(new QueryKey("id", QueryKey.Operators.EQ), tq.getTask_class());
            Task tmp = taskService.findOne(param);
            Class c = Class.forName(tmp.getClass_name());
            ScheduleJobTask job = (ScheduleJobTask) c.newInstance();
            job.setTaskQueue(tq);
            SchedulerUtil util = new SchedulerUtil(schedulerFactoryBean.getScheduler());
            util.add(job);
        }catch (Exception e){
            e.printStackTrace();
            return this.createBaseResult(GenericConstants.HTTP_STATUS_SUCCESS,"新增任务失败.", false);
        }
        if(taskQueueService.insertTaskQueue(tq)) {
            return this.createBaseResult("新增任务成功", true);
        }else{
            return this.createBaseResult("新增任务失败", false);
        }
    }


    /**
     * 修改策略信息
     *
     * @param rsp
     * @return
     */
    @RequestMapping(value = "/upt", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateCornExpression(
            HttpServletRequest request,
            @RequestParam(value = "id",required = true)Integer id,
            @RequestBody TaskQueue rsp) {

        if(isRunning(id)){
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"任务正在运行中", false);
        }

        if(StringUtils.isBlank(rsp.getCron_expression())){
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"策略为空", false);
        }

        //获取user ID
        UserRsp session = (UserRsp)request.getAttribute(GenericConstants.REQUEST_AUTH);
        Integer userId = session.getUser().getId();

        SchedulerUtil util = new SchedulerUtil(schedulerFactoryBean.getScheduler());
        try {
            util.updateCronExpression(rsp);
        }catch (SchedulerException e){
            e.printStackTrace();
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"系统异常", false);
        }
        if (taskQueueService.updateCornExpression(rsp.getCron_expression(),rsp.getCron_text(),id,userId)) {
            return this.createBaseResult("修改策略成功", true);
        }else{
            return this.createBaseResult(GenericConstants.HTTP_STATUS_FAILURE,"修改策略失败失败", false);
        }
    }

	@RequestMapping(value = "/lst", method = RequestMethod.GET)
	@ResponseBody
	public Object findList(
            @RequestParam(value = "iDisplayStart", required=false,defaultValue=DEFAULT_LIST_PAGE_INDEX) Integer startRecord,
            @RequestParam(value = "iDisplayLength", required=false,defaultValue=DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,
			@RequestParam(value = "task_name", required=false) String task_name,
            @RequestParam(value = "task_stauts", required=false) String task_status) {

        if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
            startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
        if (Validation.isEmpty(maxRecords) || maxRecords < 1)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
        if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

        /*SchedulerUtil util = new SchedulerUtil(schedulerFactoryBean.getScheduler());

        try {
            List<TaskQueue> lists = util.normalScheduleJobTask();
            for (TaskQueue taskQueue : lists){
                System.out.print(taskQueue.getTask_name());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/

        TaskQueueRsp rsp = new TaskQueueRsp();
        rsp.setTask_name(task_name);
        rsp.setTask_status(task_status);
        rsp.setSize(maxRecords);
        rsp.setStart(startRecord);

        Integer count = taskQueueService.getTaskQueueListCount(rsp);
        PageInfoRsp page = new PageInfoRsp(count);

        List<TaskQueueRsp> list = taskQueueService.getTaskQueueList(rsp);
		return this.createBaseResult("查询成功", list, page);
	}

    /**
     * 检查是否运行中
     *
     * @param id
     * @return
     */
    private boolean isRunning(Integer id){
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
        param.put(new QueryKey("task_status", QueryKey.Operators.EQ),TaskQueueService.TASK_STATUS_RUN);
        TaskQueue tq = taskQueueService.findOne(param);
        return tq != null;
    }

}
