package cn.dgg.CRM365.util.commonUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 该方法用于对通用Bean对象，遍历其内容以取得bean的内部属性<br>
 * 该方法是通过使用检查公共成员变量和不需要参数的公共方法来实现
 * 
 * @author 王科(小)
 * 
 */
public class BeanMessage {

	public static String message(Object bean) {
		if (bean == null)
			return "对象为空!";
		StringBuffer sbf = new StringBuffer();
		Class<?> clazz = bean.getClass();
		sbf.append("\n" + clazz.getName() + " 检查开始:\n");
		try {
			sbf.append("  检查公共成员变量：\n");
			Field[] fs = clazz.getFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				sbf.append("    " + clazz.getName() + "." + f.getName() + ": "
						+ f.get(bean) + "\n");
			}
			sbf.append("  检查公共方法：\n");
			Method[] ms = clazz.getMethods();
			for (int i = 0; i < ms.length; i++) {
				Method m = ms[i];
				if ((!m.getReturnType().getName().equals("void") && m
						.getParameterTypes().length == 0)) {
					sbf.append("    " + clazz.getName() + "." + m.getName()
							+ "(): " + m.invoke(bean) + "\n");
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		sbf.append(clazz.getName() + " 检查结束!");
		return sbf.toString();
	}

	public static String toMessage(Object bean) {
		return ReflectionToStringBuilder.toString(bean);
	}
}
