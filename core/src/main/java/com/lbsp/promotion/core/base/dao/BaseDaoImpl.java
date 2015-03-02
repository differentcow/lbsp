package com.lbsp.promotion.core.base.dao;

import com.lbsp.promotion.core.dao.GenericDao;
import com.lbsp.promotion.entity.base.*;
import com.lbsp.promotion.entity.exception.CommonRunException;
import com.lbsp.promotion.entity.exception.MethodReflectException;
import com.lbsp.promotion.entity.exception.SqlStatementException;
import com.lbsp.promotion.entity.query.GenericQueryParam;
import com.lbsp.promotion.entity.query.QueryKey;
import com.lbsp.promotion.entity.query.SortCond;
import com.lbsp.promotion.entity.table.annotation.MyTable;
import com.lbsp.promotion.entity.table.annotation.Transient;
import com.lbsp.promotion.entity.table.annotation.util.MySqlFilterKeyUtils;
import com.lbsp.promotion.util.Security;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class BaseDaoImpl<T> implements BaseDao<T> {
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private GenericDao genericDao;
	protected Class<T> cls;

	public BaseDaoImpl(Class<T> cls, GenericDao genericDao) {
		this.cls = cls;
		this.genericDao = genericDao;
	}

	/**
	 * 分页参数校验
	 * 
	 * @param params
	 * @param rowCount
	 * @return
	 */
	protected void pageParamValidate(GenericQueryParam params, int rowCount) {
		int page = params.getPage();
		int pageSize = params.getPageSize();

		if (page < 1)
			page = 1;
		if (pageSize < 1)
			pageSize = 1;
		if (pageSize > GenericQueryParam.MAX_PAGE_SIZE)
			pageSize = GenericQueryParam.MAX_PAGE_SIZE;
		int maxPage = (int) Math.ceil((double) rowCount / pageSize);
		if (page > maxPage)
			page = maxPage;

		params.setPage(page);
		params.setPageSize(pageSize);
	}

	@Override
	public String getTableName() {
		MyTable myTable = this.cls.getAnnotation(MyTable.class);
		if (myTable == null || myTable.value() == null
				|| "".equals(myTable.value())) {
			return this.cls.getSimpleName().toLowerCase();
		} else {
			return myTable.value();
		}
	}

    private void setFilterField(List<String> filters){
        this.filter = filters;
    }

    private List<String> getFilterField(){
        return this.filter;
    }

    public List<String> getNotUpdateField() {
        List<String> list = getFilterField();
        if (list == null){
            list = new ArrayList<String>();
        }else if(!list.isEmpty()) {
            return list;
        }
        MyTable myTable = this.cls.getAnnotation(MyTable.class);
        if (myTable != null) {
            String fields = myTable.notUpdateField();
            String[] filters = fields.split(",");
            for (String f : filters){
                list.add(f);
            }
        } else {
            list.add("create_date");
            list.add("create_user");
        }
        return list;
    }

	@Override
	public String getPrimaryKey() {
		MyTable myTable = this.cls.getAnnotation(MyTable.class);
		if (myTable != null) {
			return myTable.primaryKey();
		} else {
			return "id";
		}
	}

	private void recursiveInsert(Object obj, Class cls, StringBuffer keyBuffer,
			StringBuffer valueBuffer) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			Transient tst = field.getAnnotation(Transient.class);
			if (tst != null && tst.value()) {
				continue;
			}
			String fieldName = field.getName();
			String firstChar = fieldName.substring(0, 1);
			String name = fieldName.replaceFirst(firstChar,
					firstChar.toUpperCase());
			try {
				Method[] methods = cls.getMethods();
				for (Method method : methods) {
					if (method.getName().equals("get" + name)) {
						Object v = method.invoke(obj, new Object[] {});
						/*if(fieldName.equals(this.getPrimaryKey())){
							if(v == null){
								v = Security.generateUUIDStr();
							}
						}*/
						if (v != null) {
							keyBuffer.append(filterKey(fieldName) + ",");
							if (v instanceof String) {
								valueBuffer.append("'" + Security.encodeSQLLike(v.toString()) + "'" + ",");
							} else if (v instanceof Date) {
								valueBuffer.append("'" + DEFAULT_DATE_FORMAT.format(v) + "'"
										+ ",");
							} else {
								valueBuffer.append(v + ",");
							}
						}
						break;
					}
				}
			} catch (Exception e) {
				throw new MethodReflectException("get" + name
						+ " method reflect exception");
			}
		}
		if (cls.getSuperclass() != null) {
			recursiveInsert(obj, cls.getSuperclass(), keyBuffer, valueBuffer);
		}
	}

    private String filterKey(String fieldName){
        MyTable table = this.cls.getAnnotation(MyTable.class);
        if (table == null) {
            return MySqlFilterKeyUtils.getFilterKey(fieldName);
        } else {
            if("mysql".equals(table.filterKey())){
                return MySqlFilterKeyUtils.getFilterKey(fieldName);
            }else {
                return fieldName;
            }
        }
    }

	@Override
	public int insert(T obj) {
		StringBuffer keyBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		this.recursiveInsert(obj, obj.getClass(), keyBuffer, valueBuffer);
		String key = keyBuffer.toString();
		key = key.length() > 0 ? key.substring(0, key.length() - 1) : null;
		if (key == null) {
			throw new SqlStatementException("insert into (null) exception ");
		}
		String value = valueBuffer.toString();
		value = value.length() > 0 ? value.substring(0, value.length() - 1)
				: null;
		if (value == null) {
			throw new SqlStatementException(
					"insert into values(null) exception ");
		}
		BaseInsertEntity entity = new BaseInsertEntity(getTableName(), key,
				value);
		return this.genericDao.insert(entity);
	}

	private String recursiveWhere(Object obj, Class cls, String primaryKey) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			Transient tst = field.getAnnotation(Transient.class);
			if (tst != null && tst.value()) {
				continue;
			}
			String fieldName = field.getName();
			String firstChar = fieldName.substring(0, 1);
			String name = fieldName.replaceFirst(firstChar,
					firstChar.toUpperCase());
			if (fieldName.equals(primaryKey)) {
				try {
					Method[] methods = cls.getMethods();
					for (Method method : methods) {
						if (method.getName().equals("get" + name)) {
							Object v = method.invoke(obj, new Object[] {});
							String where = null;
							if (v != null) {
								if (v instanceof String) {
									where = primaryKey + "=" + "'" + Security.encodeSQLLike(v.toString()) + "'";
								} else {
									where = primaryKey + "=" + v;
								}
							}
							return where;
						}
					}
				} catch (Exception e) {
					throw new MethodReflectException("get" + name
							+ " method reflect exception");
				}
			}
		}
		if (cls.getSuperclass() != null) {
			return recursiveWhere(obj, cls.getSuperclass(), primaryKey);
		}
		return null;
	}

    private List<String> filter;

    private boolean checkUpdateField(String field){
        List<String> fields = getNotUpdateField();
        boolean flag = true;
        for (String f : fields){
            if(f.equalsIgnoreCase(field)){
                flag = false;
                break;
            }
        }
        return flag;
    }

	private void recursiveKvs(Object obj, Class cls, String primaryKey,StringBuffer kvs) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			Transient tst = field.getAnnotation(Transient.class);
			if (tst != null && tst.value()) {
				continue;
			}
			String fieldName = field.getName();
			String firstChar = fieldName.substring(0, 1);
			String name = fieldName.replaceFirst(firstChar,
					firstChar.toUpperCase());
			if (!fieldName.equals(primaryKey)
                    && checkUpdateField(fieldName)) {
				try {
					Method[] methods = cls.getMethods();
					for (Method method : methods) {
						if (method.getName().equals("get" + name)) {
							Object v = method.invoke(obj, new Object[] {});
							if (v != null) {
								if (v instanceof String) {
									kvs.append(filterKey(fieldName) + "=" + "'" + Security.encodeSQLLike(v.toString()) + "'"
											+ ",");
								} else if (v instanceof Date) {
									kvs.append(filterKey(fieldName) + "=" + "'"
											+ DEFAULT_DATE_FORMAT.format(v) + "'" + ",");
								} else {
									kvs.append(filterKey(fieldName) + "=" + v + ",");
								}
							}
							break;
						}
					}
				} catch (Exception e) {
					throw new MethodReflectException("get" + name
							+ " method reflect exception");
				}
			}
		}
		if (cls.getSuperclass() != null) {
			recursiveKvs(obj, cls.getSuperclass(), primaryKey, kvs);
		}
	}

	@Override
	public int update(T obj) {
        setFilterField(null);
		String primaryKey = getPrimaryKey();
		String where = recursiveWhere(obj, obj.getClass(), primaryKey);
		return updatePub(obj,primaryKey,where);
	}

    @Override
    public int update(T obj,List<String> filter){
        setFilterField(filter);
        String primaryKey = getPrimaryKey();
        String where = recursiveWhere(obj, obj.getClass(), primaryKey);
        return updatePub(obj,primaryKey,where);
    }

    @Override
    public int update(T obj,List<String> filter,GenericQueryParam param){
        setFilterField(filter);
        return updatePub(obj,null,this.getWhereByQueryParam(param));
    }

	private int updatePub(T obj,String primaryKey,String where){
		StringBuffer kvs = new StringBuffer();
		recursiveKvs(obj, obj.getClass(), primaryKey, kvs);
		String key = kvs.toString();
		key = key.length() > 0 ? key.substring(0, key.length() - 1) : null;
		if (key == null) {
			throw new SqlStatementException("update set null exception ");
		}
		if (where == null) {
			throw new SqlStatementException(
					"update where condition is null exception ");
		}
		BaseUpdateEntity entity = new BaseUpdateEntity(getTableName(), where,
				key);
		return this.genericDao.update(entity);
	}

	@Override
	public int delete(String id) {
		BaseDeleteEntity entity = new BaseDeleteEntity(getTableName(),
				getPrimaryKey() + "=" + "'"+ Security.encodeSQLLike(id)+"'");
		return this.genericDao.delete(entity);
	}

	@Override
	public T findOne(String id) {
		return findOne(id, null);
	}

	private T findOneByFileds(String id, String fileds) {
		BaseFindFieldsEntity entity = new BaseFindFieldsEntity(getTableName(),
				getPrimaryKey() + "=" + "'"+ Security.encodeSQLLike(id) + "'", fileds);
		Map map = this.genericDao.selectOneByFields(entity);
		T obj = null;
		try {
			if(map!=null&&!map.isEmpty()){
				obj = this.cls.newInstance();
				BeanUtils.populate(obj, map);
				return obj;
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new CommonRunException("map convert bean["
					+ this.cls.getSimpleName() + "] exception");
		}
	}

	private String getWhereByQueryParam(GenericQueryParam param) {
		StringBuffer buff = new StringBuffer();
		Iterator<Entry<QueryKey, Object>> it =  param.entrySet().iterator();
		while(it.hasNext()){
			Entry<QueryKey,Object> entry = it.next(); 
			Object v = entry.getValue();
			QueryKey queryKey =  entry.getKey();
			String suffix = " ";
			String prefix = queryKey.getPrefix().value();
			if(it.hasNext()){
				suffix = queryKey.getSuffix().value();
			}else if(queryKey.getSuffix() == QueryKey.Suffix.R_KH){
				suffix = QueryKey.Suffix.R_KH.value();
			}
			if (v != null) {
				if (v instanceof String) {
					if(queryKey.getOperator().equals(QueryKey.Operators.LIKE)){
						buff.append(prefix + queryKey.getKey()+" "
								+ queryKey.getOperator().value() + " '%"+ Security.encodeSQLLike(v.toString()) +"%'" + " "+suffix+" ");
					}else if(queryKey.getOperator().equals(QueryKey.Operators.PRELIKE)){
                        buff.append(prefix + queryKey.getKey()+" like '%"+ Security.encodeSQLLike(v.toString()) + "' "+suffix+" ");
                    }else if(queryKey.getOperator().equals(QueryKey.Operators.LASTLIKE)){
                        buff.append(prefix + queryKey.getKey()+" like '"+ Security.encodeSQLLike(v.toString()) +"%'" + " "+suffix+" ");
                    }else if(queryKey.getOperator().equals(QueryKey.Operators.DEFINELIKE)){
                        buff.append(prefix + queryKey.getKey()+" like '"+ Security.encodeSQLLike(v.toString()) +"'" + " "+suffix+" ");
                    }else{
						buff.append(prefix + queryKey.getKey() + ""
								+ queryKey.getOperator().value() + " '" + Security.encodeSQLLike(v.toString())
								+ "'" + " "+suffix+" ");
					}
				} else if (v instanceof Date) {
					buff.append(prefix + queryKey.getKey() + " "
							+ queryKey.getOperator().value() + " '"
							+ DEFAULT_DATE_FORMAT.format(v) + "'" + " "+suffix+" ");
				}else if(v instanceof Collection){
					Collection coll = (Collection)v;
					Iterator collIt = coll.iterator();
					StringBuffer sb = new StringBuffer("(");
					while(collIt.hasNext()){
						Object obj = collIt.next();
						String suf = "";
						if(collIt.hasNext()){
							suf = ",";
						}
						if(obj instanceof String){
							sb.append(" '" + Security.encodeSQLLike(obj.toString())
									+ "'"+suf);
						}else if(obj instanceof Date){
							sb.append(" '" + DEFAULT_DATE_FORMAT.format(obj)
									+ "'"+suf);
						}else{
							sb.append(obj
									+ suf);
						}
					}
					sb.append(")");
					buff.append(prefix + queryKey.getKey() +" " + queryKey.getOperator().value()+" "+ sb.toString());
				}else if(v instanceof String[]){
					StringBuffer sb = new StringBuffer("(");
					sb.append(StringUtils.arrayToDelimitedString((String[])v, ",")).append(")");
					buff.append(prefix + queryKey.getKey() +" " + queryKey.getOperator().value()+" "+ sb.toString());
				}else {
					buff.append(prefix + queryKey.getKey()+" "
							+ queryKey.getOperator().value() + " " + Security.encodeSQLLike(v.toString()) + " "+suffix+" ");
				}
			}else{
				if(queryKey.getOperator().equals(QueryKey.Operators.ISNULL)||queryKey.getOperator().equals(QueryKey.Operators.NOTNULL)){
					buff.append(prefix + queryKey.getKey()+" "
							+ queryKey.getOperator().value() +suffix+" ");
				}
			}
		}
		return buff.toString();
	}

	@Override
	public int count(GenericQueryParam param) {
		String where = getWhereByQueryParam(param);
		BaseComplexEntity entity = new BaseComplexEntity(getTableName(), where,
				null);
		return this.genericDao.selectCount(entity);
	}

	@Override
	public List<T> find(GenericQueryParam param) {
		return find(param,null);
	}

	private List<T> findByFields(GenericQueryParam param, String fields) {
		String where = getWhereByQueryParam(param);
		BaseComplexFieldsEntity entity = new BaseComplexFieldsEntity(
				getTableName(), where, null, fields);
		List<T> result = new ArrayList<T>();
		List<SortCond> sortList = param.getSortCond();
		String orderBy = "";
		if (sortList.size() > 0) {
			for (SortCond sordCond : sortList) {
				orderBy += sordCond.getColumn() + " " + sordCond.getOrder()
						+ ",";
			}
			orderBy = orderBy.substring(0, orderBy.length() - 1);
			entity.setOrderBy(orderBy);
		}
		if(param.isNeedPaging()){
            int offset = 0;
            int pageSize = param.getPageSize();
            if (param.getOffset() != 0){
                offset = param.getOffset();
            }else{
                int page = param.getPage();
                offset = page * pageSize;
            }
			RowBounds rowBounds = new RowBounds(offset, pageSize);
			entity.setRowBounds(rowBounds);
		}
		List<Map> list = this.genericDao.selectListByFields(entity);
		if(list == null || list.size() == 0){
			return new ArrayList<T>();
		}
		if (list != null && list.size() > 0) {
			try {
				for (Map map : list) {
					if(map == null)
						continue;
					T obj = this.cls.newInstance();
					BeanUtils.populate(obj, map);
					result.add(obj);
				}
			} catch (Exception e) {
				throw new CommonRunException("map convert bean["
						+ this.cls.getSimpleName() + "] exception");
			}
		}
		return result;
	}

	@Override
	public T findOne(String id, String[] fields) {
		if (fields == null || fields.length == 0) {
			return findOneByFileds(id, "*");
		}
		StringBuffer buff = new StringBuffer();
		for (String field : fields) {
			buff.append(field + ",");
		}
		return findOneByFileds(id, buff.substring(0, buff.length() - 1));
	}

	@Override
	public List<T> find(GenericQueryParam param, String[] fields) {
		StringBuffer buff = new StringBuffer();
		if (fields == null || fields.length == 0) {
			return this.findByFields(param, "*");
		}
		for (String field : fields) {
			buff.append(field + ",");
		}
		return this.findByFields(param, buff.substring(0, buff.length() - 1));
	}

	@Override
	public int delete(Object id) {
		return this.delete(String.valueOf(id));
	}

	@Override
	public T findOne(Object id) {
		return this.findOne(String.valueOf(id));
	}

	@Override
	public T findOne(Object id, String[] fields) {
		return this.findOne(String.valueOf(id),fields);
	}

	@Override
	public int delete(GenericQueryParam param) {
		String where = getWhereByQueryParam(param);
		BaseDeleteEntity entity = new BaseDeleteEntity(getTableName(),where);
		return this.genericDao.delete(entity);
	}

	@Override
	public int update(T obj, GenericQueryParam param) {
        setFilterField(null);
		return updatePub(obj,null,this.getWhereByQueryParam(param));
	}

	@Override
	public void insertAll(List<T> objs) {
		String keys = null;
		List<String> values = new ArrayList<String>();
		for(T obj : objs){
			StringBuffer keyBuffer = new StringBuffer();
			StringBuffer valueBuffer = new StringBuffer();
			this.recursiveInsert(obj, obj.getClass(), keyBuffer, valueBuffer);
			if(keys == null){
				keys = keyBuffer.toString();
				keys = keys.length() > 0 ? keys.substring(0, keys.length() - 1) : null;
				if (keys == null) {
					throw new SqlStatementException("insert into table(null) exception ");
				}
			}
			String value = valueBuffer.toString();
			value = value.length() > 0 ? value.substring(0, value.length() - 1)
					: null;
			if (value == null) {
				throw new SqlStatementException(
						"insert into table values(null) exception ");
			}
			values.add(value);
		}
		BaseInsertAllEntity entity = new BaseInsertAllEntity(getTableName(), keys,
				values);
		this.genericDao.insertAll(entity);
	}
}
