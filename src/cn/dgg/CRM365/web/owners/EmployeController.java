package cn.dgg.CRM365.web.owners;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.dgg.CRM365.domain.authority.Department;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.owners.Employee;
import cn.dgg.CRM365.domain.owners.Position;
import cn.dgg.CRM365.domain.resources.AutoAssign;
import cn.dgg.CRM365.domain.resourcesManage.ClientDifRecord;
import cn.dgg.CRM365.domain.resourcesManage.XzAllocation;
import cn.dgg.CRM365.util.commonUtil.ExportExcelUtil;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.mvc.view.AbstractMimeView;
import cn.dgg.CRM365.util.mvc.view.MimeBytesView;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;

/**
 * 员工的增删改查、员工的管理 <功能简述> <功能详细描述>
 * 
 * @author chenqin
 * @version [版本号, Aug 10, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */

@SuppressWarnings("all")
@RequestMapping("/employee")
@Controller
public class EmployeController {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Employee> dao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Position> pdao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Department> ddao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<User> udao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object> objDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ClientDifRecord> cddao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<AutoAssign> autoDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<XzAllocation> xaDao;
	
	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage() {
		return new ModelAndView("owners/employee");
	}

	/**
	 * 查找所有员工 <功能简述> <功能详细描述>
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
	@RequestMapping("/loadEmployee.do")
	public ModelAndView loadEmployee(@ModelAttribute("params")
	GridLoadParams gridLoadParams, HttpServletRequest request,
			@RequestParam("conditions")
			String conditions) {
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		User userSession = (User) request.getSession().getAttribute("userSession");
		String hql = "from Employee e where e.delState='"+"0'";
		try {
			StringBuffer wherehql = new StringBuffer();
			if(userSession.getRole().getRoleCode().equals("201202")){
				wherehql.append(" and e.department=").append(userSession.getDepartment().getId());
			}
			if(userSession.getRole().getRoleCode().equals("201203")){
				wherehql.append(" and e.id=").append(userSession.getId());
			}
			if (conditions != null && !"".equals(conditions.trim())) {
				JSONObject jsonObject2 = jsonObject.fromObject(conditions);
				String dcb = String.valueOf(jsonObject2.get("dcb"));
				String pc = String.valueOf(jsonObject2.get("pc"));
				String ct = String.valueOf(jsonObject2.get("ct"));
				String st = String.valueOf(jsonObject2.get("st"));
				String _empName = String.valueOf(jsonObject2.get("_empName"));
				if (!dcb.equals("") && dcb != null) {
					wherehql.append(" and e.department.id =").append(dcb);
				}
				if (!_empName.equals("") && _empName != null) {
					wherehql.append(" and e.name like '").append(_empName)
							.append("%'");
				}
			}
			List<Employee> dataList = dao.findAll(hql + wherehql, pagination);
			jsonObject.element("totalCount", pagination.getTotalResults());
			// 有无数据都要产生data节点
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Object d : dataList) {
					JSONObject item = new JSONObject();
					Employee field = (Employee) d;
					item.element("id", MvcUtil.toJsonString(field.getId()));
					//员工姓名
					item.element("name", MvcUtil.toJsonString(field.getName()));
					//部门id
					item.element("dId", MvcUtil.toJsonString(field.getDepartment().getId()));
					//部门名称
					item.element("dName", MvcUtil.toJsonString(field.getDepartment().getDepaName()));
					//生日
					item.element("birthday", MvcUtil.toJsonString(field.getBirthday()));
					//年龄
					item.element("age", MvcUtil.toJsonString(field.getAge()));
					//手机号码
					item.element("IDcard", MvcUtil.toJsonString(field.getIDcard()));
					//邮箱
					item.element("mailbox", MvcUtil.toJsonString(field.getMailbox()));
					//身份证号码
					item.element("idCardNum", MvcUtil.toJsonString(field.getIdCardNum()));
					//地址
					item.element("address", MvcUtil.toJsonString(field.getAddress()));
					//细数
					item.element("counts", MvcUtil.toJsonString(field.getCounts()));
					//备注
					item.element("remark", MvcUtil.toJsonString(field.getRemark()));
					if("0".equals(field.getSignStatus())){
						//签单状态
						item.element("signStatus", MvcUtil.toJsonString("不接单"));
					}else if("1".equals(field.getSignStatus())){
						//签单状态
						item.element("signStatus", MvcUtil.toJsonString("接单"));
					}
					if("1".equals(field.getSex())){
						//性别
						item.element("sex", MvcUtil.toJsonString("男"));
					}else if("2".equals(field.getSex())){
						//性别
						item.element("sex", MvcUtil.toJsonString("女"));
					}
					if("1".equals(field.getMarriage())){
						//婚姻状态
						item.element("marriage", MvcUtil.toJsonString(field.getMarriage()));
						item.element("marriageName", MvcUtil.toJsonString("已婚"));
					}else if("2".equals(field.getMarriage())){
						//婚姻状态
						item.element("marriage", MvcUtil.toJsonString(field.getMarriage()));
						item.element("marriageName", MvcUtil.toJsonString("未婚"));
					}
					if("1".equals(field.getState())){
						//在职状态
						item.element("state", MvcUtil.toJsonString(field.getState()));
						item.element("stateName", MvcUtil.toJsonString("实习"));
					}else if("2".equals(field.getState())){
						//在职状态
						item.element("state", MvcUtil.toJsonString(field.getState()));
						item.element("stateName", MvcUtil.toJsonString("见习"));
					}else if("3".equals(field.getState())){
						//在职状态
						item.element("state", MvcUtil.toJsonString(field.getState()));
						item.element("stateName", MvcUtil.toJsonString("正式"));
					}
					item.element("counts", MvcUtil.toJsonString(field.getCounts()));
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
	 * 查找所有部门信息 <功能简述> <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadDepartment.do")
	public ModelAndView loadDepartment(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		User user = null;
		try {
			user = (User) request.getSession().getAttribute("userSession");
			String hql = "from Department d";
			StringBuilder sb = new StringBuilder(" where 1=1");
			if("201208".equals(user.getRole().getRoleCode())){
				sb.append(" and d.superId = ").append(user.getDepartment().getSuperId());
			}
			List<Department> dataList = ddao.findAll(hql+sb);
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Department field : dataList) {
					JSONObject item = new JSONObject();
					item.element("departId", MvcUtil.toJsonString(field.getId()));
					item.element("departName", MvcUtil.toJsonString(field.getDepaName()));
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
	 * 查找所有职位信息 <功能简述> <功能详细描述>
	 * 
	 * @param gridLoadParams
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadPosition.do")
	public ModelAndView loadPosition() {
		JSONObject jsonObject = new JSONObject();
		try {
			String hql = "from Position p";
			List<Position> dataList = pdao.findAll(hql);
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Position field : dataList) {
					JSONObject item = new JSONObject();
					item.element("pId", MvcUtil.toJsonString(field.getId()));
					item.element("pName", MvcUtil.toJsonString(field.getName()));
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
	 * 保存和修改员工信息 <功能简述> <功能详细描述>
	 * 
	 * @param employee
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员] xiexiaoming 修改 2012-11-23 11：53
	 */
	@RequestMapping("/saveOrUpdateEmployee.do")
	public ModelAndView saveOrUpdateEmployee(Employee employee, HttpServletRequest request,	@RequestParam("depart") String _depart, 
			@RequestParam("_sign") String _sign,@RequestParam("_sexcomboxType1") String _sexcomboxType1,
			@RequestParam("_marriageCombox") String _marriageCombox,@RequestParam("_stateCombox") String _stateCombox) {
		JSONObject jsonObject = new JSONObject();
		try {
			if (request.getSession() != null) {//设置签单状态（0为不签单、1为签单）
				if("签单".equals(_sign) || "1".equals(_sign)){
					employee.setSignStatus("1");
				}else{
					employee.setSignStatus("0");
				}
				if("男".equals(_sexcomboxType1) || "1".equals(_sexcomboxType1)){
					employee.setSex("1");
				}else{
					employee.setSex("2");
				}
				User userSession = (User) request.getSession().getAttribute(
						"userSession");
				Department dep = new Department();
				dep.setId(Long.parseLong(_depart));
				employee.setDepartment(dep);
				employee.setMarriage(_marriageCombox);
				employee.setState(_stateCombox);
				employee.setDelState("0");
				if ("".equals(employee.getId()) || employee.getId() == null) {
					dao.save(employee);
					jsonObject.element("success", true);
					jsonObject.element("msg", "添加员工信息成功！");
					return MvcUtil.jsonObjectModelAndView(jsonObject);	
				} else {
					dao.update(employee);
					jsonObject.element("success", true);
					jsonObject.element("msg", " 修改员工信息成功！");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", " 保存员工信息失败！");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 删除员工信息 <功能简述> <功能详细描述>
	 * 
	 * @param id
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/deleteEmployee.do")
	public ModelAndView deleteEmployee(@RequestParam("id")String id, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = id.split(",");
		int sum = 0;
		SqlBuilder sb = null;
		try {
			for (String i : ids) {
				List<Object> userId = objDao.findAll("select e.id from User e where e.employee.id="+Long.parseLong(i)+" and e.userDelState='0'");
				List<Object> cuser = new ArrayList<Object>();
				if(userId.size() > 0){
					Long uid = Long.parseLong(userId.get(0).toString());
					cuser = objDao.findAll("select c.id from Client c where c.follower.id="+uid+" and c.clientStatus <> '3'");
					if(cuser.size() == 0){//没有管理客户
						sb = new SqlBuilder("Employee", SqlBuilder.TYPE_UPDATE);
						sb.addField("delState", "1");
						sb.addWhere("id", Long.parseLong(i));
						dao.updateByHQL(sb.getSql(), sb.getParams());
						sb = new SqlBuilder("User", SqlBuilder.TYPE_UPDATE);
						sb.addField("userDelState", "1");
						sb.addField("employee.id", null);
						sb.addWhere("id", uid);
						dao.updateByHQL(sb.getSql(), sb.getParams());
						cddao.updateByHQL("delete from ClientDifRecord cd where cd.userid = ?", new Object[]{uid.toString()});
						autoDao.updateByHQL("delete from AutoAssign where user.id = ?", new Object[]{uid});
						xaDao.updateByHQL("delete from XzAllocation where userid.id = ?", new Object[]{uid});
						sum++;
					 }
				}else{
					sb = new SqlBuilder("Employee", SqlBuilder.TYPE_UPDATE);
					sb.addField("delState", "1");
					sb.addWhere("id", Long.parseLong(i));
					dao.updateByHQL(sb.getSql(), sb.getParams());
					sum++;
				}
			}
			if (sum > 0) {
				jsonObject.element("success", true);
				jsonObject.element("msg", "删除成功，删除了" + sum + "条数据!!!");
			} else {
				jsonObject.element("failure", true);
				jsonObject.element("msg", "该员工有分配的资源，不能被删除!");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 下拉列表查询所以员工信息
	 * 
	 * @param request
	 * @return xiexiaoming 2012-11-29 10:55
	 */
	@RequestMapping("/employeeStore.do")
	public ModelAndView employeeStore(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String hql = "from Employee p where p.delState='0'";
			List<Employee> dataList = dao.findAll(hql);
			JSONArray data = new JSONArray();
			if (dataList.size() > 0) {
				for (Employee field : dataList) {
					JSONObject item = new JSONObject();
					item.element("empId", MvcUtil.toJsonString(field.getId()));
					item.element("empName", MvcUtil.toJsonString(field
							.getName()));
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
	 * <功能简述>通过部门编号取员工 <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/loadEmployees.do")
	public ModelAndView loadEmployees(HttpServletRequest request) {
		JSONObject jObject = new JSONObject();
		String eid = request.getParameter("eid");
		JSONArray data = new JSONArray();
		if (!"".equals(eid)) {
			String hql = "from Employee e where e.department.id=? and e.delState=?";
			try {
				List<Employee> list = dao.findByHql(hql, new Object[] { Long
						.parseLong(eid),"0" });
				for (Employee e : list) {
					JSONObject item = new JSONObject();
					item.element("empId", MvcUtil.toJsonString(e.getId()));
					item.element("empName", MvcUtil.toJsonString(e.getName()));
					data.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jObject.element("data", data);
		return MvcUtil.jsonObjectModelAndView(jObject);
	}
	/**
	 * 导出数据
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * 王玉川
	 * 2012-09-06
	 */
    @RequestMapping("/EmpPortDate.do")
	public ModelAndView exportExcle(HttpServletRequest request) throws UnsupportedEncodingException{
		String hsql="from Employee e where e.delState='"+"0'";
		String _name=request.getParameter("_name");
		String departComboBox1=request.getParameter("_departComboBox1");
	    StringBuffer sb = new StringBuffer();
    	ExportExcelUtil util = new ExportExcelUtil();
    	List<Employee> list = null;
    	if(!_name.equals("") || !"".equals(_name)){
    		sb.append(" and e.name = '").append(_name).append("'");
    	}
    	if(!departComboBox1.equals("") || !"".equals(departComboBox1)){
    		sb.append(" and e.department.id = ").append(Long.parseLong(departComboBox1));
    	}
		try {
			list = dao.findAll(hsql+sb);
		} catch (Exception e) {
			
		}
		if(list.size()>0){
    	String[] headers = new String[]{"员工名字","所属部门","性别","年龄","生日","婚姻状况","在职状态","手机号码","邮箱","身份证号码","地址","分配系数","接单状态","备注"};
    	Map<String, String> configMap = new HashMap<String, String>();
    	configMap.put("员工名字", "name");
    	configMap.put("所属部门", "department.depaName");
    	configMap.put("性别", "sex");
    	configMap.put("年龄", "age");
    	configMap.put("生日", "birthday");
    	configMap.put("婚姻状况", "marriage");
    	configMap.put("在职状态", "state");
    	configMap.put("手机号码", "IDcard");
    	configMap.put("邮箱", "mailbox");
    	configMap.put("身份证号码", "idCardNum");
    	configMap.put("地址", "address");
    	configMap.put("分配系数", "counts");
    	configMap.put("接单状态", "signStatus");
    	configMap.put("备注", "remark");
    	Map<String, Object> model = new HashMap<String, Object>();
    	byte[] bytes;
    	bytes = util.executeExport(list,headers, configMap, "员工管理", "员工管理");
    	model.put(AbstractMimeView.FILE_NAME, "员工管理.xls");
    	model.put(AbstractMimeView.FILE_DATA, bytes);
    	return new ModelAndView(new MimeBytesView(), model);
		}else{
			System.out.println("没有数据可导...");
		}
		return null;
    }
    /**
     * 批量修改员工的状态
     * @param req
     * @param id 员工id集合
     * @param flag 标志改为什么状态（0改为不接单，1改为接单）
     * @return
     */
    @RequestMapping("/change.do")
    public ModelAndView change(HttpServletRequest req, @RequestParam("id")String eid, @RequestParam("flag")String flag){
    	JSONObject json = new JSONObject();
    	SqlBuilder sb = null;
    	try {
			if(!"".equals(eid) && eid != null){
				String[] ids = eid.split(",");
				for(String id : ids){
					sb = new SqlBuilder("Employee", SqlBuilder.TYPE_UPDATE);
					if("0".equals(flag)){
						sb.addField("signStatus", "0");//不签单
					}else{
						sb.addField("signStatus", "1");//签单
					}
					sb.addWhere("id", Long.parseLong(id));
					dao.updateByHQL(sb.getSql(), sb.getParams());
				}
				json.element("success", true);
				json.element("msg", "修改成功！");
			}
		} catch (Exception e) {
			json.element("failure", true);
			json.element("msg", "系统错误，请联系管理员!");
			e.printStackTrace();
			return MvcUtil.jsonObjectModelAndView(json);
		}
		return MvcUtil.jsonObjectModelAndView(json);
    }
}
