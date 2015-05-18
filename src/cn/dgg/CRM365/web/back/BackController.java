package cn.dgg.CRM365.web.back;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.backstage.LoanSource;
import cn.dgg.CRM365.domain.backstage.SignClientEntity;
import cn.dgg.CRM365.domain.backstage.SignClientView;
import cn.dgg.CRM365.domain.backstage.SourceLog;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;


/**
 * 客户管理控制器
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  黄剑锋
  * @version  [版本号, Dec 17, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@SuppressWarnings("all")
@RequestMapping("/sign_client")
@Controller
public class BackController {
	
	ICommonDAO<SignClientView> dao;
	public ICommonDAO<SignClientView> getDao() {
		return dao;
	}
	@Autowired
	public void setDao(@Qualifier("commonDAOProxy")ICommonDAO<SignClientView> dao) {
		this.dao = dao;
	}
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<LoanSource> lsDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<SignClientEntity> scDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<SourceLog> slDao;
	
	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage(){
		return new ModelAndView("back/back_stage");
	}
	/**
	 * 加载签单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/loadSignClient")
	public ModelAndView loadSignClient(@ModelAttribute("params")GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("condition") String condition) {
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		User user = null;
		StringBuilder sb = new StringBuilder(" where 1=1");
		String roleCode = null;	//得到当前登录人的角色代码 
		String hql = "from SignClientView sc";
		List<SignClientView> data = new ArrayList<SignClientView>(20);
		try {
			user = (User) request.getSession().getAttribute("userSession");
			roleCode = user.getRole().getRoleCode();
			if("201202".equals(roleCode)){
				sb.append(" and sc.deptId = ").append(user.getDepartment().getId());
			}
			if("201203".equals(roleCode)){
				sb.append(" and sc.follower = ").append(user.getId());
			}
			data = dao.findAll(hql, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.element("data", data);
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 签单
	 * @param sc
	 * @param request
	 * @param cid
	 * @return
	 */
	@RequestMapping("/saveOrUpdateSign.do")
	public ModelAndView saveOrUpdateSign(SignClientEntity sc, HttpServletRequest request,
			@RequestParam("cid") int cid){
		JSONObject jsonObject = new JSONObject();
		try{
			if(request.getSession() != null){
				if(sc.getId() == null){
					scDao.save(sc);
				}else{
					SqlBuilder sb = new SqlBuilder("SignClientEntity", SqlBuilder.TYPE_UPDATE);
					sb.addField("clientCode", sc.getClientCode());
					sb.addField("loanType", sc.getLoanType());
					sb.addField("loanBank", sc.getLoanBank());
					sb.addField("loanSource", sc.getLoanSource());
					sb.addField("loanAmount", sc.getLoanAmount());
					sb.addField("followInfo", sc.getFollowInfo());
					sb.addField("status", sc.getStatus());
					sb.addWhere("id", sc.getId());
					scDao.updateByHQL(sb.getSql(), sb.getParams());
				}
				jsonObject.element("success", true);
				jsonObject.element("msg", "保存成功！");
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
	 * 退单 0资料未齐 1进件 2退单
	 * @param request
	 * @param sid
	 * @return
	 */
	@RequestMapping("/exitClient.do")
	public ModelAndView exitClient(HttpServletRequest request, @RequestParam("sid") int sid){
		JSONObject jsonObject = new JSONObject();
		if(sid <= 0){
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交异常!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		try{
			SqlBuilder sb = new SqlBuilder("SignClientEntity", SqlBuilder.TYPE_UPDATE);
			sb.addField("backDate", StaticValues.sdf.format(new Date()));
			sb.addField("status", 2);
			sb.addWhere("id", sid);
			scDao.updateByHQL(sb.getSql(), sb.getParams());
			jsonObject.element("success", true);
			jsonObject.element("msg", "退单成功！");
		}catch(Exception e){
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交失败,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 删除签单
	 * @param ls
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSignClient.do")
	public ModelAndView deleteSignClient(HttpServletRequest request, @RequestParam("id") int id){
		JSONObject jsonObject = new JSONObject();
		if(id <= 0){
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交异常!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		try{
			if(request.getSession() != null){
				scDao.updateByHQL("delete SignClientEntity sc where sc.id=?", new Object[]{id});
				jsonObject.element("success", true);
				jsonObject.element("msg", "删除成功!");
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
	 * 加载渠道
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/loadSourceBySign")
	public ModelAndView loadSourceBySign(@ModelAttribute("params")GridLoadParams gridLoadParams,
			HttpServletRequest request, @RequestParam("signId") int signId) {
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		String hql = "from LoanSource ls";
		StringBuilder sb = new StringBuilder(" where 1=1");
		List<LoanSource> data = new ArrayList<LoanSource>(20);
		if(signId <= 0){
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		try {
			sb.append(" and ls.signClient = ").append(signId);
			data = lsDao.findAll(hql, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.element("data", data);
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 保存渠道
	 * @param ls
	 * @param request
	 * @param signId
	 * @return
	 */
	@RequestMapping("/saveOrUpdateSource.do")
	public ModelAndView saveOrUpdateSource(LoanSource ls, HttpServletRequest request,
			@RequestParam("signId") int signId){
		JSONObject jsonObject = new JSONObject();
		try{
			if(request.getSession() != null){
				if(ls.getId() == null){
					ls.setSignClient(signId);
					lsDao.save(ls);
				}else{
					SqlBuilder sb = new SqlBuilder("LoanSource", SqlBuilder.TYPE_UPDATE);
					sb.addField("sourceName", ls.getSourceName());
					sb.addField("sourceAmount", ls.getSourceAmount());
					sb.addField("receiveAmount", ls.getReceiveAmount());
					sb.addField("serviceFee", ls.getServiceFee());
					sb.addWhere("id", ls.getId());
					dao.updateByHQL(sb.getSql(), sb.getParams());
				}
				jsonObject.element("success", true);
				jsonObject.element("msg", "保存成功！");
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
	 * 放款
	 * @param ls
	 * @param request
	 * @param signId
	 * @return
	 */
	@RequestMapping("/makeLoans.do")
	public ModelAndView makeLoans(LoanSource ls, HttpServletRequest request,
			@RequestParam("signId") int signId){
		JSONObject jsonObject = new JSONObject();
		if(signId <= 0){
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交异常!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		try{
			if(request.getSession() != null){
				SqlBuilder sb = new SqlBuilder("LoanSource", SqlBuilder.TYPE_UPDATE);
				sb.addField("loanAmount", ls.getLoanAmount());
				sb.addField("loanDate", ls.getLoanDate());
				sb.addField("loanYear", ls.getLoanYear());
				sb.addField("loanInterest", ls.getLoanInterest());
				sb.addField("interestType", ls.getInterestType());
				sb.addField("serviceFee", ls.getServiceFee());
				sb.addField("status", 1);//0未通过，1放款
				sb.addWhere("id", ls.getId());
				dao.updateByHQL(sb.getSql(), sb.getParams());
				jsonObject.element("success", true);
				jsonObject.element("msg", "放款成功！");
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
	 * 删除渠道
	 * @param ls
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSource.do")
	public ModelAndView deleteSource(HttpServletRequest request, @RequestParam("id") int id){
		JSONObject jsonObject = new JSONObject();
		if(id <= 0){
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交异常!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		try{
			if(request.getSession() != null){
				lsDao.updateByHQL("delete LoanSource ls where ls.id=?", new Object[]{id});
				jsonObject.element("success", true);
				jsonObject.element("msg", "删除成功!");
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
	 * 加载渠道
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/loadLogBySource")
	public ModelAndView loadLogBySource(@ModelAttribute("params")GridLoadParams gridLoadParams,
			HttpServletRequest request, @RequestParam("sourceId") int sourceId) {
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		String hql = "from SourceLog ls";
		StringBuilder sb = new StringBuilder(" where 1=1");
		List<SourceLog> data = new ArrayList<SourceLog>(20);
		if(sourceId <= 0){
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		try {
			sb.append(" and ls.source = ").append(sourceId);
			data = slDao.findAll(hql, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObject.element("data", data);
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	
	/**
	 * 保存渠道日志
	 * @param ls
	 * @param request
	 * @param sourceId
	 * @return
	 */
	@RequestMapping("/saveOrUpdateLog.do")
	public ModelAndView saveOrUpdateLog(SourceLog sl, HttpServletRequest request,
			@RequestParam("sourceId") int sourceId){
		JSONObject jsonObject = new JSONObject();
		try{
			if(request.getSession() != null){
				if(sl.getId() == null){
					sl.setSource(sourceId);
					slDao.save(sl);
				}else{
					SqlBuilder sb = new SqlBuilder("SourceLog", SqlBuilder.TYPE_UPDATE);
					sb.addField("logInfo", sl.getLogInfo());
					sb.addWhere("id", sl.getId());
					dao.updateByHQL(sb.getSql(), sb.getParams());
				}
				jsonObject.element("success", true);
				jsonObject.element("msg", "保存成功！");
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
	 * 删除渠道日志
	 * @param ls
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteSourceLog.do")
	public ModelAndView deleteSourceLog(HttpServletRequest request, @RequestParam("id") int id){
		JSONObject jsonObject = new JSONObject();
		if(id <= 0){
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交异常!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		try{
			if(request.getSession() != null){
				lsDao.updateByHQL("delete SourceLog sl where sl.id=?", new Object[]{id});
				jsonObject.element("success", true);
				jsonObject.element("msg", "删除成功!");
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交失败,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
}
