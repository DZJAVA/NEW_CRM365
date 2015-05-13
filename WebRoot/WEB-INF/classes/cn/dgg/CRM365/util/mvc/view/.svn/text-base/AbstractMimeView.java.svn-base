package cn.dgg.CRM365.util.mvc.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractView;

public abstract class AbstractMimeView extends AbstractView {

	public static final String FILE_PATH = "filePath";
	public static final String FILE_NAME = "fileName";
	public static final String FILE_DATA = "fileData";

	public AbstractMimeView() {
		setContentType("application/download");
	}

	protected void buildHeader(HttpServletResponse response, String mimeName,
			long mimeLength) {
		response.setContentType(getContentType());
		if (mimeLength > 0L)
			response.setHeader("Content-Length", String.valueOf(mimeLength));
		try {
			mimeName = URLEncoder.encode(mimeName, "UTF-8");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {
		}
		response.setHeader("Content-disposition", (new StringBuilder(
				"attachment; filename=\"")).append(mimeName).append("\"")
				.toString());
	}
}
