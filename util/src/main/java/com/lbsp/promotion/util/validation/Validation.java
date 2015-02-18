package com.lbsp.promotion.util.validation;

import com.lbsp.promotion.util.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {


	// Regex expression: REX_INTEGER

	public static final String REX_INTEGER = "^-?\\\\d+$";

	// Regex expression: REX_BOOLEAN

	public static final String REX_BOOLEAN = "^0|1|False|True|false|true$";

	// Regex expression: REX_NUMERIC

	public static final String REX_NUMERIC = "^-?\\\\d+(\\\\.\\\\d+)?$";

	// Regex expression: REX_DATE

	public static final String REX_DATE = "^\\\\d{2,4}[-\\\\/]\\\\d{1,2}[-\\\\/]\\\\d{1,4}$";

	// Regex expression: REX_DATETIME

	public static final String REX_DATETIME = "^\\\\d{2,4}[-\\\\/]\\\\d{1,2}[-\\\\/]\\\\d{1,4}(?:\\\\d{1,2}:\\\\d{1,2}:\\\\d{1,2})?$";

	// Regex expression: REX_IPV4

	public static final String REX_IPV4 = "^(\\\\d{1,3})\\\\.(\\\\d{1,3})\\\\.(\\\\d{1,3})\\\\.(\\\\d{1,3})$";

	// Regex expression: REX_IPV4 with *, 192.168.*.*

	public static final String REX_IPV4_ANONYMS = "^(\\\\d{1,3})\\\\.(\\\\d{1,3}|\\\\*)\\\\.(\\\\d{1,3}|\\\\*)\\\\.(\\\\d{1,3}|\\\\*)$";

	// Regex expression: REX_EMAIL

	public static final String REX_EMAIL = "^[0-9a-zA-Z]+([\\\\-_\\\\.][0-9a-zA-Z]+)*\\\\[0-9a-zA-Z]+(\\\\-[0-9a-zA-Z]+)*(\\\\.[0-9a-zA-Z]+(\\\\-[0-9a-zA-Z]+)*)+$";

	// Regex expression: REX_HTTP

	public static final String REX_HTTP = "^(REX_HTTP|https):\\\\/\\/[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*(\\.[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*)+(\\:\\d+)?(\\S+)?$";

	// Regex expression: REX_URL

	public static final String REX_URL = "^[a-zA-Z]+:\\/\\/[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*(\\.[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*)+(\\:\\d+)?(\\S+)?$";

	// Regex expression: REX_ID_LIST (e.g. 123,123,123...)

	public static final String REX_ID_LIST = "^\\d+(?:,\\d+)*$";

	// Regex expression: REX_SAFE_REG_NAME (e.g. block characters
	// '"&lt;&gt;,&amp;\\n\\r\\f\\t)

	public static final String REX_SAFE_REG_NAME = "^[^'\"<>,&\\n\\r\\f\\t]+$";

	// Regex expression: REX_SAFE_IMG_TYPE (image/gif, image/pjpeg, image/png,
	// image/bmp)

	public static final String REX_SAFE_IMG_TYPE = "^(?:(?:image/gif)|(?:image/pjpeg)|(?:image/png)|(?:image/bmp))$";

	// Regex expression: REX_SAFE_IMG_EXT (gif|jpeg|jpg|png|bmp)

	public static final String REX_SAFE_IMG_EXT = "\\.?(gif|jpeg|jpg|png|bmp)$";

	// Regex expression: REX_PHONE_CA (e.g. 123-456-7890 88888)

	public static final String REX_PHONE_CA = "^\\d{3}\\-\\d{3}\\-\\d{4}( \\d{1,5})?$";

	// Regex expression: REX_POSTALCODE_CA (e.g. M9M 9M9)

	public static final String REX_POSTALCODE_CA = "^[A-Za-z][0-9][A-Za-z] [0-9][A-Za-z][0-9]$";

	// Regex expression: REX_PHONE_CN (e.g. 10086,0731-88888888,13800000000)

	public static final String REX_PHONE_CN = "^(0\\d{2,4}\\-)?[^0]\\d{4,10}(\\-\\d{1,5})?$";

	// Regex expression: REX_POSTALCODE_CA (e.g. 410000)
	public static final String REX_POSTALCODE_CN = "^\\d{6}$";
	
	// Regex expression: REX_MOBILE (e.g. 13873100000)
	public static final String REX_MOBILE_PHONE = "^1\\d{10}$";
	/*
	 * 是否空字符串
	 * 
	 * @param c 文本或其它基本数字类型对象
	 */
	public static boolean isEmpty(Object c) {
		return c == null || c.toString().trim().equals("");
	}
	// 校验
	// @param c 字符串
	// return 异常集合，返回NULL为没有异常
	public static List<Exception> verify(boolean throwException, ValidationItem[] items) throws Exception{
		List<Exception> results = new ArrayList<Exception>();
		for (ValidationItem item : items){
			String msg = null;
			if (!isEmpty(item.getValue())){
				if (item.isRequired()){
					msg = "不能为空！";
				}else if (item.getRule() == ValidationRule.INTEGER){
					if (!isInteger(item.getValue().trim()))
						msg = "不是正确的整数格式！";
					else if (item.getMin() != null && Integer.parseInt(item.getValue()) < Integer.parseInt(item.getMin()))
						msg = "的值不能小于"+item.getMin()+"！";
					else if (item.getMax() != null && Integer.parseInt(item.getValue()) > Integer.parseInt(item.getMax()))
						msg = "的值不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.NUMERIC){
					if (!isNumeric(item.getValue().trim()))
						msg = "不是正确的数字格式！";
					else if (item.getMin() != null && Double.parseDouble(item.getValue()) < Double.parseDouble(item.getMin()))
						msg = "的值不能小于"+item.getMin()+"！";
					else if (item.getMax() != null && Double.parseDouble(item.getValue()) > Double.parseDouble(item.getMax()))
						msg = "的值不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.DATE){
					if (!isDate(item.getValue().trim()))
						msg = "不是正确的日期格式！";
					else if (item.getMin() != null && Convert.stringToDate(item.getValue(), "yyyy-MM-dd").before(Convert.stringToDate(item.getMin(), "yyyy-MM-dd")))
						msg = "的值不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && Convert.stringToDate(item.getValue(), "yyyy-MM-dd").after(Convert.stringToDate(item.getMax(), "yyyy-MM-dd")))
						msg = "的值不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.DATETIME){
					if (!isDateTime(item.getValue().trim()))
						msg = "不是正确的时间格式！";
					else if (item.getMin() != null && Convert.stringToDate(item.getValue(), "yyyy-MM-dd hh:mi:ss").before(Convert.stringToDate(item.getMin(), "yyyy-MM-dd hh:mi:ss")))
						msg = "的值不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && Convert.stringToDate(item.getValue(), "yyyy-MM-dd hh:mi:ss").after(Convert.stringToDate(item.getMax(), "yyyy-MM-dd hh:mi:ss")))
						msg = "的值不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.EMAIL){
					if (!isEmail(item.getValue().trim()))
						msg = "不是正确的电子邮箱格式！";
					else if (item.getMin() != null && item.getValue().length() < Integer.parseInt(item.getMin()))
						msg = "的字符长度不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && item.getValue().length() > Integer.parseInt(item.getMax()))
						msg = "的字符长度不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.MOBILE_PHONE){
					if (!isMobilePhone(item.getValue().trim()))
						msg = "不是正确的手机号码格式！";
					else if (item.getMin() != null && item.getValue().length() < Integer.parseInt(item.getMin()))
						msg = "的字符长度不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && item.getValue().length() > Integer.parseInt(item.getMax()))
						msg = "的字符长度不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.PHONE_CN){
					if (!isPhoneCN(item.getValue().trim()))
						msg = "不是正确的电话号码格式！";
					else if (item.getMin() != null && item.getValue().length() < Integer.parseInt(item.getMin()))
						msg = "的字符长度不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && item.getValue().length() > Integer.parseInt(item.getMax()))
						msg = "的字符长度不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.HTTP){
					if (!isPhoneCN(item.getValue().trim()))
						msg = "不是正确的网站址格式！";
					else if (item.getMin() != null && item.getValue().length() < Integer.parseInt(item.getMin()))
						msg = "的字符长度不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && item.getValue().length() > Integer.parseInt(item.getMax()))
						msg = "的字符长度不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.URL){
					if (!isPhoneCN(item.getValue().trim()))
						msg = "不是正确的网页链接格式！";
					else if (item.getMin() != null && item.getValue().length() < Integer.parseInt(item.getMin()))
						msg = "的字符长度不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && item.getValue().length() > Integer.parseInt(item.getMax()))
						msg = "的字符长度不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.SAFE_IMG_EXT){
					if (!isSafeImgExt(item.getValue().trim()))
						msg = "不是正确的图片格式！";
				}else if (item.getRule() == ValidationRule.SAFE_IMG_TYPE){
					if (!isSafeImgType(item.getValue().trim()))
						msg = "不是正确的图片格式！";
				}else if (item.getRule() == ValidationRule.SAFE_REG_NAME){
					if (!isSafeRegName(item.getValue().trim()))
						msg = "不能包含字符：^'\"<>,&\\n\\r\\f\\t";
					else if (item.getMin() != null && item.getValue().length() < Integer.parseInt(item.getMin()))
						msg = "的字符长度不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && item.getValue().length() > Integer.parseInt(item.getMax()))
						msg = "的字符长度不能大于"+item.getMax()+"！";
				}else if (item.getRule() == ValidationRule.BOOLEAN){
					if (!isBoolean(item.getValue().trim()))
						msg = "不是正确的布尔格式！";
				}else if (item.getRule() == ValidationRule.IPV4){
					if (!isBoolean(item.getValue().trim()))
						msg = "不是正确的IP格式！";
				}else if (item.getRule() == ValidationRule.IPV4_ANONYMS){
					if (!isBoolean(item.getValue().trim()))
						msg = "不是正确的IP格式！";
				}else{
					if (item.getMin() != null && item.getValue().length() < Integer.parseInt(item.getMin()))
						msg = "的字符长度不能小于"+item.getMin()+"！";
					else if (item.getMin() != null && item.getValue().length() > Integer.parseInt(item.getMax()))
						msg = "的字符长度不能大于"+item.getMax()+"！";
				}
				if (msg != null){
					Exception exception = new Exception("[" + item.getFieldName() + "]" + msg);
					if (throwException)
						throw exception;
					results.add(exception);
				}
			}
		}
		return results;
	}
	// isMobilePhone
	// @param c 字符串
	// return
	public static boolean isMobilePhone(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_DATETIME);
	}
	
	// isDateTime
	// @param c 字符串
	// return
	public static boolean isDateTime(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_DATETIME);
	}


	// isInteger
	// @param c 字符串
	// return
	public static boolean isInteger(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_INTEGER);
	}


	// isDate
	// @param c 字符串
	// return
	public static boolean isDate(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_DATE);
	}

	// isNumeric
	// @param c 字符串
	// return
	public static boolean isNumeric(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_NUMERIC);
	}


	// isEmail
	// @param c 字符串
	// return
	public static boolean isEmail(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_EMAIL);
	}


	// isHttp
	// @param c 字符串
	// return
	public static boolean isHttp(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_HTTP);
	}


	// isUrl
	// @param c 字符串
	// return
	public static boolean isUrl(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_URL);
	}


	// isPhoneCA
	// @param c 字符串
	// return
	public static boolean isPhoneCA(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_PHONE_CA);
	}


	// isPhoneCN
	// @param c 字符串
	// return
	public static boolean isPhoneCN(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_PHONE_CN);
	}


	// isPostalcodeCA
	// @param c 字符串
	// return
	public static boolean isPostalcodeCA(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_POSTALCODE_CA);
	}


	// isPostalcodeCN
	// @param c 字符串
	// return
	public static boolean isPostalcodeCN(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_POSTALCODE_CN);
	}


	// isIDList
	// @param c 字符串
	// return
	public static boolean isIDList(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_ID_LIST);
	}


	// isBoolean
	// @param c 字符串
	// return
	public static boolean isBoolean(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_BOOLEAN);
	}


	// isSafeRegName
	// @param c 字符串
	// return
	public static boolean isSafeRegName(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_SAFE_REG_NAME);
	}


	// isSafeImgType
	// @param c 字符串
	// return
	public static boolean isSafeImgType(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_SAFE_IMG_TYPE);
	}


	// isSafeImgExt
	// @param c 字符串
	// return
	public static boolean isSafeImgExt(String c) {
		if (isEmpty(c))
			return false;
		return c.matches(REX_SAFE_IMG_EXT);
	}


	// isIP
	// @param c 字符串
	// return
	public static boolean isIPv4(String c) {
		if (isEmpty(c))
			return false;
		Matcher match = Pattern.compile(REX_IPV4).matcher(c);
		if (match.matches()) {
			int ip1 = Integer.parseInt(match.group(1));
			int ip2 = Integer.parseInt(match.group(2));
			int ip3 = Integer.parseInt(match.group(3));
			int ip4 = Integer.parseInt(match.group(4));
			if (ip1 < 0 || ip1 > 255 || ip2 < 0 || ip2 > 255 || ip3 < 0
					|| ip3 > 255 || ip4 < 0 || ip4 > 255)
				return false;
			else
				return true;
		} else {
			return false;
		}
	}

	// isIPAnonyms, 192.168.1.*
	// @param c 字符串
	// return
	public static boolean isIPv4Anonyms(String c) {
		if (isEmpty(c))
			return false;
		Matcher match = Pattern.compile(REX_IPV4_ANONYMS).matcher(c);
		if (match.matches()) {
			int ip1 = Integer.parseInt(match.group(1).replace('*', '1'));
			int ip2 = Integer.parseInt(match.group(2).replace('*', '1'));
			int ip3 = Integer.parseInt(match.group(3).replace('*', '1'));
			int ip4 = Integer.parseInt(match.group(4).replace('*', '1'));
			if (ip1 < 0 || ip1 > 255 || ip2 < 0 || ip2 > 255 || ip3 < 0
					|| ip3 > 255 || ip4 < 0 || ip4 > 255)
				return false;
			else
				return true;
		} else {
			return false;
		}
	}


	// isSafePassword (Includes at least one small letter and one capital letter
	// and one digit.)
	// @param c 字符串
	// return
	public static boolean isSafePassword(String c) {
		if (isEmpty(c))
			return false;
		if (c.matches("[A-Z]")
				&& c.matches("[a-z]")
				&& c.matches("[0-9]"))
			return true;
		else
			return false;
	}

	// isHexNumber
	// @param c 字符串
	// return
	public static boolean isHexNumber(char c) {
		return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F')
				|| (c >= 'a' && c <= 'f');
	}
}
