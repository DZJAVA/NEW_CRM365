package cn.dgg.CRM365.util.orm;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * <功能简述> <功能详细描述>
 * 
 * @author 王科（小）
 * @version [版本号, Jul 10, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PageHibernateDaoSupport extends HibernateDaoSupport {

	public PageHibernateDaoSupport() {
	}

	protected HibernateTemplate createHibernateTemplate(
			SessionFactory sessionFactory) {
		return new PageHibernateTemplate(sessionFactory);
	}

	public PageHibernateTemplate getTemplate() {
		return (PageHibernateTemplate) getHibernateTemplate();
	}
}
