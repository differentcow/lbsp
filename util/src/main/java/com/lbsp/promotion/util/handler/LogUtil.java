package com.lbsp.promotion.util.handler;

import org.apache.log4j.Logger;

public class LogUtil {
	private static Logger logger;
	
	
	private static Logger getLogUtilInstance(){
		if(logger ==null){
			logger = Logger.getLogger(LogUtil.class);
		}
		return logger;
	}

	public final static void logInfo(Object info) {
		getLogUtilInstance().info(info);
	}

	public final static void logDebug(Object debug) {
		getLogUtilInstance().debug(debug);
	}

	public final static void logWarn(Object warn) {
		getLogUtilInstance().warn(warn);
	}

	public final static void logError(Object error) {
		getLogUtilInstance().error(error);
	}
	
	
	
}
