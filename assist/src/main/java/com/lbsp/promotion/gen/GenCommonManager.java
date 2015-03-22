package com.lbsp.promotion.gen;

import com.lbsp.promotion.gen.GenUtil;
import com.lbsp.promotion.gen.assist.AssistTemplate;
import com.lbsp.promotion.gen.template.controller.ControllerTemplate;
import com.lbsp.promotion.gen.template.dao.DaoTemplate;
import com.lbsp.promotion.gen.template.model.ModelField;
import com.lbsp.promotion.gen.template.model.ModelTemplate;
import com.lbsp.promotion.gen.template.service.ServiceTemplate;
import com.lbsp.promotion.gen.template.xml.SQLXmlTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by HLJ on 2015/3/22.
 */
public class GenCommonManager {

    private ResultSet rs = null;
    private Connection conn = null;
    private PreparedStatement preparedStatement = null;

    private String modelPackage;
    private String daoPackage;
    private String servicePackage;
    private String controllerPackage;
    private String modelPath;
    private String daoPath;
    private String xmlPath;
    private String servicePath;
    private String controllerPath;
    private String tableComment;
    private String tableName;
    private String databasePath;
    private String batPath;

    private List<AssistTemplate> queue;

    private static final String DATABASE_CORE_PATH = "core.properties";
    private static final String MAVEN_RUN = "run-mvn.bat";

    private String url;
    private String password;
    private String username;
    private String drive;

    private static Log logger = LogFactory.getLog(GenCommonManager.class);

    public GenCommonManager(){
        /**
         * 获取数据库信息
         */
        try {
            logger.info("loading the properties.");
            Properties properties = new Properties();
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(DATABASE_CORE_PATH);
            properties.load(in);

            /**
             * 获取数据库必须连接信息
             *
             */
            logger.info("loading the Database connection information.");
            url = properties.getProperty("jdbc.url","");
            drive = properties.getProperty("jdbc.driverClassName","");
            username = properties.getProperty("jdbc.username","");
            password = properties.getProperty("jdbc.password","");

            logger.info("loading the package information.");
            modelPackage = properties.getProperty("model.package","");
            daoPackage = properties.getProperty("dao.package","");
            servicePackage = properties.getProperty("service.package","");
            controllerPackage = properties.getProperty("controller.package","");

            logger.info("loading the path information.");
            modelPath = properties.getProperty("model.path","");
            daoPath = properties.getProperty("dao.path","");
            xmlPath = properties.getProperty("xml.path","");
            servicePath = properties.getProperty("service.path","");
            controllerPath = properties.getProperty("controller.path","");
            databasePath = properties.getProperty("database.path","");
            batPath = properties.getProperty("bat.path","");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Exception:" + e.getMessage());
        }
        queue = new ArrayList<AssistTemplate>();
    }

    public void setDaoPath(String daoPath) {
        this.daoPath = daoPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    /**
     * 初始化要生成的代码内容
     *
     * @param table --- 表名
     * @param isGen --- 生成代码内容后是否输出文件
     * @param parentCode --- 如果为空，不会自动添加页面，功能访问权限（建议添加），这里的值是第一级页面的code值
     * @param isCompile --- 完成上述功能后，是否自动编译项目
     * @throws Exception
     */
    public void build(String table,boolean isGen,String parentCode,boolean isCompile) throws Exception{
        logger.info("connecting the database...");
        makeConnection();
        logger.info("creating the model...");
        createModel(table);
        logger.info("creating the sql mapper xml...");
        createXml(table);
        logger.info("creating the dao...");
        createDao(table);
        logger.info("creating the service...");
        createService(table);
        logger.info("creating the controller...");
        createController(table);
        if (StringUtils.isNotBlank(parentCode)){
            logger.info("creating the access list...");
            mapAccess(parentCode);
        }
        if (isGen){
            logger.info("generating the file ...");
            GenUtil.batchGenFile(queue);
        }
        if (isCompile){
            logger.info("starting mvn...");
            URL url = this.getClass().getClassLoader().getResource(MAVEN_RUN);
            runbat(url.getFile());
        }
        logger.info("close the connection.");
        closeConnection();
    }

    public static void main(String[] arg){
        GenCommonManager manager = new GenCommonManager();
        URL url = manager.getClass().getClassLoader().getResource(MAVEN_RUN);
        manager.runbat(url.getFile());
    }

    private void mapAccess(String parent) throws Exception{
        StringBuffer sql = new StringBuffer("select id from page_operate where code = '");
        sql.append(parent).append("'");
        preparedStatement = conn.prepareStatement(sql.toString());
        rs = preparedStatement.executeQuery();
        sql.setLength(0);
        //查询制定的父页面ID
        if(rs.next()){
            Integer id = rs.getInt("id");
            sql.append("select max(sort_index) as sortIndex from page_operate where parent_id = ").append(id);
            preparedStatement = conn.prepareStatement(sql.toString());
            rs = preparedStatement.executeQuery();
            sql.setLength(0);
            //查询父页面下的最大排序号
            if (rs.next()){
                Integer max = rs.getInt("sortIndex");
                List<String> insertSql = new ArrayList<String>();
                //插入页面数据
                long now = new java.util.Date().getTime();
                String insertPage = "insert into page_operate(code,parent_id,parent_code,`name`,sort_index,create_time,update_time,create_user,update_user) VALUES";
                sql.append(insertPage).append("('").append(tableName).append("',").append(id).append(",'").append(parent)
                   .append("','").append(tableComment).append("',").append(max).append(",").append(now).append(",")
                   .append(now).append(",1,1)");
                preparedStatement = conn.prepareStatement(sql.toString());
                preparedStatement.execute();
                insertSql.add(sql.toString());
                sql.setLength(0);
                sql.append("select id from page_operate where code = '").append(tableName).append("' and parent_id = ").append(id);
                preparedStatement = conn.prepareStatement(sql.toString());
                rs = preparedStatement.executeQuery();
                sql.setLength(0);
                //如果插入成功，开始处理功能访问数据
                if (rs.next()){
                    Integer pageId = rs.getInt("id");
                    //插入功能访问数据
                    String insertFunc = "insert into function_operate(code,`name`,page_id,url,base_url,method,path_param,sort_index,create_time,update_time,create_user,update_user) VALUES ";
                    sql.append(insertFunc).append("('view").append(tableName).append("','查看").append(tableComment).append("',").append(pageId)
                       .append(",'/").append(tableName).append("/lst',NULL,'GET',0,1,").append(now).append(",").append(now).append(",1,1),");
                    sql.append("('view").append(tableName).append("','查看").append(tableComment).append("',").append(pageId).append(",'/")
                       .append(tableName).append("/{id}',NULL,'GET',1,2,").append(now).append(",").append(now).append(",1,1),");
                    sql.append("('add").append(tableName).append("','添加").append(tableComment).append("',").append(pageId).append(",'/")
                       .append(tableName).append("/add',NULL,'POST',0,3,").append(now).append(",").append(now).append(",1,1),");
                    sql.append("('del").append(tableName).append("','删除").append(tableComment).append("',").append(pageId).append(",'/")
                       .append(tableName).append("/del',NULL,'DELETE',0,4,").append(now).append(",").append(now).append(",1,1),");
                    sql.append("('modify").append(tableName).append("','修改").append(tableComment).append("',").append(pageId).append(",'/")
                       .append(tableName).append("/upt',NULL,'PUT',0,5,").append(now).append(",").append(now).append(",1,1)");
                    preparedStatement = conn.prepareStatement(sql.toString());
                    preparedStatement.execute();
                    insertSql.add(sql.toString());
                    sql.setLength(0);
                    //插入映射
                    String insertMap = "insert into privilege(privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_time,update_time,create_user,update_user) ";
                    sql.append(insertMap).append(" select 'role',1, 'page',id,'E',").append(now).append(",").append(now).append(",1,1 from page_operate where id = ").append(pageId);
                    preparedStatement = conn.prepareStatement(sql.toString());
                    preparedStatement.execute();
                    insertSql.add(sql.toString());
                    sql.setLength(0);
                    sql.append(insertMap).append(" select 'role',1, 'function',id,'E',").append(now).append(",").append(now).append(",1,1 from function_operate where code in ('")
                       .append("view").append(tableName).append("','").append("del").append(tableName).append("','").append("modify").append(tableName).append("','")
                       .append("add").append(tableName).append("')");
                    preparedStatement = conn.prepareStatement(sql.toString());
                    preparedStatement.execute();
                    insertSql.add(sql.toString());
                    sql.setLength(0);
                    //如果初始化sql文件路径不为空，则追加内容到初始化文件
                    if (StringUtils.isNotBlank(databasePath)){
                        String date = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new java.util.Date());
                        sql.append("\n\n").append("-- ------------------").append(date).append(" support content------------------\n");
                        for (String s:insertSql){
                            sql.append("\n").append(s).append(";\n");
                        }
                        sql.append("\n").append("-- ------------------").append(date).append(" support content------------------\n");
                        GenUtil.genFile(databasePath,sql.toString(),true,"UTF-8");
                    }
                }
            }
        }
    }

    public List<AssistTemplate> getGenListData(){
        return queue;
    }

    private void makeConnection() throws ClassNotFoundException, SQLException {
        /**
         * 连接数据库
         */
        Class.forName(drive);
        conn = DriverManager.getConnection(url, username, password);
    }

    private List<String> filterKey = Arrays.asList("id","create_time","create_user","update_time","update_user");

    private void closeConnection(){
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

    public void runbat(String batName) {
        InputStream in = null;
        try {
            Process ps = Runtime.getRuntime().exec(batName);
            in = ps.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                logger.info(line);// 如果你不需要看输出，这行可以注销掉
            }
            /*int c;
            while ((c = in.read()) != -1) {

            }
            in.close();*/
            ps.waitFor();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("ok.");
    }

    /**
     * 创建Model文件
     *
     * @param table
     * @throws java.sql.SQLException
     */
    private void createModel(String table) throws SQLException {
        //1,扫描model文件,并记录已存在的model文件
        File dir = new File(modelPath);
        String[] javaFiles = null;
        if(dir.isDirectory()){
            javaFiles = dir.list(new FilenameFilter(){
                public boolean accept(File f , String name){
                    return name.endsWith(".java");
                }
            });
        }
        if (javaFiles == null){
            return;
        }

        //2,查询数据库对应的表
        StringBuffer sql = new StringBuffer("select count(1) as cnt,table_comment as comment from information_schema.tables where table_schema = 'db_lbsp' and table_name = '");
        sql.append(table).append("'");
        preparedStatement = conn.prepareStatement(sql.toString());
        rs = preparedStatement.executeQuery();
        sql.setLength(0);
        //3,判断是否存在表
        if (rs.next() && rs.getInt("cnt") > 0){
            this.tableComment = StringUtils.isNotBlank(rs.getString("comment"))?rs.getString("comment"):table;
            //创建model模板
            ModelTemplate model = new ModelTemplate(table,modelPackage);
            String fileName = model.getFileName();
            this.tableName = fileName.trim().toLowerCase();
            for (String javaFile : javaFiles){
                if (javaFile.equals(fileName+".java")){
                    throw new RuntimeException("the table:["+table+"] had created.\n");
                }
            }
            //查询对应表所有的column，以及其对应类型
            sql.append("SELECT column_name as columnName, DATA_TYPE as dataType from information_schema.columns where table_schema = 'db_lbsp' and table_name = '");
            sql.append(table).append("'");
            preparedStatement = conn.prepareStatement(sql.toString());
            rs = preparedStatement.executeQuery();
            sql.setLength(0);
            while (rs.next()){
                if (!filterKey.contains(rs.getString("columnName"))){
                    model.addFields(new ModelField(rs.getString("columnName"),rs.getString("dataType")));
                }
            }
            queue.add(new AssistTemplate("model",model.genModel(),modelPath + model.getFileName(),"java"));
        }else{
            throw new RuntimeException("find not the table:["+table+"].\n");
        }
    }

    private void createXml(String table){
        SQLXmlTemplate xml = new SQLXmlTemplate(table);
        queue.add(new AssistTemplate("xml",xml.genXml(),xmlPath + xml.getFileName(),"xml"));
    }

    /**
     * 创建Dao文件
     *
     * @param table
     */
    private void createDao(String table){
        DaoTemplate dao = new DaoTemplate(table,daoPackage);
        queue.add(new AssistTemplate("dao",dao.genDao(),daoPath + dao.getFileName(),"java"));
    }

    /**
     * 创建Service文件
     *
     * @param table
     */
    private void createService(String table){
        ServiceTemplate service = new ServiceTemplate(table,servicePackage);
        String dirPath = servicePath + service.getSubDir();
        GenUtil.genDir(dirPath);
        String realPath = dirPath + "/" + service.getSrvFilePath();
        queue.add(new AssistTemplate("service",service.genService(),realPath,"java"));
        realPath = dirPath + "/" + service.getSrvImplFilePath();
        queue.add(new AssistTemplate("serviceImpl",service.genServiceImpl(),realPath,"java"));
    }

    /**
     * 创建Controller文件
     *
     * @param table
     */
    private void createController(String table){
        ControllerTemplate controller = new ControllerTemplate(table,controllerPackage);
        String dirPath = controllerPath + controller.getSubdir();
        GenUtil.genDir(dirPath);
        String realPath = dirPath + "/" + controller.getFileName();
        queue.add(new AssistTemplate("controller",controller.genController(),realPath,"java"));
    }

}
