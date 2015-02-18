package com.lbsp.promotion.entity.constants;

import java.math.BigDecimal;

public class GenericConstants {
	public static final String AUTHKEY = "authKey";
    public static final String LBSP_ADMINISTRATOR_ID = "000000000001";
    public static final String LBSP_PRIVILEGE_ACCESS_TYPE_PAGE = "page";
    public static final String LBSP_PRIVILEGE_OPERATION_ENABLED = "E";
    public static final String LBSP_PRIVILEGE_OPERATION_DISABLED = "D";
    public static final String LBSP_PRIVILEGE_ACCESS_TYPE_FUNC = "function";
    public static final String LBSP_PRIVILEGE_MASTER_TYPE_USER = "user";
    public static final String LBSP_PRIVILEGE_MASTER_TYPE_ROLE = "role";
	public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
	public static final String APPLICATION_FORM_JSON_VALUE = "application/json";
	public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	public static final int HTTP_STATUS_SUCCESS = 200;
    public static final int HTTP_STATUS_FAILURE = 500;
    public static final int LBSP_STATUS_SUCCESS = 200000;
    public static final int LBSP_STATUS_LOGIN_CERTIFY_ERROR = 500098;
    public static final int LBSP_STATUS_FAILURE = 500000;
    public static final String TEXT_CODE_UTF_8 = "UTF-8";
    public static final String TEXT_CODE_GBK = "GBK";

    public final static String RESOURCES_FIELD_FUNID = "funcId";
    public final static String RESOURCES_FIELD_PARENTID = "parentId";
    public final static String RESOURCES_FIELD_TYPE = "type";
    public final static String RESOURCES_FIELD_NAME = "name";
    public final static String RESOURCES_FIELD_URL = "url";
    public final static String RESOURCES_FIELD_INDEX = "index";
    public final static String RESOURCES_FIELD_ICON = "icon";
    public final static String RESOURCES_FIELD_TARGET = "target";
    public final static String RESOURCES_XML_NAME = "resource-map.xml";

    public static final String SYSTEM_TITLE_TEXT = "ESL管理平台";

	//全局计划任务多久检查一次
	public static final long SCHEDULE_CHECK_INTEVAL = 1 * 60 * 1000;
	
	//分页默认配置
	public static final int DEFAULT_LIST_PAGE_SIZE = 20;
	public static final int DEFAULT_LIST_PAGE_MAX_SIZE = 50;
	public static final int DEFAULT_LIST_PAGE_INDEX = 0;
	
	//结算默认阀值
	public static final BigDecimal SETTLE_AMOUNT_LIMIT = BigDecimal.valueOf(2000);
	//强制结算周期（天）
	public static final int SETTLE_FORCE_DAYS	 = 30;
	//每次处理记录数
	public static final int SETTLE_OI_COUNT = 100;
	public static final int EXPIRE_OI_COUNT = 100;
	//每次处理企业帐户数
	public static final int SETTLE_EP_COUNT = 100;
	//每次处理支付超时订单数
	public static final int PAY_EXPIRE_COUNT = 100;
	//每次处理待办事项企业数
	public static final int WAIT_WORK_COUNT = 100;
	//每次处理待办事项保存在缓存中的秒数
	public static final long WAIT_WORK_EXPIRES = 24*60*60;
	public static final String WORKSCHEDULE_IN_REDIS_PREFIX = "workschedule";
	public static final String REQUEST_AUTH = "REQUEST_AUTH";
	
	public static final String NOT_CHECK = "/ws";
	
	public static final String ACCESS_CHECK = "/service";
	
	public static final String COLNAME_EXT_FIELD 	= "ExtFields";
	public static final String COLNAME_PROD 			= "prod";
	public static final String COLNAME_PROD_SUB 	= "prodSub";
	public static final String COLNAME_ORDER = "order";
	
	
	//任伤重试最大次数
	public static final Integer MAX_TIMES_INFINITY=-1;
	public static final Integer MAX_TIMES_ONCE=1;
	public static final Integer MAX_TIMES_DEFAULT=10;
	
	//任务最长执行间隔时间
	public static final Long MAX_INTERVAL=4*60*60*1000L; //unit ms
	
	
	//基本权限角色的ID
//	public static final Long BASE_ROLE_ID = -1L;
	
	//redis过期默认时间
	public static final Long DEFAULT_EXPIRE_TIME = 24*60*60L;
	//redis key prefix
	
	public static final String MSG_REDIS_KEY_PREFIX = "MSG";
		
	public static final String RS_REDIS_KEY_PREFIX ="RS";
	
	public static enum RefundReason{
		NORMAL,
		EXPIRY,
		ORDER_CANCEL
	}
}
