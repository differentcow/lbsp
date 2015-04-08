package com.lbsp.promotion.core.service.category;

import com.lbsp.promotion.core.dao.CategoryDao;
import com.lbsp.promotion.core.service.BaseServiceImpl;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.model.Category;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;
import com.lbsp.promotion.entity.response.CategoryRsp;
import com.lbsp.promotion.entity.response.TreeNode;
import com.lbsp.promotion.entity.response.TreeNodeUserData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

/**
 *
 * Created on 2015-04-01 09:21:24
 *
 * @author 简易自动代码创建工具
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements
		CategoryService<Category> {

	@Autowired
	private CategoryDao categoryDao;


    /**
     * 根据父节点查询分类信息
     *
     * @param parentId
     * @return
     */
    public TreeNode getTreeNodeByParent(Integer parentId){
        List<CategoryRsp> list = categoryDao.getTreeNodeByParent(parentId);
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        for(CategoryRsp p : list){
            TreeNode node = new TreeNode();
            node.setId(p.getId());
            node.setText(p.getName());
            List<TreeNodeUserData> userDatas = new ArrayList<TreeNodeUserData>();
            userDatas.add(new TreeNodeUserData("id",p.getId()+""));
            userDatas.add(new TreeNodeUserData("name",p.getName()));
            userDatas.add(new TreeNodeUserData("depth",p.getDepth()+""));
            userDatas.add(new TreeNodeUserData("parentName",p.getParentName()));
            userDatas.add(new TreeNodeUserData("query_code",p.getQuery_code()));
            userDatas.add(new TreeNodeUserData("parent_id",p.getParent_id()+""));
            userDatas.add(new TreeNodeUserData("priority",String.valueOf(p.getPriority())));
            userDatas.add(new TreeNodeUserData("status",p.getStatus()+""));
            node.setUserdata(userDatas);
            node.setChild(1);
            nodes.add(node);
        }
        TreeNode node = new TreeNode();
        node.setId(parentId==null?0:parentId);
        node.setItem(nodes);
        node.setChild(1);
        return node;
    }

	/**
	 *
	 * 根据ID查询详细信息
	 *
	 * @param id
	 * @return 
	 */
	public CategoryRsp getDetailById(Integer id){
		return categoryDao.getDetailById(id);
	}

	/**
	 *
	 * 获取信集合(分页)
	 *
	 * @param from
	 * @param to
	 * @param start
	 * @param size
	 * @return 
	 */
	public PageResultRsp getPageList(Long from,Long to,Integer start,Integer size){
		GenericQueryParam param = new GenericQueryParam();
		if(from != null)
			param.put(new QueryKey("create_time", QueryKey.Operators.GTE),from);
		if(to != null)
			param.put(new QueryKey("create_time", QueryKey.Operators.LTE),to);
		param.addSortCond(new SortCond("update_time", SortCond.Order.DESC));
		int count = this.count(param);
		param.setNeedPaging(true);
		param.setOffset(start);
		param.setPageSize(size);
		List list = this.find(param);
		PageResultRsp page = new PageResultRsp();
		page.loadPageInfo(count);
		page.setResult(list);
		return page;
	}

	/**
	 *
	 * 保存信息
	 *
	 * @param category
	 * @return 
	 */
	@Transactional
	public boolean saveCategory(Category category ) {
		Long currentTime = System.currentTimeMillis();
		if (category.getCreate_time() == null){
			category.setCreate_time(currentTime);
		}
		if (category.getUpdate_time() == null){
			category.setUpdate_time(currentTime);
		}
		return this.insert(category) > 0;
	}

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     *
     * 保存信息
     *
     * @param categorys
     * @return
     */
    public boolean saveBatchCategory(List<Category> categorys,Integer userId){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        Long currentTime = System.currentTimeMillis();
        int result = 0;
        Map<String,Category> map = new HashMap<String, Category>();
        Map<String,Category> mapParent = new HashMap<String, Category>();
        Map<Integer,String> mapMark = new HashMap<Integer,String>();
        Integer pId = null;
        for (Category category:categorys){
            mapMark.put(category.getId(),category.getName());
            category.setId(null);
            pId = category.getParent_id();
            category.setParent_id(null);
            category.setCreate_time(currentTime);
            category.setUpdate_time(currentTime);
            category.setUpdate_user(userId);
            category.setCreate_user(userId);
            result = this.insert(category);
            if (result == 0){
                transactionManager.rollback(status);
                return false;
            }else{
                GenericQueryParam param = new GenericQueryParam();
                param.put(new QueryKey("name", QueryKey.Operators.EQ),category.getName());
                Category c = this.findOne(param);
                String[] sq = category.getQuery_code().split(",");
                if (pId == null){
                    c.setQuery_code(""+c.getId());
                    result = this.update(c);
                    map.put(c.getName(),c);
                }else if(sq.length == 2){
                    String[] qq = sq[0].split("-");
                    if (StringUtils.isNumeric(qq[qq.length - 1])){
                        c.setParent_id(Integer.parseInt(qq[qq.length - 1]));
                    }
                    c.setQuery_code(sq[0]+"-"+c.getId());
                    result = this.update(c);
                    map.put(c.getName(),c);
                }else{
                    mapParent.put(mapMark.get(pId)+c.getId(),c);
                }
                if (result == 0){
                    transactionManager.rollback(status);
                    return false;
                }
                result = 0;
            }
        }
        boolean flag = updateQueryCodeWhenSave(map, mapParent);
        if (!flag){
            transactionManager.rollback(status);
            return false;
        }
        transactionManager.commit(status);
        return true;
    }

    /**
     * 更新QueryCode
     *
     * @param map
     * @param mapParent
     * @return
     */
    private boolean updateQueryCodeWhenSave(Map<String,Category> map,Map<String,Category> mapParent){
        int count = 0;
        int result = 0;
        int size = mapParent.size();
        if (size == 0){
            return true;
        }
        Map<String,Category> newMap = new HashMap<String, Category>();
        for(Map.Entry<String,Category> m : map.entrySet()){
            if (mapParent.get(m.getKey()) != null){
                Category c = mapParent.get(m.getKey());
                c.setParent_id(m.getValue().getId());
                String queryCode = m.getValue().getQuery_code()+"-"+c.getId();
                c.setQuery_code(queryCode);
                result = this.update(c);
                if(result == 0){
                    return false;
                }
                newMap.put(m.getKey(),c);
                mapParent.remove(m.getKey());
                count++;
            }
        }
        boolean flag = true;
        if (count < size){
            flag = updateQueryCodeWhenSave(newMap,mapParent);
        }
        return flag;
    }

	/**
	 *
	 * 更新信息
	 *
	 * @param category
	 * @return 
	 */
	@Transactional
	public boolean updateCategory(Category category) {
		if (category.getUpdate_time() == null){
            Long currentTime = System.currentTimeMillis();
			category.setUpdate_time(currentTime);
		}
		return this.update(category) > 0;
	}

    /**
     *
     * 更新信息
     *
     * @param categorys
     * @return
     */
    public boolean updateBatchCategory(List<Category> categorys,Integer userId){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        int result = 0;
        Long currentTime = System.currentTimeMillis();
        for (Category category : categorys){
            category.setUpdate_time(currentTime);
            category.setUpdate_user(userId);
            result = this.update(category);
            if (result == 0){
                transactionManager.rollback(status);
                return false;
            }else{
                result = 0;
            }
        }
        transactionManager.commit(status);
        return true;
    }

	/**
	 *
	 * 删除信息
	 *
	 * @param id
	 * @return 
	 */
	@Transactional
	public boolean deleteCategory(Integer id) {
		GenericQueryParam param = new GenericQueryParam();
		param.put(new QueryKey("id", QueryKey.Operators.EQ),id);
		return this.delete(param) > 0;
	}

	/**
	 *
	 * 批量删除信息
	 *
	 * @param ids
	 * @return 
	 */
	@Transactional
	public boolean batchDeleteCategory(List<Integer> ids){
		return categoryDao.batchDelete(ids) > 0;
	}

    /**
     * 批量删除
     *
     * @param categorys
     * @return
     */
    public boolean deleteBatchCategory(List<Category> categorys){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        int result = 0;
        for(Category category : categorys){
            GenericQueryParam param = new GenericQueryParam();
            param.put(new QueryKey("query_code", QueryKey.Operators.LASTLIKE),category.getQuery_code());
            result = this.delete(param);
            if (result == 0){
                transactionManager.rollback(status);
                return false;
            }else{
                result = 0;
            }
        }
        transactionManager.commit(status);
        return true;
    }

}