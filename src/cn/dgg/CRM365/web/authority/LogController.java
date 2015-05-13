package cn.dgg.CRM365.web.authority;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.authority.Log;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;

/**
 * 日志控制类
 * 
 * @author c 王科(大) 2012-11-14
 */
@SuppressWarnings("all")
@RequestMapping("/log")
@Controller
public class LogController {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Log> logDao;

	/**
	 * 打开登录日志界面
	 * 
	 * @return 王科(大) 2012-11-13
	 */
	@RequestMapping("/openLogView.do")
	public ModelAndView jumpPage() {
		return new ModelAndView("authority/logInfo");
	}

	/**
	 * 显示所有登录日志
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @return 王科(大) 2012-11-13
	 */
	@RequestMapping("/loadLog.do")
	public ModelAndView loadLog(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		String hql = "from Log l ";
		try {
			List<Log> dataList = logDao.findByHql(hql, null, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Object d : dataList) {
					JSONObject item = new JSONObject();
					Log field = (Log) d;
					item.element("id", MvcUtil.toJsonString(field.getId()));
					item.element("logTime", MvcUtil.toJsonString(field
							.getLogTime()));
					item.element("ip", MvcUtil.toJsonString(field.getIp()));
					item.element("loginId", MvcUtil.toJsonString(field
							.getLoginId()));
					item.element("userName", MvcUtil.toJsonString(field
							.getLoginId()));
					item.element("remark", MvcUtil.toJsonString(field
							.getRemark()));
					data.add(item);
				}
			}
			jsonObject.element("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 删除登录日志
	 * 
	 * @param id
	 * @return 王科(大) 2012-11-13
	 */
	@RequestMapping("/deleteLog.do")
	public ModelAndView deleteLog(@RequestParam("id")
	String id) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		for (String i : ids) {
			try {
				logDao.deleteById(Long.parseLong(i), Log.class);
				jsonObject.element("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("success", false);
				jsonObject.element("msg", "删除日志出错！");
			}
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
}
