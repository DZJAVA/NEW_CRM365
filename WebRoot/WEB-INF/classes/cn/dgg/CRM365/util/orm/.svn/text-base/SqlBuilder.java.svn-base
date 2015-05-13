package cn.dgg.CRM365.util.orm;

import java.util.ArrayList;
import java.util.List;

/**
 * 组装HQL语句类
 * 
 * @author 王科（小）
 * 
 */
@SuppressWarnings("all")
public class SqlBuilder {

	private static final int STATE_FIELD = 0;
	private static final int STATE_WHERE = 1;
	private static final int STATE_COMPLETE = 2;
	private static final String TABLE_ALIAS = "tab";
	public static final int TYPE_INSERT = 1;
	public static final int TYPE_UPDATE = 2;
	public static final int TYPE_DELETE = 3;
	private StringBuilder sql;
	private List params;
	private int type;
	private int state;

	public SqlBuilder(String tableName, int type) {
		state = 0;
		this.type = type;
		if (type == 1) {
			sql = new StringBuilder("insert into ");
			sql.append(tableName + " ");
			sql.append(TABLE_ALIAS + " (");
		} else if (type == 2) {
			sql = new StringBuilder("update ");
			sql.append(tableName + " ");
			sql.append(TABLE_ALIAS + " set ");
		} else {
			sql = new StringBuilder("delete from ");
			sql.append(tableName + " ");
			sql.append(TABLE_ALIAS + " ");
		}
		params = new ArrayList();
	}

	public void addField(String fieldName, Object value) {
		if (state == 0) {
			sql.append(TABLE_ALIAS + "." + fieldName);
			if (type == 1)
				sql.append(',');
			else if (type == 2)
				sql.append("=?,");
			params.add(value);
		}
	}

	public void addFieldSql(String fieldSql, Object value) {
		if (state == 0) {
			sql.append(fieldSql);
			sql.append(',');
			params.add(value);
		}
	}

	public void addFieldSql(String fieldSql) {
		if (state == 0) {
			sql.append(fieldSql);
			sql.append(',');
		}
	}

	public void addWhere(String fieldName, Object value) {
		addWhere(fieldName, value, "=");
	}

	public void addWhere(String fieldName, Object value, String compareOperator) {
		if (type != 1) {
			if (state == 0) {
				if (type == 2)
					sql.setCharAt(sql.length() - 1, ' ');
				sql.append("where 1=1 ");
				state = 1;
			}
			if (state == 1) {
				sql.append(" and ");
				sql.append(TABLE_ALIAS + "." + fieldName);
				sql.append(compareOperator);
				sql.append("? ");
				params.add(value);
			}
		}
	}

	public void complete() {
		if (state != 2) {
			if (type == 1) {
				sql.setCharAt(sql.length() - 1, ')');
				sql.append(" values(");
				for (int i = 0; i < params.size(); i++)
					sql.append("?,");

				sql.setCharAt(sql.length() - 1, ')');
			} else if (state == 0)
				sql.setCharAt(sql.length() - 1, ' ');
			state = 2;
		}
	}

	public String getSql() {
		complete();
		return sql.toString();
	}

	public Object[] getParams() {
		return params.toArray();
	}
}
