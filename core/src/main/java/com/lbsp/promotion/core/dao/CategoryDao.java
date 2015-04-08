package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.response.CategoryRsp;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 *
 * Created on 2015-04-01 09:21:24
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

    /**
     * 通过父类别ID查询其下一级的分类信息
     *
     * @param parentId
     * @return
     */
    List<CategoryRsp> getTreeNodeByParent(@Param("parentId")Integer parentId);

    /**
     * 通过ID查询信息
     *
     * @param id
     * @return
     */
    CategoryRsp getDetailById(@Param("id")Integer id);

}