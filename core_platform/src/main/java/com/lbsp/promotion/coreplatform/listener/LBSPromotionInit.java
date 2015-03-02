package com.lbsp.promotion.coreplatform.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lbsp.promotion.core.service.task.TaskQueueService;
import com.lbsp.promotion.core.service.task.TaskService;
import com.lbsp.promotion.coreplatform.job.LoadTask;
import com.lbsp.promotion.entity.model.Task;
import com.lbsp.promotion.entity.model.TaskQueue;
import com.lbsp.promotion.util.Security;
import com.lbsp.promotion.util.json.JsonMapper;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import scala.util.parsing.combinator.testing.Str;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Barry on 2014/11/21.
 */
public class LBSPromotionInit implements ServletContextListener {

    private static WebApplicationContext context;

    private static final String DATABASE_CORE_PATH = "core.properties";
    private static final String DATABASE_SQL_PATH = "data-init.sql";
    private static final String ORIGIN_CREATOR_INIT = "origin-config.txt";

    private PasswordEncoder passwordEncoder;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        /**
         * 存储上下文
         */
        context = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());

        /**
         * 初始化
         *
         */
        init();

        /**
         * 加载任务
         */
        TaskQueueService<TaskQueue> taskQueueService = context.getBean("taskQueueServiceImpl", TaskQueueService.class);
        TaskService<Task> taskService = context.getBean("taskServiceImpl", TaskService.class);
        Scheduler scheduler = context.getBean("schedulerFactoryBean", Scheduler.class);
        LoadTask task = new LoadTask(taskService,taskQueueService,scheduler);
        task.init();

    }

    private ResultSet rs = null;
    private Connection conn = null;
    private PreparedStatement preparedStatement = null;

    private boolean init(){
        try {
            /**
             * 获取数据库信息
             */
            Properties properties = new Properties();
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(DATABASE_CORE_PATH);
            properties.load(in);

            /**
             * 获取数据库必须连接信息
             *
             */
            String url = properties.getProperty("jdbc.url.orgin","");
            String drive = properties.getProperty("jdbc.driverClassName","");
            String username = properties.getProperty("jdbc.username","");
            String password = properties.getProperty("jdbc.password","");

            /**
             * 连接数据库
             */
            Class.forName(drive);
            conn = DriverManager.getConnection(url, username, password);

            boolean isNeedInit = true;
            //如果存在，说明不需要创建DB
            preparedStatement = conn.prepareStatement("show databases like 'db_lbsp'");
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                //使用该数据库
                preparedStatement = conn.prepareStatement("USE db_lbsp");
                preparedStatement.execute();

                //判断是否存在参数表，如果存在，查找INIT_MARK对应的值是否为Y，如果为Y即为已经初始化，否则未初始化
                preparedStatement = conn.prepareStatement("show tables like 'parameter'");
                rs = preparedStatement.executeQuery();
                if (rs.next()){
                    preparedStatement = conn.prepareStatement("select count(1) as cnt from parameter where type='INIT_MARK' and code = 'Y'");
                    rs = preparedStatement.executeQuery();
                    if(rs.next() && rs.getInt("cnt") > 0){
                        //不需要初始化
                        isNeedInit = false;
                    }
                }
            }

            if(isNeedInit){
                //初始化数据表结构
                initData();

                //初始化创始人权限
                initCreator();

                //删除以前的信息
                preparedStatement = conn.prepareStatement("delete from `parameter` where `type` = 'INIT_MARK'");
                preparedStatement.execute();

                //标记已初始化
                long times = System.currentTimeMillis();
                preparedStatement = conn.prepareStatement("INSERT INTO `parameter` (id,`name`, `code`, `type`, `type_meaning`, `status`, `create_user`, `create_time`, `update_time`, `update_user`) VALUES " +
                        "(1,'是否已初始化', 'Y', 'INIT_MARK', '初始化标记类型', NULL, 1,"+times+", "+times+",1)");
                preparedStatement.execute();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭结果集对象
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            // 关闭PreparedStatement对象
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            // 关闭Connection 对象
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return false;
    }

    private void initCreator(){
        try {
            /**
             * 读取sql内容
             *
             */
            InputStreamReader is = null;
            is = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(ORIGIN_CREATOR_INIT),"UTF-8");
            String line; // 用来保存每行读取的内容
            BufferedReader reader = new BufferedReader(is);
            StringBuffer sb = new StringBuffer("");
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JsonMapper json = JsonMapper.getJsonMapperInstance();
            TypeReference<Map> type = new TypeReference<Map>() {
            };

            Map result = json.readValue(sb.toString(),type);

            //如果代号为200，执行创始人初始化动作
            if((Integer)result.get("code") == 200){
                List<Map<String,String>> maps = (List<Map<String,String>>)result.get("config");
                if (maps != null && !maps.isEmpty()){
                    passwordEncoder = context.getBean("org.springframework.security.crypto.password.StandardPasswordEncoder",PasswordEncoder.class);
                    StringBuffer saveSql = new StringBuffer(" INSERT INTO `user` (`id`,`account`, `username`, `email`, `password`, `security_key`, `security_time`, `create_time`, `update_time`, `create_user`, `status`, `update_user`, `login_time`, `auth_code`, `auth_time`) VALUES ( ");
                    int count = 0;
                    StringBuffer roleS = new StringBuffer("INSERT INTO `user_role` (`user_id`, `role_id`, `create_time`, `create_user`,`update_user`,`update_time`) VALUES ");
                    long times = System.currentTimeMillis();
                    for (Map<String,String> m : maps){
                        if(count == 0){
                            ++count;
                            roleS.append(" (").append(count+1).append(", 1, "+times+", 1,1, "+times+" ) ");
                            String pwd = passwordEncoder.encode(m.get("password"));
                            saveSql.append(count+1).append(", '").append(m.get("name")).append("','")
                                    .append(m.get("name")).append("','").append(m.get("email")).append("','").append(pwd)
                                    .append("',NULL,NULL,"+times+", "+times+",1, 1, 1, NULL, NULL, NULL)");
                        }else{
                            ++count;
                            roleS.append(", (").append(count+1).append(", 1, "+times+", 1,1, "+times+" ) ");
                            String pwd = passwordEncoder.encode(m.get("password"));
                            saveSql.append(",(").append(count+1).append(", '").append(m.get("name")).append("','")
                                    .append(m.get("name")).append("','").append(m.get("email")).append("','").append(pwd)
                                    .append("',NULL,NULL,"+times+", "+times+",1, 1, 1, NULL, NULL, NULL)");
                        }
                    }

//                    System.out.println(roleS.toString());

                    preparedStatement = conn.prepareStatement(" delete from `user` where id <> 1 ");
                    preparedStatement.execute();

                    preparedStatement = conn.prepareStatement(saveSql.toString());
                    preparedStatement.execute();

                    preparedStatement = conn.prepareStatement(" delete from `user_role` where user_id <> 1 ");
                    preparedStatement.execute();

                    preparedStatement = conn.prepareStatement(roleS.toString());
                    preparedStatement.execute();

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initData(){
        try {

            List<String> sql = new ArrayList<String>();

            /**
             * 读取sql内容
             *
             */
            InputStreamReader is = null;
            is = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(DATABASE_SQL_PATH),"UTF-8");
            String line; // 用来保存每行读取的内容
            BufferedReader reader = new BufferedReader(is);
            StringBuffer sb = new StringBuffer("");
            boolean flag = false;
            while ((line = reader.readLine()) != null) { // 如果 line 为空说明读完了
                if(line.toUpperCase().startsWith("CREATE") || line.toUpperCase().startsWith("USE")
                        || line.toUpperCase().startsWith("INSERT INTO") || line.toUpperCase().startsWith("DELETE FROM")){
                    flag = true;
                }
                if(flag){
                    sb.append(line);
                }
                if(flag && line.endsWith(";")){
                    flag = false;
                    sql.add(sb.toString());
                    sb.setLength(0);
                }
            }

            for (String s : sql) {
                preparedStatement = conn.prepareStatement(s);
                preparedStatement.execute();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public static WebApplicationContext getApplicationContext(){
        return context;
    }

}
