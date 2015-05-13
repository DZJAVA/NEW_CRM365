package cn.dgg.CRM365.web.authority;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.authority.Log;
import cn.dgg.CRM365.domain.authority.Role;
import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.commonUtil.StringUtil;
import cn.dgg.CRM365.util.commonUtil.loadingVerificationCodeUtil;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;

/**
 * 用户登录
 * 
 * @author 王科（小）
 * @param <V>
 * 
 */
@Controller
public class LoginController<V> {

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<User> dao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object[]> objDao;

	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Log> logDao;

	/***************************************************************************
	 * 获取登录IP
	 * 
	 * @param request
	 * @return
	 */
	private static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == "0:0:0:0:0:0:1" || "0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	
	  /**
	    * 获取登录IP
	    * @return
	    * 王科(大)
	    * 2012-11-14
	    */
	   private static String getIpAddress(){   
	       InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}   
	       return address.getHostAddress();   
	   }  
	/**
	 * 当前登录人，保存到session
	 * 
	 * @param user
	 * @param request
	 *            王科(大) 2012-11-13
	 */
	private void saveInfoToSession(User user, HttpServletRequest request) {
		request.getSession().setAttribute(StaticValues.USER_SESSION, user);
	}
	/**
	 * 修改密码
	 * @author 王玉川
	 * @param request
	 * @param user 页面封装的数据对象
	 * @param pwd 原始密码
	 * @param _pwd2 确认密码
	 * @param _idCardNum 身份证号码
	 * @param _mailbox 邮箱
	 * @param _qfpwd 区分密码修改状态
	 * @param sd 区分该用户是月份登录还是次数登录		
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updataPwd.do")
	public ModelAndView updataPwd(HttpServletRequest req,
			@RequestParam("_pwd") String pwd,@RequestParam("_pwd2") String _pwd2) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		User user = (User)req.getSession().getAttribute(StaticValues.USER_SESSION);
		if(pwd.equals(user.getPassword())){
			SqlBuilder empsb = new SqlBuilder("User", SqlBuilder.TYPE_UPDATE);
			empsb.addField("password", _pwd2);
			empsb.addWhere("id", user.getId());
			dao.updateByHQL(empsb.getSql(), empsb.getParams());
			jsonObject.element("success", true);
			jsonObject.element("msg", "修改密码成功!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}else{
			jsonObject.element("failure", true);
			jsonObject.element("msg", "原始密码不对");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public ModelAndView loginSystem(HttpServletRequest request, User user)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		if(!user.getRemark().equalsIgnoreCase(request.getSession().getAttribute("randomCode").toString())){
			jsonObject.element("failure", true);
			jsonObject.element("msg", "输入错误,请从新输入!");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		List<Object[]> list = objDao.findAll("select u.id, u.userName, u.loginId, u.password, u.role," +
				"u.department.id, u.department.depaName, u.department.superId from User u where u.loginId='"+user.getLoginId()+"' and u.password='"+user.getPassword()+"' and u.isOrNotEnable=2");
		if (list.size() > 0) {
			Object[] obj = list.get(0);
			User loginUser = new User(Long.parseLong(obj[0].toString()), obj[1].toString(), obj[2].toString(),obj[3].toString(),
					(Role)obj[4], Long.parseLong(obj[5].toString()), obj[6].toString(), obj[7] == null ? 0: Integer.parseInt(obj[7].toString()));
			saveInfoToSession(loginUser, request);
			Log log = new Log();
		    log.setIp(LoginController.getIpAddress());
			log.setLoginId(loginUser.getLoginId().toString());
			log.setLogTime(new SimpleDateFormat(StringUtil.YMDHMS)
					.format(new Date()));
			log.setUserName(loginUser.getUserName());
			logDao.save(log);// 保存登录日志
			jsonObject.element("success", true);
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		jsonObject.element("failure", true);
		jsonObject.element("msg", "输入错误,请从新输入!");
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	/**
	 * 加载验证码
	 * 
	 * @param request
	 * @param mshUser
	 * @return 王科(大) 2012-10-23
	 */
	@RequestMapping(value = "/loginyanzhengma.do")
	public void loadingVerificationCode(HttpServletRequest request,
			HttpServletResponse response) {

		BufferedImage bais = loadingVerificationCodeUtil.createImage();
		// 禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的响应是图片
		response.setContentType("image/jpeg");
		request.getSession().removeAttribute("randomCode");// 销毁验证码
		request.getSession().setAttribute("randomCode",
				loadingVerificationCodeUtil.strCode);// 把验证码存到session
		try {
			ImageIO.write(bais, "JPEG", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 欢迎界面
	 * 
	 * @return
	 */
	@RequestMapping("/to_welcome")
	public ModelAndView to_welcome(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request,
			HttpServletResponse response) {
		String _theCurrent = null;
		String _cpid = request.getParameter("_cpid");
		if(request.getSession() != null){
			User user = (User)request.getSession().getAttribute(StaticValues.USER_SESSION);
			String roleCode = user.getRole().getRoleCode();//角色代码
			if("201204".equals(roleCode)){//根据角色的不同权限跳往不同的页面
				return new ModelAndView("authority/userInfo");
			}else if("201206".equals(roleCode)){
				return new ModelAndView("resourcestrack/resourcestrack");
			}else if("201207".equals(roleCode)){
				return new ModelAndView("replyment/loanDetailInfo");
			}
			if("".equals(request.getParameter("_theCurrent")) || request.getParameter("_theCurrent") == null){
				_theCurrent = "";
			}
			if(_theCurrent == null){
				_theCurrent = "";
			}
			if(_cpid == null){
				_cpid = "";
			}
		}
		return new ModelAndView("common/welcome",_theCurrent,_cpid);
	}
	
	@RequestMapping("/sb")
	public ModelAndView sb() {
		return new ModelAndView("resource/clientStaInfo");
	}

	/**
	 * 
	 * <功能简述> 跳转页面 <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/to_main")
	public ModelAndView to_main(HttpServletRequest request) {
		String upPwd = request.getParameter("upPwd");
		String loginName = request.getParameter("loginName");
		Map<String, Object> sd = new HashMap();
//		User user = (User)request.getSession().getAttribute(StaticValues.USER_SESSION);
//		String roleCode = user.getRole().getRoleCode();//角色代码
//		if("201204".equals(roleCode)){//根据角色的不同权限跳往不同的页面
//			return new ModelAndView("authority/userInfo");
//		}else if("201206".equals(roleCode) || "201208".equals(roleCode)){
//			return new ModelAndView("resourcestrack/resourcestrack");
//		}else if("201207".equals(roleCode)){
//			return new ModelAndView("replyment/loanDetailInfo");
//		}
		if (upPwd != null) {
			sd.put("upPwd", upPwd);
			sd.put("loginName", loginName);
		} else {
			sd.put("upPwd", "1");
		}
		return new ModelAndView("common/main", sd);
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	@RequestMapping("/to_login")
	public ModelAndView reLogin(HttpServletRequest request) {
		try {
			// 清除session
			request.getSession().removeAttribute(StaticValues.USER_SESSION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("../../index");
	}

	/**
	 * 
	 * <功能简述> 修改密码 <功能详细描述>
	 * 
	 * @param pwd1
	 *            原始密码
	 * @param pwd2
	 *            新密码
	 * @param request
	 * @return [参数说明]
	 * 
	 * @return ModelAndView [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/to_updatePwd.do")
	public ModelAndView updatePwd(@RequestParam("_pwd")
	String pwd1, @RequestParam("_rpwd")
	String pwd2, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			User sessionUser = (User) request.getSession().getAttribute(
					StaticValues.USER_SESSION);
			List<User> list = dao.findByHql("from User u where u.loginId = ? and u.password=? and u.isOrNotEnable=?",new Object[] { sessionUser.getLoginId(), pwd1, "2" });
			if (list != null && list.size() > 0) {
				SqlBuilder sqlBuilder = new SqlBuilder("User",
						SqlBuilder.TYPE_UPDATE);
				sqlBuilder.addField("password", pwd2);
				sqlBuilder.addWhere("id", sessionUser.getId());
				dao.updateByHQL(sqlBuilder.getSql(), sqlBuilder.getParams());
				jsonObject.element("success", true);
			} else {
				jsonObject.element("success", false);
				jsonObject.element("msg", "您输入的原始密码有错！");
				return MvcUtil.jsonObjectModelAndView(jsonObject);
			}

		} catch (Exception e) {
			jsonObject.element("success", false);
			jsonObject.element("msg", "修改密码出错！");
			e.printStackTrace();
		}

		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}

	public ICommonDAO<User> getDao() {
		return dao;
	}

	public void setDao(ICommonDAO<User> dao) {
		this.dao = dao;
	}
public static void main(String[] args) {
	String df="09";
	System.out.println(Integer.parseInt(df));
}
}
