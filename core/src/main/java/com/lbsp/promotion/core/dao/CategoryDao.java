package com.lbsp.promotion.core.dao;

import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 *
 * Created on 2015-04-01 06:53:52
 *
 * @author 简易自动代码创建工具
 */
public interface CategoryDao {

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	int batchDelete(@Param("ids")List<Integer> ids);

}