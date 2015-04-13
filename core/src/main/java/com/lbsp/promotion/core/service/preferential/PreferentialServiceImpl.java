package com.lbsp.promotion.core.service.preferential;

import com.lbsp.promotion.core.dao.PreferentialDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Preferential;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.response.PreferentialRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

;

/**
 *
 * Created on 2015-03-31 09:41:11
 *
 * @author 简易自动代码创建工具
 */
@Service
public class PreferentialServiceImpl extends BaseServiceImpl<Preferential> implements
		PreferentialService<Preferential> {

	@Autowired
	private PreferentialDao preferentialDao;

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public PreferentialRsp getDetailById(Integer id){
		return preferentialDao.getDetailById(id);
	}

	/**
	 *
	 * 获取信集合(分页)
	 *
     * @param type
     * @param title
     * @param status
     * @param shop
     * @param from
     * @param to
     * @param start
     * @param size
     * @return
	 */
	public PageResultRsp getPageList(String type,String title,Integer status,String shop,Long from,Long to,Integer start,Integer size){
		int count = preferentialDao.getListCount(type,title,shop,status,from,to);
		List list = preferentialDao.getList(type,title,shop,status,from,to,start,size);
		PageResultRsp page = new PageResultRsp();
		page.loadPageInfo(count);
		page.setResult(list);
		return page;
	}

	/**
	 *
	 * 保存信息
	 *
	 * @param preferential
	 * @return 
	 */
	@Transactional
	public boolean savePreferential(PreferentialRsp preferential) {
		Long currentTime = System.currentTimeMillis();
		if (preferential.getCreate_time() == null){
			preferential.setCreate_time(currentTime);
		}
		if (preferential.getUpdate_time() == null){
			preferential.setUpdate_time(currentTime);
		}
        Preferential p = buildPreferential(preferential);
        int result =  this.insert(p);
        if (result > 0){
            Integer id = preferentialDao.getLastId();
            preferential.setId(id);
            preferentialDao.savePreferentialWithCategory(preferential);
        }
        return false;
	}

    private Preferential buildPreferential(PreferentialRsp rsp){
        Preferential p = new Preferential();
        p.setUpdate_user(rsp.getUpdate_user());
        p.setDescription(rsp.getDescription());
        p.setEnd_time(rsp.getEnd_time());
        p.setMark(rsp.getMark());
        p.setNow_price(rsp.getNow_price());
        p.setOff(rsp.getOff());
        p.setPic_path(rsp.getPic_path());
        p.setShop_id(rsp.getShop_id());
        p.setStart_time(rsp.getStart_time());
        p.setStatus(rsp.getStatus());
        p.setTitle(rsp.getTitle());
        p.setType(rsp.getType());
        p.setWas_price(rsp.getWas_price());
        p.setUpdate_time(rsp.getUpdate_time());
        p.setId(rsp.getId());
        p.setCreate_time(rsp.getCreate_time());
        p.setCreate_user(rsp.getCreate_user());
        return p;
    }

	/**
	 *
	 * 更新信息
	 *
	 * @param preferential
	 * @return 
	 */
	@Transactional
	public boolean updatePreferential(PreferentialRsp preferential ) {
		Long currentTime = System.currentTimeMillis();
		if (preferential.getUpdate_time() == null){
			preferential.setUpdate_time(currentTime);
		}
        int result = preferentialDao.updatePreferentialWithCategory(preferential.getOrg_id(),
                preferential.getId(),
                preferential.getCategory_id(),
                preferential.getOrg_category_id(),
                preferential.getUpdate_user(),
                preferential.getUpdate_time());
        if(result > 0){
            Preferential p = buildPreferential(preferential);
            return this.update(p) > 0;
        }

		return false;
	}

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	@Transactional
	public boolean deletePreferential(Integer id) {
        int result = preferentialDao.deletePreferentialWithCategory(id);
        if (result > 0){
            GenericQueryParam param = new GenericQueryParam();
            param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
            return this.delete(param) > 0;
        }
		return false;
	}

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	@Transactional
	public boolean batchDeletePreferential(List<Integer> ids){
        int result = preferentialDao.deleteBatchPreferentialWithCategory(ids);
        if (result > 0){
            return preferentialDao.batchDelete(ids) > 0;
        }
		return false;
	}

}