package com.lbsp.promotion.core.service.param;

import com.lbsp.promotion.core.service.BaseService;
import com.lbsp.promotion.entity.model.Parameter;


/**
 * Created by Barry on 2014/9/28.
 */
public interface ParameterService<T> extends BaseService<T> {

    /**
     * 根据类型以及编码查找参数信息
     *
     * @param type
     * @param code
     * @return
     */
    boolean isExistParameter(String type, String code);

    /**
     * 删除参数
     *
     * @param ids
     * @return
     */
    boolean delParam(String ids);

    /**
     * 添加参数
     *
     * @param param
     * @return
     */
    boolean addParam(Parameter param);

    /**
     *修改参数
     *
     * @param param
     * @return
     */
    boolean updateParam(Parameter param);
}
