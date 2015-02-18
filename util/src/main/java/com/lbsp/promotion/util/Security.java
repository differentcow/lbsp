package com.lbsp.promotion.util;

import com.lbsp.promotion.util.validation.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Security {

	/*
	public static void main(String[] arg0){
		Set<Long> seqs = new HashSet<Long>();
		System.out.println(new Date().getTime());
		for (int i = 0; i < 100; i++){
			seqs.add(generateUUID());
		}
		System.out.println(new Date().getTime());
		System.out.println(seqs);
	}
	*/
	private static long __previous_time__ 	= 0L;
	private static int __machine_key__ 		= 0;
	private static int __sequence_no__ 		= 0;


    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "aPQbcdCDEFGefgklmntuvRSTUVWXwxosy3zABHIJKYZ012456hij78pLMNOqr9";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /*
	 * 生成唯一向前滚动的序列号。
	 * 根据IP在不同机器上保持唯一性。
	 * 最大性能为10ms产生最多10个不重复序列号
	 */
	public static long generateUUID(){
		if (__machine_key__ == 0L){
			/*
			//获取本机IP地址
			String ip = Machine.getFristIPv4();
			if (ip == null || ip.equals(""))
				ip = "127.0.0.1";
			//int[] args = Convert.ipv4ToIntArray(ip);
			if (args == null) args = new int[]{127,0,0,1};
			__machine_key__ = args[0]+args[1]+args[2]+args[3];
			if (__machine_key__ > 999)
				__machine_key__ -= 1000;
			__machine_key__ = __machine_key__*1000000+args[3]+(int)(Math.random()*255);
			*/
			__machine_key__ = (int)(Math.random()*9) * 1000
					+ (int)(Math.random()*9) * 100
					+ (int)(Math.random()*9) * 10;
		}
		long now = System.currentTimeMillis();
		if (now == __previous_time__){
			__sequence_no__++;
		}else{
			__previous_time__ = now;
			__sequence_no__ = 0;
		}
		return __previous_time__*1000+__machine_key__+__sequence_no__;
	}
	/*
	 * 生成验证码
	 * @param count 验证码的位数
	 * @param hasCapitalLetter 是否有大写
	 * @param hasLowercase 是否有小写
	 * @param hasDigit 是否有数字
	 * @param hasInterpunction 是否有标点符号
	 * @return 验证码，例：7kFlr9#
	 */
    public static String generateCode(
    		int count,
    		boolean hasCapitalLetter,
    		boolean hasLowercase,
    		boolean hasDigit,
    		boolean hasInterpunction){
        String result = "";
        if (count < 1)
        	return result;

        String capitalLetter = "ABCDEFGHJKLMNPQRSTUVWXY";
        String lowercase = "abcdefghjklmnpqrstuvwxy";
        String digit = "23456789";
        String interpunction = "~!@#$%^&*()_+=-{}|][:;?/.";

        List<Boolean> matchs = new ArrayList<Boolean>();
        List<String> condis = new ArrayList<String>();
        if (hasCapitalLetter){
            condis.add(capitalLetter);
            matchs.add(false);
        }
        if (hasLowercase){
            condis.add(lowercase);
            matchs.add(false);
        }
        if (hasDigit){
            condis.add(digit);
            matchs.add(false);
        }
        if (hasInterpunction){
            condis.add(interpunction);
            matchs.add(false);
        }

        int conditionCount = condis.size();
        int unmatchedCount = conditionCount;
        if (conditionCount == 0)
        	return result;

        while (count > 0){
        	double rand = Math.random();
            int baseSeed = (int) (rand * (conditionCount - 1));
            if (!matchs.get(baseSeed)){
            	matchs.set(baseSeed,true);
                unmatchedCount--;
            }
            else if (count <= unmatchedCount)
            {
                condis.remove(baseSeed);
                matchs.remove(baseSeed);
                conditionCount = condis.size();
                continue;
            }
            result += condis.get(baseSeed).charAt((int) (rand * (condis.get(baseSeed).length() - 1)));
            count--;
        }
        return result;
    }

    public static String generateUUIDStr(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String encodeSQLLike(String c){
    	if (Validation.isEmpty(c))
    		return c;
    	else
    		return c
    				.replace("'", "''")
    				.replace("#", "\\#")
    				.replace("%","\\%");
    }
    public static long hex_10(String hex){
		return Long.parseLong(hex, 16);
	}
}
