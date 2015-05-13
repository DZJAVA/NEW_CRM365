package cn.dgg.CRM365.util.commonUtil;

import java.util.ResourceBundle;

public class PropertiesUtil {
	
	public static String getValue(String src,String key){//src ��properties�Ĳ����׺���ļ���key��Ҫȡ��ĸ���������
		ResourceBundle rb = ResourceBundle.getBundle(src);
		String value = rb.getString(key);
		return value;
	}
}
