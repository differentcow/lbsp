/**
 * 
 */
package com.lbsp.promotion.core.dao;


import org.apache.ibatis.annotations.Param;

/**
 * @author Barry
 *
 */
public interface ParameterDao {


    /**
     * 根据编码以及类型查找参数信息
     *
     * @param type
     * @param code
     * @return
     */
    int isExistParameter(@Param("type") String type, @Param("code") String code);

}
