package com.lbsp.promotion.util;


import com.lbsp.promotion.util.validation.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {
	/*
	 * convert the IPv4 string to a integer array.
	 * @param c : IPv4 string
	 * @return int[]
	 */
	public static int[] ipv4ToIntArray(String c){
		if (Validation.isEmpty(c))
			return null;
		Matcher match = Pattern.compile(Validation.REX_IPV4).matcher(c);
		if (match.matches()) {
			int ip1 = Integer.parseInt(match.group(1));
			int ip2 = Integer.parseInt(match.group(2));
			int ip3 = Integer.parseInt(match.group(3));
			int ip4 = Integer.parseInt(match.group(4));
			if (ip1 < 0 || ip1 > 255 || ip2 < 0 || ip2 > 255 || ip3 < 0
					|| ip3 > 255 || ip4 < 0 || ip4 > 255)
				return new int[]{ip1,ip2,ip3,ip4};
			else
				return null;
		} else {
			return null;
		}
	}
	//字符串转时间
	public static Date stringToDate(String c, String format){
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			return f.parse(c);
		} catch (ParseException e) {
			return null;
		}
	}
	//时间转字符串
	public static String dateToString(Date c, String format){
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(c);
	}
}
