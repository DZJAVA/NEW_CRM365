package cn.dgg.CRM365.util.commonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class UploadFileUtil {
	public static String upload(String uploadFileName, String savePath,
			File uploadFile) {
		String newFileName = getUUIDName(uploadFileName, savePath);
		try {
			FileOutputStream fos = new FileOutputStream(savePath + newFileName);
			FileInputStream fis = new FileInputStream(uploadFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0)
				fos.write(buffer, 0, len);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newFileName;
	}

	public static String getUUIDName(String fileName, String dir) {
		String[] split = fileName.split("\\.");
		String extendFile = "." + split[(split.length - 1)].toLowerCase();
		return UUID.randomUUID().toString() + extendFile;
	}

	public static boolean mkDirectory(String path) {
		File file = null;
		try {
			file = new File(path);
			if (!file.exists())
				return file.mkdirs();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			file = null;
		}
		return false;
	}
}
