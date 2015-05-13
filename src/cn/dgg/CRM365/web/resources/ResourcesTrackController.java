package cn.dgg.CRM365.web.resources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import cn.dgg.CRM365.domain.authority.Department;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.resources.AddClient;
import cn.dgg.CRM365.domain.resources.Eliminate;
import cn.dgg.CRM365.domain.resources.EliminateClient;
import cn.dgg.CRM365.domain.resources.ResourcesTrack;
import cn.dgg.CRM365.domain.resources.SignClient;
import cn.dgg.CRM365.domain.resourcesManage.Client;
import cn.dgg.CRM365.domain.resourcesManage.ClientUser;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.commonUtil.StringUtil;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;
import cn.dgg.CRM365.web.resource.ClientController;


/**
 * 资源列表 <功能简述> <功能详细描述>
 * 
 * @author wangqiang
 * @version [版本号, Aug 12, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 资源列表
 * 资源跟踪  
 * 新增资源跟踪, 
 * 编辑资源跟踪,
 * 淘汰资源,
 * 提出资源,
 * 确定已签单 .
 */

@SuppressWarnings("all")
@RequestMapping("/ResourcesTrack")
@Controller
public class ResourcesTrackController {
	

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<ResourcesTrack> rtDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<User> uDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Client> cDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Department> dDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Eliminate> eliDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object> odao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object[]> objdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<EliminateClient> eliClientDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<AddClient> addDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<SignClient> signDao;
	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage() {
		return new ModelAndView("resourcestrack/resourcestrack");
	}
	/**
	 * 修改签单状态为已签单
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/updateStatus")
	public ModelAndView updateStatus(@RequestParam("s_id") String s_id,HttpServletRequest req
			,@RequestParam("s_remark") String s_remark,@RequestParam("s_qdoppType") String s_qdoppType,@RequestParam("s_loanAmount11") String s_loanAmount11){
		JSONObject jsonObject = new JSONObject();
		String now = StaticValues.format.format(new Date());
		User user = null;
		try{
			if(req.getSession() != null){
				user = (User) req.getSession().getAttribute("userSession");
				if(!"".equals(s_id) && s_id != null){
					Client client = new Client();
					client.setId(Long.parseLong(s_id));
					SignClient sc = new SignClient();
					sc.setSignTime(now);//签单时间
					sc.setUser(user);//签单人
					sc.setClient(client);//签单客户
					sc.setRemark(s_remark);//签单备注
					SqlBuilder sb = new SqlBuilder("Client", SqlBuilder.TYPE_UPDATE);
					sb.addField("clientStatus", "1");
					sb.addField("signTime", now);
					sb.addField("oppType", s_qdoppType);
					sb.addField("loanAmount", s_loanAmount11);
					sb.addWhere("id", Long.parseLong(s_id));
					cDao.updateByHQL(sb.getSql(), sb.getParams());
					signDao.save(sc);
					jsonObject.element("success", true);
					jsonObject.element("msg", " 签单成功!");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("faliure", true);
			jsonObject.element("msg", "签单失败,请重新登录或联系管理员!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 添加或修改资料跟踪
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/saveOrUpdateResourcesTrack")
	public ModelAndView saveOrUpdateResourcesTrack(HttpServletRequest request,ResourcesTrack rtrack,@RequestParam("_cpid") String cid
			,@RequestParam("gen") String gen,@RequestParam("_cs") String _cs){
		JSONObject jsonObject = new JSONObject();
		try{
			Client client = new Client();
			client.setId(Long.parseLong(cid));//保存资料ID
			rtrack.setClient_name(client);//保存资料ID
			User userSession = (User)request.getSession().getAttribute(StaticValues.USER_SESSION);
			rtrack.setResourcespeople(userSession);//跟踪人
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String nowTime = sdf.format(new Date());
			rtrack.setResourcestime(nowTime);//跟踪时间
			rtrack.setTypes(gen);
			if(request.getSession() != null){
				SqlBuilder sb = new SqlBuilder("Client", SqlBuilder.TYPE_UPDATE);
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				sb.addField("editTime", sdf.format(new Date()));
				sb.addField("signPossible", _cs);
				sb.addField("workPlanNewTime", rtrack.getPlantime());
				sb.addWhere("id", Long.parseLong(cid));
				if(_cs.equals("1") || _cs.equals("100%")){
					_cs = "1";
				}else if(_cs.equals("2") || _cs.equals("80%")){
					_cs = "2";
				}else if(_cs.equals("3") || _cs.equals("50%")){
					_cs = "3";
				}else if(_cs.equals("4") || _cs.equals("10%")){
					_cs = "4";
				}else if(_cs.equals("5") || _cs.equals("0%")){
					_cs = "5";
				}
				if ("".equals(rtrack.getRtid()) || rtrack.getRtid() == null) {
					cDao.updateByHQL(sb.getSql(), sb.getParams());
					rtDao.save(rtrack);
					jsonObject.element("success", true);
					jsonObject.element("msg", " 保存成功!");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}else{
					if(gen.equals("1") || gen.equals("已上门")){
						rtrack.setTypes("1");
					}if(gen.equals("2") || gen.equals("未上门")){
						rtrack.setTypes("2");
					}
					cDao.updateByHQL(sb.getSql(), sb.getParams());
					rtDao.update(rtrack);
					jsonObject.element("success", true);
					jsonObject.element("msg", " 修改成功!");
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "保存失败!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);                   
	}
	
	/**
	 * 根据分配签单记录查询该记录下的  资料跟踪明细
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 资料跟踪明细
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/findByTrackrecord")
	private ModelAndView findByTrackrecord(@RequestParam("_cpid") String cid,@ModelAttribute("params") GridLoadParams gridLoadParams,
			HttpServletRequest request,@RequestParam("conditions")String conditions){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		User user = null;
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		try {
			if(request.getSession() != null){
				String nowTime = StaticValues.sdf.format(new Date());
				StringBuffer sb = new StringBuffer();
				if("".equals(cid) || cid == null){
					jsonObject.element("data", "");
				}else{
					user = (User)request.getSession().getAttribute(StaticValues.USER_SESSION);
					String[] cids = cid.split(",");
					String hql="from ResourcesTrack rt";
					if(cids.length == 2){
						sb.append(" where rt.client_name.id =").append(Long.parseLong(cids[0]));
					}else{
						sb.append(" where rt.client_name.id =").append(Long.parseLong(cid));
					}
					sb.append(" ORDER by rt.plantime DESC");
					List<ResourcesTrack> rtrackList = rtDao.findAll(hql+sb,pagination);
					jsonObject.element("totalCount", pagination.getTotalResults());
					JSONArray data = new JSONArray();
					if (rtrackList.size() > 0) {
						for (ResourcesTrack field : rtrackList) {
							JSONObject item = new JSONObject();
							item.element("rtid", MvcUtil.toJsonString(field.getRtid()));
							item.element("resourcescontent", MvcUtil.toJsonString(field.getResourcescontent()));//跟踪内容
							item.element("resourcestime", MvcUtil.toJsonString(field.getResourcestime()));//跟踪时间
							item.element("plantime", MvcUtil.toJsonString(field.getPlantime()));//计划工作时间
							user = field.getResourcespeople();
							if(user != null){
								item.element("resourcespeople_name", MvcUtil.toJsonString(user.getUserName()));//跟踪人
							}else{
								item.element("resourcespeople_name", MvcUtil.toJsonString("无跟踪人员"));//跟踪人
							}
							item.element("intoasinglerate", MvcUtil.toJsonString(field.getIntoasinglerate()));//成单率
							item.element("workplan", MvcUtil.toJsonString(field.getWorkplan()));//工作计划
							item.element("calltime", MvcUtil.toJsonString(field.getCalltime()));//上门时间
							if("1".equals(field.getTypes())){
								item.element("types", MvcUtil.toJsonString("已上门"));////类型（1已上门，2未上门）
							}else if("2".equals(field.getTypes())){
								item.element("types", MvcUtil.toJsonString("未上门"));////类型（1已上门，2未上门）
							}
							item.element("remark", MvcUtil.toJsonString(field.getRemark()));//备注
							data.add(item);
						}
					}
					jsonObject.element("data", data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 截取电话号码
	 */
	public static void hideNum(String code, String field, Object content, JSONObject item){
		if("".equals(content) || content==null){
			item.element(field, MvcUtil.toJsonString(""));//联系电话
		}else{
			if(code.equals("201201")){
				item.element(field, MvcUtil.toJsonString(content));
			}else{
				String con = content.toString();
				if(con.length()>=4){
					item.element(field, MvcUtil.toJsonString(con.substring(0, con.length()-4)+"****"));
				}else{
					item.element(field, MvcUtil.toJsonString(con));
				}
			}
		}
	}
	
	public static void hideDeptNum(JSONObject item, String[] fields, String[] contents, String roleCode, Long uid, Long followId){
		int i = 0;
		if("201202".equals(roleCode)){
			if(uid.intValue() == followId.intValue()){
				for(String field : fields){
					item.element(field, MvcUtil.toJsonString(contents[i++]));
				}
			}else{
				String content;
				for(String field : fields){
					content = contents[i++];
					if(content != null && !"".equals(content)){
						item.element(field, MvcUtil.toJsonString(content.substring(0, content.length()-4)+"****"));
						continue;
					}
					item.element(field, MvcUtil.toJsonString(content));
				}
			}
		}else{
			for(String field : fields){
				item.element(field, MvcUtil.toJsonString(contents[i++]));
			}
		}
	}
	/**
	 * 客户查看淘汰方法
	 * 所有人可以才看到所有淘汰的客户
	 * 高级搜索
	 * 
	 */
	@RequestMapping("/loadEliminateClient.do")
	public ModelAndView loadClient(@ModelAttribute("params") GridLoadParams gridLoadParams,
			HttpServletRequest request, @RequestParam("conditions") String _condition){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		User userSession = (User) request.getSession().getAttribute("userSession");
		String nowTime = StaticValues.sdf.format(new Date());//当前时间
		String hql = "from EliminateClient c";
		StringBuffer sb = new StringBuffer(" where 1=1");
		try {
				if(_condition != null && !"".equals(_condition)){//查询条件
					JSONObject json = JSONObject.fromObject(_condition);
					ClientController.connectConditions(json, sb, nowTime, 0);//拼接查询条件
					String _createStartttzytime = String.valueOf(json.get("_createStartttzytime"));//淘汰开始日期
					String _createEndttzytime = String.valueOf(json.get("_createEndttzytime"));//淘汰结束日期
					String nameOrNum = json.getString("nameOrNum");//客户名字
					String _provinces1 = json.getString("_provinces1");//省份
					String _citys1 = json.getString("_citys1");//城市
					String[] dates = nowTime.split("-");
					if(!"".equals(nameOrNum) && nameOrNum != null){
						sb.append(" and c.clientName like '").append(nameOrNum).append("%'");
					}
					//淘汰时间
					if(!"".equals(_createStartttzytime)){
						sb.append(" and c.eliTime >= '").append(_createStartttzytime).append("'");
					}
					if(!"".equals(_createEndttzytime)){
						sb.append(" and c.eliTime <= '").append(_createEndttzytime).append("'");
				    }
					if(_provinces1 != null && !"".equals(_provinces1)){
						sb.append(" and c.province = ").append(_provinces1);
					}
					if(_citys1 != null && !"".equals(_citys1)){
						sb.append(" and c.city = ").append(_citys1);
					}
				}
				List<EliminateClient> rtList = eliClientDao.findByHql(hql + sb,null,pagination);
				jsonObject.element("totalCount", pagination.getTotalResults());
				// 有无数据都要产生data节点
				JSONArray data = new JSONArray();
				if (rtList.size() > 0) {
					for (EliminateClient field : rtList) {
						JSONObject item = new JSONObject();
						item.element("id", MvcUtil.toJsonString(field.getId()));
						item.element("clientName", MvcUtil.toJsonString(field.getClientName()));//客户姓名
						item.element("loanAmount", MvcUtil.toJsonString(field.getLoanAmount()));//贷款金额
						item.element("clientAdd", MvcUtil.toJsonString(field.getClientAdd()));//客户地址
						item.element("clientStatus", MvcUtil.toJsonString(field.getClientStatus()));//客户状态(1为已签单、2为未签单、3为淘汰)
						if("1".equals(field.getSignPossible())){//签单可能性(1为100%、2为80%、3为50%、4为0%)
							item.element("signPossible", MvcUtil.toJsonString("100%"));
						}else if("2".equals(field.getSignPossible())){
							item.element("signPossible", MvcUtil.toJsonString("80%"));
						}else if("3".equals(field.getSignPossible())){
							item.element("signPossible", MvcUtil.toJsonString("50%"));
						}else if("4".equals(field.getSignPossible())){
							item.element("signPossible", MvcUtil.toJsonString("0%"));
						}
						item.element("assignDate", MvcUtil.toJsonString(field.getAssignDate()));//分配日期
						item.element("assignTime", MvcUtil.toJsonString(field.getAssignTime()));//分配时间
						item.element("remark", MvcUtil.toJsonString(field.getRemark()));//备注
						if(field.getClientSourse() != null){
							item.element("clientSource", MvcUtil.toJsonString(field.getClientSourse().getName()));//客户来源
						}
						String ruleDe = null;	//得到当前登录人的角色代码 
						ruleDe = userSession.getRole().getRoleCode();//得到角色代码
						hideNum(ruleDe, "contactTel", field.getContactTel(), item);//截取电话号码
						hideNum(ruleDe, "spareTel1", field.getSpareTel1(), item);
						hideNum(ruleDe, "spareTel2", field.getSpareTel2(), item);
						if(field.getFollower() != null){
							item.element("emp_name",MvcUtil.toJsonString(field.getFollower().getDepartment().getDepaName()+"："
									+field.getFollower().getUserName()));//员工姓名(上一次跟单人)
						}else{
							item.element("emp_name", "无跟踪人员");
						}
						item.element("eliTime", MvcUtil.toJsonString(field.getEliTime()));//淘汰时间
						String oppTypes = "";//贷款类型
						String dkje = "";//贷款金额
						String khmz = "";//客户名字
						if("1".equals(field.getOppType())){//商机类型(1为房贷、2为信贷、3为短借、4为企贷)
							oppTypes="房贷";
						}else if("2".equals(field.getOppType())){
							oppTypes="信贷";
						}else if("3".equals(field.getOppType())){
							oppTypes="短借";
						}else if("4".equals(field.getOppType())){	
							oppTypes="企贷";
						}
						if(!"".equals(field.getLoanAmount()) && field.getLoanAmount() != null){
							dkje=field.getLoanAmount();
						}
						if(!"".equals(field.getClientName()) && field.getClientName() != null){
							khmz=field.getClientName();
						}
						item.element("khxinx", MvcUtil.toJsonString(khmz+oppTypes+dkje));//客户信息
						item.element("city1", MvcUtil.toJsonString(field.getCity()));
						item.element("province1",MvcUtil.toJsonString(field.getProvince()));
						item.element("proCity1", MvcUtil.toJsonString(field.getProvince() +"," +field.getCity()));//省份 城市
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
	 * 点击淘汰资源按钮 
	 * 弹出 层。必须填写备注
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/saveFailureResource")
	public ModelAndView saveFailureResource(HttpServletRequest req){
		JSONObject jsonObject = new JSONObject();
		String cid = req.getParameter("cid");
		String remark = req.getParameter("eli");//淘汰备注
		try{
			if(req.getSession() != null){
				User user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				String hql = "select e.id from Eliminate e where e.client.id= "+ Long.parseLong(cid);//得到当前资源是不是已经被淘汰过一次
				List<Object> clientUsers = odao.findAll(hql);
				//如果size>0则表示该条信息被淘汰过
				if(clientUsers.size() >0){
					hql = "select max(a.addTime) from AddClient a where a.client.id = " + Long.parseLong(cid);//查出添加时间
					List<Object> addList = odao.findAll(hql);
					if(addList.size() > 0){
						Object o = addList.get(0);
						if(o != null){//添加的客户淘汰
							String addTime = addList.get(0).toString();//得到添加为自己的时间
							String nowTime = StaticValues.sForomat.format(new Date());//当前时间
							String[] dates = addTime.split(" +");
							Calendar cal = Calendar.getInstance();
							cal.setTime(StaticValues.sdf.parse(dates[0]));
							cal.add(Calendar.DATE, 15);
							addTime = StaticValues.sForomat.format(cal.getTime());//领取15天之后的时间
							if(Long.parseLong(nowTime) <Long.parseLong(addTime)){//领取时间没有超过15天
								jsonObject.element("success", true);
								jsonObject.element("msg", "该客户已被淘汰过，并且离领取时间没有达到15天，不能淘汰！");
								return MvcUtil.jsonObjectModelAndView(jsonObject);
							}else{//超过15天
								eliminateClient(jsonObject, cid, user, remark);
								return MvcUtil.jsonObjectModelAndView(jsonObject);
							}
						}else{//将淘汰的客户分配出去后的淘汰
							eliminateClient(jsonObject, cid, user, remark);
						}
					}
				}else{
					eliminateClient(jsonObject, cid, user, remark);
					return MvcUtil.jsonObjectModelAndView(jsonObject);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "操作失败!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 淘汰客户
	 * @param json
	 * @param cid 客户id
	 * @param user 淘汰人
	 * @param remark 淘汰备注
	 * @throws Exception
	 */
	public void eliminateClient(JSONObject json, String cid, User user, String remark) throws Exception{
		String now = StaticValues.simple.format(new Date());
		Eliminate eli = new Eliminate();
		Client client = new Client();
		client.setId(Long.parseLong(cid));
		eli.setClient(client);
		eli.setUser(user);
		eli.setEliTime(now);
		eli.setRemark(remark);
		SqlBuilder sb = new SqlBuilder("Client",SqlBuilder.TYPE_UPDATE);
		sb.addField("clientStatus", "3");
		sb.addField("eliTime", now);
		sb.addField("editTime", now.split(" +")[0]);
		sb.addWhere("id", Long.parseLong(cid));
		cDao.updateByHQL(sb.getSql(), sb.getParams());
		eliDao.save(eli);
		json.element("success", true);
		json.element("msg", "该客户淘汰成功!");
	}
	/**
	 * 进入查看淘汰资料时候 点击添加资源。添加为自己的资源
	 * 点击添加资源按钮 
	 * 弹出 层。必须填写备注
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/saveAddResource")
	public ModelAndView saveAddResource(HttpServletRequest req){
		JSONObject jsonObject = new JSONObject();
		String lostTime = null;
		String now = StaticValues.sForomat.format(new Date());
		String id = req.getParameter("id");//客户id
		String addRemark = req.getParameter("add");//添加备注
		User userSession = null;
		try{
			if(req.getSession() != null){
				userSession = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
				lostTime = getTime(req.getParameter("eli").split(" +")[0]);
				if(Long.parseLong(lostTime) > Long.parseLong(now)){
					jsonObject.element("failure", true);
					jsonObject.element("msg", "添加失败,改淘汰资源还未过三个月！");
				}else{
					addSourse(jsonObject, userSession.getId(), id, addRemark);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "添加失败，重新登录或联系管理员!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	/**
	 * 成功添加资源
	  *<功能简述>
	  *<功能详细描述>
	  * @param json
	  * @param id
	  * @param tcid
	  * @param cusers
	  * @return
	  * @throws Exception [参数说明]
	  * 
	  * @return JSONObject [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public void addSourse(JSONObject json, Long id, String tcid, String remark) throws Exception{
		AddClient add = new AddClient();
		String now = StaticValues.format.format(new Date());
		add.setAddTime(now);//添加时间
		Client client = new Client();
		client.setId(Long.parseLong(tcid));
		add.setClient(client);//添加客户
		User user = new User();
		user.setId(id);
		add.setUser(user);//添加用户
		add.setRemark(remark);//添加备注
		SqlBuilder sqlBuilder = new SqlBuilder("Client",SqlBuilder.TYPE_UPDATE);
		sqlBuilder.addField("clientStatus", "2");
		sqlBuilder.addField("assignTime", now);
		sqlBuilder.addField("editTime", null);
		sqlBuilder.addField("follower.id", id);
		sqlBuilder.addWhere("id", Long.parseLong(tcid));
		cDao.updateByHQL(sqlBuilder.getSql(), sqlBuilder.getParams());
		addDao.save(add);
		json.element("success", true);
		json.element("msg", "该客户添加成功!请查看!");
	}
	/**
	 * 取三个月之后的时间
	  *<功能简述>
	  *<功能详细描述>
	  * @param date
	  * @return [参数说明]
	  * 
	  * @return String [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	public static String getTime(String date) throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.setTime(StaticValues.sdf.parse(date));
		cal.add(Calendar.MONTH, 3);
		return StaticValues.sForomat.format(cal.getTime());
	}
	/**
	 * 淘汰客户
	 * 选择淘汰客户
	 * 显示跟踪记录
	 */
	@RequestMapping("/findTrackrecorAction")
	private ModelAndView findTrackrecorAction(@RequestParam("tcid") String tcid,@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request,@RequestParam("conditions")String conditions){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		User user = null;
		if("".equals(tcid) || tcid == null){
			jsonObject.element("data", "");
		}else{
			String hql="from ResourcesTrack rt where rt.client_name.id ='"+ tcid +"'";
			StringBuffer sb = new StringBuffer(" and 1=1");
			sb.append(" and rt.resourcestime!=''");
			try{
				List<ResourcesTrack> rtrackList = rtDao.findAll(hql+sb,pagination);
				jsonObject.element("totalCount", pagination.getTotalResults());
				JSONArray data = new JSONArray();
				if (rtrackList.size() > 0) {
					for (ResourcesTrack field : rtrackList) {
						JSONObject item = new JSONObject();
						item.element("gzid", MvcUtil.toJsonString(field.getRtid()));
						item.element("gzresourcescontent", MvcUtil.toJsonString(field.getResourcescontent()));//跟踪内容
						item.element("gzresourcestime", MvcUtil.toJsonString(field.getResourcestime()));//跟踪时间
						user = field.getClient_name().getFollower();
						if(user != null){
							item.element("gzresourcespeople_name", MvcUtil.toJsonString(user.getUserName()));//跟踪人
						}
						item.element("gzcalltime", MvcUtil.toJsonString(field.getCalltime()));//上门时间
						if("1".equals(field.getTypes())){
							item.element("gztypes", MvcUtil.toJsonString("已上门"));////类型（1已上门，2未上门）
						}else if("2".equals(field.getTypes())){
							item.element("gztypes", MvcUtil.toJsonString("未上门"));////类型（1已上门，2未上门）
						}
						data.add(item);
					}
				}
				jsonObject.element("data", data);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
}
