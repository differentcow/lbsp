/**
 * 
 */
package com.lbsp.promotion.webplatform.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Barry
 *
 */
public class UserFunctionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 974479726248194131L;
	private String funcId;
	private String name;
	private String url;
	private String type;
	private String index;
	private String parentId;
	private String icon;
	private String target;
	private List<UserFunctionDTO> subfunctions;
	
	
	public UserFunctionDTO(){}
	
	public UserFunctionDTO(String funcId,String name,String url,String type,String index,String icon,String target,String parentId){
		this.funcId = funcId;
		this.name = name;
		this.url = url;
		this.type = type;
		this.index = index;
		this.icon = icon;
		this.target = target;
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	public List<UserFunctionDTO> getSubfunctions() {
		return subfunctions;
	}
	public void setSubfunctions(List<UserFunctionDTO> subfunctions) {
		this.subfunctions = subfunctions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	
	
}
