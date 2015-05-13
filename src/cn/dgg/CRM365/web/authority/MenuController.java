package cn.dgg.CRM365.web.authority;

import groovy.time.BaseDuration.From;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.dgg.CRM365.domain.authority.Menu;
import cn.dgg.CRM365.domain.authority.MenuRole;
import cn.dgg.CRM365.domain.authority.Role;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.authority.UserRole;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;

import com.sun.org.apache.bcel.internal.generic.Select;

/**
 * 菜单管理
 * 
 * @author 王科(大) 2012-11-21
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Menu> menuDao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<MenuRole> menuRoleDao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object[]> userMenuDao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<UserRole> userRoleDao;

	Logger logger = Logger.getLogger(MenuController.class);

	@RequestMapping("/openMenuView.do")
	public ModelAndView jumpPage() {
		return new ModelAndView("authority/menuInfo");
	}

	@RequestMapping(value = "rootNode.do")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initHeader(response);
		renderText(this.getTreePanelList(request), response);
	}

	@RequestMapping(value = "childNode.do")
	public void zijiedian(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		initHeader(response);
		renderText(this.getTreeNodeList(request, request.getParameter("id")), response);
	}

	// 横向树
	@RequestMapping(value = "childNodeb.do")
	public void childNodeb(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String content = "new Ext.menu.Menu({'items':[";
		List<Menu> menuList = menuDao
				.findAll("from Menu m where m.systemMenu.id='"
						+ Long.parseLong(request.getParameter("id"))
						+ "' order by smenIndex");
		for (int i = 0; i < menuList.size(); i++) {
			content += "{'id':'" + request.getParameter("id") + "-" + (i + 1)
					+ "','text':'" + menuList.get(i).getSmenCaption() + "'";
			List<Menu> ddList = menuDao
					.findAll("from Menu m where m.systemMenu.id='"
							+ menuList.get(i).getId() + "' order by smenIndex");
			if (ddList.size() > 0) {
				content += ",'menu':{'items':[";
			}
			for (int j = 0; j < ddList.size(); j++) {
				content += "{'text':'" + ddList.get(j).getSmenCaption()
						+ "','id':'" + menuList.get(i).getId() + "-" + (j + 1)
						+ "'";
				if (j == ddList.size() - 1) {
					content += "}";
				} else {
					content += "},";
				}
			}
			if (ddList.size() > 0) {
				content += "]}";
			}

			if (i == menuList.size() - 1) {
				content += "}";
			} else {
				content += "},";
			}
		}
		content += "]})";
		renderText(content, response);
	}

	public String getTreeNodeList(HttpServletRequest request, String id) {
		String content = "new Ext.tree.AsyncTreeNode({expanded:true,children:[";
		User user = (User) request.getSession().getAttribute(
				StaticValues.USER_SESSION);
		List<Menu> menuList = null;
		String hsql = "select m.id,m.resourceURL,m.smenCaption,m.smenIcon from MenuRole mor,Menu m where mor.role.id='"
				+ user.getRole().getId()
				+ "' and m.id=mor.menu.id and m.systemMenu.id='"
				+ Long.parseLong(id) + "'";
		List<Object[]> userMenuList = userMenuDao.findAll(hsql);
		for (int i = 0; i < userMenuList.size(); i++) {
			content += "{'id':'" + userMenuList.get(i)[0] + "-" + (i + 1)
					+ "','url':'" + userMenuList.get(i)[1] + "','text':'"
					+ userMenuList.get(i)[2] + "','iconCls':'"
					+ userMenuList.get(i)[3] + "','leaf':'true'";
			if (i == userMenuList.size() - 1) {
				content += "}";
			} else {
				content += "},";
			}
		}
		content += "]})";
		return content;
	}

	public String getTreePanelList(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(StaticValues.USER_SESSION);
		JSONArray array = new JSONArray();// 取得所有菜单
		// 根据用户session查询，用户角色
		String hsql = "select m.systemMenu.id,m.systemMenu.smenIcon,m.systemMenu.smenCaption from MenuRole mor,Menu m where mor.role.id='"
				+ user.getRole().getId()
				+ "' and m.id=mor.menu.id group by m.systemMenu.id";
		List<Object[]> userMenuList = userMenuDao.findAll(hsql);
		for (int i = 0; i < userMenuList.size(); i++) {
			JSONObject json = new JSONObject();
			json.element("id", userMenuList.get(i)[0].toString());// 编号
			json.element("iconCls", userMenuList.get(i)[1].toString());// 图片路径
			json.element("title", userMenuList.get(i)[2].toString());// 标题
			array.add(json);
		}
		return array.toString();
	}

	public static void renderText(final String content,
			HttpServletResponse response) {
		try {
			response = initHeader(response);
			response.getWriter().write(content);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HttpServletResponse initHeader(HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("UTF-8");
		return response;
	}

	/**
	 * 查询出上级菜单
	 * 
	 * @return 王科(大) 2012-11-19
	 */
	// @RequestMapping("/loadPMenu.do")
	@RequestMapping("/loadPMenu.do")
	public ModelAndView loadPMenu() {
		JSONObject jsonObject = new JSONObject();
		try {
			String hsql = "from Menu m where m.systemMenu.id is null";
			List<Menu> dataList = menuDao.findAll(hsql);
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Menu field : dataList) {
					JSONObject item = new JSONObject();
					item.element("id", MvcUtil.toJsonString(field.getId()));
					item.element("menu_name", MvcUtil.toJsonString(field
							.getSmenCaption()));
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
	 * 加载菜单信息
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @return 谢小明 2012-11-27 14：44
	 */
	@RequestMapping("/loadMenu.do")
	public ModelAndView loadGrid(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("_sel_name")
			String sel_name) {
		String hql = "from Menu u where 1=1 ";
		if (!"".equals(sel_name) && sel_name != null) {
			hql += " and u.smenCaption like '%" + sel_name + "%'";
		}
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		try {
			List<Menu> dataList = menuDao.findAll(hql, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();

			if (dataList.size() > 0) {

				for (Menu field : dataList) {
					JSONObject item = new JSONObject();
					item.element("id", MvcUtil.toJsonString(field.getId()));
					if (field.getSystemMenu() != null
							&& !"".equals(field.getSystemMenu())) {
						item.element("systemMenu_id", MvcUtil
								.toJsonString(field.getSystemMenu().getId()));
						item.element("systemMenu_name", MvcUtil
								.toJsonString(field.getSystemMenu()
										.getSmenCaption()));
					} else {
						item.element("systemMenu_id", "");
						item.element("systemMenu_name", "");
					}
					item.element("resourceURL", MvcUtil.toJsonString(field
							.getResourceURL()));
					item.element("smenIndex", MvcUtil.toJsonString(field
							.getSmenIndex()));
					item.element("smenCaption", MvcUtil.toJsonString(field
							.getSmenCaption()));
					item.element("smenIcon", MvcUtil.toJsonString(field
							.getSmenIcon()));
					item.element("smenHint", MvcUtil.toJsonString(field
							.getSmenHint()));
					data.add(item);
				}

			}
			jsonObject.element("data", data);
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(null);
	}

	/**
	 * 刪除菜单项 <功能简述> <功能详细描述>
	 * 
	 * @param id
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/deleteMenu.do")
	public ModelAndView deleteMenu(@RequestParam("id")
	String id, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		for (String i : ids) {
			try {
				List<MenuRole> list = menuRoleDao.findByHql(
						"from MenuRole mr where mr.menu.id = ?",
						new Object[] { Long.parseLong(i) });
				if (list != null && list.size() > 0) {
					jsonObject.element("success", false);
					jsonObject.element("message", "'"
							+ list.get(0).getMenu().getSmenCaption()
							+ "'菜单中有相关数据关联不能进行删除！");
					MvcUtil.jsonObjectModelAndView(jsonObject);
				} else {
					menuDao.deleteById(Long.parseLong(i), Menu.class);
					jsonObject.element("success", true);
					jsonObject.element("message", "删除菜单项成功！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element("failure", true);
				jsonObject.element("message", "删除菜单项出错！");
			}
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 添加栏目
	 * 
	 * @return
	 */
	@RequestMapping("/saveMenu.do")
	public ModelAndView saveMenu(Menu menu, @RequestParam("pMenuId")
	String pMenuId) {
		JSONObject jsonObject = new JSONObject();
		try {
			if (null != pMenuId && !"".equals(pMenuId)) {
				Menu pMenu = new Menu();
				pMenu.setId(Long.parseLong(pMenuId));
				menu.setSystemMenu(pMenu);
			}
			if (menu.getId() == null) {
				List<Menu> list = menuDao.findByHql(
						"from Menu m where m.smenCaption = ?",
						new Object[] { menu.getSmenCaption() });
				if (list.size() == 0 && list.isEmpty()) {
					menuDao.save(menu);
					jsonObject.element("success", true);
				} else {
					jsonObject.element("success", false);
					jsonObject.element("msg", "该菜单名已经存在，请选择其他菜单名进行添加！");
				}
			} else {
				menuDao.update(menu);
				jsonObject.element("success", true);
			}
		} catch (Exception e) {
			jsonObject.element("success", false);
			jsonObject.element("msg", "菜单添加失败！");
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 为栏目添加角色
	 * 
	 * @return
	 */
	@RequestMapping("/saveRoleAndMenu.do")
	public ModelAndView saveRoleAndMenu(@RequestParam("menuid")
	String menuId, @RequestParam("roleid")
	String roleId) {
		JSONObject jsonObject = new JSONObject();
		try {

			Menu menu = new Menu();
			Role role = new Role();
			menu.setId(Long.parseLong(menuId));
			role.setId(Long.parseLong(roleId));
			MenuRole menuRole = new MenuRole();
			menuRole.setMenu(menu);
			menuRole.setRole(role);

			List<MenuRole> list = menuRoleDao.findByHql(
					"from MenuRole mr where mr.role.id = ? and mr.menu.id = ?",
					new Object[] { Long.parseLong(roleId),
							Long.parseLong(menuId) });
			if (list.size() == 0 && list.isEmpty()) {
				menuRoleDao.save(menuRole);
				jsonObject.element("success", true);
			} else {
				jsonObject.element("success", false);
				jsonObject.element("msg", "该菜单已经配置了该角色，请选择其他角色进行配置！");
			}
		} catch (Exception e) {
			jsonObject.element("success", false);
			jsonObject.element("msg", "菜单配置角色失败！");
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 查询所有子菜单项
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @param sel_name
	 * @param roleId
	 * @return xiexiaoming 2012-11-29 13:52
	 */
	@RequestMapping("/loadChildMenu.do")
	public ModelAndView loadChildMenuGrid(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("_sel_name")
			String sel_name, @RequestParam("_roleId")
			String roleId) {
		String hql = "from Menu u where u.id != ? and u.systemMenu.id != ? ";
		List<Menu> dataList = null;
		try {
			List valueList = new ArrayList();
			valueList.add(1l);
			valueList.add(1l);
			JSONObject jsonObject = new JSONObject();
			Pagination pagination = new Pagination();
			pagination
					.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
			if (!"".equals(roleId) && roleId != null) {
				if (!"".equals(sel_name) && sel_name != null) {
					hql += " and u.smenCaption like '%"
							+ sel_name
							+ "%' and u.id not in(select mr.menu.id from MenuRole mr where mr.role.id=?)";
				} else {
					hql += " and u.id not in(select mr.menu.id from MenuRole mr where mr.role.id=?)";
				}
				valueList.add(Long.parseLong(roleId));
			}

			dataList = menuDao.findByHql(hql, valueList.toArray(), pagination);

			jsonObject.element("totalCount", pagination.getTotalResults());
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();

			if (dataList.size() > 0) {

				for (Menu field : dataList) {
					JSONObject item = new JSONObject();
					item.element("id", MvcUtil.toJsonString(field.getId()));
					if (field.getSystemMenu() != null
							&& !"".equals(field.getSystemMenu())) {
						item.element("systemMenu_id", MvcUtil
								.toJsonString(field.getSystemMenu().getId()));
						item.element("systemMenu_name", MvcUtil
								.toJsonString(field.getSystemMenu()
										.getSmenCaption()));
					} else {
						item.element("systemMenu_id", "");
						item.element("systemMenu_name", "");
					}
					item.element("resourceURL", MvcUtil.toJsonString(field
							.getResourceURL()));
					item.element("smenIndex", MvcUtil.toJsonString(field
							.getSmenIndex()));
					item.element("smenCaption", MvcUtil.toJsonString(field
							.getSmenCaption()));
					item.element("smenIcon", MvcUtil.toJsonString(field
							.getSmenIcon()));
					item.element("smenHint", MvcUtil.toJsonString(field
							.getSmenHint()));
					data.add(item);
				}

			}
			jsonObject.element("data", data);
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(null);
	}

	/**
	 * 
	 * <功能简述> 加载角色下面关联的菜单项 <功能详细描述>
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @param roleId
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadRoleWithMenu.do")
	public ModelAndView loadRoleWithMenu(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("_roleId")
			String roleId) {
		String hql = "from MenuRole u where 1=1 ";
		if (!"".equals(roleId) && roleId != null) {
			hql += " and u.role.id=" + roleId;
		}
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		try {
			List<MenuRole> dataList = menuRoleDao.findAll(hql, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();

			if (dataList.size() > 0) {

				for (MenuRole field : dataList) {
					JSONObject item = new JSONObject();
					item.element("mId", MvcUtil.toJsonString(field.getMenu()
							.getId()));
					if (field.getMenu().getSystemMenu() != null
							&& !"".equals(field.getMenu().getSystemMenu())) {
						item.element("mPName", MvcUtil.toJsonString(field
								.getMenu().getSystemMenu().getSmenCaption()));
					} else {
						item.element("mPName", "");
					}
					item.element("mURL", MvcUtil.toJsonString(field.getMenu()
							.getResourceURL()));
					item.element("mIndex", MvcUtil.toJsonString(field.getMenu()
							.getSmenIndex()));
					item.element("mCaption", MvcUtil.toJsonString(field
							.getMenu().getSmenCaption()));
					item.element("mIcon", MvcUtil.toJsonString(field.getMenu()
							.getSmenIcon()));
					item.element("mHint", MvcUtil.toJsonString(field.getMenu()
							.getSmenHint()));
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

}
