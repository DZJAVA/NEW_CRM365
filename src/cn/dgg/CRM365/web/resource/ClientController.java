package cn.dgg.CRM365.web.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.authority.Department;
import cn.dgg.CRM365.domain.authority.Role;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.authority.UserRole;
import cn.dgg.CRM365.domain.replyment.LoanDetail;
import cn.dgg.CRM365.domain.replyment.Rcount;
import cn.dgg.CRM365.domain.resources.AddClient;
import cn.dgg.CRM365.domain.resources.Eliminate;
import cn.dgg.CRM365.domain.resources.ResourcesTrack;
import cn.dgg.CRM365.domain.resources.SignClient;
import cn.dgg.CRM365.domain.resourcesManage.Client;
import cn.dgg.CRM365.domain.resourcesManage.ClientDifRecord;
import cn.dgg.CRM365.domain.resourcesManage.ClientSource;
import cn.dgg.CRM365.domain.resourcesManage.ClientUser;
import cn.dgg.CRM365.domain.resourcesManage.SeeClient;
import cn.dgg.CRM365.domain.resourcesManage.ShowClient;
import cn.dgg.CRM365.domain.resourcesManage.WrongClient;
import cn.dgg.CRM365.domain.resourcesManage.XzAllocation;
import cn.dgg.CRM365.domain.resourcesManage.XzShituAllocation;
import cn.dgg.CRM365.util.commonUtil.DBManager;
import cn.dgg.CRM365.util.commonUtil.ExportExcelUtil;
import cn.dgg.CRM365.util.commonUtil.ImportExcelUtil;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.commonUtil.UploadFile;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.mvc.view.AbstractMimeView;
import cn.dgg.CRM365.util.mvc.view.MimeBytesView;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;
import cn.dgg.CRM365.web.resources.ResourcesTrackController;


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
@RequestMapping("/client")
@Controller
public class ClientController {
	
	ICommonDAO<Client> dao;
	public ICommonDAO<Client> getDao() {
		return dao;
	}
	@Autowired
	public void setDao(@Qualifier("commonDAOProxy")ICommonDAO<Client> dao) {
		this.dao = dao;
	}
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Department> ddao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<User> udao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ClientDifRecord> cdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<UserRole> urdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object> odao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object[]> objDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<WrongClient> wdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Rcount> rdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<LoanDetail> lddao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ClientSource> csdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ClientUser> cudao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ResourcesTrack> trackDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<SeeClient> seeDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Eliminate> eliDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<AddClient> addDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<SignClient> signDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<XzAllocation> xaDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<XzShituAllocation> xsaDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<SeeClient> scDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ShowClient> showDao;
	private static Client c = null;//客户信息实体
	private static SqlBuilder sb = null;;//更新实体
	private static Map<String, String> cityMap = null;
	private static Map<String, String> proMap = null;
	
	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage(){
		return new ModelAndView("resource/client");
	}
	/**
	 * 
	 * @param sb
	 * @param flags
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> MsgCondition(StringBuffer sb, String[] flags) throws Exception{
		List<Object[]> list = new ArrayList<Object[]>();
		String fields = "select ci.id, ci.clientName, ci.contactTel, ci.spareTel1, ci.spareTel2, ci.loanAmount, ci.clientAdd, ci.clientStatus, ci.signPossible," +
			"ci.assignDate, ci.assignTime, ci.remark, ci.oppType, ci.city, ci.province, ci.clientSourse, ci.workPlanNewTime, ci.eliTime,ci.follower.id," +
			"ci.follower.userName, ci.assignId, ci.assignName";
		String hql = fields + " from ShowClient ci";
		String max = hql + " where ci.id =(select MAX(c.id) from ShowClient c ";
		String min = hql + " where ci.id =(select MIN(c.id) from ShowClient c ";
		String nowTime = StaticValues.sdf.format(new Date());//当前时间
		if("0".equals(flags[0])){
			sb.append(" and (c.assignTime like '"+nowTime+"%' or c.assignDate = '"+nowTime+"') ").append(" and c.id < ").append(Long.parseLong(flags[1])).append(")");
			list = objDao.findAll(max+sb);
		}else if("1".equals(flags[0])){
			sb.append(" and (c.assignTime like '"+nowTime+"%' or c.assignDate = '"+nowTime+"') ").append(" and c.id > ").append(Long.parseLong(flags[1])).append(")");
			list = objDao.findAll(min+sb);
		}else if("6".equals(flags[0])){
			String[] conditions = flags[2].split(":");
			StatementsController.judgeBasicConditions(sb, conditions[6], conditions[3], conditions[4], conditions[2], conditions[0], conditions[1], conditions[5]);
			sb.append(" and c.id < ").append(Long.parseLong(flags[1])).append(")");
			list = objDao.findAll(max+sb);
		}else if("7".equals(flags[0])){
			String[] conditions = flags[2].split(":");
			StatementsController.judgeBasicConditions(sb, conditions[6], conditions[3], conditions[4], conditions[2], conditions[0], conditions[1], conditions[5]);
			sb.append(" and c.id > ").append(Long.parseLong(flags[1])).append(")");
			list = objDao.findAll(min+sb);
		}
		return list;
	}
	/**
	 * 根据角色权限来判断条件
	  *<功能简述>
	  *<功能详细描述>
	  * @param roleCode
	  * @param sb
	  * @param uid
	  * @param did
	  * @return [参数说明]
	  * 
	  * @return StringBuffer [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public void roleCondition(String roleCode, StringBuffer sb, String userHql, String deptHql, User user, String centralHql){
		if("201203".equals(roleCode)){//员工只能看分配给自己的资源
			sb.append(userHql).append(user.getId()).append(" and c.clientStatus <> '3'");
		}else if("201202".equals(roleCode)){//部门经理能够看他所在的部门的所有资源
			sb.append(deptHql).append(user.getDepartment().getId()).append(" and c.clientStatus <> '3'");
		}else if("201208".equals(roleCode)){
			sb.append(centralHql).append(user.getDepartment().getSuperId());
		}
	}
	/**
	 * 处理上一个下一个
	  *<功能简述>
	  *<功能详细描述>
	  * @param _flag
	  * @param user
	  * @param jsonObject
	  * @param sb
	  * @param dataList
	  * @return
	  * @throws Exception [参数说明]
	  * 
	  * @return List<Client> [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public List<Object[]> lastNext(String _flag, User user, JSONObject jsonObject, StringBuffer sb, List<Object[]> dataList, String hql) throws Exception{
		String nowTime = StaticValues.sdf.format(new Date());//当前时间
		Role role = user.getRole();
		String[] _flags = _flag.split(",");
		//String hql = "select c from ShowClient c";
		StringBuffer sbu = new StringBuffer();
		if("4".equals(_flags[0])){//工作计划对应的上一个客户
			StringBuffer sBuffer = new StringBuffer();
			String rhql = "select rt.rtid, rt.plantime from ResourcesTrack rt , Client c where c.id = rt.client_name and rt.plantime like '"+nowTime+"%' and rt.plantime <= '" + _flags[2] + "'";
			roleCondition(role.getRoleCode(), sBuffer, " and c.follower.id = ", " and c.follower.department.id = ", 
					user, " and c.follower.department.superId = ");
			sBuffer.append(" order by rt.plantime, rt.rtid");
			List<Object[]> list = objDao.findAll(rhql + sBuffer);
			sbu.append(", ResourcesTrack rt where c.id = rt.client_name and rt.rtid = ");
			if(list.size() > 0){
				for(int i = list.size() - 1; i >= 0; i--){
					Object[] obj = list.get(i);
					if(_flags[2].equals(list.get(i)[1])){
						if(Long.parseLong(obj[0].toString()) < Long.parseLong(_flags[1])){
							jsonObject.element("nextId", obj[0] + "," + obj[1]);
							sbu.append(Long.parseLong(obj[0].toString()));
							dataList = objDao.findAll(hql + sbu);
							break;
						}
					}else{
						jsonObject.element("nextId", obj[0] + "," + obj[1]);
						sbu.append(Long.parseLong(obj[0].toString()));
						dataList = objDao.findAll(hql + sbu);
						break;
					}
				}
				if(dataList.size() == 0){
					jsonObject.element("nextId", _flags[1] + "," + _flags[2]);
					sbu.append(Long.parseLong(_flags[1]));
					dataList = objDao.findAll(hql + sbu);
				}
			}else{//没上一个资源时
				jsonObject.element("nextId", _flags[1] + "," + _flags[2]);
				sbu.append(Long.parseLong(_flags[1]));
				dataList = objDao.findAll(hql + sbu);
			}
		}else if("5".equals(_flags[0])){//工作计划对应的下一个客户
			StringBuffer buffer = new StringBuffer();
			String rhql = "select rt.rtid, rt.plantime from ResourcesTrack rt, Client c where c.id = rt.client_name and rt.plantime like '"+nowTime+"%' and rt.plantime >= '" + _flags[2] + "'";
			roleCondition(role.getRoleCode(), buffer, " and c.follower.id = ", " and c.follower.department.id = ", 
					user, " and c.follower.department.superId = ");
			buffer.append(" order by rt.plantime, rt.rtid");
			List<Object[]> list = objDao.findAll(rhql + buffer);
			sbu.append(", ResourcesTrack rt where c.id = rt.client_name and rt.rtid = ");
			if(list.size() > 0){
				for(Object[] obj : list){
					if(_flags[2].equals(obj[1])){
						if(Long.parseLong(obj[0].toString()) > Long.parseLong(_flags[1])){
							jsonObject.element("nextId", obj[0] + "," + obj[1]);
							sbu.append(Long.parseLong(obj[0].toString()));
							dataList = objDao.findAll(hql + sbu);
							break;
						}
					}else{
						jsonObject.element("nextId", obj[0] + "," + obj[1]);
						sbu.append(Long.parseLong(obj[0].toString()));
						dataList = objDao.findAll(hql + sbu);
						break;
					}
				}
				if(dataList.size() == 0){
					jsonObject.element("nextId", _flags[1] + "," + _flags[2]);
					sbu.append(Long.parseLong(_flags[1]));
					dataList = objDao.findAll(hql + sbu);
				}
			}else{//没下一个资源时
				jsonObject.element("nextId", _flags[1] + "," + _flags[2]);
				sbu.append(Long.parseLong(_flags[1]));
				dataList = objDao.findAll(hql + sbu);
			}
		}else if("2".equals(_flags[0])){//签单商机对应的上一个
			StringBuffer sBuffer = new StringBuffer();
			String shql = "select c.id, c.signTime from Client c where " +
					"c.clientStatus = '1' and c.signTime > '" + _flags[2] + "'";
			roleCondition(role.getRoleCode(), sBuffer, " and c.follower.id = ", " and c.follower.department.id = ", 
					user, " and c.follower.department.superId = ");
			sBuffer.append(" order by c.signTime desc");
			List<Object[]> list = objDao.findAll(shql + sBuffer);
			if(list.size() > 0){
				jsonObject.element("nextId", list.get(list.size() - 1)[0] + "," + list.get(list.size() - 1)[1]);
				sbu.append(" where c.id = ").append(Long.parseLong(list.get(list.size() - 1)[0].toString()));
				dataList = objDao.findAll(hql + sbu);
			}else{//没有上一个
				jsonObject.element("nextId", _flags[1] + "," + _flags[2]);
				sbu.append(" where c.id = ").append(Long.parseLong(_flags[1]));
				dataList = objDao.findAll(hql + sbu);
			}
		}else if("3".equals(_flags[0])){//签单商机对应的下一个
			StringBuffer sBuffer = new StringBuffer();
			String shql = "select c.id, c.signTime from Client c where " +
					"c.clientStatus = '1' and c.signTime < '" + _flags[2] + "'";
			roleCondition(role.getRoleCode(), sBuffer, " and c.follower.id = ", " and c.follower.department.id = ", 
					user, " and c.follower.department.superId = ");
			sBuffer.append(" order by c.signTime desc LIMIT 0,1");
			List<Object[]> list = objDao.findAll(shql + sBuffer);
			if(list.size() > 0){
				jsonObject.element("nextId", list.get(0)[0] + "," + list.get(0)[1]);
				sbu.append(" where c.id = ").append(Long.parseLong(list.get(0)[0].toString()));
				dataList = objDao.findAll(hql + sbu);
			}else{//没有上一个
				jsonObject.element("nextId", _flags[1] + "," + _flags[2]);
				sbu.append(" where c.id = ").append(Long.parseLong(_flags[1]));
				dataList = objDao.findAll(hql + sbu);
			}
		}else{
			roleCondition(role.getRoleCode(), sb, " and c.follower.id = ", " and c.follower.department.id = ", 
					user, " and c.follower.department.superId = ");
			dataList = MsgCondition(sb, _flags);
			if(dataList.size() == 0){//没上一个或下一个资源时
				sbu.append(" where c.id = ").append(Long.parseLong(_flags[1]));
				dataList = objDao.findAll(hql + sbu);
			}
		}
		return dataList;
	}
	/**
	 * 处理从首页传过来的ids
	  *<功能简述>
	  *<功能详细描述>
	  * @param _pid
	  * @param user
	  * @param jsonObject
	  * @param sb
	  * @param dataList
	  * @param hql
	  * @return
	  * @throws Exception [参数说明]
	  * 
	  * @return List<Client> [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public List<Object> passClient(String _pid, User user, JSONObject jsonObject, StringBuffer sb, List<Object> dataList, String hql) throws Exception{
		Role role = user.getRole();
		String[] pids = _pid.split(",");//判断是否为百度搜索传过来的id
		if(role != null){
			if(pids.length == 1){//今日新增商机传来的id
				sb.append(" and c.id = ").append(Long.parseLong(_pid));
			}else if(pids.length == 2){
				if(pids[1].equals("0")){//百度搜索传来的id
					sb.append(" and c.id = ").append(Long.parseLong(pids[0]));
				}else{//签单商机传来的id
					sb.append(" and c.id = ").append(Long.parseLong(pids[0]));
					jsonObject.element("nextId", pids[0] + "," + pids[1]);
				}
			}else{//今日工作计划传来的id
				roleCondition(role.getRoleCode(), sb, " and c.follower.id = ", " and c.follower.department.id = ",
						user, " and c.follower.department.superId = ");
				sb.append(" and c.id = ").append(Long.parseLong(pids[0]));
				jsonObject.element("nextId", pids[1] + "," + pids[2]);
			}
			dataList = odao.findAll(hql + sb);
		}
		return dataList;
	};
	/**
	 * 拼接查询条件
	 * @param json
	 * @param sb
	 * @param nowTime当前时间
	 */
	public static void connectConditions(JSONObject json, StringBuffer sb, String nowTime, int flag){
		String _createDate = json.getString("_createDate");//创建时间类型（1为今天、2为本周、3为本月、4为本年）
		String _createStart = json.getString("_createStart");//创建开始日期
		String _createEnd = json.getString("_createEnd");//创建结束日期
		String _dept = json.getString("_dept");//跟单部门
		String _emp = json.getString("_emp");//跟单人
		String _opp = json.getString("_oppType");//商机类型
		String _signPos = json.getString("_signPos");//签单可能性
		String _startLoan = json.getString("_startLoan");//贷款开始金额
		String _endLoan = json.getString("_endLoan");//贷款结束金额
		int dept_is_super = json.getInt("dept_is_super");
		
		String[] dates = nowTime.split("-");
		if(flag == 1){
			String _assignStatus = json.getString("_assignStatus");//签单状态
			String _notAssignCom = json.getString("_notAssignCom");//是否已分配
			String _clients = json.getString("_clientsel");
			String _assignTime = json.getString("_assignTime");
			String signTime = json.getString("signTime");//签单时间
			String signStart = json.getString("signStart");//签单开始时间
			String signEnd = json.getString("signEnd");//签单结束时间
		
			if(!"".equals(_assignTime) && _assignTime != null){
				sb.append(" and c.assignTime like '").append(_assignTime).append("%'");
			}
			if(_clients != null && !"".equals(_clients)){
				sb.append(" and c.clientSourse.id = ").append(Long.parseLong(_clients));
			}
			if(_assignStatus != null && !"".equals(_assignStatus)){
				sb.append(" and c.clientStatus = ").append(_assignStatus);
			}
			if(signTime == null || "".equals(signTime)){
				if(!"".equals(signStart) && !"".equals(signEnd)){
					sb.append(" and c.signTime between '").append(signStart).append(" 00:00:00' and '").append(signEnd).append(" 23:59:59'");
				}
				if(!"".equals(signEnd) && signEnd != null && (signStart == null || "".equals(signStart))){
					sb.append(" and c.signTime like '").append(signEnd).append("%'");
				}
				if(!"".equals(signStart) && signStart != null && (signEnd == null || "".equals(signEnd))){
					sb.append(" and c.signTime like '").append(signStart).append("%'");
				}
			}else{
				if("1".equals(signTime)){//今天
					sb.append(" and c.signTime like '").append(nowTime).append("%'");
				}else if("2".equals(signTime)){//本周
					String monday = StatementsController.getWeekTime(nowTime);//获取本周一时间
					sb.append(" and c.signTime between '").append(monday).append(" 00:00:00' and '").append(nowTime).append(" 23:59:59'");
				}else if("3".equals(signTime)){//本月
					sb.append(" and c.signTime like '").append(dates[0] + "-" + dates[1]).append("%'");
				}else{//本年
					sb.append(" and c.signTime like '").append(dates[0]).append("%'");
				}
			}
			if(_notAssignCom != null && !"".equals(_notAssignCom)){
				if(_notAssignCom.equals("1")){
					sb.append(" and c.assignTime != ''");
				}else if(_notAssignCom.equals("2")){
					sb.append(" and (c.assignTime is null or c.assignTime = '')");
				}
			}
		}
		if(_createDate == null || "".equals(_createDate)){
			if(!"".equals(_createStart) && !"".equals(_createEnd)){
				sb.append(" and c.assignDate between '").append(_createStart).append("' and '").append(_createEnd).append("'");
			}
			if(!"".equals(_createEnd) && _createEnd != null && (_createStart == null || "".equals(_createStart))){
				sb.append(" and c.assignDate like '").append(_createEnd).append("%'");
			}
			if(!"".equals(_createStart) && _createStart != null && (_createEnd == null || "".equals(_createEnd))){
				sb.append(" and c.assignDate like '").append(_createStart).append("%'");
			}
		}else{
			if("1".equals(_createDate)){//今天
				sb.append(" and c.assignDate like '").append(nowTime).append("%'");
			}else if("2".equals(_createDate)){//本周
				String monday = StatementsController.getWeekTime(nowTime);//获取本周一时间
				sb.append(" and c.assignDate between '").append(monday).append("' and '").append(nowTime).append("'");
			}else if("3".equals(_createDate)){//本月
				sb.append(" and c.assignDate like '").append(dates[0] + "-" + dates[1]).append("%'");
			}else{//本年
				sb.append(" and c.assignDate like '").append(dates[0]).append("%'");
			}
		}
		if(_opp != null && !"".equals(_opp)){
			sb.append(" and c.oppType = ").append(_opp);
		}
		if(_signPos != null && !"".equals(_signPos)){
			sb.append(" and c.signPossible = ").append(_signPos);
		}
		if(_endLoan != null && !"".equals(_endLoan) && _startLoan != null && !"".equals(_startLoan)){
			sb.append(" and c.loanAmount between ").append(_startLoan).append(" and ").append(_endLoan);
		}
		if(_dept != null && !"".equals(_dept)){
			if(flag == 1){
				String manage = json.getString("manage");
				if(!"2".equals(manage)){
					if(dept_is_super > 0){
						sb.append(" and c.follower.department.superId = ").append(Integer.parseInt(_dept));
					}else{
						sb.append(" and c.follower.department.id = ").append(Long.parseLong(_dept));
					}
				}
			}else{
				sb.append(" and c.follower.department.id = ").append(Long.parseLong(_dept));
			}
		}
		if(_emp != null && !"".equals(_emp)){
			if(flag == 1){
				String manage = json.getString("manage");
				if("2".equals(manage)){
					sb.append(" and c.assignId = ").append(Long.parseLong(_emp));
				}else{
					sb.append(" and c.follower.id = ").append(Long.parseLong(_emp));
				}
			}else{
				sb.append(" and c.follower.id = ").append(Long.parseLong(_emp));
			}
		}
	};
	/**
	 * 查找所有客户信息
	 * @param gridLoadParams
	 * @param request
	 * @param _condition 查询条件
	 * @return
	 * 黄剑锋
	 * 2012-12-17 16:53第一次新建
	 */
	@RequestMapping("/loadClient.do")
	public ModelAndView loadClient(@ModelAttribute("params") GridLoadParams gridLoadParams, @RequestParam("_flag") String _flag,
			HttpServletRequest request, @RequestParam("conditions") String _condition,@RequestParam("_cpid") String _pid, 
			@RequestParam("importId") String importId){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		String fields = "select c.id, c.clientName, c.contactTel, c.spareTel1, c.spareTel2, c.loanAmount, c.clientAdd, c.clientStatus, c.signPossible," +
				"c.assignDate, c.assignTime, c.remark, c.oppType, c.city, c.province, c.clientSourse, c.workPlanNewTime, c.eliTime,c.follower.id," +
				"c.follower.userName, c.assignId, c.assignName";
		String hql = fields + " from ShowClient c";
		StringBuffer sb = new StringBuffer(" where 1=1");
		Role role = null;//角色
		User user = null;
		String nowTime = StaticValues.sdf.format(new Date());//当前时间
		List<Object[]> dataList = new ArrayList<Object[]>();
		User u = null;
		try {
			HttpSession session = request.getSession();
			if(session != null){
				user = (User)session.getAttribute(StaticValues.USER_SESSION);//获取登录用户
				role = user.getRole();
				String roleCode = role.getRoleCode();
				if(!"".equals(importId) && importId != null){
					sb.append(" and c.id = ").append(Long.parseLong(importId));
					dataList = objDao.findByHql(hql + sb,null,pagination);
				}else if(!"".equals(_flag) && _flag != null){//上一个下一个客户资源
					if("have".equals(_flag)){
						sb.append(" and c.assignPerson.role.remind='1' and (c.editTime is null or c.editTime = '') and c.assignTime like'"+nowTime+"%'");
						roleCondition(roleCode, sb, " and c.follower.id = ", " and c.follower.department.id = ",
							user, " and c.follower.department.superId = ");
						dataList = objDao.findAll(hql + sb);	
					}else{
						dataList = lastNext(_flag, user, jsonObject, sb, dataList, hql);
					}
				}else if(!_pid.equals("")){
					sb.append(" and c.id = ").append(Long.parseLong(_pid));
					dataList = objDao.findAll(hql + sb);
				}else{
					roleCondition(roleCode, sb, " and c.follower.id = ", " and c.follower.department.id = ",
						user, " and c.follower.department.superId = ");
					if(_condition != null && !"".equals(_condition)){//查询条件
						JSONObject json = JSONObject.fromObject(_condition);
						String _jihuaTime = json.getString("_jihuaTime");//工作计划时间
						String _provinces = json.getString("_provinces");//省份
						String _citys = json.getString("_citys");//城市
						String phoneNum = json.getString("phoneNum");//电话号码
						if("201206".equals(roleCode)){
							if(!"".equals(phoneNum) && phoneNum != null){
								sb.append(" and c.contactTel like '").append(phoneNum).append("%'");
							}else{
								sb.append(" and c.assignDate like '").append(nowTime).append("%'");
							}
						}else{
							if(!"".equals(phoneNum) && phoneNum != null){
								sb.append(" and c.contactTel like '").append(phoneNum).append("%'");
							}
							if(!"".equals(_jihuaTime) && _jihuaTime != null){
								hql += ",ResourcesTrack rt";
								sb.append(" and c.id = rt.client_name and rt.plantime like '").append(_jihuaTime).append("%'");
							}
							if(_provinces != null && !"".equals(_provinces)){
								sb.append(" and c.province = ").append(_provinces);
							}
							if(_citys != null && !"".equals(_citys)){
								sb.append(" and c.city = ").append(_citys);
							}
							connectConditions(json, sb, nowTime, 1);//拼接查询条件
						}
					}else{
						if("201206".equals(roleCode)){
							sb.append(" and c.assignDate like '").append(nowTime).append("%'");
						}
					}
					if("201206".equals(roleCode)){
						sb.append(" and c.assignId = ").append(user.getId());
						sb.append(" order by assignDate desc");
					}else{
						sb.append(" order by workPlanNewTime desc");
					}
					dataList = objDao.findAll(hql + sb,pagination);
				}
				jsonObject.element("totalCount", pagination.getTotalResults());
				// 有无数据都要产生data节点
				JSONArray data = new JSONArray();
				ShowClient sc = null;
				if (dataList.size() > 0) {
					for (Object[] o : dataList) {
						JSONObject item = new JSONObject();
						sc = new ShowClient();
						packetUser(o, sc, u);
						item.element("id", MvcUtil.toJsonString(sc.getId()));
						item.element("clientName", MvcUtil.toJsonString(sc.getClientName()));//客户名称
						if("3".equals(sc.getClientStatus()) || "201208".equals(roleCode)){
							ResourcesTrackController.hideNum(role.getRoleCode(), "contactTel", sc.getContactTel(), item);
							ResourcesTrackController.hideNum(role.getRoleCode(), "spareTel1",sc.getSpareTel1(), item);
							ResourcesTrackController.hideNum(role.getRoleCode(), "spareTel2", sc.getSpareTel2(), item);
						}else{
							ResourcesTrackController.hideDeptNum(item, new String[]{"contactTel","spareTel1","spareTel2"}, 
								new String[]{sc.getContactTel(),sc.getSpareTel1(),sc.getSpareTel2()}, roleCode, user.getId(), 
									sc.getFollower()==null?null:sc.getFollower().getId());
						}
						item.element("loanAmount", MvcUtil.toJsonString(sc.getLoanAmount()));//贷款金额
						item.element("clientAdd", MvcUtil.toJsonString(sc.getClientAdd()));//客户地址
						item.element("clientStatus", MvcUtil.toJsonString(sc.getClientStatus()));
						if("1".equals(sc.getSignPossible())){//签单可能性(1为100%、2为80%、3为50%、4为0%)
							item.element("signPossible", MvcUtil.toJsonString("100%"));
						}else if("2".equals(sc.getSignPossible())){
							item.element("signPossible", MvcUtil.toJsonString("80%"));
						}else if("3".equals(sc.getSignPossible())){
							item.element("signPossible", MvcUtil.toJsonString("50%"));
						}else if("4".equals(sc.getSignPossible())){
							item.element("signPossible", MvcUtil.toJsonString("10%"));
						}else if("5".equals(sc.getSignPossible())){
							item.element("signPossible", MvcUtil.toJsonString("0%"));
						}
						item.element("assignDate", MvcUtil.toJsonString(sc.getAssignDate()));//分配日期
						item.element("assignTime", MvcUtil.toJsonString(sc.getAssignTime()));//分配时间
						item.element("remark", MvcUtil.toJsonString(sc.getRemark()));//备注
						String oppTypes = "";//贷款类型
						String dkje = "";//贷款金额
						String khmz ="";//客户名字
						if("1".equals(sc.getOppType())){//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
							oppTypes = "房贷";
						}else if("2".equals(sc.getOppType())){
							oppTypes = "信贷";
						}else if("3".equals(sc.getOppType())){
							oppTypes = "短借";
						}else if("4".equals(sc.getOppType())){	
							oppTypes = "企贷";
						}
						item.element("oppType", oppTypes);
						if(!"".equals(sc.getLoanAmount()) && sc.getLoanAmount() != null){
							dkje = sc.getLoanAmount() + "万";
						}
						if(!"".equals(sc.getClientName()) && sc.getClientName() != null){
							khmz = sc.getClientName();
						}
						item.element("khxinx", MvcUtil.toJsonString(khmz+oppTypes+dkje));//客户信息
						item.element("city", MvcUtil.toJsonString(sc.getCity()));
						item.element("province",MvcUtil.toJsonString(sc.getProvince()));
						item.element("proCity", MvcUtil.toJsonString(sc.getProvince() +"," +sc.getCity()));//省份 城市
						if(sc.getClientSourse() != null){
							item.element("clientSourse", MvcUtil.toJsonString(sc.getClientSourse().getName()));
							item.element("clientSourseId", MvcUtil.toJsonString(sc.getClientSourse() .getId()));
						}
						item.element("workPlanNewTime", MvcUtil.toJsonString(sc.getWorkPlanNewTime()));
						item.element("eliTime", MvcUtil.toJsonString(sc.getEliTime()));
						if("201203".equals(roleCode)){
							item.element("userId", MvcUtil.toJsonString(user.getId()));
							item.element("emp_name", MvcUtil.toJsonString(user.getUserName()));
						}else{
							if(sc.getFollower() != null){
								if(sc.getFollower() != null){
									item.element("userId", MvcUtil.toJsonString(sc.getFollower().getId()));
									item.element("emp_name", MvcUtil.toJsonString(sc.getFollower().getUserName()));
								}else{
									item.element("emp_name", "无跟踪人员");
								}
							}
						}
						if(sc.getAssignId() != null){
							item.element("assignId", MvcUtil.toJsonString(sc.getAssignId()));
							item.element("assignName", MvcUtil.toJsonString(sc.getAssignName()));
						}
						data.add(item);
					}
				}
				jsonObject.element("data", data);
			}else{
				jsonObject.element("nextId", "timeout");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	
	public void packetUser(Object[] o, ShowClient c, User u){
		c.setId(Long.parseLong(o[0].toString()));
		c.setClientName(o[1]==null?"":o[1].toString());
		c.setContactTel(o[2]==null?"":o[2].toString());
		c.setSpareTel1(o[3]==null?"":o[3].toString());
		c.setSpareTel2(o[4]==null?"":o[4].toString());
		c.setLoanAmount(o[5]==null?"":o[5].toString());
		c.setClientAdd(o[6]==null?"":o[6].toString());
		c.setClientStatus(o[7]==null?"":o[7].toString());
		c.setSignPossible(o[8]==null?"":o[8].toString());
		c.setAssignDate(o[9]==null?"":o[9].toString());
		c.setAssignTime(o[10]==null?"":o[10].toString());
		c.setRemark(o[11]==null?"":o[11].toString());
		c.setOppType(o[12]==null?"":o[12].toString());
		c.setCity(o[13]==null?"":o[13].toString());
		c.setProvince(o[14]==null?"":o[14].toString());
		if(o[15]==null) {
			c.setClientSourse(null);
		}else{
			c.setClientSourse((ClientSource)o[15]);
		}
		c.setWorkPlanNewTime(o[16]==null?"":o[16].toString());
		c.setEliTime(o[17]==null?"":o[17].toString());
		if(o[18] != null){
			u = new User();
			u.setId(Long.parseLong(o[18].toString()));
			u.setUserName(o[19].toString());
			c.setFollower(u);
		}
		if(o[20] != null){
			c.setAssignId(Long.parseLong(o[20].toString()));
			c.setAssignName(o[21].toString());
		}
	}
	/**
	 * 导出客户管理信息
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/exportDate.do")
	public ModelAndView exportDate(HttpServletRequest request){
		Role role = null;//角色
		User user = null;
		String hql = "from ShowClient c";
		ExportExcelUtil util = new ExportExcelUtil();
		StringBuffer sb = new StringBuffer(" where 1=1");
		HttpSession session = request.getSession();
		if(session != null){
			user = (User)session.getAttribute(StaticValues.USER_SESSION);//获取登录用户
			role = user.getRole();
			String nowTime = StaticValues.sdf.format(new Date());//当前时间
			String _createDate = request.getParameter("_createDate");//创建时间类型（1为今天、2为本周、3为本月、4为本年）
			String _createStart = request.getParameter("_createStart");//创建开始日期
			String _createEnd = request.getParameter("_createEnd");//创建结束日期
			String _dept = request.getParameter("_dept");//跟单部门
			String _emp = request.getParameter("_emp");//跟单人
			String _opp = request.getParameter("_oppType");//商机类型
			String _signPos = request.getParameter("_signPos");//签单可能性
			String _startLoan = request.getParameter("_startLoan");//贷款开始金额
			String _endLoan = request.getParameter("_endLoan");//贷款结束金额
			String _assignStatus = request.getParameter("_assignStatus");//签单状态
			String _notAssignCom = request.getParameter("_notAssignCom");//是否已分配
			String _clients = request.getParameter("_clientsel");
			String _assignTime = request.getParameter("_assignTime");//分配时间
			String citys = request.getParameter("city");
			String pros = request.getParameter("pro");
			String plan = request.getParameter("planTime");
			String[] dates = nowTime.split("-");
			if(role != null){
				roleCondition(role.getRoleCode(), sb, " and c.follower.id = ", " and c.follower.department.id = ",
						user, " and c.follower.department.superId = ");
			}
			if(!"".equals(plan) && plan != null){
				hql = "select c from ResourcesTrack rt, ShowClient c";
				sb.append(" and c.id = rt.client_name and rt.plantime like '").append(plan).append("%'");
			}
			if(!"".equals(_assignTime) && _assignTime != null){
				sb.append(" and c.assignTime like '").append(_assignTime).append("%'");
			}
			if(_clients != null && !"".equals(_clients)){
				sb.append(" and c.clientSourse.id = ").append(Long.parseLong(_clients));
			}
			if(_assignStatus != null && !"".equals(_assignStatus)){
				sb.append(" and c.clientStatus = ").append(_assignStatus);
			}
			if(_createDate == null || "".equals(_createDate)){
				if(!"".equals(_createStart) && !"".equals(_createEnd)){
					sb.append(" and c.assignDate between '").append(_createStart).append("' and '").append(_createEnd).append("'");
				}
				if(!"".equals(_createEnd) && _createEnd != null && (_createStart == null || "".equals(_createStart))){
					sb.append(" and c.assignDate = '").append(_createEnd).append("'");
				}
				if(!"".equals(_createStart) && _createStart != null && (_createEnd == null || "".equals(_createEnd))){
					sb.append(" and c.assignDate = '").append(_createStart).append("'");
				}
			}else{
				if("1".equals(_createDate)){//今天
					sb.append(" and c.assignDate = '").append(nowTime).append("'");
				}else if("2".equals(_createDate)){//本周
					String monday = StatementsController.getWeekTime(nowTime);//获取本周一时间
					sb.append(" and c.assignDate between '").append(monday).append("' and '").append(nowTime).append("'");
				}else if("3".equals(_createDate)){//本月
					sb.append(" and c.assignDate like '").append(dates[0] + "-" + dates[1]).append("%'");
				}else{//本年
					sb.append(" and c.assignDate like '").append(dates[0]).append("%'");
				}
			}
			if(_opp != null && !"".equals(_opp)){
				sb.append(" and c.oppType = ").append(_opp);
			}
			if(_signPos != null && !"".equals(_signPos)){
				sb.append(" and c.signPossible = ").append(_signPos);
			}
			if(_endLoan != null && !"".equals(_endLoan) && _startLoan != null && !"".equals(_startLoan)){
				sb.append(" and c.loanAmount between ").append(_startLoan).append(" and ").append(_endLoan);
			}
			if(_dept != null && !"".equals(_dept)){
				sb.append(" and c.follower.department.id = ").append(Long.parseLong(_dept));
			}
			if(_emp != null && !"".equals(_emp)){
				sb.append(" and c.follower.id = ").append(Long.parseLong(_emp));
			}
			if(_notAssignCom != null && !"".equals(_notAssignCom)){
				if(_notAssignCom.equals("1")){
					sb.append(" and c.assignTime != ''");
				}else if(_notAssignCom.equals("2")){
					sb.append(" and c.assignTime is null");
				}
			}
			if(!"".equals(citys) && citys != null){
				sb.append(" and c.city = ").append(citys);
			}
			if(!"".equals(pros) && pros != null){
				sb.append(" and c.province = ").append(pros);
			}
			try {
				List<ShowClient> clients = showDao.findAll(hql + sb);
				for(ShowClient c : clients){
					String pro = "";
					String city = "";
					if(!"".equals(c.getProvince()) && c.getCity() != null){
						pro = getProvince(c.getProvince(), 1);
					}
					if(!"".equals(c.getCity()) && c.getCity() != null){
						city = getCity(c.getCity(), 1);
					}
					if("".equals(city)){
						c.setProvince(pro);
					}else if("".equals(pro)){
						c.setProvince(city);
					}else{
						c.setProvince(pro + "," + city);
					}
				}
				String[] headers = new String[]{"客户名字","省市区域","商机类型","客户联系方式","备用电话1","备用电话2","客户状态","成单率","客户地址","管理人","创建日期","分配时间","客户来源","备注"};
		    	Map<String, String> configMap = new HashMap<String, String>();
		    	configMap.put("客户名字", "clientName");
		    	configMap.put("省市区域", "province");
		    	configMap.put("商机类型", "oppType");
		    	configMap.put("客户联系方式", "contactTel");
		    	configMap.put("备用电话1", "spareTel1");
		    	configMap.put("备用电话2", "spareTel2");
		    	configMap.put("客户状态", "clientStatus");
		    	configMap.put("成单率", "signPossible");
		    	configMap.put("客户地址", "clientAdd");
		    	configMap.put("管理人", "follower.userName");
		    	configMap.put("创建日期", "assignDate");
		    	configMap.put("分配时间", "assignTime");
		    	configMap.put("客户来源", "clientSourse.name");
		    	configMap.put("备注", "remark");
		    	Map<String, Object> model = new HashMap<String, Object>();
		    	byte[] bytes;
		    	bytes = util.executeExport(clients,headers, configMap, "客户管理", "客户管理");
		    	model.put(AbstractMimeView.FILE_NAME, "客户管理.xls");
		    	model.put(AbstractMimeView.FILE_DATA, bytes);
		    	return new ModelAndView(new MimeBytesView(), model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 判断电话的重复性条件
	 * @param num1
	 * @param num2
	 * @param num3
	 * @return
	 */
	public Long judgeNum(String num1, String num2, String num3, List<Object[]> list) throws Exception{
		String contactTel = null, spareTel1=null, spareTel2=null;
		if(list.size() > 0){
			for(Object[] obj : list){
				contactTel = obj[1].toString();
				spareTel1 = obj[2].toString();
				spareTel2 = obj[3].toString();
				if(!"".equals(num1) && num1 != null){//电话号码不能与客户的任意号码重复
					if(num1.equals(contactTel) || num1.equals(spareTel1) || num1.equals(spareTel2)){
						return Long.parseLong(obj[0].toString());
					}
				}
				if(!"".equals(num2) && num2 != null){//备用电话1不能与客户的任意号码重复
					if(num2.equals(contactTel) || num2.equals(spareTel1) || num2.equals(spareTel2)){
						return Long.parseLong(obj[0].toString());
					}
				}
				if(!"".equals(num3) && num3 != null){//备用电话2不能与客户的任意号码重复
					if(num3.equals(contactTel) || num3.equals(spareTel1) || num3.equals(spareTel2)){
						return Long.parseLong(obj[0].toString());
					}
				}
			}
			return 0L;
		}else{
			return 0L;
		}
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
	@RequestMapping("/saveOrUpdateClient.do")
	public ModelAndView saveOrUpdateClient(Client client, HttpServletRequest request, @RequestParam("_opp") String opp, 
			@RequestParam("_sign") String sign, @RequestParam("_client") String _client){
		JSONObject jsonObject = new JSONObject();
		String num1 = client.getContactTel();
		String num2 = client.getSpareTel1();
		String num3 = client.getSpareTel2();
		String pro = request.getParameter("pro");//省份
		String city =request.getParameter("city");//城市
		String hql = "select c.id from Client c";
		String telHql = "select c.id, c.contactTel, c.spareTel1, c.spareTel2 from Client c";
		String nowTime = StaticValues.sdf.format(new Date());//当前时间
		User user = null;
		try{
			//通过联系方式、备用电话1、备用电话2三个判断客户信息的唯一性
			if(request.getSession() != null){
				user = (User) request.getSession().getAttribute("userSession");
				client.setProvince(pro);
				client.setCity(city);
				if("1".equals(opp) || "房贷".equals(opp)){//设置商机类型
					client.setOppType("1");
				}else if("2".equals(opp) || "信贷".equals(opp)){
					client.setOppType("2");
				}else if("3".equals(opp) || "短借".equals(opp)){
					client.setOppType("3");
				}else if("4".equals(opp) || "企贷".equals(opp)){
					client.setOppType("4");
				}else{
					client.setOppType("5");
				}
				if("1".equals(sign) || "100%".equals(sign)){//设置签单可能性
					client.setSignPossible("1");
				}else if("2".equals(sign) || "80%".equals(sign)){
					client.setSignPossible("2");
				}else if("3".equals(sign) || "50%".equals(sign)){
					client.setSignPossible("3");
				}else if("5".equals(sign) || "0%".equals(sign)){
					client.setSignPossible("5");
				}else if("4".equals(sign) || "10%".equals(sign)){ 
					client.setSignPossible("4");
				}else{
					client.setSignPossible("6");
				}
				ClientSource clientSource = new ClientSource();
				clientSource.setId(Long.parseLong(_client));
				client.setClientSourse(clientSource);
				List<Object[]> alllist = objDao.findAll(telHql);//查出所有客户的电话
				Long flag = judgeNum(num1, num2, num3, alllist);//判断电话号码的重复性
				Role role = user.getRole();//登录的角色
				if("".equals(client.getId()) || client.getId() == null){
					if(flag != 0){//判断重复添加
						jsonObject.element("failure", true);
						if("201206".equals(role.getRoleCode())){
							jsonObject.element("id", flag);
							jsonObject.element("msg", "客户信息已经存在,是否显示？");
						}else{
							jsonObject.element("msg", "客户信息已经存在,添加失败!");
						}
					}else{
						client.setClientStatus("2");//新增一条客户信息时默认设置为未签单
						client.setAssignPerson(user);
						if(role != null){
							client.setFollower(user);
							client.setAssignTime(StaticValues.format.format(new Date()));
							Long uid = user.getId();
							if("201203".equals(role.getRoleCode()) || "201202".equals(role.getRoleCode())){//员工录入的客户资源是属于自己的资源
								dao.save(client);
							}else{
								if("201206".equals(role.getRoleCode())){
									client.setImportTime(StaticValues.sdf.format(new Date()));
								}
								dao.save(client);
							}
						}
						jsonObject.element("success", true);
						jsonObject.element("msg", "保存客户信息成功!");
					}
				}else{
					if(flag == 0 || flag.equals(client.getId())){
						SqlBuilder sb = new SqlBuilder("Client", SqlBuilder.TYPE_UPDATE);
						sb.addField("clientName", client.getClientName());//客户名称
						sb.addField("province", client.getProvince());//省份
						sb.addField("city",client.getCity());//城市
						sb.addField("contactTel", client.getContactTel());//客户联系方式
						sb.addField("loanAmount", client.getLoanAmount());//贷款金额
						sb.addField("clientAdd", client.getClientAdd());//客户地址
						sb.addField("oppType", client.getOppType());//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
						sb.addField("spareTel1", client.getSpareTel1());//备用电话1
						sb.addField("spareTel2", client.getSpareTel2());//备用电话2
						sb.addField("remark", client.getRemark());//备注
						sb.addField("assignDate", client.getAssignDate());//分配日期
						sb.addField("signPossible", client.getSignPossible());//签单可能性
						sb.addField("editTime", nowTime);
						sb.addField("clientSourse.id", client.getClientSourse().getId());
						sb.addWhere("id", client.getId());
						dao.updateByHQL(sb.getSql(), sb.getParams());
						jsonObject.element("success", true);
						jsonObject.element("msg", "保存客户信息成功！");
					}else{
						jsonObject.element("failure", true);
						jsonObject.element("msg", "客户信息已经存在，无法修改！");
					}
				}
			}else{
				jsonObject.element("failure", true);
				jsonObject.element("msg", "页面停留时间过长,请重新登录!");
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
	 * 根据渠道分配
	 * @param client
	 * @return
	 */
	public void AssignBySource(Client client, String source, List<XzShituAllocation> xzsList) throws Exception{
		User u = null;
		XzAllocation xza = null;
		for(XzShituAllocation xs : xzsList){
			if(("1").equals(xs.getJiedan()) && ("1").equals(xs.getDepjiedan()) && "2".equals(xs.getQiyong())){
				u = new User();
				u.setId(xs.getId());
				client.setFollower(u);
				xzsList.remove(xs);
				break;
			}
		}
		for(XzShituAllocation xs : xzsList){
			xza = new XzAllocation();
			u = new User();
			u.setId(xs.getId());
			xza.setUserid(u);
			xza.setCsourceid(source);
			xaDao.save(xza);
		}
	}
	/**
	 * 查找所有部门信息
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	  * 黄剑锋
	  * 2012-12-18 14:51 第一次创建
	 */
	@RequestMapping("/loadDepartment.do")
	public ModelAndView loadDepartment(HttpServletRequest req) {
		JSONObject jsonObject = new JSONObject();
		int flag = Integer.parseInt(req.getParameter("flag"));
		StringBuffer sb = new StringBuffer();
		User user =  null;
		try {
			user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
			String role = user.getRole().getRoleCode();
			String hql = "from Department d where 1=1 and d.orderStatus = '1'";
			sb.append(" and d.isFront = ").append(flag);
			if("201203".equals(role)){
				return MvcUtil.jsonObjectModelAndView(jsonObject);
			}else if("201202".equals(role)){
				sb.append(" and d.id = ").append(user.getDepartment().getId());
			}else if("201208".equals(role)){
				sb.append(" and d.superId = ").append(user.getDepartment().getSuperId());
			}
			List<Department> dataList = ddao.findAll(hql+sb);
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Department field : dataList) {
					JSONObject item = new JSONObject();
					item.element("departId", MvcUtil.toJsonString(field.getId()));//部门id
					item.element("departName",MvcUtil.toJsonString(field.getDepaName()));//部门名称
					item.element("superId", field.getSuperId());
					data.add(item);
				}
			}
			jsonObject.element("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "提交失败,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 
	  *<功能简述>查看所有员工信息 
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadEmployee.do")
	public ModelAndView loadEmployee(HttpServletRequest req){
		JSONObject jObject = new JSONObject();
		int flag = Integer.parseInt(req.getParameter("flag"));
		StringBuffer sb = new StringBuffer();
		String hql = "select u.id, u.userName from User u where u.isOrNotEnable = 2";
		JSONArray data = new JSONArray();
		User user = null;
		try {
			if(req.getSession() != null){
				user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				if("201202".equals(user.getRole().getRoleCode())){
					sb.append(" and u.department.id = ").append(user.getDepartment().getId());
				}
				if("201208".equals(user.getRole().getRoleCode())){
					sb.append(" and u.department.superId = ").append(user.getDepartment().getSuperId());
				}
				if("201203".equals(user.getRole().getRoleCode())){
					sb.append(" and u.id = ").append(user.getId());
				}
			}
			sb.append(" and u.department.isFront = ").append(flag);
			List<Object[]> list = objDao.findAll(hql+sb);
			for(Object[] obj : list){
				JSONObject item = new JSONObject();
				item.element("eId", MvcUtil.toJsonString(obj[0]));//员工id
				item.element("eName", MvcUtil.toJsonString(obj[1]));//员工名字
				data.add(item);
			}
			jObject.element("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jObject);
	}
	/**
	 * 
	  *<功能简述>通过部门编号取员工
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	  * 黄剑锋
	  * 2012-12-18 15:38第一次新建
	*/
	@RequestMapping("/loadEmployees.do")
	public ModelAndView loadEmployees(HttpServletRequest request){
		JSONObject jObject = new JSONObject();
		String eid = request.getParameter("eid");
		String hql = "from User u where u.isOrNotEnable = 2 and u.department.id= " + Long.parseLong(eid);
		JSONArray data = new JSONArray();
		User user = null;
		StringBuffer sb = new StringBuffer();
		try {
			user = (User)request.getSession().getAttribute(StaticValues.USER_SESSION);
			if("201202".equals(user.getRole().getRoleCode())){
				sb.append(" and u.department.id = ").append(user.getDepartment().getId());
			}
			if("201208".equals(user.getRole().getRoleCode())){
				sb.append(" and u.department.superId = ").append(user.getDepartment().getSuperId());
			}
			if("201201".equals(user.getRole().getRoleCode())){
				sb.append(" and u.id = ").append(user.getId());
			}
			List<User> list = udao.findAll(hql);
			for(User u : list){
				JSONObject item = new JSONObject();
				item.element("eId", MvcUtil.toJsonString(u.getId()));//员工id
				item.element("eName", MvcUtil.toJsonString(u.getUserName()));//员工名字
				data.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jObject.element("failure", true);
			jObject.element("msg", "提交失败,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(jObject);
		}
		jObject.element("data", data);
		return MvcUtil.jsonObjectModelAndView(jObject);
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
	public ModelAndView assignClient(HttpServletRequest req, @RequestParam("num") int num, @RequestParam("_emp") int _emp,
			@RequestParam("ids") String ids){
		JSONObject json = new JSONObject();
		String hql = "select c.id from Client c where c.follower.id = ? and c.clientStatus <> '3'";
		User user = null;
		Connection conn = null;
		boolean autoFlag = true;
		PreparedStatement ps = null;
		try {
			if(num <= 0 && "".equals(ids)){
				json.element("success", true);
				json.element("msg", "参数异常");
				return MvcUtil.jsonObjectModelAndView(json);
			}
			if(req.getSession() != null){
				user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				String now = StaticValues.format.format(new Date());
				conn = DBManager.getConnection();
				autoFlag = conn.getAutoCommit();
				conn.setAutoCommit(false);//禁止自动提交
				ps = conn.prepareStatement("update dgg_client set follower=?,assignTime=?,editTime=? where id=?");
				if(num > 0){
					Pagination page = new Pagination();
					page.set(0, num);
					List<Object> clientList = odao.findByHql(hql, new Object[]{user.getId()}, page);
					for(int i = 0, len = clientList.size(); i < len; i++){
						ps.setInt(1, _emp);
						ps.setString(2, now);
						ps.setNull(3, Types.VARCHAR);
						ps.setInt(4, Integer.parseInt(clientList.get(i).toString()));
						ps.addBatch();
						ps.clearParameters();
					}
				}else{
					String[] id = ids.split(",");
					for(int i = 0, len = id.length; i < len; i++){
						ps.setInt(1, _emp);
						ps.setString(2, now);
						ps.setNull(3, Types.VARCHAR);
						ps.setInt(4, Integer.parseInt(id[i]));
						ps.addBatch();
						ps.clearParameters();
					}
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
	 * 自动分配客户资源
	  *<功能简述>
	  *<功能详细描述>
	  * @param _id 没有分配的客户资源id集合
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/autoAssignClient.do")
	public ModelAndView autoAssignClient(HttpServletRequest req, @RequestParam("num") int num){
		JSONObject json = new JSONObject();
		String difHql = "from ClientDifRecord cdr where flag = 0";
		User user = null;
		Connection conn = null;
		boolean autoFlag = true;
		PreparedStatement ps = null;
		String userHql = "select u.id, u.counts from User u where u.signStatus = 1 and u.isOrNotEnable = 2 and " +
			"u.department.orderStatus = '1' and u.department.isFront = 1";
		int flag = 0;
		try {
			if(req.getSession() != null){
				Date date = new Date();
				String now = StaticValues.format.format(date);
				String day = StaticValues.sdf.format(date);
				conn = DBManager.getConnection();
				autoFlag = conn.getAutoCommit();
				conn.setAutoCommit(false);//禁止自动提交
				ps = conn.prepareStatement("update dgg_client set follower=?,assignTime=?,clientStatus=?,editTime=? where id=?");
				user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION); 
				String role = user.getRole().getRoleCode();
				Pagination page = new Pagination();
				page.set(0, num);
				StringBuilder sb = new StringBuilder("select c.id from Client c where 1=1");
				if("201208".equals(role)){
					sb.append(" and c.follower.id = ").append(user.getId());
					userHql = "select u.id, u.counts from User u where u.signStatus = 1 and u.isOrNotEnable = 2 and " +
						"u.department.orderStatus = '1' and u.department.superId = "+user.getDepartment().getSuperId();
					difHql = "from ClientDifRecord cdr where flag = "+user.getDepartment().getSuperId();
					flag = user.getDepartment().getSuperId();
				}else{
					sb.append(" and assignTime is null or assignTime = ''");
				}
				List<Object> clientList = odao.findAll(sb.toString(),page);
				if(clientList.size()>0){
					int commitIndex = 0;
					int size = clientList.size();
					List<Object> assignList = new ArrayList<Object>(size);
					int difference = 0;//差额
					List<ClientDifRecord> clist = cdao.findAll(difHql);//查出上次自动分配时没有分配完的信息
					if(clist.size() > 0){
						for(ClientDifRecord cdr : clist){
							difference = cdr.getClientDif();
							if(difference >= size){//一个用户的资源差数大于总的分配数，客户资源分配完
								for(Object c : clientList){
									fillPsParam(ps,cdr.getUserid(),now,Integer.parseInt(c.toString()));
									assignList.add(c);
								}
								if(difference == size){//相等删除差额记录
									ps.addBatch("DELETE FROM dgg_clientDifRecord WHERE id = "+cdr.getId());
								}else{//大于客户数则更新差额记录
									ps.addBatch("update dgg_clientDifRecord set clientDif="+(difference-size)+
										" where id = "+cdr.getId());
								}
								clientList.removeAll(assignList);//去掉已经分配的客户
								size = clientList.size();
								executePs(ps,conn);
								json.element("success", true);
								json.element("msg", "分配成功!");
								return MvcUtil.jsonObjectModelAndView(json);
							}else{
								for(int i = 0; i < difference; i++){
									commitIndex++;
									fillPsParam(ps,cdr.getUserid(),now,Integer.parseInt(clientList.get(i).toString()));
									assignList.add(clientList.get(i));
								}
								clientList.removeAll(assignList);//去掉已经分配的客户
								assignList.clear();
								size = clientList.size();
								commitIndex++;
								ps.addBatch("DELETE FROM dgg_clientDifRecord WHERE id="+cdr.getId());
								if(commitIndex%100 == 0){//一百条SQL执行一次
									executePs(ps,conn);
								}
							}
						}
						if(size > 0){
							List<Object[]> userList = objDao.findAll(userHql);
							assignByNum(userList, clientList, commitIndex, ps, conn, now, day, flag);
						}
					}else{
						List<Object[]> userList = objDao.findAll(userHql);
						assignByNum(userList, clientList, commitIndex, ps, conn, now, day, flag);
					}
					ps.executeBatch();
					conn.commit();
				}else{
					json.element("success", false);
					json.element("msg", "没有可分配的客户!");
					return MvcUtil.jsonObjectModelAndView(json);
				}
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
	 * 按照系数来自动分配
	 * @param map 装配返回信息
	 * @param userList 用户列表
	 * @param clientList 分配的客户列表
	 * @param commitIndex SQL索引
	 * @param ps PreparedStatement
	 * @param conn JDBC连接
	 * @param nums 定时分配数量
	 * @param now 时间
	 * @param type 标识定时分配和自动分配
	 * @return
	 * @throws Exception
	 */
	public void assignByNum(List<Object[]> userList, List<Object> clientList,
			int commitIndex, PreparedStatement ps, Connection conn, String now, String day, int flag) throws Exception{
		if(userList.size() > 0){
			Client client = null;
			int assignNum = 0;
			int count = getCounts(userList);//获取接单员工的分配系数总和
			int size = clientList.size();
			List<Object> assignList = new ArrayList<Object>(size);//去掉分配了的客户信息
			while((size - count) > 0){//能进行一轮的自动分配
				for(Object[] user : userList){
					assignNum = Integer.parseInt(user[1].toString());
					for(int i = 0; i < assignNum; i++){
						commitIndex++;
						fillPsParam(ps,Integer.parseInt(user[0].toString()),now,Integer.parseInt(clientList.get(i).toString()));
						assignList.add(clientList.get(i));
					}
					clientList.removeAll(assignList);//去掉已经分配的客户
					assignList.clear();
					if(commitIndex%100 == 0){//一百条SQL执行一次
						executePs(ps,conn);
					}
				}
				size = clientList.size();
			}
			for(Object[] user : userList){//不够一轮分配的在按系数分配，不够的记录在差额表中，下次继续分配
				assignNum = Integer.parseInt(user[1].toString());
				if(assignNum >= size){//分配不够
					for(Object c : clientList){
						commitIndex++;
						fillPsParam(ps,Integer.parseInt(user[0].toString()),now,Integer.parseInt(c.toString()));
						assignList.add(c);
					}
					clientList.removeAll(assignList);//去掉已经分配的客户
					if(assignNum > size){
						commitIndex++;
						ps.addBatch("insert into dgg_clientDifRecord(userid,clientDif,flag) values("
								+user[0]+","+(assignNum - size)+","+flag+")");
					}
					if(commitIndex%100 == 0){//一百条SQL执行一次
						executePs(ps,conn);
					}
				}else{
					for(int i = 0; i < assignNum; i++){
						commitIndex++;
						fillPsParam(ps,Integer.parseInt(user[0].toString()),now,Integer.parseInt(clientList.get(i).toString()));
						assignList.add(clientList.get(i));
					}
					clientList.removeAll(assignList);//去掉已经分配的客户
					assignList.clear();
					if(commitIndex%100 == 0){//一百条SQL提交一次
						executePs(ps,conn);
					}
				}
				size = clientList.size();
			}
		}
	}
	/**
	 * 获取需分配用户的分配系数总和
	 * @param list
	 * @return
	 */
	public int getCounts(List<Object[]> list){
		int count = 0;
		for(Object[] obj : list){
			count += Integer.parseInt(obj[1].toString());
		}
		return count;
	}
	/**
	 * 执行批量sql
	 * @param ps PreparedStatement
	 * @param conn JDBC连接
	 * @throws Exception
	 */
	public void executePs(PreparedStatement ps, Connection conn) throws Exception{
		ps.executeBatch();
		ps.clearBatch();
	}
	/**
	 * 装配PreparedStatement参数
	 * @param ps PreparedStatement
	 * @param userId 用户id
	 * @param now 当前时间
	 * @param cid 客户id
	 * @throws Exception
	 */
	public void fillPsParam(PreparedStatement ps, int userId, String now, int cid) throws Exception{
		ps.setInt(1, userId);
		ps.setString(2, now);
		ps.setString(3, "2");
		ps.setNull(4, Types.VARCHAR);
		ps.setInt(5, cid);
		ps.addBatch();
		ps.clearParameters();
	}
	/**
	 * 获得省份的数据库代码
	 * @param str
	 * @return
	 */
	public String getProvince(String str, int flag){
		if(!"".equals(str) && str != null){
			if(proMap == null){
				proMap = new HashMap<String, String>();
				fillMap(proMap);
			}
			Iterator<String> it = proMap.keySet().iterator();
			String location = "";//迭代标识
			String val = "";
			while(it.hasNext()){
				location = it.next();
				val = proMap.get(location);
				if(flag == 0){//将中文名字转换成数据库存的代码
					if(val.indexOf(str) != -1 || str.indexOf(val) != -1){
						str = location;
						break;
					}
				}else{//将数据库存的代码转换成中文的代码
					if(str.equals(location)){
						str = val;
						break;
					}
				}
			}
		}
		return str;
	}
	/**
	 * 城市名字与在数据库中的代码互相转换
	 * @param str 
	 * @param flag 标识转换类型（0是将名字转换成数据库代码，1是将数据库代码转换成名字）
	 * @return
	 */
	public String getCity(String str, int flag){
		if(!"".equals(str) && str != null){
			if(cityMap == null){
				cityMap = new HashMap<String, String>();
				fillCityMap(cityMap);
			}
			Iterator<String> it = cityMap.keySet().iterator();
			String location = "";//迭代标识
			String val = "";
			while(it.hasNext()){
				location = it.next();
				val = cityMap.get(location);
				if(flag == 0){//将中文名字转换成数据库存的代码
					if(val.indexOf(str) != -1 || str.indexOf(val) != -1){
						str = location;
						break;
					}
				}else{//将数据库存的代码转换成中文的代码
					if(str.equals(location)){
						str = val;
						break;
					}
				}
			}
		}
		return str;
	}
	/**
	 * 导入excel文件
	  *<功能简述>
	  *<功能详细描述>
	  * @param _value 文件路径
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/importClient.do")
	public ModelAndView importClient(@RequestParam("uploadFilePath")MultipartFile multipartFile, HttpServletRequest req){
		JSONObject json = new JSONObject();
		List<Map<String, String>> clientList = new ArrayList<Map<String,String>>();
		Iterator<String> it = null;//迭代器
		String location = "";//迭代位置
		int num = 0;
		String nowTime = StaticValues.sdf.format(new Date());//当前时间e
		Date date = null;
		User user = null;
		String area = "";//省市区域
		String source = "";//客户来源
		String hql = "select c.id from Client c";
		String telHql = "select c.id, c.contactTel, c.spareTel1, c.spareTel2 from Client c";
		//实体字段名
		String[] fields = {"录入日期", "客户信息", "省市区域", "客户地址", "客户联系方式", "备用电话1", "备用电话2", "客户状态", "备注", "客户来源"};
		try {
			if(req.getSession() != null){
				List<Object[]> alllist = objDao.findAll(telHql);//查出所有客户的电话
				user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				clientList = ImportExcelUtil.readXls(0, multipartFile.getInputStream(), fields, clientList);
				List<Client> successList = new ArrayList<Client>();//装导入成功的list
				for(Map<String, String> map : clientList){
					it = map.keySet().iterator();
					c = new Client();
					while(it.hasNext()){
						location = it.next();
						if("录入日期".equals(location)){
							if(map.get(location) == null || "".equals(map.get(location))){
								c.setAssignDate(nowTime);
							}else{
								date = StaticValues.sdf.parse(map.get(location));
								c.setAssignDate(StaticValues.sdf.format(date));
							}
							continue;
						}
						if("客户信息".equals(location)){
							if(!"".equals(map.get(location))){
								String str = map.get(location).replace("，", ",");//统一分隔符
								String[] field = str.split(",");
								if(field.length == 1){
									c.setClientName(field[0]);
									c.setOppType("5");
								}else if(field.length == 2){
									if("房贷".equals(field[1])){//设置商机类型
										c.setOppType("1");
									}else if("信贷".equals(field[1])){
										c.setOppType("2");
									}else if("短借".equals(field[1])){
										c.setOppType("3");
									}else if("企贷".equals(field[1])){
										c.setOppType("4");
									}else{
										c.setOppType("5");
									}
									String flag = str.substring(0,1);//截取第一个字符
									if(!",".equals(flag)){
										c.setClientName(field[0]);
									}
								}else if(field.length == 3){
									c.setClientName(field[0]);
									if("房贷".equals(field[1])){//设置商机类型
										c.setOppType("1");
									}else if("信贷".equals(field[1])){
										c.setOppType("2");
									}else if("短借".equals(field[1])){
										c.setOppType("3");
									}else if("企贷".equals(field[1])){
										c.setOppType("4");
									}else{
										c.setOppType("5");
									}
									if(!"".equals(field[2]) && field[2] != null){
										String[] loans = field[2].split("万");
										c.setLoanAmount(loans[0]);
									}
								}else{
									c.setOppType("5");
								}
							}
							continue;
						}
						if("省市区域".equals(location)){
							area = map.get(location).replace("，", ",");
							String[] field = area.split(",");
							if(field.length == 1){//只有省份
								c.setProvince(getProvince(field[0], 0));
							}
							if(field.length == 2){//有省份和城市
								c.setProvince(getProvince(field[0], 0));
								c.setCity(getCity(field[1], 0));
							}
						}
						if("客户地址".equals(location)){
							c.setClientAdd(map.get(location));
							continue;
						}
						if("客户联系方式".equals(location)){
							c.setContactTel(map.get(location));
							continue;
						}
						if("备用电话1".equals(location)){
							c.setSpareTel1(map.get(location));
							continue;
						}
						if("备用电话2".equals(location)){
							c.setSpareTel2(map.get(location));
							continue;
						}
						if("备注".equals(location)){
							c.setRemark(map.get(location));
							continue;
						}
						if("客户来源".equals(location)){
							source = map.get(location);
							List<ClientSource> list = new ArrayList<ClientSource>();
							if("".equals(source) || source == null){
								list = csdao.findAll("from ClientSource c where c.name = '购买数据'");
							}else{
								list = csdao.findAll("from ClientSource c where c.name = '" + source + "'");
							}
							if(list.size() > 0){
								c.setClientSourse(list.get(0));
							}
						}
					}
					if("".equals(c.getAssignDate()) || c.getAssignDate() == null){
						c.setAssignDate(nowTime);
					}
					c.setSignPossible("3");
					c.setAssignPerson(user);
					c.setFollower(user);
					c.setClientStatus("2");
					c.setImportTime(nowTime);//导入时间
					String num1 = c.getContactTel();
					String num2 = c.getSpareTel1();
					String num3 = c.getSpareTel2();
					Long flag = judgeNum(num1, num2, num3, alllist);//判断号码的重复性
					boolean repeatFlag = judgeRepeat(successList, num1, num2, num3);
					if(flag == 0 && repeatFlag){//判断导入的客户信息是否重复
						successList.add(c);
					}else{
						String opptype = "";
						if("1".equals(c.getOppType())){//设置商机类型
							opptype = "房贷";
						}else if("2".equals(c.getOppType())){
							opptype = "信贷";
						}else if("3".equals(c.getOppType())){
							opptype = "短借";
						}else if("4".equals(c.getOppType())){
							opptype = "企贷";
						}
						opptype = c.getClientName() + "，" + opptype + "，" + c.getLoanAmount();
						WrongClient wc = new WrongClient(c.getAssignDate(), opptype, area, c.getClientAdd(), c.getContactTel(),
								c.getSpareTel1(), c.getSpareTel2(), source);
						wc.setRemark("重复客户资源");
						wdao.save(wc);
						num++;
					}
				}
				if(successList.size() > 0){//jdbc批量提交
					batchSubmit(user, successList, nowTime);//批量提交
				}
				if(num == clientList.size()){
					json.element("success", true);
					json.element("msg", "导入数据为重复数据!");
					return MvcUtil.jsonObjectModelAndView(json);
				}
				json.element("success", true);
				json.element("msg", "导入成功!");
			}else{
				json.element("failure", true);
				json.element("msg", "页面停留时间过长，请重新登录！");
			}
		} catch (ArrayIndexOutOfBoundsException a){
			a.printStackTrace();
			json.element("failure", true);
			json.element("msg", "导入失败,客户信息填写格式不合法!");
			return MvcUtil.jsonObjectModelAndView(json);
		} catch (Exception e) {
			e.printStackTrace();
			json.element("failure", true);
			json.element("msg", "导入失败,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(json);
		} 
		return MvcUtil.jsonObjectModelAndView(json);
	}
	/**
	 * 判断导入的客户中有没有重复的客户
	 * @return
	 */
	public boolean judgeRepeat(List<Client> list, String num1, String num2, String num3) throws Exception {
		String contactTel = "";//联系电话
		String spareTel1 = "";//备用电话1
		String spareTel2 = "";//备用电话2
		if(list.size() > 0){
			for(Client client : list){
				contactTel = client.getContactTel();
				spareTel1 = client.getSpareTel1();
				spareTel2 = client.getSpareTel2();
				if(!"".equals(num1) && num1 != null){//电话号码不能与客户的任意号码重复
					if(num1.equals(contactTel) || num1.equals(spareTel1) || num1.equals(spareTel2)){
						return false;
					}
				}
				if(!"".equals(num2) && num2 != null){//备用电话1不能与客户的任意号码重复
					if(num2.equals(contactTel) || num2.equals(spareTel1) || num2.equals(spareTel2)){
						return false;
					}
				}
				if(!"".equals(num3) && num3 != null){//备用电话2不能与客户的任意号码重复
					if(num3.equals(contactTel) || num3.equals(spareTel1) || num3.equals(spareTel2)){
						return false;
					}
				}
			}
			return true;
		}else{
			return true;
		}
	}
	/**
	 * 批量提交
	 * @param user
	 * @param successList
	 * @param nowTime
	 */
	public void batchSubmit(User user, List<Client> successList, String nowTime){
		Connection conn = null;
		PreparedStatement ps = null;
		boolean autoFlag = true;
		try {
			conn = DBManager.getConnection();
			autoFlag = conn.getAutoCommit();
			conn.setAutoCommit(false);
			String sql = "insert into dgg_client(clientName, contactTel, loanAmount, clientAdd, oppType, spareTel1, "
				+ "spareTel2, clientStatus, remark, signPossible, assignDate, assignPerson, follower, clientSourse, importTime, "
					+ "province, city) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			int addFlag = 0;
			for(Client c : successList){
				addFlag++;
				ps.setString(1, c.getClientName());
				ps.setString(2, c.getContactTel());
				ps.setString(3, c.getLoanAmount());
				ps.setString(4, c.getClientAdd());
				if("".equals(c.getOppType()) || c.getOppType() == null){
					ps.setString(5, "5");
				}else{
					ps.setString(5, c.getOppType());
				}
				ps.setString(6, c.getSpareTel1());
				ps.setString(7, c.getSpareTel2());
				ps.setString(8, "2");
				ps.setString(9, c.getRemark());
				ps.setString(10, "3");
				ps.setString(11, c.getAssignDate());
				ps.setLong(12, user.getId());
				ps.setLong(13, user.getId());
				ps.setLong(14, c.getClientSourse().getId());
				ps.setString(15, nowTime);
				ps.setString(16, c.getProvince());
				ps.setString(17, c.getCity());
				ps.addBatch();
				if(addFlag%100 == 0){//每100条提交一次
					ps.executeBatch();
					conn.commit();
					ps.clearBatch();
				}
			}
			if(addFlag%100 != 0){
				ps.executeBatch();
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();//回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally{
			try {
				if(conn != null){
					conn.setAutoCommit(autoFlag);//恢复以前的提交状态
					conn.close();
				}
				if(ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**	
	 * 下载导入模板
	  *<功能简述>
	  *<功能详细描述>
	  * @param request
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	  * 黄剑锋
	 */
	@RequestMapping("/downloadTemplet.do")
	public ModelAndView  downloadTemplet(HttpServletRequest request,HttpServletResponse response){
		UploadFile uf = new UploadFile();
		Map<String, Object> model = new HashMap<String, Object>();
		model = uf.downloadAttachment("Excel导入模板.xls", request.getRealPath("")+"//file");
		return new ModelAndView(new MimeBytesView(), model);
	}
	/**
	 * 导出导入错误的客户资源
	  *<功能简述>
	  *<功能详细描述>
	  * @param request
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/exportWrong.do")
	public ModelAndView exportWrong(HttpServletRequest request){
		String hql = "from WrongClient wc";
		Map<String, Object> model = null;
		String[] fields = {"录入日期", "客户信息", "省市区域", "客户地址", "客户联系方式", "备用电话1", "备用电话2", "客户状态", "备注", "客户来源"};
		try {
			List<WrongClient> wlist = wdao.findAll(hql);
			Map<String, String> configMap = new HashMap<String, String>();
			ExportExcelUtil util = new ExportExcelUtil();
			configMap.put("录入日期", "inputTime");
			configMap.put("客户信息", "clientMsg");
			configMap.put("省市区域", "area");
			configMap.put("客户地址", "clientAdd");
			configMap.put("客户联系方式", "clientTel");
			configMap.put("备用电话1", "spareTel1");
			configMap.put("备用电话2", "spareTel2");
			configMap.put("客户状态", "clientStatus");
			configMap.put("备注", "remark");
			configMap.put("客户来源", "source");
			model = new HashMap<String, Object>();
			byte[] bytes;
			bytes = util.executeExport(wlist,fields, configMap, "重复客户资源", "重复客户资源");
			model.put(AbstractMimeView.FILE_NAME, "重复客户资源.xls");
			model.put(AbstractMimeView.FILE_DATA, bytes);
			if(bytes != null){
				wdao.deleteAl(wlist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new MimeBytesView(), model);
	}
	/**
	 * 删除客户信息
	 * 同时删除手动分配的中间表记录
	 * 同时删除跟踪客户记录表记录
	 * 根据ID 删除
	 */
	@RequestMapping("/deleteclientAction.do")
	public ModelAndView deleteclientAction(@RequestParam("id")
		final String id, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String[] ids = id.split(",");//删除的id集合
		Long cid = null;
		int num = 0;
		String hql = "from Rcount r where r.loanDetail.client.id = ?";
		try {
			if(ids.length > 0){
				for(String _cid : ids){
					cid = Long.parseLong(_cid);
					//删除手动分配中间表记录
					cudao.updateByHQL("delete ClientUser cu where cu.client=?", new Object[] {cid});
					trackDao.updateByHQL("delete from ResourcesTrack tr where tr.client_name.id = ?", 
							new Object[]{cid});//删除客户对应的跟踪记录
					eliDao.updateByHQL("delete from Eliminate eli where eli.client.id = ?", new Object[]{cid});//删除淘汰记录
					addDao.updateByHQL("delete from AddClient ad where ad.client.id = ?", new Object[]{cid});//删除添加记录
					signDao.updateByHQL("delete from SignClient sc where sc.client.id = ?", new Object[]{cid});//删除签单记录
					List<Rcount> counts = rdao.findByHql(hql, new Object[]{cid});
					if(counts.size() > 0){
						rdao.deleteAll(counts);
					}
					lddao.updateByHQL("delete from LoanDetail ld where ld.client.id = ?", new Object[] {cid});
					seeDao.updateByHQL("delete SeeClient s where s.c_id=?",new Object[]{String.valueOf(cid)});
					dao.deleteById(cid, Client.class);//删除客户
					num++;
				}
				json.element("success", true);
				json.element("msg", "删除成功，"+"共删除了" + num + "条数据!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.element("failure", true);
			json.element("msg", "删除失败，请联系管理员或重新登录系统！");
			return MvcUtil.jsonObjectModelAndView(json);
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}
	/**
	 * 
	  *<功能简述>
	  *<功能详细描述>
	  * @param request
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/selectToday.do")
	public ModelAndView selectToday(HttpServletRequest request){
		JSONObject json = new JSONObject();
		String nowTime = StaticValues.sdf.format(new Date());//当前时间
		StringBuffer sb = new StringBuffer("");
		String hql = "select rt.client_name.id from ResourcesTrack rt where rt.resourcestime = '"+nowTime+"'";
		try {
			List<Object> list = odao.findAll(hql);
			if(list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					if(i == list.size() - 1){
						sb.append(list.get(i).toString());
					}else{
						sb.append(list.get(i).toString()).append(",");
					}
				}
			}
			json.element("success", true);
			json.element("msg", sb.toString());
		} catch (RuntimeException e) {
			json.element("failure", false);
			json.element("msg", "显示失败，请联系管理员!");
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}
	/**
	 * 查找编辑过的今日新增商机的id的集合
	  *<功能简述>
	  *<功能详细描述>
	  * @param request
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/selectTodayAdd.do")
	public ModelAndView selectTodayAdd(HttpServletRequest request){
		JSONObject json = new JSONObject();
		String nowTime = StaticValues.sdf.format(new Date());//当前时间
		if(request.getSession() != null){
			User user = (User)request.getSession().getAttribute(StaticValues.USER_SESSION);
			String roleCode = user.getRole().getRoleCode();
			String hql = "";
			if("201202".equals(roleCode)){
				hql = "select c.id from Client c where c.editTime = '"+nowTime+"' and c.follower.department.id = "+user.getDepartment().getId();
			}else if("201203".equals(roleCode)){
				hql = "select c.id from Client c where c.editTime = '"+nowTime+"' and c.follower.id = "+user.getId();
			}else if("201208".equals(roleCode)){
				hql = "select c.id from Client c where c.editTime = '"+nowTime+"' and c.follower.department.superId = "+user.getDepartment().getSuperId();
			}else{
				hql = "select c.id from Client c where c.editTime = '"+nowTime+"'";
			}
			List<Object> olist = odao.findAll(hql);
			if(olist.size() > 0){
				StringBuffer sb = new StringBuffer("");
				for(int i = 0; i < olist.size(); i++){
					if(i == olist.size() - 1){
						sb.append(olist.get(i));
					}else{
						sb.append(olist.get(i)).append(",");
					}
				}
				json.element("msg", sb.toString());
			}else{
				json.element("operateIds", "null");
			}
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}
	public static void fillMap(Map<String, String> map){
		map.put("01", "北京");
		map.put("02", "上海");
		map.put("03", "重庆");
		map.put("04", "天津");
		map.put("05", "安徽");
		map.put("06", "福建");
		map.put("07", "甘肃");
		map.put("08", "广东");
		map.put("09", "广西");
		map.put("10", "贵州");
		map.put("11", "海南");
		map.put("12", "河北");
		map.put("13", "黑龙江");
		map.put("14", "河南");
		map.put("15", "湖北");
		map.put("16", "湖南");
		map.put("17", "江苏");
		map.put("18", "江西");
		map.put("19", "吉林");
		map.put("20", "辽宁");
		map.put("21", "内蒙古");
		map.put("22", "宁夏");
		map.put("23", "青海");
		map.put("24", "山东");
		map.put("25", "山西");
		map.put("26", "陕西");
		map.put("27", "四川");
		map.put("28", "新疆");
		map.put("29", "西藏");
		map.put("30", "云南");
		map.put("31", "浙江");
		map.put("32", "香港");
		map.put("33", "澳门");
		map.put("34", "台湾");
	}
	/**
	 * 填充城市map
	 * @param map
	 * @return
	 */
	public static void fillCityMap(Map<String, String> map){
		map.put("001","东城");map.put("002","西城");map.put("003","崇文");map.put("004","宣武");map.put("005","朝阳");map.put("006","丰台");
		map.put("007","石景山");map.put("008","海淀");map.put("009","门头沟");map.put("010","房山");map.put("011","通州");map.put("012","顺义");
		map.put("013","昌平");map.put("014","大兴");map.put("015","怀柔");map.put("016","平谷");map.put("017","密云");
		map.put("018","延庆");
		
		map.put("019","崇明县");map.put("020","黄浦");map.put("021","卢湾");map.put("022","徐汇");
		map.put("023","长宁");map.put("024","静安");map.put("025","普陀");map.put("026","闸北");
		map.put("027","虹口");map.put("028","杨浦");map.put("029","闵行");map.put("030","宝山");
		map.put("031","嘉定");map.put("032","浦东");map.put("033","金山");map.put("034","松江");map.put("035","青浦");
		map.put("036","南汇");map.put("037","奉贤");map.put("038","朱家角");
		
		map.put("039","万州");map.put("040","涪陵");
		map.put("041","渝中");map.put("042","大渡口");map.put("043","江北");map.put("044","沙坪坝");map.put("045","九龙坡");
		map.put("046","南岸");map.put("047","北碚");map.put("048","万盛");map.put("049","双桥");map.put("050","渝北");
		map.put("051","巴南");map.put("052","黔江");map.put("053","长寿");map.put("054","綦江");map.put("055","潼南");
		map.put("056","铜梁");map.put("057","大足");map.put("058","荣昌");map.put("059","璧山");map.put("060","梁平");
		map.put("061","丰都");map.put("062","垫江");map.put("063","武隆");map.put("064","忠县");map.put("065","开县");
		map.put("066","云阳");map.put("067","奉节");map.put("068","巫山");map.put("069","巫溪");map.put("070","石柱");
		map.put("071","秀山");map.put("072","酉阳");map.put("073","彭水");map.put("074","江津");map.put("075","合川");
		map.put("076","永川");map.put("077","南川");
		
		map.put("078","和平");map.put("079","河东");map.put("080","河西");
		map.put("081","南开");map.put("082","河北");map.put("083","红桥");map.put("084","塘沽");map.put("085","汉沽");
		map.put("086","大港");map.put("087","东丽");map.put("088","西青");map.put("089","津南");map.put("090","北辰");
		map.put("091","武清");map.put("092","宝坻");map.put("093","宁河");map.put("094","静海");map.put("095","蓟县");
		
		map.put("096","合肥");map.put("097","芜湖");map.put("098","蚌埠");map.put("099","淮南");map.put("100","马鞍山");
		map.put("101","淮北");map.put("102","铜陵");map.put("103","安庆");map.put("104","黄山");map.put("105","滁州");
		map.put("106","阜阳");map.put("107","宿州");map.put("108","巢湖");map.put("109","六安");map.put("110","毫州");
		map.put("111","池州");map.put("112","宣城");
		
		map.put("113","福州");map.put("114","厦门");map.put("115","莆田");map.put("116","三明");
		map.put("117","泉州");map.put("118","漳州");map.put("119","南平");map.put("120","龙岩");map.put("121","宁德");
		
		map.put("122","兰州");map.put("123","嘉峪关");map.put("124","金昌");map.put("125","天水");
		map.put("126","武威");map.put("127","张掖");map.put("128","平凉");map.put("129","酒泉");map.put("130","庆阳");
		map.put("131","定西");map.put("132","陇南");map.put("133","临夏");map.put("134","甘南");
		
		map.put("135","广州");map.put("136","韵关");map.put("137","深圳");map.put("138","珠海");map.put("139","汕头");
		map.put("140","佛山");map.put("141","江门");map.put("142","湛江");map.put("143","茂名");map.put("144","肇庆");
		map.put("145","惠州");map.put("146","梅州");map.put("147","汕尾");map.put("148","河源");map.put("149","阳江");
		map.put("150","清远");map.put("151","东莞");map.put("152","中山");map.put("153","潮州");map.put("154","揭阳");
		map.put("155","云浮");
		
		map.put("156","南宁");map.put("157","柳州");map.put("158","桂林");map.put("159","梧州");
		map.put("160","北海");map.put("161","防城港");map.put("162","钦州");map.put("163","贵港");map.put("164","玉林");
		map.put("165","百色");map.put("166","贺州");map.put("167","河池");map.put("168","来宾");map.put("169","崇左");
		
		map.put("170","贵阳");map.put("171","六盘山");map.put("172","遵义");map.put("173","安顺");map.put("174","铜仁");
		map.put("175","毕节");map.put("176","黔西南");map.put("177","黔东南");map.put("178","黔南");
		
		map.put("179","海口");map.put("180","三亚");map.put("181","儋州");map.put("182","万宁");map.put("183","文昌");
		map.put("184","五指山");map.put("185","琼海");map.put("186","东方");
		
		map.put("187","石家庄");map.put("188","唐山");map.put("189","秦皇岛");
		map.put("190","邯郸");map.put("191","邢台");map.put("192","保定");map.put("193","张家口");map.put("194","承德");
		map.put("195","沧州");map.put("196","廊坊");map.put("197","衡水");
		
		map.put("198","哈尔滨");map.put("199","齐齐哈尔");map.put("200","鸡西");map.put("201","鹤岗");map.put("202","双鸭山");
		map.put("203","大庆");map.put("204","伊春");map.put("205","佳木斯");map.put("206","七台河");map.put("207","杜江丹");
		map.put("208","黑河");map.put("209","绥化");map.put("210","大兴安岭");
		
		map.put("211","郑州");map.put("212","开封");map.put("213","洛阳");map.put("214","平顶山");
		map.put("215","安阳");map.put("216","鹤壁");map.put("217","新乡");map.put("218","焦作");map.put("219","濮阳");
		map.put("220","许昌");map.put("221","漯河");map.put("222","三门峡");map.put("223","南阳");map.put("224","商丘");
		map.put("225","信阳");map.put("226","周口");map.put("227","驻马店");map.put("228","济源");
		
		map.put("229","武汉");map.put("230","黄石");map.put("231","十堰");map.put("232","宜昌");map.put("233","襄樊");map.put("234","鄂州");
		map.put("235","荆门");map.put("236","孝感");map.put("237","黄冈");map.put("238","咸宁");map.put("239","随州");
		map.put("240","恩施");map.put("241","仙桃");map.put("242","潜江");map.put("243","天门");map.put("244","神农架");
		
		map.put("245","长沙");map.put("246","株洲");map.put("247","湘潭");map.put("248","衡阳");map.put("249","邵阳");
		map.put("250","岳阳");map.put("251","常德");map.put("252","张家界");map.put("253","益阳");map.put("254","郴州");
		map.put("255","永州");map.put("256","怀化");map.put("257","娄底");map.put("258","湘西");
		
		map.put("259","南京");map.put("260","无锡");map.put("261","徐州");map.put("262","常州");map.put("263","苏州");map.put("264","南通");
		map.put("265","连云港");map.put("266","淮安");map.put("267","盐城");map.put("268","扬州");map.put("269","镇江");
		map.put("270","秦州");map.put("271","宿迁");
		
		map.put("272","南昌");map.put("273","景德镇");map.put("274","萍乡");
		map.put("275","九江");map.put("276","新余");map.put("277","鹰潭");map.put("278","赣州");map.put("279","吉安");
		map.put("280","宜春");map.put("281","抚州");map.put("282","上饶");
		
		map.put("283","长春");map.put("284","吉林");map.put("285","四平");map.put("286","辽源");map.put("287","通化");
		map.put("288","白山");map.put("289","松原");map.put("290","延边");
		
		map.put("291","沈阳");map.put("292","大连");map.put("293","鞍山");map.put("294","抚顺");
		map.put("295","本溪");map.put("296","丹东");map.put("297","锦州");map.put("298","营口");map.put("299","阜新");
		map.put("300","辽阳");map.put("301","盘锦");map.put("302","铁岭");map.put("303","朝阳");map.put("304","葫芦岛");
		
		map.put("305","呼和浩特");map.put("306","包头");map.put("307","乌海");map.put("308","赤峰");map.put("309","通辽");
		map.put("310","鄂尔多斯");map.put("311","呼伦贝尔");map.put("312","巴彦淖尔");map.put("313","乌兰察布");map.put("314","兴安盟");
		map.put("315","锡林郭勒盟");map.put("316","阿拉善盟");
		
		map.put("317","银川");map.put("318","石嘴山");map.put("319","吴忠");map.put("320","固原");map.put("321","中卫");
		
		map.put("322","西宁");map.put("323","海东");map.put("324","海北");
		map.put("325","黄南");map.put("326","海南");map.put("327","果洛");map.put("328","玉树");map.put("329","海西");
		
		map.put("330","济南");map.put("331","青岛");map.put("332","淄博");map.put("333","枣庄");map.put("334","东营");
		map.put("335","烟台");map.put("336","潍坊");map.put("337","济宁");map.put("338","泰安");map.put("339","威海");
		map.put("340","日照");map.put("341","莱芜");map.put("342","临沂");map.put("343","德州");
		map.put("344","聊城");map.put("345","滨州");map.put("346","菏泽");
		
		map.put("347","太原");map.put("348","大同");map.put("349","阳泉");map.put("350","长治");map.put("351","晋城");map.put("352","朔州");map.put("353","晋中");
		map.put("354","运城");map.put("355","忻州");map.put("356","临汾");map.put("357","吕梁");
		
		map.put("358","西安");map.put("359","铜川");map.put("360","宝鸡");map.put("361","咸阳");map.put("362","渭南");map.put("363","延安");
		map.put("364","汉中");map.put("365","榆林");map.put("366","安康");map.put("367","商洛");
		
		map.put("368","成都");map.put("369","自贡");map.put("370","攀枝花");map.put("371","泸州");map.put("372","德阳");map.put("373","绵阳");
		map.put("374","广元");map.put("375","遂宁");map.put("376","内江");map.put("377","乐山");map.put("378","南充");
		map.put("379","眉山");map.put("380","宜宾");map.put("381","广安");map.put("382","达州");map.put("383","雅安");
		map.put("384","巴中");map.put("385","资阳");map.put("386","阿坝");map.put("387","甘孜");map.put("388","凉山");
		
		map.put("389","乌鲁木齐");map.put("390","克拉玛依");map.put("391","吐鲁番");map.put("392","哈密");
		map.put("393","昌吉");map.put("394","博尔塔拉");map.put("395","巴音郭楞");map.put("396","阿克苏");
		map.put("397","克孜勒");map.put("398","喀什");map.put("399","和田");map.put("400","伊犁");
		map.put("401","塔城");map.put("402","阿勒泰");map.put("403","石河子");map.put("404","阿拉尔");map.put("405","图木舒克");map.put("406","五家渠");
		
		map.put("407","拉萨");map.put("408","昌都");map.put("409","山南");
		map.put("410","日喀则");map.put("411","那曲");map.put("412","阿里");map.put("413","林芝");
		
		map.put("414","昆明");map.put("415","曲靖");map.put("416","玉溪");map.put("417","保山");map.put("418","昭通");map.put("419","丽江");
		map.put("420","思茅");map.put("421","临沧");map.put("422","楚雄");map.put("423","红河");map.put("424","文山");
		map.put("425","西双版纳");map.put("426","大理");map.put("427","德宏");map.put("428","怒江");map.put("429","迪庆");map.put("430","普洱");
		
		map.put("431","杭州");map.put("432","宁波");map.put("433","温州");map.put("434","嘉兴");
		map.put("435","湖州");map.put("436","绍兴");map.put("437","金华");map.put("438","衡州");map.put("439","舟山");
		map.put("440","台州");map.put("441","丽水");
		
		map.put("442","香港");map.put("443","九龙");map.put("444","新界");map.put("445","澳门");
		
		map.put("446","台北");map.put("447","基隆");map.put("448","台南");
		map.put("449","高雄");map.put("450","屏东");map.put("451","南投");map.put("452","云林");map.put("453","新竹");
		map.put("454","彰化");map.put("455","苗栗");map.put("456","嘉义");map.put("457","花莲");map.put("458","桃园");
		map.put("459","宜兰");map.put("460","台东");map.put("461","金门");map.put("462","马祖");map.put("463","澎湖");
	}
	@RequestMapping("/saveClickClient.do")
	public ModelAndView saveClickClient(HttpServletRequest req){
		String cid = req.getParameter("cid");
		String name = req.getParameter("name");//客户信息
		String opp = req.getParameter("opp");//商机类型
		JSONObject json = new JSONObject();
		User user = null;
		JSONArray arr = new JSONArray();
		try {
			if(req.getSession() != null){
				user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				List<Object> objList = odao.findAll("select sc.id from SeeClient sc where sc.c_id = "+cid);
				String seeHql = "from SeeClient sc where sc.u_id = "+user.getId();
				List<SeeClient> scList = scDao.findAll(seeHql);//查找所有的点击记录
				if(objList.size() > 0){//表中已存在该条客户的点击记录
					fillClickRec(arr, scList);
				}else{
					SeeClient sc = new SeeClient();
					sc.setC_id(Integer.parseInt(cid));
					sc.setC_name(name+opp);
					sc.setU_id(user.getId().intValue());
					scDao.save(sc);
					if(scList.size() >= 5){//点击记录大于5时删除一条后再保存点击的客户信息
						scDao.deleteById(scList.get(0).getId(), SeeClient.class);
						scList.remove(0);
					}
					scList.add(sc);
					fillClickRec(arr, scList);
				}
				json.element("data", arr);
				json.element("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.element("data", arr);
			json.element("failure", true);
			json.element("msg", "系统异常,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(json);
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}
	public void fillClickRec(JSONArray arr, List<SeeClient> list) throws Exception{
		JSONObject item = null;
		for(SeeClient sc : list){
			item = new JSONObject();
			item.element("id", sc.getC_id());
			item.element("name", sc.getC_name());
			arr.add(item);
		}
	}
	@RequestMapping("/showSeeClient.do")
	public ModelAndView showSeeClient(HttpServletRequest req){
		JSONObject json = new JSONObject();
		JSONArray arr = new JSONArray();
		User user = null;
		try {
			if(req.getSession() != null){
				user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				String seeHql = "from SeeClient sc where sc.u_id = "+user.getId();
				List<SeeClient> scList = scDao.findAll(seeHql);//查找所有的点击记录
				if(scList.size() > 0){
					fillClickRec(arr, scList);
				}
				json.element("data", arr);
				json.element("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.element("data", arr);
			json.element("failure", true);
			json.element("msg", "系统异常,请联系管理员!");
			return MvcUtil.jsonObjectModelAndView(json);
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}
//	@RequestMapping("/test.do")
//	public void test(HttpServletRequest req) throws Exception{
//		Connection con = DBManager.getConnection();
//		String hql = "SELECT c.id from Client c, ResourcesTrack r where c.id = r.client_name and c.clientStatus = '3' GROUP BY c.id having COUNT(*) >= 3";
//		Long[] ids = {88l, 89l, 93l};
//		List<Object> objs = odao.findAll(hql);
//		List<Object> list = new ArrayList<Object>();
//		boolean autoFlag = con.getAutoCommit();
//		String now = StaticValues.format.format(new Date());
//		con.setAutoCommit(false);
//		Long cid = null;
//		PreparedStatement ps = con.prepareStatement("update dgg_client set clientStatus = '2', follower = ?, assignTime = ? where id = ?");
//		for(Long id : ids){
//			for(int i = 0; i < 2259 && i < objs.size(); i++){
//				list.add(objs.get(i));
//				cid = Long.parseLong(objs.get(i).toString());
//				ps.setLong(1, id);
//				ps.setString(2, now);
//				ps.setLong(3, cid);
//				ps.addBatch();
//				ps.clearParameters();
//				if(i%100 == 0 || i == 2258 || i == objs.size() - 1){
//					ps.executeBatch();
//					con.commit();
//					ps.clearBatch();
//				}
//			}
//			objs.removeAll(list);
//			list.clear();
//		}
//		ps.close();
//		con.setAutoCommit(autoFlag);
//		con.close();
//	}
}
