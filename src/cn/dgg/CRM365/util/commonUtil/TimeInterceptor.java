package cn.dgg.CRM365.util.commonUtil;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.dgg.CRM365.domain.authority.User;

//HandlerInterceptorAdapter适配器（一种适配器设计模式的实现），允许我们只实现需要的回调方法。

//流程：
//1、访问需要登录的资源时，由拦截器重定向到登录页面；
//2、如果访问的是登录页面，拦截器不应该拦截；
//3、用户登录成功后，判断是浏览器地址访问，返回空，如果是菜单操作访问，就放行
public class TimeInterceptor extends HandlerInterceptorAdapter {
	//这个方法在业务处理器处理请求之前被调用，
	//在该方法中对用户请求request进行处理。
	//如果程序员决定该拦截器对请求进行拦截处理后还要调用其他的拦截器，
	//或者是业务处理器去进行处理，则返回true；
	//如果程序员决定不需要再调用其他的组件去处理请求，则返回false。
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		User user = (User) request.getSession().getAttribute(StaticValues.USER_SESSION);
		response.setContentType("text/html;charset=utf-8");
		 // 后台session控制  
        String[] noFilters = new String[] {"loginyanzhengma.do","login.do","to_login.do","findPwd.do"};  
        String uri = request.getRequestURI(); 
        System.out.println("URL="+uri);
        if (uri.indexOf("CRM") != -1) {  
            boolean beFilter = false;  
            for (String s : noFilters) {  
                if (uri.indexOf(s) != -1) {  
                    beFilter = true;  
                    break;  
                }  
            }  
            if(beFilter){
            	return super.preHandle(request, response, handler);
            }else{
            	if(user == null){
            		PrintWriter out = response.getWriter();  
            		StringBuilder builder = new StringBuilder();  
            		builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");  
            		builder.append("alert(\"您在别处登录或较长时间未操作，请重新登录！\");");  
            		builder.append("window.top.location.href=\"");  
            		builder.append("/CRM/index.jsp\";</script>");  
            		out.print(builder.toString());  
            		out.close();  
            		return false;
            	}else{
            		return super.preHandle(request, response, handler);
            	}
            }
        }
		return false;
	}
	//这个方法在业务处理器处理完请求后，
	//但是DispatcherServlet向客户端返回请求前被调用，
	//在该方法中对用户请求request进行处理。
	@Override  
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object o, ModelAndView mav)
			throws Exception {
//		System.out.println("postHandle");
	}
	//这个方法在DispatcherServlet完全处理完请求后被调用，
	//可以在该方法中进行一些资源清理的操作。
	@Override  
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object o, Exception excptn)
			throws Exception {
//		System.out.println("afterCompletion");
	}

}
