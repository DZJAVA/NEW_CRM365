package cn.dgg.CRM365.util.orm;

import java.sql.SQLException;
import java.util.*;

import org.hibernate.*;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import cn.dgg.CRM365.util.page.Pagination;

/**
 * <功能简述> 封装HibernateTemplate中的一些操作数据库的方法 <功能详细描述>
 * 
 * @author 王科（小）
 * @version [版本号, Jul 10, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PageHibernateTemplate extends HibernateTemplate {

	public PageHibernateTemplate(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * 分页查询
	 * 
	 * @param hql
	 *            查询语句
	 * @param values
	 *            查询所需值
	 * @param params
	 *            分页数据
	 * @return
	 */
	@SuppressWarnings("all")
	public List findByHql(final String hql, final Object[] values,
			final Pagination params) {
		List data = null;
		try {
			data = (List) executeWithNativeSession(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					int amount = 0;
					Query query = session.createQuery(hql);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							query.setParameter(i, values[i]);
						}
					}
					query.setFirstResult(params.getFirstResult());
					query.setMaxResults(params.getPageResults());
					List list = query.list();
					return list;
				}
			});
			params.setTotalResults(getRows(hql, values));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 
	 * <功能简述> 根据参数返回结果集 <功能详细描述>
	 * 
	 * @param hql
	 * @param values
	 * @return [参数说明]
	 * 
	 * @return List [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public List findByHql(final String hql, final Object[] values) {
		List data = null;
		try {
			data = (List) executeWithNativeSession(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					Query query = session.createQuery(hql);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							query.setParameter(i, values[i]);
						}
					}
					List list = query.list();
					return list;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public int getRows(String hql, Object[] values) throws Exception {
		String[] hqls = hql.split("from");
		hql = "select count(*) ";
		for(int i = 1; i < hqls.length; i++){
			hql += "from" + hqls[i];
		}
		List list = super.find(hql, values);
		if (list != null && !list.isEmpty()) {
			return Integer.parseInt(list.get(0).toString());
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public void updateByHQL(final String hql, final Object[] values)
			throws Exception {
		executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				query.executeUpdate();
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void updateBySQL(final String sql, final Object[] values)
			throws Exception {
		executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				query.executeUpdate();
				return null;
			}
		});
	}

}
