package cn.dgg.CRM365.util.jxlUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AttendFieldImpl implements PropertyFilter {
	/**
	 * 需要输出的属性名称
	 */
	private String[] fields;

	/**
	 * 是否忽略集合
	 */
	private boolean ignoreColl = false;

	/**
	 * 空参构造方法<br/> 默认不忽略集合
	 */
	public AttendFieldImpl() {
		// empty
	}

	/**
	 * 构造方法
	 * 
	 * @param fields
	 *            需要输出的属性名称数组
	 */
	public AttendFieldImpl(String[] fields) {
		this.fields = fields;
	}

	/**
	 * 构造方法
	 * 
	 * @param ignoreColl
	 *            是否忽略集合
	 * @param fields
	 *            需要输出的属性名称数组
	 */
	public AttendFieldImpl(boolean ignoreColl, String[] fields) {
		this.fields = fields;
		this.ignoreColl = ignoreColl;
	}

	/**
	 * 构造方法
	 * 
	 * @param ignoreColl
	 *            是否忽略集合
	 */
	public AttendFieldImpl(boolean ignoreColl) {
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
		if (fields == null || fields.length == 0)// 如果没有设置需要输出的字段项，则全部输出。
			return true;
		boolean isPass = false;
		for (String s : fields) {
			if (s.equals(filed.getName())) {
				isPass = true;
				break;
			}
		}
		return isPass;
	}

	public String[] getFields() {
		return fields;
	}

	/**
	 * 设置需要输出的属性
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