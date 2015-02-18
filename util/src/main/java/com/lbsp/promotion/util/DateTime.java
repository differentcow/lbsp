package com.lbsp.promotion.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

//import com.hentre.all580.entity.share.response.DateRsp;

public class DateTime {
	private static final int Calendar_DATE = -1;
	private static final int HOUR_OF_DAY = 24;
	private static final int Calendar_TIME = 0;

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd");

	/*
	 * get date without time format.
	 * 
	 * @return now date
	 */
	public static Date getDate() {
		try {
			return getDate(new Date());
		} catch (Exception e) {
			// ignore this exception;
		}
		return null;
	}

    public static final String DATE_STRING_WITH_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_STRING_WITH_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_STRING_WITH_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    public static final String DATE_STRING_WITH_YYYY_MM_DD_24HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date转化String
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String paresDateToStr(Date date,String pattern){
        if(date == null)return null;
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * String转化Date
     *
     * @param date
     * @param pattern
     * @return
     * @throws java.text.ParseException
     */
    public static Date paresStrToDate(String date,String pattern) throws ParseException {
        if(StringUtils.isBlank(date))return null;
        return new SimpleDateFormat(pattern).parse(date);
    }

    /*
     * get data String
     * @return now date String
     */
    public static String getTodayString(String parse){
        try {
            return new SimpleDateFormat(parse).format(new Date());
        } catch (Exception e) {
            // ignore this exception;
        }
        return null;
    }

	/*
	 * get date without time format.
	 * 
	 * @return now date
	 */
	public static Date getDate(Date now) throws ParseException {
		DateFormat df = DateFormat.getDateInstance();
		return df.parse(df.format(now));
	}

	public static String formatToDay(Date date) {
		return sdf.format(date);
	}

	public static Date parseToDay(String date) {
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException("日期转换出错");
		}
	}

	public static Date parseToDay(Long timeStamp) {
		if (timeStamp != null) {
			return new Date(timeStamp);
		}
		return null;
	}

	/**
	 * 得到首尾之间的日期,包含首包
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getBetween(Date start, Date end,
			List<String> exculdeWeeks) {
		List<Date> list = new LinkedList<Date>();
		Calendar calStartDate = Calendar.getInstance();
		calStartDate.setTime(start);
		Calendar calEndDate = Calendar.getInstance();
		calEndDate.setTime(end);
		do {
			int dayOfWeek = calStartDate.get(Calendar.DAY_OF_WEEK) - 1;
			if (exculdeWeeks != null && exculdeWeeks.size() > 0) {
				if (!exculdeWeeks.contains(String.valueOf(dayOfWeek))) {
					list.add(calStartDate.getTime());
				}
			} else {
				list.add(calStartDate.getTime());
			}
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		} while (calStartDate.before(calEndDate)
				|| calStartDate.equals(calEndDate));
		return list;
	}

	/**
	 * 得到首尾之间的日期,包含首包
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getBetween(String start, String end) {
		try {
			return getBetween(sdf.parse(start), sdf.parse(end), null);
		} catch (Exception e) {
			throw new RuntimeException("日期格式有错");
		}
	}

	/**
	 * 得到首尾之间的日期,包含首包,去掉某些星期
	 * 
	 * @param start
	 * @param end
	 * @param exculdeWeeks
	 *            0:周日,1:周一,2:周二....
	 * @return
	 */
	public static List<Date> getBetween(String start, String end,
			List<String> exculdeWeeks) {
		try {
			return getBetween(sdf.parse(start), sdf.parse(end), exculdeWeeks);
		} catch (Exception e) {
			throw new RuntimeException("日期格式有错");
		}
	}

    /**
     * 两个日期相减，返回毫秒
     *
     * @param minuend
     * @param subtrahend
     * @return
     */
    public static long compareTo(Date minuend,Date subtrahend){
        Calendar nowDate = Calendar.getInstance();
        Calendar oldDate = Calendar.getInstance();
        nowDate.setTime(minuend);
        oldDate.setTime(subtrahend);
        long now = nowDate.getTimeInMillis();
        long old = oldDate.getTimeInMillis();
        return now - old;
    }

    public static final int UNIT_SEC = 1;
    public static final int UNIT_MIN = 2;
    public static final int UNIT_HOUR = 3;
    public static final int UNIT_DAY = 4;


    public static BigDecimal turnUnit(Long millis,int unit){
        BigDecimal m1 = new BigDecimal(millis);
        BigDecimal result = new BigDecimal(millis);
            switch (unit){
                case UNIT_SEC:
                    result =  m1.divide(new BigDecimal(1000),BigDecimal.ROUND_HALF_UP);
                    break;
                case UNIT_MIN:
                    result =  m1.divide(new BigDecimal(1000*60),BigDecimal.ROUND_HALF_UP);
                    break;
                case UNIT_HOUR:
                    result =  m1.divide(new BigDecimal(1000*60*60),BigDecimal.ROUND_HALF_UP);
                    break;
                case UNIT_DAY:
                    result =  m1.divide(new BigDecimal(1000*60*60*24),BigDecimal.ROUND_HALF_UP);
                    break;
            }
        return result;
    }

	/*public static DateRsp getTimeBetween(int startOffSet, int endOffSet,
			int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, Calendar_DATE + startOffSet);
		calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY + hour);
		calendar.set(Calendar.MINUTE, Calendar_TIME);
		calendar.set(Calendar.SECOND, Calendar_TIME);
		calendar.set(Calendar.MILLISECOND, Calendar_TIME);
		Date tddayDate = calendar.getTime();
		Long todayDate = tddayDate.getTime() * 1000;
		calendar.add(Calendar.DATE, Calendar_DATE + endOffSet);
		Date ystdayDate = calendar.getTime();
		Long ystDate = ystdayDate.getTime() * 1000;

		DateRsp dateRsp = new DateRsp();
		dateRsp.setTddayDate(tddayDate);
		dateRsp.setTodayDate(todayDate);
		dateRsp.setYstDate(ystDate);
		dateRsp.setYstdayDate(ystdayDate);
		return dateRsp;
	}*/
	public static void main(String args[]){
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		minute -= minute%10;
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()));
	}
}
