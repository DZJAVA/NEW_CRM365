package cn.dgg.CRM365.util.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import cn.dgg.CRM365.util.commonUtil.Persistent;
import cn.dgg.CRM365.util.page.Pagination;

/**
 * 通用DAO类
 * 
 * @author 王科（小）
 * 
 * @param <T>
 */
@SuppressWarnings("all")
public class CommonDAO<T> extends PageHibernateDaoSupport implements
		ICommonDAO<T> {

	public void delete(T entity) throws Exception {
		getTemplate().delete(entity);
	}

	public void deleteAll(Collection<T> entitis) throws Exception {
		getTemplate().deleteAll(entitis);
	}

	public void deleteById(Long id, Class clazz) throws Exception {
		getTemplate().delete(getTemplate().get(clazz, id));
	}

	public List<T> findAll(final String hql, final Pagination params)
			throws Exception {
		return getTemplate().findByHql(hql, null, params);
	}

	public List<T> findByHql(final String hql, final Object[] values,
			final Pagination params) {
		return getTemplate().findByHql(hql, values, params);
	}

	public T get(Serializable id, Class clazz) throws Exception {
		return (T) getTemplate().get(clazz, id);
	}

	public T load(Serializable id, Class clazz) throws Exception {
		return (T) getTemplate().load(clazz, id);
	}

	public int getRows(String hql, Object[] values) throws Exception {
		List<T> list = super.getHibernateTemplate().find(hql, values);
		if (list != null && !list.isEmpty()) {
			return list.size();
		} else {
			return 0;
		}
	}

	public void save(T entity) throws Exception {
		// TODO Auto-generated method stub
		getTemplate().save(entity);
	}

	public void update(T entity) throws Exception {
		getTemplate().update(entity);
	}

	public List<T> findAll(String hql) {
		// TODO Auto-generated method stub
		return super.getHibernateTemplate().find(hql);
	}

	public void deleteById(Serializable id) throws Exception {
		// TODO Auto-generated method stub

	}

	public void updateByHQL(final String hql, final Object[] values)
			throws Exception {
		getTemplate().updateByHQL(hql, values);
	}

	public void updateBySQL(final String sql, final Object[] values)
			throws Exception {
		getTemplate().updateBySQL(sql, values);
	}

	public List<Object[]> findAllToObject(String hql, Object[] values)
			throws Exception {
		return this.getHibernateTemplate().find(hql, values);
	}

	public Object updateAction(IDaoAction dao) {
		return dao.execute(getTemplate());
	}

	public List<T> findByHql(String hql, Object[] values) {
		return getTemplate().find(hql, values);
	}

	public List<T> findList(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRows(T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<T> list(T entity, int start, int size)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map> findAll(String hql, Object[] values) {
		return getTemplate().findByHql(hql, values);
	}

	public void deleteAl(List<?> list) throws Exception {
		getTemplate().deleteAll(list);
	}

}
