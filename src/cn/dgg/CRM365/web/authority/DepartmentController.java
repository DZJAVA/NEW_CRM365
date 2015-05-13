package cn.dgg.CRM365.web.authority;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.authority.Department;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;



/**
 * 公司管理 <功能简述> <功能详细描述>
 * 
 * @author chenqin
 * @version [版本号, Aug 9, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */

@RequestMapping("/tt")
@Controller
public class DepartmentController {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Department> dao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Department> ddao;

	@RequestMapping("/cc.do")
	public ModelAndView jumpPage(HttpServletRequest request) {
		String menuId = request.getParameter("menu_id");
		System.out.println(menuId);
		return new ModelAndView("authority/departmentInfo");
	}

	/**
	 * 查询所有部门信息 <功能简述> <功能详细描述>
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @param conditions
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadDepartment.do")
	public ModelAndView loadDepartment(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("conditions") String conditions) {
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		String hql = "from Department d ";
		StringBuffer wherehql = new StringBuffer("where 1=1");
		User user = null;
		try {
			user = (User) request.getSession().getAttribute("userSession");
			if (conditions != null && !"".equals(conditions.trim())) {
				JSONObject jsonObject2 = JSONObject.fromObject(conditions);
				String dn = String.valueOf(jsonObject2.get("dn"));
				if (!dn.equals("") && dn != null) {
					wherehql.append(" and d.depaName like '").append(dn).append("%'");
				}
			}
			if("201208".equals(user.getRole().getRoleCode())){
				wherehql.append(" and d.superId = ").append(user.getDepartment().getSuperId());
			}
			List<Department> dataList = dao.findByHql(hql + wherehql, null,
					pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();

			if (dataList.size() > 0) {
				JSONObject item = null;
				for (Department field : dataList) {
					item = new JSONObject();
					item.element("id", MvcUtil.toJsonString(field.getId()));
					item.element("depaName", MvcUtil.toJsonString(field.getDepaName()));
					item.element("depaNotes", MvcUtil.toJsonString(field.getDepaNotes()));
					if("0".equals(field.getOrderStatus())){
						item.element("orderStatus", MvcUtil.toJsonString("不接单"));
					}else if("1".equals(field.getOrderStatus())){
						item.element("orderStatus", MvcUtil.toJsonString("接单"));
					}
					if(field.getDepa() != null){
						item.element("superId", MvcUtil.toJsonString(field.getDepa().getId()));
						item.element("superName", MvcUtil.toJsonString(field.getDepa().getDepaName()));
					}
					item.element("remark", MvcUtil.toJsonString(field.getRemark()));
					data.add(item);
				}
			}
			jsonObject.element("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	
	@RequestMapping("/loadDepartments.do")
	public ModelAndView loadDepartments( HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String hql = "select new Department(d.id, d.depaName, d.superId) from Department d ";
		try {
			List<Department> dataList = dao.findAll(hql);
			jsonObject.element("data", dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 新增和修改部门信息
	 * 
	 * @param department
	 * @param request
	 * @param _tboxType
	 * @param _deComboBox
	 * @param multipartFile
	 * @return
	 */
	@RequestMapping("/qq.do")
	public ModelAndView saveOrUpdatePosition(Department department, HttpServletRequest request,
			@RequestParam("superId") Long superDept, @RequestParam("super_Id") int superId) {
		JSONObject jsonObject = new JSONObject();
		String status = request.getParameter("status");
		if(!"".equals(status) && status != null){
			department.setOrderStatus(status);
		}
		boolean flag = true;
		try {
			String hql = "from Department d where d.depaName='"+department.getDepaName()+"'";
			List<Department> ds = dao.findAll(hql);
			if (request.getSession() != null) {
				if(superDept > 0){
					Department dept = new Department();
					dept.setId(superDept);
					department.setDepa(dept);
					department.setSuperId(superId);
					flag = false;
				}
				if (department.getId() == null) {
					if (ds.size() == 0) {
						dao.save(department);
						if(flag){
							SqlBuilder sb = new SqlBuilder("Department", SqlBuilder.TYPE_UPDATE);
							sb.addField("superId", department.getId().intValue());
							sb.addWhere("id", department.getId());
							dao.updateByHQL(sb.getSql(), sb.getParams());
						}
						jsonObject.element("success", true);
						jsonObject.element("msg", "添加部门信息成功！");
						return MvcUtil.jsonObjectModelAndView(jsonObject);
					} else {
						jsonObject.element("failure", true);
						jsonObject.element("msg", "添加部门信息失败，该部门已存在！");
						return MvcUtil.jsonObjectModelAndView(jsonObject);
					}
				}else{
					if(ds.size()>0){
						if(department.getId().equals(ds.get(0).getId())){
							dao.update(department);
							jsonObject.element("success", true);
							jsonObject.element("msg", " 修改部门信息成功！");
							return MvcUtil.jsonObjectModelAndView(jsonObject);
						}else{
							jsonObject.element("failure", true);
							jsonObject.element("msg", "添加部门信息失败,改部门已存在！");
							return MvcUtil.jsonObjectModelAndView(jsonObject);	
						}
					}else{
							dao.update(department);
							jsonObject.element("success", true);
							jsonObject.element("msg", " 修改部门信息成功！");
							return MvcUtil.jsonObjectModelAndView(jsonObject);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", " 保存公司信息失败！");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}


	/**
	 * 根据楼盘ID查找该楼盘下所有委员会成员信息，跳转页面 <功能简述> <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return ICommonDAO<Department> [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadfindoid")
	public ModelAndView loadfindoid(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			HttpServletResponse response) {
		String lpid = request.getParameter("lpid");
		return new ModelAndView("owners/ownersListInfo", lpid, null);
	}



	/**
	 * 删除部门信息 <功能简述> <功能详细描述>
	 * 
	 * @param id
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/deleteDepartment.do")
	public ModelAndView deleteDepartment(@RequestParam("id")
	String id) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		int sum = 0;
		for (String i : ids) {
			try {
				dao.deleteById(Long.parseLong(i), Department.class);
				sum++;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		if (sum > 0) {
			jsonObject.element("success", true);
			jsonObject.element("msg", "删除成功，删除了" + sum + "条数据!!!");
		} else {
			jsonObject.element("failure", false);
			jsonObject.element("msg", "删除部门信息出错,部门信息已被引用，不能进行删除！");

		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 取出该楼盘信息 <功能简述> <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return ICommonDAO<Department> [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/findDeparment")
	public ModelAndView findDeparment(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			HttpServletResponse response) {
		String lpid = request.getParameter("deparementId");
		JSONObject jsonObject = new JSONObject();
		String hql = "from Department d where d.id='" + Long.parseLong(lpid)
				+ "'";
		try {
			List<Department> dataList = dao.findAll(hql);
			// 有无数据都要产生data节点
			if (dataList.size() > 0) {
				for (Object d : dataList) {
					Department field = (Department) d;
					find_deparment(jsonObject, field);
				}
			}
			jsonObject.element("success", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	// 填充返回数据
	private void find_deparment(JSONObject jsonObject, Department commandObject) {
		JSONObject data = new JSONObject();

		data.element("id", MvcUtil.toJsonString(commandObject.getId()));
		data.element("deparmentName", MvcUtil.toJsonString(commandObject
				.getDepaName()));
		data.element("remark", MvcUtil.toJsonString(commandObject.getRemark()));
		jsonObject.element("data", data);
	}

	public ICommonDAO<Department> getDao() {
		return dao;
	}

	public void setDao(ICommonDAO<Department> dao) {
		this.dao = dao;
	}
}
