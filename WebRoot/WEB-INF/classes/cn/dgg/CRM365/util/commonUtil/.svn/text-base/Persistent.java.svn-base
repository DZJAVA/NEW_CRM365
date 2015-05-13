package cn.dgg.CRM365.util.commonUtil;

import java.io.Serializable;

/**
 * 
 * @author 王科（小）
 * 
 */
public abstract class Persistent implements Serializable {

	public Persistent() {
	}

	public abstract Long getId();

	public abstract void setId(Long id);

	public boolean equals(Object x) {
		if (x == null)
			return false;
		return ((Persistent) x).getId() == getId();
	}
}
