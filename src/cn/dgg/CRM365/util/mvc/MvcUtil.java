package cn.dgg.CRM365.util.mvc;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.util.mvc.view.JSONView;

/**
 * springMVC返回值工具类
 * 
 * @author 王科（小）
 * 
 */
@SuppressWarnings("all")
public class MvcUtil {
	private static final JSONView jsonLibView = new JSONView();
	private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat(
			"hh:mm:ss");
	private static final SimpleDateFormat FORMAT_TIMESTAMP = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	public MvcUtil() {
	}

	public static ModelAndView jsonObjectModelAndView(JSONObject jsonObject) {
		return new ModelAndView(jsonLibView, "jsonObject", jsonObject);
	}

	public static ModelAndView jsonArrayModelAndView(JSONArray jsonArray) {
		return new ModelAndView(jsonLibView, "jsonArray", jsonArray);
	}

	public static boolean checkBindingResult(BindingResult bindingResult,
			JSONObject returnObject) {
		if (bindingResult.hasErrors()) {
			if (bindingResult.hasFieldErrors()) {
				List list = bindingResult.getFieldErrors();
				JSONObject errors = new JSONObject();
				for (int i = 0; i < list.size(); i++) {
					FieldError fieldError = (FieldError) list.get(i);
					errors.element(fieldError.getField(), fieldError
							.getRejectedValue().toString());
				}

				returnObject.element("errors", errors);
			}
			if (bindingResult.hasGlobalErrors()) {
				List list = bindingResult.getGlobalErrors();
				String errorMessage = "";
				for (int i = 0; i < list.size(); i++) {
					ObjectError fieldError = (ObjectError) list.get(i);
					if (i > 0)
						errorMessage = (new StringBuilder(String
								.valueOf(errorMessage))).append("; ")
								.toString();
					errorMessage = (new StringBuilder(String
							.valueOf(errorMessage))).append(
							fieldError.getDefaultMessage()).toString();
				}

				returnObject.element("errorMessage", errorMessage);
			}
			return false;
		} else {
			return true;
		}
	}

	public static String toJsonString(Object obj) {
		if (obj != null)
			return obj.toString();
		else
			return null;
	}

	public String toJsonString(Date obj) {
		if (obj != null)
			return FORMAT_DATE.format(obj);
		else
			return null;
	}

	public String toJsonString(Time obj) {
		if (obj != null)
			return FORMAT_TIME.format(obj);
		else
			return null;
	}

	public String toJsonString(Timestamp obj) {
		if (obj != null)
			return FORMAT_TIMESTAMP.format(obj);
		else
			return null;
	}

	public static Long parseLong(HttpServletRequest request, String paramName) {
		Long result = null;
		String s = request.getParameter(paramName);
		if (s != null && !"".equals(s))
			result = Long.valueOf(Long.parseLong(s));
		return result;
	}
}
