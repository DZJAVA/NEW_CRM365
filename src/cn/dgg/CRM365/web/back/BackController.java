package cn.dgg.CRM365.web.back;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
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
import cn.dgg.CRM365.domain.resourcesManage.Client;
import cn.dgg.CRM365.util.commonUtil.DBManager;
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
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Client> cDao;
	
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
			int right = user.getCounts();
			if(right > 0){
				if(right == 1){
					sb.append(" and sc.frontSuperId = ").append(user.getDepartment().getSuperId());
				}
				if(right == 2){
					sb.append(" and sc.frontDeptId = ").append(user.getDepartment().getId());
				}
				if(right == 3){
					sb.append(" and sc.frontUserId = ").append(user.getId());
				}
				if(right == 4){
					sb.append(" and sc.status > 0");
				}
				if(right == 5){
					sb.append(" and sc.status > 0");
				}
				if(right == 6){
					sb.append(" and sc.follower = ").append(user.getId());
				}
				data = dao.findAll(hql, pagination);
			}
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
					sc.setClient(cid);
					scDao.save(sc);
					SqlBuilder sb = new SqlBuilder("Client", SqlBuilder.TYPE_UPDATE);
					sb.addField("clientStatus", "1");
					sb.addWhere("id", Long.parseLong(cid+""));
					cDao.updateByHQL(sb.getSql(), sb.getParams());
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
	 * 手动分配客户信息
	  *<功能简述>
	  *<功能详细描述>
	  *@param id 客户信息id集合
	  *@param _emp 用户id
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	  * 黄剑锋
	  * 2012-12-18 16:35 第一次新建
	 */
	@RequestMapping("/assignClient.do")
	public ModelAndView assignClient(HttpServletRequest req, @RequestParam("_emp") int _emp,
			@RequestParam("ids") String ids){
		JSONObject json = new JSONObject();
		String hql = "select c.id from Client c where c.follower.id = ? and c.clientStatus <> '3'";
		User user = null;
		Connection conn = null;
		boolean autoFlag = true;
		PreparedStatement ps = null;
		try {
			if("".equals(ids)){
				json.element("success", true);
				json.element("msg", "参数异常");
				return MvcUtil.jsonObjectModelAndView(json);
			}
			if(req.getSession() != null){
				user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				String now = StaticValues.sdf.format(new Date());
				conn = DBManager.getConnection();
				autoFlag = conn.getAutoCommit();
				conn.setAutoCommit(false);//禁止自动提交
				ps = conn.prepareStatement("update sign_client set follower=?,followDate=? where id=?");
				String[] id = ids.split(",");
				for(int i = 0, len = id.length; i < len; i++){
					ps.setInt(1, _emp);
					ps.setString(2, now);
					ps.setInt(3, Integer.parseInt(id[i]));
					ps.addBatch();
					ps.clearParameters();
				}
				ps.executeBatch();
				conn.commit();
			}
			json.element("success", true);
			json.element("msg", "分配成功!");
			return MvcUtil.jsonObjectModelAndView(json);
		} catch (Exception e) {
			try {
				conn.rollback();//回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			json.element("failure", true);
			json.element("msg", "提交失败,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(json);
		} finally {
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null){
				try {
					conn.setAutoCommit(autoFlag);//恢复以前的提交状态
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
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
			data = lsDao.findAll(hql+sb, pagination);
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
			@RequestParam("sourceId") int sourceId){
		JSONObject jsonObject = new JSONObject();
		if(sourceId <= 0){
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
				sb.addWhere("id", sourceId);
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
			data = slDao.findAll(hql+sb, pagination);
			String[] date = null;
			for(SourceLog sl : data){
				date = sl.getLogDate().split(":");
				sl.setLogDate(date[0] + ":" + date[1]);
			}
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
