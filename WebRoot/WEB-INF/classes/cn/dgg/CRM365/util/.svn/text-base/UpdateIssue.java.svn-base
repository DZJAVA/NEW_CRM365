package cn.dgg.CRM365.util;

import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;

/**
 * 更新发稿主题里面的father_id字段 <功能简述> <功能详细描述>
 * 
 * @author 黄剑锋
 * @version [版本号, Dec 4, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UpdateIssue {
	public static void update(String tab, ICommonDAO<?> idao, String _ids,
			Long xid) {
		if (!"".equals(_ids)) {
			String[] _isIds = _ids.split(",");// 截取出发稿主题的id
			SqlBuilder iiSB = null;
			for (String id : _isIds) {// 父类实体对应的发稿主题father_tab字段全更新为1.1
				iiSB = new SqlBuilder("IssueItems", SqlBuilder.TYPE_UPDATE);
				iiSB.addField("father_tab", tab);// 父类实体tabPanel值
				iiSB.addField("father_id", String.valueOf(xid));// 父类实体id
				iiSB.addWhere("id", Long.parseLong(id));
				try {
					idao.updateByHQL(iiSB.getSql(), iiSB.getParams());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
