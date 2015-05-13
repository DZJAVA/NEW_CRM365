package cn.dgg.CRM365.util.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.view.AbstractView;

/**
 * 
 * @author 王科（小）
 * 
 */
public class JSONView extends AbstractView {
	public JSONView() {
		setContentType("text/html");
	}

	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/html");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0L);
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		JSONArray jsonArray = (JSONArray) model.get("jsonArray");
		if (jsonArray != null) {
			jsonArray.write(response.getWriter());
		} else {
			JSONObject jsonObject = (JSONObject) model.get("jsonObject");
			if (jsonObject != null) {
				if (jsonObject.get("success") != null
						&& !jsonObject.getBoolean("success"))
					response.setStatus(417);
				jsonObject.write(response.getWriter());
			} else {
				jsonArray = new JSONArray();
				jsonArray.write(response.getWriter());
			}
		}
	}
}
