package com.lbsp.promotion.util.session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionMap extends ConcurrentHashMap<String, Object> {
	private static final long serialVersionUID = 7247926780137109633L;
	private static SessionMap sessionMap;

	private SessionMap() {

	}

	private static class InnerSessionMap {
		public static SessionMap getSessionMap() {
			if (sessionMap == null) {
				sessionMap = new SessionMap();
			}
			return sessionMap;
		}
	}

	protected class InnerCls {
		private Object obj;
		private Long updateTime;

		public void setObj(Object obj) {
			this.obj = obj;
		}

		public void setUpdateTime(Long updateTime) {
			this.updateTime = updateTime;
		}

		public Object getObj() {
			return obj;
		}

		public Long getUpdateTime() {
			return updateTime;
		}

	}

	public static SessionMap getSessionMapInstance() {
		return InnerSessionMap.getSessionMap();
	}

	@Override
	public Object put(String key, Object value) {
		InnerCls cls = new InnerCls();
		cls.setObj(value);
		cls.setUpdateTime(System.currentTimeMillis());
		return super.put(key, cls);
	}

	@Override
	public Object get(Object key) {
		InnerCls cls = (InnerCls) super.get(key);
		cls.setUpdateTime(System.currentTimeMillis());
		return cls.getObj();
	}

	public Object isExists(Object key) {
		boolean isExist = super.containsKey(key);
		if (isExist) {
			return this.get(key);
		}
		return null;
	}
}
