package cn.dgg.CRM365.util.mvc.view;

import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MimeBytesView extends AbstractMimeView {

	public MimeBytesView() {
	}

	protected void renderMergedOutputModel(Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = (String) model.get("fileName");
		byte fileData[] = (byte[]) model.get("fileData");
		if (fileName != null && fileData != null) {
			buildHeader(response, fileName, fileData.length);
			OutputStream out = response.getOutputStream();
			out.write(fileData);
			out.flush();
		}
	}
}
