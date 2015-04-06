package com.lbsp.promotion.webplatform.security.access;

import com.lbsp.promotion.util.cache.CacheMapping;
import com.lbsp.promotion.util.xml.XmlUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomMetadataSource implements
		FilterInvocationSecurityMetadataSource, InitializingBean {
	public static final String ALL = "all";
	public static final String RESOURCE_FILE = "resource-map";
    public static final String RESOURCE_FILE_SUFFIX = ".xml";
	private static List<Map<String, String>> resources;
	private List<String> pathForAllRoles;

    @Value("${resource.map.path}")
    private String resourcePath;

    public List<String> getPathForAllRoles() {
		return pathForAllRoles;
	}

	public void setPathForAllRoles(List<String> pathForAllRoles) {
		this.pathForAllRoles = pathForAllRoles;
	}

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		HttpServletRequest request = ((FilterInvocation) object).getRequest();
		request.getSession().setAttribute("resources", resources);
		UrlUtils.buildRequestUrl(request);
		/*
		if(path.indexOf('?')>0){
			path = path.substring(0,path.indexOf('?'));
		}
		String func = null;
		if (resources != null) {
			for (final Map<String, String> map : this.resources) {
				if (map.get("url")!=null&&map.get("url").endsWith(path)) {
					func = map.get("funcId");
					break;
				}
			}
		}
		for (String pathReg : pathForAllRoles) {
			pathReg = pathReg.replaceAll("\\*", ".*");
			if (path.matches(pathReg)) {
				func = ALL;
				break;
			}
		}
		*/
		String func = ALL;
		// if (path.startsWith("/forward") ||
		// path.equals("/")||path.equals("/index.html")) {
		// func = ALL;
		// }
		final String funcId = func;
		List<ConfigAttribute> collections = new ArrayList<ConfigAttribute>();
		collections.add(new ConfigAttribute() {
			private static final long serialVersionUID = 1989113482648580593L;

			public String getAttribute() {
				return String.valueOf(funcId);
			}
		});
		return collections;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		if (resources == null) {
			throw new RuntimeException("resources properties is null");
		}
		Collection<ConfigAttribute> collections = new ArrayList<ConfigAttribute>();
		for (final Map<String, String> map : resources) {
			collections.add(new ConfigAttribute() {
				private static final long serialVersionUID = -2248931371325415550L;

				public String getAttribute() {
					// key代表功能ID
					return map.get("funcId");
				}
			});
		}
		return collections;
	}

	public boolean supports(Class<?> paramClass) {
		return FilterInvocation.class.isAssignableFrom(paramClass);
	}

    /**
     * 获取资源(Resource-map.xml)
     *
     * @return
     */
	public static List<Map<String, String>> getResources() {
        List<Map<String, String>> map = (List<Map<String,String>>) CacheMapping.get(RESOURCE_QNAME);
        if(map == null){
            return resources;
        }else{
            return map;
        }

	}

    /**
     * 获取资源(Resource-map.xml)
     *
     * @return
     */
    public static List<Map<String, String>> getResources(String locale) {
        List<Map<String, String>> map = (List<Map<String,String>>)CacheMapping.get(RESOURCE_QNAME+"_"+locale);
        if(map == null){
            refreshResources(locale);
            return resources;
        }else{
            return map;
        }

    }

    /**
     * 获取资源(Resource-map.xml)
     *
     */
    public static void refreshResources(String locale){
        //如果语言设定不为空
        String localeFix = "";
        if(StringUtils.isNotBlank(locale)){
            localeFix = "_"+locale;
        }
        resources = XmlUtil.getXmlUtilInstance().parseFile(resourceFilePath+localeFix+RESOURCE_FILE_SUFFIX);
        if(resources == null){
            refreshResources("");
        }
        CacheMapping.put(RESOURCE_QNAME+localeFix,resources);
    }

	public void afterPropertiesSet() throws Exception {
        if (resources == null) {
            if (StringUtils.isBlank(resourcePath)){
                resourcePath = this.getClass().getResource("/").getFile();
            }
            setResourceFilePath(resourcePath);
			resources = XmlUtil.getXmlUtilInstance().parseFile(resourceFilePath+RESOURCE_FILE_SUFFIX);
            CacheMapping.put(RESOURCE_QNAME,resources);
		}
	}

    private static String resourceFilePath;

    private static void setResourceFilePath(String path){
        resourceFilePath = (path + File.separator + RESOURCE_FILE);
    }

    public static final String RESOURCE_QNAME = "resource_map";
	
}
