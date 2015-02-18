package com.lbsp.promotion.entity.enumeration;

import com.lbsp.promotion.entity.exception.ParamValueException;

import java.lang.reflect.Field;

public class Params {
	public static enum ParamCategory {
		GenericStatus, GenericGender, ParamType, EnterpriseStatus, CustomerStatus, LocationArea;
	}
	/**
	 * 实时统计分类枚举
	 */
	public static enum RealStatsCategory{
		TotalProductCount,
		TotalRefundingCount,
		TotalRefundCount,
		TotalOrderCount,
		TotalUnprocComplaintCount,
		TotalCustCount,
	}
	
	public static void main(String args[]){
		Params.isCorrect(ProductEvlLevel.class, 2500);
	}
	public static void isCorrect(Class<?> cls, Integer code) {
		if (code == null)
			return;
		boolean b = false;
		Field[] fields = cls.getFields();
		Integer start = null;
		Integer end = null;
		for (Field field : fields) {
			try {
				if (field.getName().equals("START_INDEX")) {
					start = (Integer) field.get(cls);
				}
				if (field.getName().equals("END_INDEX")) {
					end = (Integer) field.get(cls);
				}
				if (((Integer) field.get(cls)).equals(code)) {
					b = true;
				}
			} catch (Exception e) {
				throw new RuntimeException("paramsUtil获取field值出错");
			}
		}
		if(start != null && end !=null && code >= start && code <= end){
			return;
		}
		if (b && start == null && end ==null) {
			return;
		}
		throw new ParamValueException("参数值不合法");
	}

	// 通用状态位
	public static class GenericStatus {
		public static final int DISABLED = 0;
		public static final int ENABLED = 1;
	}

	// 性别
	public static class GenericGender {
		public static final int MALE = 10;
		public static final int FEMALE = 11;
	}

	// 参数类型
	public static class ParamType {
		public static final int SYSTEM = 20;
		public static final int CUSTOM = 21;
	}

	// 企业状态位
	public static class EnterpriseStatus {
		public static final int DISABLED = 100;
		public static final int PENDING = 101;
		public static final int WAIT_FOR_AUDIT = 102;
		public static final int APPROVE = 103;
		public static final int DECLINE = 104;
		public static final int VERIFIED =105;
	}

	// 会员状态位
	public static class CustomerStatus {
		public static final int DISABLED = 110;
		public static final int UNACTIVATED = 111;
		public static final int ACTIVE = 112;
	}

	// 角色类型
	public static class EnterpriseRoleType {
		public static final int PROVIDER = 120;
		public static final int PURCHASER = 121;
		public static final int BUSINESS = 122;
		public static final int OPERATOR = 123;
		public static final int PLATFORM = 124;
		public static final int OTHER = 125;
	}

	// 产品类型
	public static class ProductType {
		public static final int TICKET = 130;
		public static final int ROOM = 131;
		public static final int TRAVEL = 132;
		public static final int BUS = 133;
	}

	// 扩展字段使用类型
	public static class ExtendType {
		public static final int PRODUCT = 140;
		public static final int PRODUCT_SUB = 141;
	}

	// 字段类型
	public static class ExtFieldType {
		public static final int TEXTBOX = 150;
		public static final int TEXTAREA = 151;
		public static final int RADIO = 152;
		public static final int CHECKBOX = 153;
		public static final int SELECT = 154;
	}

	// 产品状态
	public static class ProductStatus {
		public static final int DRAFT = 160;
		public static final int WAIT_FOR_AUDIT = 161;
		public static final int APPROVE = 162;
		public static final int DECLINE = 163;
	}

	// 票据类型
	public static class TicketType {
		public static final int TIMES = 170;
		public static final int DAY = 171;
		public static final int MONTH = 172;
		public static final int YEAR = 173;
	}

	// 利润种类
	public static class ProfitType {
		public static final int FIX_PURCHASE_PRICE = 180;
		public static final int FIX_PROFIT = 181;
		public static final int PERCENT_PROFIT = 182;
	}

	// 退款种类
	public static class RefundType {
		public static final int NO_REFUND = 190;
		public static final int FULL_REFUND = 191;
		public static final int FIX_POUNDAGE = 192;
		public static final int PERCENT_POUNDAGE = 193;
	}

	// 定单概要状态
	public static class OrderStatus {
		public static final int BOOKING = 200;
		public static final int NON_PAYMENT = 201;
		public static final int PAID = 202;
		public static final int PARTIAL_REFUND = 203;
		public static final int REFUND = 204;
		public static final int CANCEL = 205;
		public static final int COMPLETED = 206;
	}

	// 付款方式
	public static class PaymentType {
		public static final int CASH = 220;
		public static final int UNION_PAY = 221;
		public static final int TAOBAO = 222;
		public static final int BALANCE = 223;
		public static final int NONE = 229;
	}

	// 票据状态
	public static class TicketStatus {
		public static final int NONE = 230;
		public static final int BOOKING = 231;
		public static final int CONFIRM = 232;
		public static final int NON_SEND = 233;
		public static final int SENT = 234;
		public static final int RETURNING = 235;
		public static final int COMPLETED = 239;
		public static final int CANCEL = 240;
		/*
		public static final int PARTIAL_USED = 235;
		public static final int USED = 236;
		public static final int RETURNING = 237;
		public static final int PARTIAL_RETURN = 238;
		public static final int RETURNED = 239;
		public static final int CANCEL = 240;
		public static final int EXPIRED_UNUSE = 241;
		public static final int EXPIRED_USED = 242;
		*/
	}

	// SMS模版类型
	public static class SMSTemplateType {
		public static final int PURCHASE = 250;
		public static final int ADS = 251;
		public static final int AFTER_SALE = 252;
		public static final int RECHARGE = 253;
	}

	//交易类型
	public static class TransType {
		public static final int PAYMENT = 260;
		public static final int REFUND = 261;
		public static final int RECHARGE = 262; 
		public static final int SETTLE = 263;
	}

	// 定单类型
	public static class OrderType {
		public static final int PORTAL = 270;
		public static final int PLATFORM = 271;
	}

	// 交易状态
	public static class TransStatus {
		public static final int SUCCESS = 280;
		public static final int FAILED = 281;
		public static final int PROCESSING = 282;
		public static final int WAIT_PAY = 283;
	}

	// 定单条目结算状态
	public static class OrderSettleStatus {
		public static final int NON_SETTLE = 290;
		public static final int WAITING = 291;
		public static final int SETTLED = 292;
	}

	// 消息类型
	public static class MsgType {
		public static final int RECHARGE = 300;
		public static final int REFUND = 301;
		public static final int PAYMENT = 302;
		public static final int CONFIRM = 303;
		public static final int CUSTOM = 304;
		public static final int SETTLE = 305;
		public static final int AUDITING = 306;
	}
	/**
	 * 消息状态
	 * @author Ray.Lee
	 *
	 */
	public static class MsgStatus {
		public static final int UNREAD = 310;
		public static final int READED = 311;
	}

	/**
	 * 支付状态
	 * @author Ray.Lee
	 *
	 */
	public static class LogType {
		public static final int ORDER_REFUNDING = 320;
		public static final int ORDER_REFUND = 321;
		public static final int ORDER_CANCEL = 322;
		public static final int ORDER_CONFIRM = 323;
		public static final int RECHARGE = 324;
	}
	
	/**
	 * 
	 * 任务定时类别
	 * @author Ray.Lee
	 */
	public static class TaskType {
		public static final int TICKET_SEND = 330;
		public static final int TICKET_RETURN = 331;
		public static final int EMAIL_SEND = 332;
		public static final int SMS_SEND = 333;
		public static final int SETTLE_CONFIRM = 334;
	}
	
	/**
	 * 
	 * 用户类别
	 * @author Ray.Lee
	 */
	public static class UserType {
		public static final int SYSTEM_ADMIN = 340;
		public static final int ENTERPRISE_ADMIN = 341;
		public static final int BASIC_STAFF = 342;
	}
	
	// 企业类型（景点，餐馆，酒店等等）
	public static class EnterpriseType {
		public static final int HOTEL = 1001;
		public static final int TRAVEL_AGENCY = 1002;
		public static final int TRAVEL_MANAGE = 1000;
		public static final int EAT_HOUSE = 1003;
		public static final int START_INDEX = 1000;
		public static final int END_INDEX = 1999;
	}

	// 二维码公司
	public static class QRCodeCompany {
		public static final int START_INDEX = 2100;
		public static final int END_INDEX = 2199;
		public static final int H80 = 2100;
	}

	// 自定义产品评估类型（风景名胜，古迹，人文景观，酒店客房，中餐，西餐）
	public static class ProductEvlType {
		public static final int START_INDEX = 2200;
		public static final int END_INDEX = 2499;
	}

	// 自定义产品级别评估
	public static class ProductEvlLevel {
		public static final int NO_LEVEL = 2500;
		// 不同ProductEvlType，比如景点: AAAA, AAAAA，酒店：三星级，四星级，五星级
		public static final int START_INDEX = 2500;
		public static final int END_INDEX = 2599;
	}
	//产品上架状态
	public static class ProductPutawayStatus{
		public static final int ON_PUTAWAY = 2600;
		public static final int OFF_PUTAWAY = 2601;
	}
	//取消定购的原因
	public static class OrderCancelReason {
		public static final int START_INDEX = 2700;
		public static final int END_INDEX = 2799;
	}
	//取消定购的原因
	public static class OrderReturnReason {
		public static final int START_INDEX = 2800;
		public static final int END_INDEX = 2899;
	}
	
	// 位置区域列表
	public static class LocationArea {
		public static final int ROOT_PID = 0;
		public static final int START_INDEX = 10000;
		public static final int END_INDEX = 19999;
		// 省份
		public static final int PROV_START_INDEX = 10000;
		public static final int PROV_END_INDEX = 10499;
		// 城市
		public static final int CITY_START_INDEX = 10500;
		public static final int CITY_END_INDEX = 12499;
		// 区县
		public static final int AREA_START_INDEX = 12500;
		public static final int AREA_END_INDEX = 19999;
	}
}