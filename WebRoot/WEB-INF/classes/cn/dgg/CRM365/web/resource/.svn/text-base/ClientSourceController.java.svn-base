package cn.dgg.CRM365.web.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.resourcesManage.ClientSource;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;
/**
 * 客户来源
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  Administrator
  * @version  [版本号, Mar 9, 2013]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@SuppressWarnings("all")
@RequestMapping("/clientsource")
@Controller
public class ClientSourceController {
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ClientSource> dao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object> odao;
	
	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage(){
		return new ModelAndView("resource/clientsource");
	}
	/**
	 * 查找所有客户信息
	 * @param gridLoadParams
	 * @param request
	 * @param _condition 查询条件
	 * @return
	 * 黄剑锋
	 * 2012-12-17 16:53第一次新建
	 */
	@RequestMapping("/loadClientSource.do")
	public ModelAndView loadClient(@ModelAttribute("params") GridLoadParams gridLoadParams,	HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		String hql = "from ClientSource c";
		try {
			HttpSession session = request.getSession();
			if(session != null){
				List<ClientSource> list = dao.findByHql(hql,null,pagination);
				jsonObject.element("totalCount", pagination.getTotalResults());
				// 有无数据都要产生data节点
				JSONArray data = new JSONArray();
				if (list.size() > 0) {
					for (ClientSource field : list) {
						JSONObject item = new JSONObject();
						item.element("id", MvcUtil.toJsonString(field.getId()));
						item.element("name", MvcUtil.toJsonString(field.getName()));//客户地址
						item.element("remark", MvcUtil.toJsonString(field.getRemark()));
						data.add(item);
					}
				}
				jsonObject.element("data", data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 
	  *<功能简述>保存或修改客户信息
	  *<功能详细描述>
	  * @param client
	  * @param request
	  * @param opp 商机类型
	  * @param sign 签单可能性
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明] 
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("all")
	@RequestMapping("/saveOrUpdateClientSource.do")
	public ModelAndView saveOrUpdateClient(ClientSource clientSource, HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		String hql = "select c.id from ClientSource c where c.name = ?";
		try{
			List<Object> clist = odao.findByHql(hql, new Object[]{clientSource.getName()});
			if(request.getSession() != null){
				if("".equals(clientSource.getId()) || clientSource.getId() == null){
					if(clist.size() > 0){//判断重复添加
						jsonObject.element("failure", true);
						jsonObject.element("msg", "客户来源信息已经存在，无法添加！");
						return MvcUtil.jsonObjectModelAndView(jsonObject);
					}else{
						dao.save(clientSource);
						jsonObject.element("success", true);
						jsonObject.element("msg", "保存客户来源信息成功！");
						return MvcUtil.jsonObjectModelAndView(jsonObject);
					}
				}else{
					if(clist.size() == 0 || ((Long)clist.get(0)).equals(clientSource.getId())){
						SqlBuilder sb = new SqlBuilder("ClientSource", SqlBuilder.TYPE_UPDATE);
						sb.addField("name", clientSource.getName());//客户名称
						sb.addField("remark", clientSource.getRemark());
						sb.addWhere("id", clientSource.getId());
						dao.updateByHQL(sb.getSql(), sb.getParams());
						jsonObject.element("success", true);
						jsonObject.element("msg", "保存客户来源信息成功！");
						return MvcUtil.jsonObjectModelAndView(jsonObject);
					}else{
						jsonObject.element("failure", true);
						jsonObject.element("msg", "客户信息已经存在，无法修改！");
						return MvcUtil.jsonObjectModelAndView(jsonObject);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交失败,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 
	  *<功能简述>删除客户信息
	  *<功能详细描述>
	  * @param id
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	  * 黄剑锋
	  * 2012-12-17 17:17第一次新建
	 */
	@RequestMapping("/deleteClientSource.do")      
	public ModelAndView deleteClient(@RequestParam("id") String id){
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		int sum=0;
		for(String i:ids){
			try {
				dao.deleteById(Long.parseLong(i),ClientSource.class);
				sum++;
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("failure", false);
				jsonObject.element("msg", "不能删除,该客户来源信息已经被引用!");
				return MvcUtil.jsonObjectModelAndView(jsonObject);
			}
		}
		if(sum>0){
			jsonObject.element("success", true);
			jsonObject.element("msg", "删除成功，删除了"+sum+"条数据!!!");
		
		}else{
			jsonObject.element("failure", false);
			jsonObject.element("msg", "不能删除,该客户来源信息已经被引用!");
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	@RequestMapping("/loadclients.do")
	public ModelAndView loadclients(){
		JSONObject json = new JSONObject();
		String hql = "from ClientSource c";
		List<ClientSource> list = dao.findAll(hql);
		JSONArray data = new JSONArray();
		for(ClientSource cs : list){
			JSONObject item = new JSONObject();
			item.element("client_id", MvcUtil.toJsonString(cs.getId()));
			item.element("client_name", MvcUtil.toJsonString(cs.getName()));
			data.add(item);
		}
		json.element("data", data);
		return MvcUtil.jsonObjectModelAndView(json);
	}
}
