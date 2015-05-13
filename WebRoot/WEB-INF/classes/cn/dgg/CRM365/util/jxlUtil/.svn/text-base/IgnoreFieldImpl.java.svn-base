package cn.dgg.CRM365.util.jxlUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 
 * <功能简述> <功能详细描述>
 * 
 * @author 王科（小）
 * @version [版本号, Jul 1, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class IgnoreFieldImpl implements PropertyFilter {
	/**
	 * 忽略的属性名称
	 */
	private String[] fields;

	/**
	 * 是否忽略集合
	 */
	private boolean ignoreColl = false;

	/**
	 * 空参构造方法<br/> 默认不忽略集合
	 */
	public IgnoreFieldImpl() {
		// empty
	}

	/**
	 * 构造方法
	 * 
	 * @param fields
	 *            忽略属性名称数组
	 */
	public IgnoreFieldImpl(String[] fields) {
		this.fields = fields;
	}

	/**
	 * 构造方法
	 * 
	 * @param ignoreColl
	 *            是否忽略集合
	 * @param fields
	 *            忽略属性名称数组
	 */
	public IgnoreFieldImpl(boolean ignoreColl, String[] fields) {
		this.fields = fields;
		this.ignoreColl = ignoreColl;
	}

	/**
	 * 构造方法
	 * 
	 * @param ignoreColl
	 *            是否忽略集合
	 */
	public IgnoreFieldImpl(boolean ignoreColl) {
		this.ignoreColl = ignoreColl;
	}

	@SuppressWarnings("unchecked")
	public boolean apply(Field filed) {
		if (ignoreColl) {
			Class c = filed.getType();
			if (c.isInterface()) {
				if (Set.class.isAssignableFrom(c)
						|| List.class.isAssignableFrom(c)) {
					return false;
				}
			}
			if (c.isArray() || Collection.class.isAssignableFrom(c)) {
				return false;
			}
		}
		if (fields == null || fields.length == 0)
			return true;
		for (String s : fields) {
			if (s.equals(filed.getName())) {
				return false;
			}
		}
		return true;
	}

	public String[] getFields() {
		return fields;
	}

	/**
	 * 设置忽略的属性
	 * 
	 * @param fields
	 */
	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public boolean isIgnoreColl() {
		return ignoreColl;
	}

	/**
	 * 设置是否忽略集合类
	 * 
	 * @param ignoreColl
	 */
	public void setIgnoreColl(boolean ignoreColl) {
		this.ignoreColl = ignoreColl;
	}
}