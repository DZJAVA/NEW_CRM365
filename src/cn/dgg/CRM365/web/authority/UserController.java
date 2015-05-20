package cn.dgg.CRM365.web.authority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.authority.Department;
import cn.dgg.CRM365.domain.authority.Role;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.authority.UserRole;
import cn.dgg.CRM365.domain.resources.AutoAssign;
import cn.dgg.CRM365.domain.resourcesManage.ClientDifRecord;
import cn.dgg.CRM365.domain.resourcesManage.XzAllocation;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.commonUtil.StringUtil;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;

/**
 * 用户管理类
 * 
 * @author chenqin
 * 
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<User> dao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<UserRole> userRoleDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ClientDifRecord> cddao;
	
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object> objDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<AutoAssign> autoDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<XzAllocation> xaDao;
	

	Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage() {
		return new ModelAndView("authority/userInfo");
	}

	/**
	 * 加载用户信息
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @return xiexiaoming 2012-11-29 8:47
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "/loadUser.do")
	public ModelAndView loadGrid(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("_userName")
			String user_name) {
		List list = new ArrayList();
		String hql = "from User u where 1=1 and u.userDelState=0";
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		StringBuffer wherehql = new StringBuffer();
		User userSession = null;
		try {
			if(request.getSession() != null){
				userSession = (User) request.getSession().getAttribute("userSession");
				String roleCode = userSession.getRole().getRoleCode();
				if("201202".equals(roleCode)){
					wherehql.append(" and u.department.id=").append(userSession.getDepartment().getId());
				}
				if("201203".equals(roleCode)){
					wherehql.append(" and u.id=").append(userSession.getId());
				}
				if (user_name != null && !"".equals(user_name)) {
					hql += " and u.userName like '%" + user_name + "%'";
				}
				if("201208".equals(roleCode)){
					wherehql.append(" and u.department.superId=").append(userSession.getDepartment().getSuperId());
				}
				pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
				List<User> dataList = dao.findByHql(hql+wherehql, list.toArray(), pagination);
				jsonObject.element("totalCount", pagination.getTotalResults());
				// 有无数据都要产生data节点
				JSONArray data = new JSONArray();
				if (dataList.size() > 0) {
					for (Object d : dataList) {
						JSONObject item = new JSONObject();
						User field = (User) d;
						item.element("id", MvcUtil.toJsonString(field.getId()));
						// 登录帐号
						item.element("loginId", MvcUtil.toJsonString(field.getLoginId()));
						
						// 员工姓名
						item.element("userName", MvcUtil.toJsonString(field.getUserName()));
						item.element("departId", MvcUtil.toJsonString(field.getDepartment().getId()));
						// 部门姓名
						item.element("departName", MvcUtil.toJsonString(field.getDepartment().getDepaName()));
						// 是否停用
						item.element("isOrNotEnable", MvcUtil.toJsonString(field
								.getIsOrNotEnable()));
						item.element("password", MvcUtil.toJsonString(field.getPassword()));
						// 判断角色对象是否为空
						if (field.getRole() != null && !"".equals(field.getRole())) {
							item.element("roleId", MvcUtil.toJsonString(field.getRole().getId()));
							// 角色名称
							item.element("roleName", MvcUtil.toJsonString(field.getRole().getRoleName()));
						}
						item.element("counts", field.getCounts());
						item.element("signStatus", field.getSignStatus());
						item.element("remark", MvcUtil.toJsonString(field.getRemark()));
						data.add(item);
					}
				}
				jsonObject.element("data", data);
			}
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 
	 * <功能简述> 查询角色下面的用户 <功能详细描述>
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @param roleId
	 * @param storeType
	 *            1表示 查询与角色绑定的用户 2表示查询没有与角色绑定的用户
	 * @param sel_type_name
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value = "/loadRoleWithUser.do")
	public ModelAndView loadUserGrid(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("_roleId")
			String roleId, @RequestParam("_storeType")
			String storeType, @RequestParam("_sel_name")
			String sel_name) {

		String hql = null;
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		List<User> dataList = null;
		List<UserRole> urList = null;
		if (!"".equals(roleId) && roleId != null) {
			hql = "from UserRole ur where ur.role.id = ? ";
			urList = userRoleDao.findByHql(hql, new Object[] { Long
					.parseLong(roleId) }, pagination);
			if ("1".equals(storeType)) {
				if (urList != null && urList.size() > 0) {
					dataList = new ArrayList<User>();
					for (int o = 0; o < urList.size(); o++) {
						dataList.add(urList.get(o).getUser());
					}
				} else {
					dataList = new ArrayList<User>();
				}
			} else {
				if (urList != null && urList.size() > 0) {
					if ("".equals(sel_name) || sel_name == null) {
						hql = "from User u where u.id not in(select ur.user.id from UserRole ur where ur.role.id = ?) ";
					} else {
						hql = "from User u where u.id not in(select ur.user.id from UserRole ur where ur.role.id = ?) and u.userName like '%"
								+ sel_name + "%'";
					}
					dataList = dao.findByHql(hql, new Object[] { Long
							.parseLong(roleId), }, pagination);
				} else {
					if ("".equals(sel_name) || sel_name == null) {
						hql = "from User u";
						dataList = dao.findByHql(hql, new Object[] {},
								pagination);
					} else {
						hql = "from User u where u.userName like '%" + sel_name
								+ "%'";
						dataList = dao.findByHql(hql, new Object[] {},
								pagination);
					}
				}
			}
		} else {
		}
		try {
			jsonObject.element("totalCount", pagination.getTotalResults());
			// System.out.println(BeanMessage.message(pagination));
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Object d : dataList) {
					JSONObject item = new JSONObject();
					User field = (User) d;
					item.element("id", MvcUtil.toJsonString(field.getId()));
					item.element("loginId", MvcUtil.toJsonString(field
							.getLoginId()));
					item.element("userName", MvcUtil.toJsonString(field.getUserName()));
					item.element("sex", MvcUtil.toJsonString(field.getSex()));
					item.element("remark", MvcUtil.toJsonString(field
							.getRemark()));
					data.add(item);
				}
			}
			jsonObject.element("data", data);
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 添加或修改用户信息
	 * 
	 * @param user 数据封装对象
	 * @param request 请求对象
	 * @param empOrCus 获取员工下拉框的值
	 * @param find_role 获取角色下拉框的值
	 * @param comboxType1 获取用户类型下拉框的值
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "/saveOrUpdateUser.do")
	public ModelAndView saveOrupdateUser(User user, HttpServletRequest request,@RequestParam("find_role") String find_role,
			@RequestParam("dept") String dept) {
		JSONObject json = new JSONObject();
		try {
			if (request.getSession() != null) {
				User userSession = (User) request.getSession().getAttribute("userSession");
				Role role = new Role();
				role.setId(Long.parseLong(find_role));
				user.setRole(role);
				Department dep = new Department();
				dep.setId(Long.parseLong(dept));
				user.setDepartment(dep);
				List<Object> userLists=objDao.findAll("select u.id from User u where u.loginId = '"+user.getLoginId()+"' or u.userName='"+user.getUserName()+"'");
				if (user.getId() == null) {
					if(userLists.size() > 0){
						json.element("success", false);
						json.element("message", "用户已经存在，请重新填写");
						return MvcUtil.jsonObjectModelAndView(json);
					}else{
						dao.save(user);
					}
				} else {
					if(userLists.size() == 0 || (userLists.size() > 0 && Integer.parseInt(userLists.get(0).toString()) == user.getId())){
						SqlBuilder sqlBuilder = new SqlBuilder("User",SqlBuilder.TYPE_UPDATE);
						sqlBuilder.addField("userName", user.getUserName());
						sqlBuilder.addField("password", user.getPassword().trim());
						sqlBuilder.addField("remark", user.getRemark());
						sqlBuilder.addField("counts", user.getCounts());
						sqlBuilder.addField("telPhone", user.getTelPhone());
						sqlBuilder.addField("signStatus", user.getSignStatus());
						sqlBuilder.addField("department.id", user.getDepartment().getId());
						sqlBuilder.addField("isOrNotEnable", user.getIsOrNotEnable());
						if (user.getRole() != null) {
							sqlBuilder.addField("role.id", user.getRole().getId());
						}
						sqlBuilder.addWhere("id", user.getId());
						dao.updateByHQL(sqlBuilder.getSql(), sqlBuilder.getParams());
					}else{
						json.element("success", false);
						json.element("message", "用户已经存在，请重新填写");
						return MvcUtil.jsonObjectModelAndView(json);
					}
				}
				json.element("success", true);
				json.element("message", "保存成功！");
			} else {
				json.element("failure", true);
				json.element("msg", "停留时间过长，请重新登录");
				return MvcUtil.jsonObjectModelAndView(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.element("success", false);
			json.element("message", "保存失败！");
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}

	/**
	 * 删除用户关联的角色信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteUesrWithRole.do")
	public ModelAndView deleteUser(@RequestParam("_userId")
	String userId, @RequestParam("_roleIds")
	String roleIds) {
		JSONObject jsonObject = new JSONObject();
		try {
			String[] roleId = roleIds.split(",");
			for (String i : roleId) {
				userRoleDao.updateByHQL("delete UserRole ur where ur.user.id=? and ur.role.id=?",
								new Object[] { Long.parseLong(userId),Long.parseLong(i) });
			}

			jsonObject.element("success", true);
		} catch (Exception e) {
			jsonObject.element("success", false);
			jsonObject.element("message", "删除失败！");
			e.printStackTrace();
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 为栏目添加角色
	 * 
	 * @return
	 */
	@RequestMapping("/saveRoleAndUser.do")
	public ModelAndView saveRoleAndMenu(@RequestParam("userid")
	String userId, @RequestParam("roleid")
	String roleId, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			User user = new User();
			Role role = new Role();
			user.setId(Long.parseLong(userId));
			role.setId(Long.parseLong(roleId));
			UserRole userRole = new UserRole();
			userRole.setUser(user);
			userRole.setRole(role);

			List<UserRole> list = userRoleDao.findByHql(
					"from UserRole mr where mr.role.id = ? and mr.user.id = ?",
					new Object[] { Long.parseLong(roleId),
							Long.parseLong(userId) });
			if (list.size() == 0 && list.isEmpty()) {
				userRoleDao.save(userRole);
				jsonObject.element("success", true);
			} else {
				jsonObject.element("success", false);
				jsonObject.element("msg", "该用户已经配置了该角色，请选择其他角色进行配置！");
			}
		} catch (Exception e) {
			jsonObject.element("success", false);
			jsonObject.element("msg", "用户配置角色失败！");
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 
	 * <功能简述> 根据用户查询它的角色信息 <功能详细描述>
	 * 
	 * @param userId
	 *            用户ID
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadUserWithRole.do")
	public ModelAndView loadUserWithRole(@RequestParam("_userId")
	String userId, HttpServletRequest request) {

		JSONObject jsonObject = new JSONObject();
		JSONArray data = new JSONArray();
		JSONObject item = null;
		try {
			List<UserRole> urList = userRoleDao.findByHql(
					"from UserRole ur where ur.user.id=?", new Object[] { Long
							.parseLong(userId) });
			if (urList != null && urList.size() > 0) {
				for (UserRole ur : urList) {
					item = new JSONObject();
					item.element("rid", ur.getRole().getId());
					item.element("rName", ur.getRole().getRoleName());
					item.element("rRemark", ur.getRole().getRemark());
					data.add(item);
				}
			}
			jsonObject.element("data", data);
		} catch (Exception e) {
			jsonObject.element("success", false);
			jsonObject.element("msg", "获取角色失败！");
			e.printStackTrace();
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 添加用户 <功能简述> <功能详细描述>
	 * 
	 * @param position
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/saveUser.do")
	public ModelAndView saveUser(User user, HttpServletRequest request,
			@RequestParam("depart")
			String _depart, @RequestParam("posi")
			String _posi, @RequestParam("sex")
			String _sex, @RequestParam("usName")
			String _usName) {
		JSONObject jsonObject = new JSONObject();
		try {
			if (request.getSession() != null) {
				String hqlString = "from User u where u.loginId=?";
				user.setSex(_sex);

				user.setUserName(_usName);
				user.setIsOrNotEnable(2);

				List<User> users = dao.findByHql(hqlString, new Object[] { user
						.getLoginId() });
				if (users.size() > 0) {
					jsonObject.element("failure", true);
					jsonObject.element("msg", " 保存用户失败,该用户已被注册！");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				} else {
					dao.save(user);
					jsonObject.element("success", true);
					jsonObject.element("msg", "添加用户成功！");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", " 保存用户失败！");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	
	/**
	 * 像QQ一样的右下角弹出消息（每隔一段时间）弹出
	 * 根据角色来判断 
	 * 分配人是接线部门的人员
	 * 
	 */
	  @RequestMapping(value = "processexaminerecords.do")
	  public ModelAndView popupMessage(HttpServletRequest req) { 
		  JSONObject model = new JSONObject();
		  HttpSession session = req.getSession();
		  if(session != null){
			  User user = (User) req.getSession().getAttribute("userSession");
			  Role role = user.getRole();
			  String roleCode = role.getRoleCode();
			  StringBuffer sb = new StringBuffer();
			  String nowTime = StaticValues.sdf.format(new Date());//当前时间
			  List<Object> cList = new ArrayList<Object>();// 客户信息
			  //查找客户信息 。 根据分配人的角色代码 201306 和编辑时间等于空 和分配时间为今天 查询客户
			  String hql = "select c.id from Client c where c.assignPerson.role.remind='1' and c.clientStatus <> '3' and " +
			  		"c.assignTime like'"+nowTime+"%'";
			  if("201202".equals(roleCode)){//部门经理
				  sb.append(" and (c.remindFlag is null or c.remindFlag = '')");
				  sb.append(" and c.follower.department.id = ").append(user.getDepartment().getId());
			  }else{//员工
				  sb.append(" and (c.editTime is null or c.editTime = '')");
				  sb.append(" and c.follower.id = ").append(user.getId());
			  }
			  cList = objDao.findAll(hql + sb);
			  String id = "";
			  if (cList.size() > 0) {
				  for(Object o : cList){
					  id = id + o + ",";
				  }
				  model.element("success", true);
				  model.element("message", "1");
				  model.element("id", id.substring(0, id.length() - 1));//提醒的id集合
			  }
		  }
	      return MvcUtil.jsonObjectModelAndView(model);
	  }
	public static void main(String[] args) {
		String str = "123,";
		System.out.println(str.substring(0, str.length() - 1));
	}
	  
	/**
	 * 删除用户 <功能简述> <功能详细描述>
	 * 
	 * @param id
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/deleteUsers.do")
	public ModelAndView deleteUsers(@RequestParam("id")String id) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		int sum = 0;
		try {
			for (String i : ids) {
				List<Object> objList = objDao.findAll("select c.id from Client c where c.follower.id="+Long.parseLong(i)+" and c.clientStatus<>'3'");
				User userSet = new User();
				if (objList.size()==0) {
					dao.updateByHQL("delete from User u where u.id = ?", new Object[]{Long.parseLong(i)});
					sum++;
				}
			}
			if (sum > 0) {
				jsonObject.element("success", true);
				jsonObject.element("msg", "成功删除" + sum + "条!");
			} else {
				jsonObject.element("success", false);
				jsonObject.element("msg", "该用户已分配资源,不能删除！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 改变用户是否启用的状态
	 * @param req
	 * @return
	 */
	@RequestMapping("/userStatus.do")
	public ModelAndView changeUserStatus(HttpServletRequest req){
		JSONObject json = new JSONObject();
		String id = req.getParameter("id");//id集合
		String flag = req.getParameter("flag");//0启用，1停用
		String[] ids = id.split(",");
		SqlBuilder sb = null;
		try {
			if(ids.length > 0){
				for(String uid : ids){
					sb = new SqlBuilder("User", SqlBuilder.TYPE_UPDATE);
					if("0".equals(flag)){
						sb.addField("isOrNotEnable", 2);//启用
					}else{
						sb.addField("isOrNotEnable", 1);//停用
					}
					sb.addWhere("id", Long.parseLong(uid));
					dao.updateByHQL(sb.getSql(), sb.getParams());
				}
			}
			json.element("success", true);
			json.element("msg", "改变成功!");
		} catch (Exception e) {
			e.printStackTrace();
			json.element("failure", true);
			json.element("msg", "连接服务器失败，请重新登录或联系管理员!");
			return MvcUtil.jsonObjectModelAndView(json);
		}
		return MvcUtil.jsonObjectModelAndView(json);
	}
}
