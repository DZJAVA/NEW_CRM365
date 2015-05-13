package cn.dgg.CRM365.web.authority;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import cn.dgg.CRM365.domain.authority.Menu;
import cn.dgg.CRM365.domain.authority.MenuRole;
import cn.dgg.CRM365.domain.authority.Role;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.authority.UserRole;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.commonUtil.StringUtil;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;

/**
 * 角色管理
 * 
 * @author 
 * 2012-11-27 17：20
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Role> dao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<MenuRole> menuRoleDao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<UserRole> userRoleDao;

	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage() {
		return new ModelAndView("authority/roleInfo");
	}

	/**
	 * 加载角色信息
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @return xiexiaoming 2012-11-27 17:20
	 */
	@RequestMapping(value = "/loadRole.do")
	public ModelAndView loadGrid(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request) {
		List<User> list = null;
		String hql = "from Role r";
		JSONObject jsonObject = new JSONObject();
		// 翻页对象
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		try {
			// 查询所有角色信息
			List<Role> dataList = dao.findByHql(hql, new Object[] {},
					pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Object d : dataList) {
					JSONObject item = new JSONObject();
					Role field = (Role) d;
					item.element("id", MvcUtil.toJsonString(field.getId()));
					// 保存角色名称
					item.element("roleName", MvcUtil.toJsonString(field.getRoleName()));
					// 保存创建时间
					item.element("createTime", MvcUtil.toJsonString(field.getCreateTime()));
					// 保存创建人姓名
					item.element("createUser", MvcUtil.toJsonString(field.getCreateUser()));
					// 备注信息
					item.element("remark", MvcUtil.toJsonString(field.getRemark()));
					//分配提醒
					if("1".equals(field.getRemind())){
						item.element("remind", MvcUtil.toJsonString("提醒"));
					}else if("2".equals(field.getRemind())){
						item.element("remind", MvcUtil.toJsonString("不提醒"));						
					}
					if("201201".equals(field.getRoleCode())){
						// 代码角色
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("管理员"));
					}else if("201202".equals(field.getRoleCode())){
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("部门经理"));	
					}else if("201203".equals(field.getRoleCode())){
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("员工"));	
					}else if("201204".equals(field.getRoleCode())){
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("行政"));	
					}else if("201205".equals(field.getRoleCode())){
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("统计员"));	
					}else if("201206".equals(field.getRoleCode())){
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("客户导入员"));	
					}else if("201207".equals(field.getRoleCode())){
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("贷后管理员"));	
					}else if("201208".equals(field.getRoleCode())){
						item.element("roleCode", MvcUtil.toJsonString(field.getRoleCode()));
						item.element("roleCodeName", MvcUtil.toJsonString("市场调研员"));	
					}
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
	 * 加载下拉列表角色信息
	 * 
	 * @param gridLoadParams
	 * @param
	 * @return xiexiaoming 2012-11-27 17:21
	 */
	@RequestMapping(value = "/loadRoleXiaLa.do")
	public ModelAndView loadRoleXiaLa(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request) {
		List<User> list = null;
		String hql = "from Role r";
		JSONObject jsonObject = new JSONObject();
		try {
			List<Role> dataList = dao.findAll(hql);
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Object d : dataList) {
					JSONObject item = new JSONObject();
					Role field = (Role) d;
					// 上级角色id
					item.element("roleId", MvcUtil.toJsonString(field.getId()));
					// 上级角色名称
					item.element("roleName", MvcUtil.toJsonString(field
							.getRoleName()));
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
	 * 添加或修改角色信息
	 * 
	 * @param role 页面封装数据的角色对象 
	 * @param _competenceType 角色权限
	 * @param _comboxType1 角色代码
	 * @param _remindType1 分配提醒
	 * @param request
	 * @return 王玉川 2012-12-20 
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "/saveOrUpdateUser.do")
	public ModelAndView saveOrupdateUser(Role role, HttpServletRequest request,
			@RequestParam("_comboxType1")String _comboxType1,@RequestParam("_remindType1")String _remindType1) {
		JSONObject jsonObject = new JSONObject();
		User userSession = (User) request.getSession().getAttribute(
				StaticValues.USER_SESSION);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		role.setCreateTime(sdf.format(new Date()));
		role.setCreateUser(userSession.getUserName());
		try {
			if (role.getId() == null) {
				List<Role> rList = dao.findByHql("from Role g where g.roleName=?", new Object[] { role.getRoleName() });
				if(rList.size()>0){
					jsonObject.element("success", false);
					jsonObject.element("msg", "角色名已经存在!");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}else{
					dao.save(role);
				}
			} else {
				List<Role> gList = dao.findByHql("from Role g where g.id=?",new Object[] { role.getId() });
				if (gList.get(0).getRoleName().equals(role.getRoleName())) {
						role.setRoleCode(_comboxType1);
						role.setRemind(_remindType1);
						dao.update(role);	
				} else {
					List<Role> rpList = dao.findByHql("from Role g where g.roleName=? and g.id != ?",new Object[] { role.getRoleName(), role.getId() });
					if (rpList != null && rpList.size() > 0) {
						jsonObject.element("success", false);
						jsonObject.element("msg", "角色名称已经存在!");
						return MvcUtil.jsonObjectModelAndView(jsonObject);
					} else {
							role.setRoleCode(_comboxType1);
							role.setRemind(_remindType1);
							dao.update(role);	
					}
				}
			}
			jsonObject.element("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("success", false);
			jsonObject.element("message", "操作失败!");
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return xiexiaoming 2012-11-27 17:22
	 */
	@RequestMapping("/deleteRole.do")
	public ModelAndView deleteRole(@RequestParam("id")
	String id) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		for (String i : ids) {
			try {
				dao.deleteById(Long.parseLong(i), Role.class);
				jsonObject.element("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("success", false);
				jsonObject.element("msg", "该角色有关联数据，不能删除！");
			}
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 获取角色列表
	 * 
	 * @return xiexiaoming 2012-11-27 17:22
	 */
	@RequestMapping("/findRole.do")
	public ModelAndView loadRole() {
		JSONObject jsonObject = new JSONObject();
		try {
			String hql = "from Role r";
			List<Role> dataList = dao.findAll(hql);
			JSONArray data = new JSONArray();

			if (dataList.size() > 0) {
				for (Role field : dataList) {
					JSONObject item = new JSONObject();
					item.element("id", MvcUtil.toJsonString(field.getId()));
					item.element("role_name", MvcUtil.toJsonString(field
							.getRoleName()));
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
	 * 
	 * <功能简述> 给角色添加菜单栏目 <功能详细描述>
	 * 
	 * @param ids
	 * @param roleId
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员] xiexiaoming 2012-11-27 17:22
	 */
	@RequestMapping("/saveMenuWithRole.do")
	public ModelAndView saveConfigurationColumn(@RequestParam("_ids")
	String ids, @RequestParam("_roleId")
	String roleId, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		Role role = new Role();
		role.setId(Long.parseLong(roleId));
		Menu menu;
		String[] menu_ids = ids.split(",");
		MenuRole menuRole;
		for (String menuId : menu_ids) {
			try {
				List<MenuRole> list = menuRoleDao
						.findAll("from MenuRole mr where mr.role.id = "
								+ Long.parseLong(roleId) + " and mr.menu.id = "
								+ Long.parseLong(menuId));
				if (list == null || list.size() == 0) {
					menu = new Menu();
					menu.setId(Long.parseLong(menuId));
					menuRole = new MenuRole();
					menuRole.setMenu(menu);
					menuRole.setRole(role);
					menuRoleDao.save(menuRole);
					jsonObject.element("success", true);
				} else {
					jsonObject.element("success", false);
					jsonObject.element("msg", "<"
							+ list.get(0).getRole().getRoleName() + ">中配置的<"
							+ list.get(0).getMenu().getSmenCaption()
							+ ">菜单栏目已经存在，请重新选择进行配置！");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("success", false);
				jsonObject.element("msg", "配置角色出错！");
			}
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 
	 * <功能简述> 为角色添加用户 <功能详细描述>
	 * 
	 * @param ids
	 * @param roleId
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员] xiexiaoming 2012-11-27 17:22
	 */
	@RequestMapping("/saveUserWithRole.do")
	public ModelAndView saveConfigurationUser(@RequestParam("_ids")
	String ids, @RequestParam("_roleId")
	String roleId, HttpServletRequest request) {

		JSONObject jsonObject = new JSONObject();
		Role role = new Role();
		role.setId(Long.parseLong(roleId));
		User user;
		String[] user_ids = ids.split(",");
		UserRole userRole;
		for (String userId : user_ids) {
			try {
				user = new User();
				user.setId(Long.parseLong(userId));
				userRole = new UserRole();
				userRole.setUser(user);
				userRole.setRole(role);
				userRoleDao.save(userRole);
				jsonObject.element("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("success", false);
				jsonObject.element("msg", "配置用户出错！");
			}
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 
	 * <功能简述> 删除角色下的用户信息 <功能详细描述>
	 * 
	 * @param roleId
	 * @param id
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员] xiexiaoming 2012-11-27 17:22
	 */
	@RequestMapping("/deleteRoleWithUser.do")
	public ModelAndView deleteRoleWithUser(@RequestParam("_roleId")
	String roleId, @RequestParam("id")
	String id) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		for (String i : ids) {
			try {
				List<UserRole> list = userRoleDao
						.findByHql(
								"from UserRole ur where ur.role.id = ? and ur.user.id = ?",
								new Object[] { Long.parseLong(roleId),
										Long.parseLong(i) });
				userRoleDao.delete(list.get(0));
				jsonObject.element("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("success", false);
				jsonObject.element("msg", "删除用户出错！");
			}
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 
	 * <功能简述> 删除角色下的菜单栏目 <功能详细描述>
	 * 
	 * @param id
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员] xiexiaoming 2012-11-27 17：23
	 */
	@RequestMapping("/deleteRoleWithMenu.do")
	public ModelAndView deleteRoleWithMenu(@RequestParam("_roleId")
	String roleId, @RequestParam("id")
	String id) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		for (String i : ids) {
			try {
				List<MenuRole> list = menuRoleDao
						.findByHql(
								"from MenuRole mr where mr.role.id = ? and mr.menu.id = ?",
								new Object[] { Long.parseLong(roleId),
										Long.parseLong(i) });
				menuRoleDao.delete(list.get(0));
				jsonObject.element("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("success", false);
				jsonObject.element("msg", "删除角色出错！");
			}
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	public ICommonDAO<Role> getDao() {
		return dao;
	}

	public void setDao(ICommonDAO<Role> dao) {
		this.dao = dao;
	}

	public ICommonDAO<MenuRole> getMenuRoleDao() {
		return menuRoleDao;
	}

	public void setMenuRoleDao(ICommonDAO<MenuRole> menuRoleDao) {
		this.menuRoleDao = menuRoleDao;
	}

	public ICommonDAO<UserRole> getUserRoleDao() {
		return userRoleDao;
	}

	public void setUserRoleDao(ICommonDAO<UserRole> userRoleDao) {
		this.userRoleDao = userRoleDao;
	}
}
