package com.lbsp.promotion.core.dao;

import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 *
 * Created on 2015-03-24 12:01:27
 *
 * @author 简易自动代码创建工具
 */
public interface CustomerDao {

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	int batchDelete(@Param("ids")List<Integer> ids);

}