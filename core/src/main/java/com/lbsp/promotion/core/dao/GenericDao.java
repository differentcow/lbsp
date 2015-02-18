package com.lbsp.promotion.core.dao;

import com.lbsp.promotion.entity.base.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GenericDao {
	public int insert(BaseInsertEntity entity);

	public int update(BaseUpdateEntity entity);

	public int delete(BaseDeleteEntity entity);

	public Map selectOneByFields(BaseFindFieldsEntity entity);

	public int selectCount(BaseComplexEntity entity);

	public List<Map> selectListByFields(@Param("entity") BaseComplexFieldsEntity entity);
	
	public void insertAll(BaseInsertAllEntity entity);
}
