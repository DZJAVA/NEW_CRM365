package cn.dgg.CRM365.util.page;

import java.io.Serializable;
import org.hibernate.Session;

/**
 * 分页参数组装类
 * @author 王科（小）
 *
 */
public class Pagination
	implements Serializable
{

	private static final long serialVersionUID = 1L;
	private int currentPage;//当前页
	private int pageResults;//分页数
	private int totalResults;//总记录数
	private boolean initialized;
	private boolean queryData;
	private boolean queryCount;
	private Session session;

	public Pagination()
	{
		pageResults = 10;
		initialized = false;
		queryData = true;
		queryCount = true;
		session = null;
	}

	public Pagination(int pageResults)
	{
		initialized = false;
		queryData = true;
		queryCount = true;
		session = null;
		this.pageResults = pageResults;
	}

	public int getFirstResult()
	{
		int result = currentPage * pageResults;
		return result;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public boolean isInitialized()
	{
		return initialized;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getPageResults()
	{
		return pageResults;
	}

	public void setPageResults(int pageRecords)
	{
		pageResults = pageRecords;
	}

	public int getTotalResults()
	{
		return totalResults;
	}

	public int getTotalPages()
	{
		return totalResults / pageResults + (totalResults % pageResults <= 0 ? 0 : 1);
	}
	
	public void setTotalResults(int totalResults)
	{
		this.totalResults = totalResults;
		initialized = true;
		if (pageResults <= 0)
			pageResults = totalResults;
		int Pages = getTotalPages();
		if (currentPage > Pages)
			currentPage = Pages - 1;
		if (currentPage < 0)
			currentPage = 0;
	}

	public boolean isQueryData()
	{
		return queryData;
	}

	public void setQueryData(boolean queryData)
	{
		this.queryData = queryData;
	}

	public boolean isQueryCount()
	{
		return queryCount;
	}

	public void setQueryCount(boolean queryCount)
	{
		this.queryCount = queryCount;
	}

	public Session getSession()
	{
		return session;
	}

	public void setSession(Session session)
	{
		this.session = session;
	}

	public void set(int start, int limit)
	{
		setPageResults(limit);
		if (limit > 0)
			setCurrentPage(start / limit);
	}
}
