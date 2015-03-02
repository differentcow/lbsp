package com.lbsp.promotion.core.service.param;

import com.lbsp.promotion.core.dao.ParameterDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.model.Parameter;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Barry on 2014/9/28.
 */

@Service
public class ParameterServiceImpl extends BaseServiceImpl<Parameter> implements ParameterService<Parameter> {

    @Autowired
    private ParameterDao parameterDao;


    /**
     * 根据类型以及编码查找参数信息
     *
     * @param type
     * @param code
     * @return
     */
    public boolean isExistParameter(String type,String code){
        return parameterDao.isExistParameter(type,code) > 0;
    }

    /**
     * 删除参数
     *
     * @param ids
     * @return
     */
    @Transactional
    public boolean delParam(String ids){
        //解析ID
        String[] idArray = ids.split(",");
        GenericQueryParam param = new GenericQueryParam();
        param.put(new QueryKey("id", QueryKey.Operators.IN),idArray);
        return this.delete(param) != 0; //批量删除
    }

    /**
     * 添加参数
     *
     * @param param
     * @return
     */
    @Transactional
    public boolean addParam(Parameter param){
        //填充新增记录信息
        Long times = System.currentTimeMillis();
        param.setCreate_time(times);
        param.setUpdate_time(times);
        int result = this.insert(param);
        return result != 1;
    }

    /**
     *修改参数
     *
     * @param param
     * @return
     */
    @Transactional
    public boolean updateParam(Parameter param){
        //首先修改type与type_meaning
        GenericQueryParam qry = new GenericQueryParam();
        qry.put(new QueryKey("type",QueryKey.Operators.EQ),param.getType());
        Parameter tmpParam = new Parameter();
        tmpParam.setType_meaning(param.getType_meaning());
        tmpParam.setUpdate_time(System.currentTimeMillis());
        tmpParam.setUpdate_user(param.getCreate_user());
        this.update(tmpParam,qry);  //两种情况，修改影响行数为0时，说明type是新增动作，修改影响行数不为0时，说明type是修改动作
        //再次修改具体的参数记录
        param.setId(param.getId());
        param.setUpdate_time(System.currentTimeMillis());
        return this.update(param) != 0;
    }
}
