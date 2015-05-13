package cn.dgg.CRM365.util.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.dgg.CRM365.util.page.Pagination;

/**
 * 通用DAO接口
 * 
 * @author 王科（小）
 * 
 * @param <T>
 */
@SuppressWarnings("all")
public interface ICommonDAO<T> {

	/**
	 * 删除指定持久化实例
	 * 
	 * @param entity
	 *            -实体对象
	 * @throws FrameworkException
	 */
	public void delete(T entity) throws Exception;

	/**
	 * 删除集合内全部持久化类实例
	 * 
	 * @param entitys
	 *            -持久化集合实例
	 * @throws FrameworkException
	 */
	public void deleteAll(Collection<T> entitis) throws Exception;

	/**
	 * 删除指定持久化的实例list集合 <功能简述> <功能详细描述>
	 * 
	 * @param list
	 * @throws Exception
	 *             [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteAl(List<?> list) throws Exception;

	/**
	 * 删除指定持久化实例
	 * 
	 * @param id
	 *            -持久化实例ID
	 * @throws FrameworkException
	 */
	public void deleteById(Serializable id) throws Exception;

	public void deleteById(Long id, Class clazz) throws Exception;

	/**
	 * 保存实体
	 * 
	 * @param entity
	 *            -实体对象
	 * @throws FrameworkException
	 */
	public void save(T entity) throws Exception;

	/**
	 * 修改实体
	 * 
	 * @param entity
	 *            -实体对象
	 * @throws FrameworkException
	 */

	public void update(T entity) throws Exception;

	/**
	 * 
	 * <功能简述> 根据HQL语句修改信息 <功能详细描述>
	 * 
	 * @param hql
	 * @throws Exception
	 *             [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void updateByHQL(String hql, Object[] values) throws Exception;

	/**
	 * 获取单个实体
	 * 
	 * @param id ,
	 *            clazz - 主键id,clazz某个实体
	 * @return T
	 * @throws FrameworkException
	 */
	public T get(Serializable id, Class clazz) throws Exception;

	public T load(Serializable id, Class clazz) throws Exception;

	/**
	 * 无条件获取所有实体
	 * 
	 * @return list<T>
	 * @throws FrameworkException
	 */
	public List<T> findAll(String hql);

	/**
	 * 
	 * <功能简述> 查询返回map集合 <功能详细描述> 例句 select new map（x.实体属性） from 实体 x
	 * 
	 * @param hql
	 * @param values
	 * @return [参数说明]
	 * 
	 * @return List<Map> [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public List<Map> findAll(String hql, Object[] values);

	public List<Object[]> findAllToObject(String hql, Object[] values)
			throws Exception;

	public List<T> findAll(final String hql, final Pagination params)
			throws Exception;

	/**
	 * 根据条件获取所有实体
	 * 
	 * @param hql
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public List<T> findByHql(String hql, Object[] values, Pagination params);

	public List<T> findByHql(String hql, Object[] values);

	/**
	 * 分页查询
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<T> findList(int pageNo, int pageSize);

	/**
	 * 获取分页查询数据
	 * 
	 * @param entity
	 * @param page
	 * @return
	 */
	// public Page<T> queryForPage(T entity, Page<T> page);
	/**
	 * 获取分页查询数据
	 * 
	 * @param hql
	 * @param values
	 * @param page
	 * @return
	 */
	// public Page<T> queryForPage(final String hql,final Object[] values,final
	// Page<T> page);
	/**
	 * 获取记录数
	 * 
	 * @param entity
	 * @return
	 */
	public int getRows(T entity);

	/**
	 * 根据语句和条件获取记录数
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	public int getRows(String hql, final Object[] values) throws Exception;

	/**
	 * 获取分页查询数据列表
	 * 
	 * @param entity
	 * @param start
	 * @param size
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<T> list(T entity, final int start, final int size)
			throws IllegalArgumentException;

	public abstract Object updateAction(IDaoAction dao);

}
