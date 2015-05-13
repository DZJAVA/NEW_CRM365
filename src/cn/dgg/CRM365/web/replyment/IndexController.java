package cn.dgg.CRM365.web.replyment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.resources.ResourcesTrack;
import cn.dgg.CRM365.domain.resourcesManage.Client;
import cn.dgg.CRM365.domain.resourcesManage.ClientSource;
import cn.dgg.CRM365.domain.resourcesManage.ClientUser;
import cn.dgg.CRM365.domain.resourcesManage.SeeClient;
import cn.dgg.CRM365.domain.resourcesManage.ShowClient;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;
import cn.dgg.CRM365.web.resources.ResourcesTrackController;

/**
 * 页面显示首页操作
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  chenqin
  * @version  [版本号, Dec 24, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@SuppressWarnings("all")
@RequestMapping("/index")
@Controller
public class IndexController {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Client> cdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ShowClient> scdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ClientUser> cudao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ResourcesTrack> rtDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<SeeClient> scDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object[]> odao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object> objdao;
	
	/**
	 * 查找今日需完成的商机（今日的工作计划）
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/findTodayWorkPlan")
	public ModelAndView findTodayWorkPlan(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request, @RequestParam("conditions") String condition){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		User userSession = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = df.format(new Date());
		String hql = "select rt.plantime, rt.workplan, rt.id, c.follower.id, c.oppType, c.clientName, c.contactTel, c.loanAmount, " +
				"c.id, c.follower.userName, c.follower.department.depaName, c.editTime from ResourcesTrack rt, Client c where c.id = rt.client_name";	//根据权限查询
		//根据当前时间查询
		StringBuffer wherehql = new StringBuffer();
		String ruleDe = null;
		try {
			if(request.getSession() != null){
				userSession = (User) request.getSession().getAttribute("userSession");
				Long lDepartment = userSession.getDepartment().getId();
				ruleDe = userSession.getRole().getRoleCode();
				Long thisUserId = userSession.getId();
				//如果是部门经理，就根据部门id查找该部门下的内容
				if("201202".equals(ruleDe)){
					wherehql.append(" and c.follower.department = ").append(lDepartment);
				}
				//如果是当前登录人，就根据登录Id查找该用户的信息
				if("201203".equals(ruleDe)){
					wherehql.append(" and c.follower = ").append(thisUserId);
				}
				if("201208".equals(ruleDe)){
					wherehql.append(" and c.follower.department.superId = ").append(userSession.getDepartment().getSuperId());
				}
				if(condition != null && !"".equals(condition)){
					JSONObject json = JSONObject.fromObject(condition);
					String resoudep = String.valueOf(json.get("resoudep"));
					String resouemp = String.valueOf(json.get("resouemp"));
					String opp = String.valueOf(json.get("opp"));
					String plan = String.valueOf(json.get("plan"));
					String client_source = String.valueOf(json.get("client_source"));
					if(plan != null && !"".equals(plan)){
						wherehql.append(" and rt.plantime like '").append(plan).append("%'");
					}else{
						wherehql.append(" and rt.plantime like '").append(nowDate).append("%'");
					}
					if(resoudep!=null && !"".equals(resoudep)){
						wherehql.append(" and c.follower.department=").append(resoudep);
					}
					if(resouemp != null && !"".equals(resouemp)){
						wherehql.append(" and c.follower=").append(resouemp);
					}
					if(opp != null && !"".equals(opp)){
						wherehql.append(" and c.oppType = ").append(opp);
					}
					if(client_source != null && !"".equals(client_source)){
						wherehql.append(" and c.clientSourse = ").append(Long.parseLong(client_source));
					}
				}else{
					wherehql.append(" and rt.plantime like '").append(nowDate).append("%'");
				}
				wherehql.append(" and c.clientStatus <> '3'");
				wherehql.append(" order by rt.plantime, rt.rtid");
				List<Object[]> rts = odao.findAll(hql + wherehql, pagination);
				jsonObject.element("totalCount", pagination.getTotalResults());
				JSONArray data = new JSONArray();
				if (rts.size() > 0) {
					for (Object[] field : rts) {
						JSONObject item = new JSONObject();
						item.element("rtid", MvcUtil.toJsonString(field[2]));
						item.element("id", MvcUtil.toJsonString(field[8]));
						String oppType = "";//贷款类型
						String dkje = "";//贷款金额
						String khmz ="";//客户名字
						if("1".equals(field[4])){//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
							oppType = "房贷";
						}else if("2".equals(field[4])){
							oppType = "信贷";
						}else if("3".equals(field[4])){
							oppType = "短借";
						}else if("4".equals(field[4])){	
							oppType = "企贷";
						}
						if(!"".equals(field[7]) && field[7] != null){
							dkje = field[7].toString() + "万";
						}
						if(!"".equals(field[5]) && field[5] != null){
							khmz = field[5].toString();
						}
						item.element("clientName", MvcUtil.toJsonString(khmz+oppType+dkje));
						item.element("resourcespeople", MvcUtil.toJsonString(field[9]+""+field[10]));
						item.element("workPlan", MvcUtil.toJsonString(field[1]));
						if(field[6] != null){
							if("201202".equals(ruleDe)){
								if(userSession.getId().intValue() == Integer.parseInt(field[3].toString())){
									item.element("telephone", MvcUtil.toJsonString(field[6]));
								}else{
									item.element("telephone", MvcUtil.toJsonString(field[6].toString().substring(0, field[6].toString().length()-4)+"****"));
								}
							}else if("201208".equals(ruleDe)){
								item.element("telephone", MvcUtil.toJsonString(field[6].toString().substring(0, field[6].toString().length()-4)+"****"));
							}else{
								item.element("telephone", MvcUtil.toJsonString(field[6]));
							}
						}
						item.element("plantime", MvcUtil.toJsonString(field[0]));//计划时间
						item.element("editTime", MvcUtil.toJsonString(field[11]==null?"":field[11]));
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
	 * 查找提醒的客户资源
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findWarnClient")
	public ModelAndView findWarnClient(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");//提醒的id集合
		String[] ids = id.split(",");
		SqlBuilder sb = null;
		User user = null;//当前操作用户
		HttpSession session = request.getSession();
		JSONObject json = new JSONObject();
		try {
			if(session != null){
				user = (User)session.getAttribute(StaticValues.USER_SESSION);
				String roleCode = user.getRole().getRoleCode();
				if("201202".equals(roleCode)){//部门经理提醒标识置为1
					if(ids.length > 0){
						for(String cid : ids){
							sb = new SqlBuilder("Client", SqlBuilder.TYPE_UPDATE);
							sb.addField("remindFlag", "1");
							sb.addWhere("id", Long.parseLong(cid));
							cdao.updateByHQL(sb.getSql(), sb.getParams());
						}
					}
				}
				json.element("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}
	/**
	 * 首页还款管理
	  *<功能简述>
	  *<功能详细描述>
	  * @param gridLoadParams
	  * @param request
	  * @param response
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/findByRefund")
	public ModelAndView findByRefund(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request,
			HttpServletResponse response) {
		String _fid = request.getParameter("_fid");
		return new ModelAndView("replyment/loanDetailInfo", _fid, null);
	}
	/**
	
	 * 查找今日新增商机
	  *<功能简述>
	  *<功能详细描述>
	  * @param gridLoadParams
	  * @param request
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/todayAddClient")
	public ModelAndView todayAddClient(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request, @RequestParam("conditions") String condition){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = df.format(new Date());
		//根据当前时间查询
		StringBuffer wherehql = new StringBuffer(" where 1=1");
		Long lDepartment = null;	//得到当前登录人的部门信息
		String ruleDe = null;	//得到当前登录人的角色代码 
		Long thisUserId = null;	//	得到当前登录人的Id
		String hql = "select cu.id, cu.clientName, cu.contactTel, cu.loanAmount, cu.signPossible," +
			"cu.assignDate, cu.oppType, cu.city, cu.province, cu.clientSourse, cu.follower.userName, cu.follower.department.depaName, cu.editTime,cu.follower.id from Client cu";
		User userSession = null;
		try {
			userSession = (User) request.getSession().getAttribute("userSession");
			lDepartment = userSession.getDepartment().getId();
			ruleDe = userSession.getRole().getRoleCode();
			thisUserId = userSession.getId();
			//如果是部门经理，就根据部门id查找该部门下的内容
			if(ruleDe.equals("201202")){
				wherehql.append(" and cu.follower.department.id = ").append(lDepartment).append(" and cu.clientStatus <> '3'");
			}
			//如果是员工，就根据当前登录人进行查询
			else if(ruleDe.equals("201203")){
				wherehql.append(" and cu.follower.id = ").append(thisUserId).append(" and cu.clientStatus <> '3'");
			}
			if("201208".equals(ruleDe)){
				wherehql.append(" and cu.follower.department.superId = ").append(userSession.getDepartment().getSuperId());
			}
			if(condition != null && !"".equals(condition)){
				JSONObject json = JSONObject.fromObject(condition);
				String clientDep = String.valueOf(json.get("clientDep"));
				String clientEmp = String.valueOf(json.get("clientEmp"));
				String newTime = String.valueOf(json.get("newTime"));
				String opp = String.valueOf(json.get("opp"));
				String client_source = String.valueOf(json.get("client_source"));
				String _provinces = json.getString("_provinces");//省份
				String _citys = json.getString("_citys");//城市
				if(clientDep!=null && !"".equals(clientDep)){
					wherehql.append(" and cu.follower.department.id=").append(clientDep);
				}
				if(clientEmp!=null && !"".equals(clientEmp)){
					wherehql.append(" and cu.follower.id=").append(clientEmp);
				}
				if(newTime!=null && !"".equals(newTime)){
					wherehql.append(" and cu.assignDate='").append(newTime).append("'");
				}else{
					wherehql.append(" and (cu.assignDate='").append(nowDate).append("' or cu.assignTime like '").append(nowDate).append("%'").append(")");
				}
				if(opp != null && !"".equals(opp)){
					wherehql.append(" and cu.oppType = ").append(opp);
				}
				if(client_source != null && !"".equals(client_source)){
					wherehql.append(" and cu.clientSourse = ").append(Long.parseLong(client_source));
				}
				if(_provinces != null && !"".equals(_provinces)){
					wherehql.append(" and cu.province = ").append(_provinces);
				}
				if(_citys != null && !"".equals(_citys)){
					wherehql.append(" and cu.city = ").append(_citys);
				}
			
			}else{
				wherehql.append(" and (cu.assignDate like'").append(nowDate).append("%' or cu.assignTime like '").append(nowDate).append("%'").append(")");
			}
			List<Object[]> clientUsers = odao.findAll(hql + wherehql, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
			JSONArray data = new JSONArray();
			Client field = null;
			if (clientUsers.size() > 0) {
				for (Object[] o : clientUsers) {
					JSONObject item = new JSONObject();
					field = new Client();
					packetClient(field, o);
					item.element("id", field.getId());
					item.element("userName", field.getEliTime()+field.getClientAdd());
					if(field.getContactTel() != null && !"".equals(field.getContactTel())){
						if("201202".equals(ruleDe)){
							if(userSession.getId().intValue() == Integer.parseInt(o[13].toString())){
								item.element("contactTel", field.getContactTel());
							}else{
								item.element("contactTel", MvcUtil.toJsonString(field.getContactTel().substring(0, field.getContactTel().length()-4)+"****"));
							}
						}else if("201208".equals(ruleDe)){
							item.element("contactTel", MvcUtil.toJsonString(field.getContactTel().substring(0, field.getContactTel().length()-4)+"****"));
						}else{
							item.element("contactTel", field.getContactTel());
						}
					}
					if("1".equals(field.getSignPossible())){//签单可能性(1为100%、2为80%、3为50%、4为0%)
						item.element("signPossible", MvcUtil.toJsonString("100%"));
					}else if("2".equals(field.getSignPossible())){
						item.element("signPossible", MvcUtil.toJsonString("80%"));
					}else if("3".equals(field.getSignPossible())){
						item.element("signPossible", MvcUtil.toJsonString("50%"));
					}else if("4".equals(field.getSignPossible())){
						item.element("signPossible", MvcUtil.toJsonString("10%"));
					}else if("5".equals(field.getSignPossible())){
						item.element("signPossible", MvcUtil.toJsonString("0%"));
					}
					String oppType = "";//贷款类型
					String dkje = "";//贷款金额
					String khmz ="";//客户名字
					if("1".equals(field.getOppType())){//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
						oppType = "房贷";
					}else if("2".equals(field.getOppType())){
						oppType = "信贷";
					}else if("3".equals(field.getOppType())){
						oppType = "短借";
					}else if("4".equals(field.getOppType())){	
						oppType = "企贷";
					}
					if("".equals(field.getLoanAmount())||field.getLoanAmount()==null)
					{
						dkje="";
					}else{
						dkje=field.getLoanAmount() + "万";
					}
					if("".equals(field.getClientName())||field.getClientName()==null){
						khmz="";
					}else{
						khmz=field.getClientName();
					}
					item.element("clientName", khmz+oppType+dkje);
					item.element("jrproCity", MvcUtil.toJsonString(field.getProvince() +"," +field.getCity()));//省份 城市
					item.element("assignDate",MvcUtil.toJsonString(field.getAssignDate()));//创建日期
					if(field.getClientSourse() != null){
						item.element("clientSourse", MvcUtil.toJsonString(field.getClientSourse().getName()));
					}
					item.element("editTime",field.getEditTime());
					data.add(item);
				}
			}
			jsonObject.element("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	
	public void packetClient(Client c, Object[] o){
		c.setId(Long.parseLong(o[0].toString()));
		c.setClientName(o[1]==null?"":o[1].toString());
		c.setContactTel(o[2]==null?"":o[2].toString());
		c.setLoanAmount(o[3]==null?"":o[3].toString());
		c.setSignPossible(o[4]==null?"":o[4].toString());
		c.setAssignDate(o[5]==null?"":o[5].toString());
		c.setOppType(o[6]==null?"":o[6].toString());
		c.setCity(o[7]==null?"":o[7].toString());
		c.setProvince(o[8]==null?"":o[8].toString());
		if(o[9]!=null) {
			c.setClientSourse((ClientSource)o[9]);
		}
		c.setClientAdd(o[10]==null?"":o[10].toString());
		c.setEliTime(o[11]==null?"":o[11].toString());
		c.setEditTime(o[12]==null?"":o[12].toString());
	}
	/**
	 * 查找已签单商机
	  *<功能简述>
	  *<功能详细描述>
	  * @param gridLoadParams
	  * @param request
	  * @param state
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/signedClient")
	public ModelAndView signedClient(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request, @RequestParam("conditions") String condition){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(),gridLoadParams.getLimit());
		User userSession = null;
		Long lDepartment = null;	//得到当前登录人的部门信息
		String ruleDe = null;	//得到当前登录人的角色代码 
		Long thisUserId = null;	//	得到当前登录人的Id
		StringBuffer sb = new StringBuffer("");
		String hql = "select c.id, c.follower, c.oppType, c.loanAmount, c.clientName, c.contactTel, c.signTime, c.province , c.city " +
				"from Client c where c.clientStatus = '1'";
		try {
			if(request.getSession() != null){
				userSession = (User) request.getSession().getAttribute("userSession");
				lDepartment = userSession.getDepartment().getId();
				ruleDe = userSession.getRole().getRoleCode();
				thisUserId = userSession.getId();
				//如果是部门经理，就根据部门id查找该部门下的内容
				if(ruleDe.equals("201202")){
					sb.append(" and c.follower.department.id = ").append(lDepartment);
				}
				//如果是员工，就根据当前登录人进行查询
				 if(ruleDe.equals("201203")){
					 sb.append(" and c.follower.id = ").append(thisUserId);
				}
				if("201208".equals(ruleDe)){
					sb.append(" and c.follower.department.superId = ").append(userSession.getDepartment().getSuperId());
				}
				if(condition != null && !"".equals(condition)){
					JSONObject json = JSONObject.fromObject(condition);
					String signedDep = String.valueOf(json.get("signedDep"));
					String signedEmp = String.valueOf(json.get("signedEmp"));
					String qdtime = String.valueOf(json.get("qdtime"));
					String opp = String.valueOf(json.get("opp"));
					String client_source = String.valueOf(json.get("client_source"));
					
					String _provinces3 = json.getString("_provinces3");//省份
					String _citys3 = json.getString("_citys3");//城市
					
					if(signedDep!=null&&!"".equals(signedDep)){
						sb.append(" and c.follower.department.id=").append(signedDep);
					}
					if(signedEmp!=null&&!"".equals(signedEmp)){
						sb.append(" and c.follower.id=").append(signedEmp);
					}
					if(qdtime!=null&&!"".equals(qdtime)){
						sb.append(" and c.signTime like '").append(qdtime).append("%'");
					}
					if(opp != null && !"".equals(opp)){
						sb.append(" and c.oppType = ").append(opp);
					}if(client_source != null && !"".equals(client_source)){
						sb.append(" and c.clientSourse.id = ").append(Long.parseLong(client_source));
					}
					if(_provinces3 != null && !"".equals(_provinces3)){
						sb.append(" and c.province = ").append(_provinces3);
					}
					if(_citys3 != null && !"".equals(_citys3)){
						sb.append(" and c.city = ").append(_citys3);
					}
				}
				sb.append(" order by c.signTime desc");
				List<Object[]> clientUsers = odao.findAll(hql + sb, pagination);
				jsonObject.element("totalCount", pagination.getTotalResults());
				System.out.println(pagination.getTotalResults());
				JSONArray data = new JSONArray();
				User user = null;
				if (clientUsers.size() > 0) {
					System.out.println(clientUsers.size());
					for (Object[] obj : clientUsers) {
						JSONObject item = new JSONObject();
						item.element("id", obj[0]);
						user = (User)obj[1];
						item.element("userName",user.getDepartment().getDepaName()+"："+ user.getUserName());
						item.element("user_depaName", user.getDepartment().getDepaName());
						String oppType = "";//贷款类型
						String dkje = "";//贷款金额
						String khmz ="";//客户名字
						if("1".equals(obj[2])){//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
							oppType = "房贷";
						}else if("2".equals(obj[2])){
							oppType = "信贷";
						}else if("3".equals(obj[2])){
							oppType = "短借";
						}else if("4".equals(obj[2])){	
							oppType = "企贷";
						}else if("".equals(obj[2]) || obj[2]==null){
							oppType = " ";
						}
						if("".equals(obj[3])||obj[3]==null){
							dkje = "";
						}else{
							dkje = obj[3] + "万";
						}
						if("".equals(obj[4])||obj[4]==null){
							khmz = "";
						}else{
							khmz = obj[4].toString();
						}
						if("201202".equals(ruleDe)){
							if(userSession.getId().intValue() == user.getId().intValue()){
								item.element("contactTel", MvcUtil.toJsonString(obj[5]));
							}else{
								item.element("contactTel", MvcUtil.toJsonString(obj[5].toString().substring(0, obj[5].toString().length()-4)+"****"));
							}
						}else if("201208".equals(ruleDe)){
							item.element("contactTel", MvcUtil.toJsonString(obj[5].toString().substring(0, obj[5].toString().length()-4)+"****"));
						}else{
							item.element("contactTel", MvcUtil.toJsonString(obj[5]));
						}
						item.element("clientName",khmz+oppType+dkje);
						item.element("signingtime", MvcUtil.toJsonString(obj[6]));
						item.element("yqdproCity",MvcUtil.toJsonString(obj[7])+',' + MvcUtil.toJsonString(obj[8]));
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
	 * 百度搜索
	  *<功能简述>
	  *<功能详细描述>
	  * @param cusTelOrName	 客户电话,客户名称
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/findByCusNameOrTel")
	public ModelAndView findByCusNameOrTel(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request,@RequestParam("conditions") String conditions){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		User userSession = null;
		Long lDepartment = null;	//得到当前登录人的部门信息
		String ruleDe = null;	//得到当前登录人的角色代码 
		List<ShowClient> cuser = null;//资源分配 中间表（客户）
		List<Client> clients = null; //客户
		StringBuffer sb = new StringBuffer();
		String hql = "from ShowClient cu where";
		if(request.getSession() != null){
			try {
				if(conditions != null && !"".equals(conditions)){
					JSONObject json = JSONObject.fromObject(conditions);
					String cusTelOrName = String.valueOf(json.get("cusMes"));
					if(cusTelOrName != null && !"".equals(cusTelOrName)){
						sb.append(" (cu.clientName like '").append(cusTelOrName).append("%'");
						sb.append(" or cu.contactTel like '").append(cusTelOrName).append("%'");
						sb.append(" or cu.spareTel1 like '").append(cusTelOrName).append("%'");
						sb.append(" or cu.spareTel2 like '").append(cusTelOrName).append("%')");
						userSession = (User) request.getSession().getAttribute("userSession");
						ruleDe = userSession.getRole().getRoleCode();//得到角色代码
						if(ruleDe.equals("201202")){ 	//部门经理
							sb.append(" and (cu.follower.department.id =").append(userSession.getDepartment().getId()).append(" or cu.clientStatus = '3')");
						}else if(ruleDe.equals("201203")){	//员工
							sb.append(" and (cu.follower.id =").append(userSession.getId()).append(" or cu.clientStatus = '3')");
						}
						if("201208".equals(ruleDe)){
							sb.append(" and (cu.follower.department.superId = ").append(userSession.getDepartment().getSuperId()).append(" or cu.clientStatus = '3')");
						}
						cuser = scdao.findAll(hql+sb, pagination);
						jsonObject.element("totalCount", pagination.getTotalResults());
						JSONArray data = new JSONArray();
						if (cuser.size() > 0) {
							for (ShowClient field : cuser) {
								JSONObject item = new JSONObject();
								item.element("id", field.getId());
								if("3".equals(field.getClientStatus()) || "201208".equals(ruleDe)){
									ResourcesTrackController.hideNum(ruleDe, "contactTel", field.getContactTel(), item);
									ResourcesTrackController.hideNum(ruleDe, "spareTel1", field.getSpareTel1(), item);
									ResourcesTrackController.hideNum(ruleDe, "spareTel2", field.getSpareTel2(), item);
								}else{
									ResourcesTrackController.hideDeptNum(item, new String[]{"contactTel","spareTel1","spareTel2"}, 
										new String[]{field.getContactTel(),field.getSpareTel1(),field.getSpareTel2()}, ruleDe, userSession.getId(), 
										field.getFollower()==null?null:field.getFollower().getId());
								}
								item.element("clientAdd", MvcUtil.toJsonString(field.getClientAdd()));//客户地址
								String oppType = "";//贷款类型
								String dkje = "";//贷款金额
								String khmz ="";//客户名字
								if("1".equals(field.getOppType())){//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
									oppType = "房贷";
								}else if("2".equals(field.getOppType())){
									oppType = "信贷";
								}else if("3".equals(field.getOppType())){
									oppType = "短借";
								}else if("4".equals(field.getOppType())){	
									oppType = "企贷";
								}else if("".equals(field.getOppType())||field.getOppType()==null){	
									oppType = " ";
								}
								if("".equals(field.getLoanAmount())||field.getLoanAmount()==null)						{
									dkje="";
								}else{
									dkje=field.getLoanAmount() + "万";
								}
								if("".equals(field.getClientName())||field.getClientName()==null)						{
									khmz="";
								}else{
									khmz=field.getClientName();
								}
								item.element("clientName", MvcUtil.toJsonString(khmz+oppType+dkje));//客户姓名
								if("1".equals(field.getClientStatus())){
									item.element("clientStatus", MvcUtil.toJsonString("已签单"));
								}else if("2".equals(field.getClientStatus())){
									item.element("clientStatus", MvcUtil.toJsonString("未签单"));
								}else if("3".equals(field.getClientStatus())){
									item.element("clientStatus", MvcUtil.toJsonString("淘汰"));
								}else if("4".equals(field.getClientStatus())){
									item.element("clientStatus", MvcUtil.toJsonString("退单"));
								}
								if("1".equals(field.getSignPossible())){//签单可能性(1为100%、2为80%、3为50%、4为0%)
									item.element("signPossible", MvcUtil.toJsonString("100%"));
								}else if("2".equals(field.getSignPossible())){
									item.element("signPossible", MvcUtil.toJsonString("80%"));
								}else if("3".equals(field.getSignPossible())){
									item.element("signPossible", MvcUtil.toJsonString("50%"));
								}else if("4".equals(field.getSignPossible())){
									item.element("signPossible", MvcUtil.toJsonString("10%"));
								}else if("5".equals(field.getSignPossible())){
									item.element("signPossible", MvcUtil.toJsonString("0%"));
								}
								item.element("assignDate", MvcUtil.toJsonString(field.getAssignDate()));//分配日期
								item.element("assignTime", MvcUtil.toJsonString(field.getAssignTime()));//分配时间
								if(field.getClientSourse() != null){
									item.element("clientSource", MvcUtil.toJsonString(field.getClientSourse().getName()));//客户来源
								}
								if(field.getFollower() != null){
									item.element("managerUser", MvcUtil.toJsonString(field.getFollower().getUserName()));
								}else{
									item.element("managerUser", "");
								}
								item.element("bdshproCity",MvcUtil.toJsonString(field.getProvince())+',' + MvcUtil.toJsonString(field.getCity()));
								item.element("remark", MvcUtil.toJsonString(field.getRemark()));//备注
								if(field.getAssignId() != null){
									item.element("assignId", MvcUtil.toJsonString(field.getAssignId()));
									if(!"".equals(field.getAssignName()) && field.getAssignName() != null){
										item.element("assignName", MvcUtil.toJsonString(field.getAssignName()));
									}
								}
								data.add(item);
							}
						}
						jsonObject.element("data", data);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 查找还款记录
	  *<功能简述>
	  *<功能详细描述>	
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadSuccessClient")
	public ModelAndView loadSuccessClient(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest req, @RequestParam("conditions") String condition){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		StringBuffer sb = new StringBuffer("");
		String hql = "select c.id, c.follower, c.oppType, c.loanAmount, c.clientName, c.contactTel, c.signTime ,c.province , c.city " +
				"from Client c where c.clientStatus = '1'";
		Long lDepartment = null;	//得到当前登录人的部门信息
		String ruleDe = null;	//得到当前登录人的角色代码 
		Long thisUserId = null;
		try{
			if(req.getSession() != null){
				User userSession = (User) req.getSession().getAttribute("userSession");
				if(condition != null && !"".equals(condition)){
					JSONObject json = JSONObject.fromObject(condition);
					String refundDep = String.valueOf(json.get("refundDep"));
					String refundEmp = String.valueOf(json.get("refundEmp"));
					String opp = String.valueOf(json.get("opp"));
					String client_source = String.valueOf(json.get("client_source"));
					
					String _provinces2 = json.getString("_provinces2");//省份
					String _citys2 = json.getString("_citys2");//城市
					
					if(refundDep!=null&&!"".equals(refundDep)){
						sb.append(" and c.follower.department.id=").append(refundDep);
					}
					if(refundEmp!=null&&!"".equals(refundEmp)){
						sb.append(" and c.follower.id=").append(refundEmp);
					}
					if(opp != null && !"".equals(opp)){
						sb.append(" and c.oppType = ").append(opp);
					}
					if(client_source != null && !"".equals(client_source)){
						sb.append(" and c.clientSourse =").append(Long.parseLong(client_source));
					}
					if(_provinces2 != null && !"".equals(_provinces2)){
						sb.append(" and c.province = ").append(_provinces2);
					}
					if(_citys2 != null && !"".equals(_citys2)){
						sb.append(" and c.city = ").append(_citys2);
					}
				}
				lDepartment = userSession.getDepartment().getId();
				ruleDe = userSession.getRole().getRoleCode();
				thisUserId = userSession.getId();
				if(ruleDe.equals("201202")){//如果是部门经理，就根据部门id查找该部门下的内容
					sb.append(" and c.follower.department.id = ").append(lDepartment);
				}
				if(ruleDe.equals("201203")){//如果是员工，就根据当前登录人进行查询
					sb.append(" and c.follower.id = ").append(thisUserId);
				}
				if("201208".equals(ruleDe)){
					sb.append(" and c.follower.department.superId = ").append(userSession.getDepartment().getSuperId());
				}
				sb.append(" order by c.signTime desc");
				List<Object[]> clients = odao.findAll(hql + sb, pagination);
				jsonObject.element("totalCount", pagination.getTotalResults());
				JSONArray data = new JSONArray();
				User user = null;
				if (clients.size() > 0) {
					for (Object[] obj : clients) {
						JSONObject item = new JSONObject();
						item.element("id", obj[0]);
						user = (User)obj[1];
						if(user != null){
							item.element("userName",user.getDepartment().getDepaName()+"："+ user.getUserName());
							item.element("user_depaName", user.getDepartment().getDepaName());
						}
						String oppType = "";//贷款类型
						String dkje = "";//贷款金额
						String khmz ="";//客户名字
						if("1".equals(obj[2])){//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
							oppType = "房贷";
						}else if("2".equals(obj[2])){
							oppType = "信贷";
						}else if("3".equals(obj[2])){
							oppType = "短借";
						}else if("4".equals(obj[2])){	
							oppType = "企贷";
						}
						if(!"".equals(obj[3]) && obj[3] != null){
							dkje = obj[3] + "万";
						}
						if(!"".equals(obj[4]) && obj[4] != null){
							khmz = obj[4].toString();
						}
						if("201202".equals(ruleDe)){
							if(userSession.getId().intValue() == user.getId().intValue()){
								item.element("contactTel", MvcUtil.toJsonString(obj[5]));
							}else{
								item.element("contactTel", MvcUtil.toJsonString(obj[5].toString().substring(0, obj[5].toString().length()-4)+"****"));
							}
						}else if("201208".equals(ruleDe)){
							item.element("contactTel", MvcUtil.toJsonString(obj[5].toString().substring(0, obj[5].toString().length()-4)+"****"));
						}else{
							item.element("contactTel", MvcUtil.toJsonString(obj[5]));
						}
						item.element("clientName",khmz+oppType+dkje);
						item.element("signingtime", MvcUtil.toJsonString(obj[6]));
						
						item.element("hkproCity",MvcUtil.toJsonString(obj[7])+',' + MvcUtil.toJsonString(obj[8]));
						data.add(item);
					}
				}
				jsonObject.element("data", data);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
}
