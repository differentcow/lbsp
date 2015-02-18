package com.lbsp.promotion.coreplatform.job;

import com.lbsp.promotion.core.service.task.TaskQueueService;
import com.lbsp.promotion.core.service.task.TaskService;
import com.lbsp.promotion.coreplatform.annotation.TaskAnnotation;
import com.lbsp.promotion.entity.model.*;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.response.TaskQueueRsp;
import com.lbsp.promotion.util.properties.CustomizedPropertyPlaceholderConfigurer;
import com.lbsp.promotion.util.properties.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by barry on 2014/12/6.
 */
public class LoadTask {

    private TaskService<Task> taskService;

    private TaskQueueService<TaskQueue> taskQueueService;

    private Scheduler scheduler;

    private PropertiesUtil propertiesUtil;

    public LoadTask(TaskService<Task> taskService,TaskQueueService<TaskQueue> taskQueueService,Scheduler scheduler){
        super();
        this.taskService = taskService;
        this.taskQueueService = taskQueueService;
        this.scheduler = scheduler;
        propertiesUtil = new PropertiesUtil();
        String proPath = this.getClass().getResource("/").getFile();
        propertiesUtil.parsePath(proPath+"core.properties");
    }

    /**
     * 初始化工作任务，包括工作任务以及任务队列
     */
    public void init(){
        /**
         * 初始化工作任务类（存放task_class表）,task_class表不应该被管理，因为涉及到JVM内存操作，因此工作任务类不应该被管理，
         * task_class表只有系统初始化时进行数据的持久化（增删改查）
         * 先查找task_class表的记录，在查找预先定义的工作任务类路径，并且读取Class
         * 根据class name 进行比较，在task_class表中持久化
         *
         */
        List<Task> taskList = taskService.findAll(new GenericQueryParam()); //获取数据库中的任务数据
        Map<String,String> classNames = new HashMap<String, String>();
        //加载class name
        for (Task task : taskList){
            classNames.put(task.getClass_name(), "Y");
        }
        //获取工作任务类的package或class路径
        String classPath = propertiesUtil.getValue("task.class.path");
//        String classPath = (String) CustomizedPropertyPlaceholderConfigurer.getContextProperty("task.class.path");

        /**
         * 工作任务类路径可同时描述多个，使用逗号‘,’隔开
         * 如果字符串带.*号说明其是文件夹，非class类
         * 如果字符串没有带.*号，说明是class类，直接解析
         */
        String[] paths = classPath.split(",");
        Set<Class<?>> classSet = new LinkedHashSet<Class<?>>();
        for (String path : paths) {
            if (path.endsWith(".*")) {
                classSet.addAll(getClasses(path.substring(0, path.length() - 2)));
            } else {
                Class s = null;
                try{
                    s = Thread.currentThread().getContextClassLoader().loadClass(path);
                    classSet.add(s);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        /**
         * 获取工作任务类集合后，与数据库的任务数据作对比
         */
        for (Class c : classSet){
            if(c.isAnnotationPresent(TaskAnnotation.class)){
                TaskAnnotation ta = (TaskAnnotation) c.getAnnotation(TaskAnnotation.class);
                //如果数据库对应数据为空，添加到数据库
                //判断数据是否存在对应数据，如不存在，添加到数据库
                String className = c.getName();
                if(classNames.isEmpty() || StringUtils.isBlank(classNames.get(className))){
                    String name = getNameText(ta.value(),c.getSimpleName());
                    taskService.saveTask(className,name);
                }else{
                    classNames.put(className,"N");
                }
            }
        }
        //删除数据库中多余的记录
        for (Map.Entry<String,String> map : classNames.entrySet()){
            if("Y".equals(map.getValue())){
                Task task = new Task();
                task.setClass_name(map.getKey());
                taskService.deleteTask(task);
            }
        }

        /**
         * 初始化任务队列
         */
        List<TaskQueueRsp> list = taskQueueService.getTaskQueueList(new TaskQueueRsp());
        SchedulerUtil util = new SchedulerUtil(scheduler);
        if(list != null && !list.isEmpty()){
            for (TaskQueueRsp tq : list){
                try {
                    Class c = Class.forName(tq.getClass_name());
                    ScheduleJobTask job = (ScheduleJobTask)c.newInstance();
                    job.setTaskQueue(tq);
                    util.add(job);
                    if("PAUSE".equals(tq.getTask_status())){
                        util.pause(tq);
                    }
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }catch (InstantiationException e){
                    e.printStackTrace();
                }catch (SchedulerException e){
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 解析注解中的value值
     *
     * @param key
     * @return
     */
    private String getNameText(String key,String defaultVal){
        if(StringUtils.isNotBlank(key)){
            if(key.startsWith("{") && key.endsWith("}")){
                key = key.substring(1,key.length()-1);
                Object o = propertiesUtil.getValue(key);
                return o == null?key:(String)o;
            }else{
                return key;
            }
        }
        return defaultVal;
    }


    /**
     * 从包package中获取所有的Class
     *
     * @param pack
     * @return
     */
    public Set<Class<?>> getClasses(String pack) {

        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
//                    System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
//                    System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            // log
                                            // .error("添加用户自定义视图类错误 找不到此类的.class文件");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public void findAndAddClassesInPackageByFile(String packageName,
                                                 String packagePath,
                                                 final boolean recursive,
                                                 Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    //classes.add(Class.forName(packageName + '.' + className));
                    //经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
/*
    *//**
     * 加入到Quartz任务队列中
     *
     * @param job
     *//*
    private void insertQuartzQueue(ScheduleJobTask job){
        try {
            //schedulerFactoryBean 由spring创建注入
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(job.getTaskQueue().getTask_name(), job.getTaskQueue().getTask_class());

            //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //不存在，创建一个
            if (null == trigger) {
                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getTaskQueue().getTask_name(), job.getTaskQueue().getTask_class()).build();
                jobDetail.getJobDataMap().put("scheduleJobTask", job);

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getTaskQueue().getCron_expression());

                //按新的cronExpression表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getTaskQueue().getTask_name(), job.getTaskQueue().getTask_class()).withSchedule(scheduleBuilder).build();

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
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }*/

}
