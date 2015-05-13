/**
 * 文 件 名:  UploadFile.java
 * 版    权:  TX Workgroup . Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王科（小）
 * 修改时间:  Jul 5, 2012
 * <修改描述:>
 */
package cn.dgg.CRM365.util.commonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.multipart.MultipartFile;

import cn.dgg.CRM365.util.mvc.view.AbstractMimeView;

/**
 * <功能简述> 上传下载文件工具类 <功能详细描述>
 * 
 * @author 王科（小）
 * @version [版本号, Jul 5, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UploadFile {

	private Properties propertie;
	private InputStream in = null;
	private String url = null;
	File[] dataList = null;
	File f = null;

	/**
	 * 
	 * <功能简述> 保存文件 <功能详细描述>
	 * 
	 * @param multipartFile
	 * @param path
	 *            保存文件路径
	 * @param filename
	 *            文件名
	 * @throws IOException
	 *             [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void SaveFile(MultipartFile multipartFile, String filePath)
			throws IOException {
		InputStream stream = null;
		FileOutputStream fs = null;
		File file = null;
		try {
			stream = multipartFile.getInputStream();
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			// fs = new FileOutputStream(filePath);
			fs = new FileOutputStream(filePath + "//"
					+ multipartFile.getOriginalFilename());
			// byte[] buffer =new byte[1024*1024*5];
			byte[] buffer = new byte[1024 * 1024 * 512];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fs.close();
			in.close();
			stream.close();
		}
	}

	/**
	 * 上传文件 <功能简述> <功能详细描述>
	 * 
	 * @param multipartFile
	 * @param date
	 * @param filePath
	 * @throws IOException
	 *             [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void SaveFile(MultipartFile multipartFile, String date,
			String filePath) throws IOException {
		InputStream stream = null;
		FileOutputStream fs = null;
		File file = null;
		try {
			stream = multipartFile.getInputStream();
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			// fs = new FileOutputStream(filePath);
			fs = new FileOutputStream(filePath + "//" + date
					+ multipartFile.getOriginalFilename());
			// byte[] buffer =new byte[1024*1024*5];
			byte[] buffer = new byte[1024 * 1024 * 512];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fs.close();
			in.close();
			stream.close();
		}
	}

	/**
	 * 
	 * <功能简述> 保存文件 <功能详细描述>
	 * 
	 * @param multipartFile
	 * @param path
	 *            保存文件路径
	 * @param filename
	 *            文件名
	 * @throws IOException
	 *             [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void SaveFile(MultipartFile multipartFile, String filePath, long num)
			throws IOException {
		InputStream stream = null;
		FileOutputStream fs = null;
		File file = null;
		try {
			stream = multipartFile.getInputStream();
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			// fs = new FileOutputStream(filePath);
			fs = new FileOutputStream(filePath + "//"
					+ multipartFile.getOriginalFilename());
			// byte[] buffer =new byte[1024*1024*5];
			byte[] buffer = new byte[1024 * 1024 * 512];

			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fs.close();
			stream.close();
		}
	}

	/**
	 * 
	 * <功能简述> 保存文件 <功能详细描述>
	 * 
	 * @param multipartFile
	 * @param path
	 *            保存文件路径
	 * @param filename
	 *            文件名
	 * @throws IOException
	 *             [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void SaveFile1(MultipartFile multipartFile, String filePath)
			throws IOException {
		InputStream stream = null;
		FileOutputStream fs = null;
		File file = null;
		try {
			stream = multipartFile.getInputStream();
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			// fs = new FileOutputStream(filePath);
			fs = new FileOutputStream(filePath + "//" + "top_bg6.jpg");
			byte[] buffer = new byte[1024 * 1024 * 500];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fs.close();
			in.close();
			stream.close();
		}
	}

	/**
	 * 
	 * <功能简述> 下载附件 <功能详细描述>
	 * 
	 * @param fileName
	 *            文件名
	 * @param path
	 *            路径
	 * @param response
	 *            [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("all")
	public Map<String, Object> downloadAttachment(String fileName,
			String filePath) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (fileName != null && !"".equals(fileName)) {
			FileInputStream fis = null;
			try {

				f = new File(filePath);
				dataList = f.listFiles();
				byte[] byData = null;
				for (int i = 0; i < dataList.length; i++) {
					if (dataList[i].getName().equals(fileName)) {
						fis = new FileInputStream(dataList[i].getAbsolutePath());
						byData = new byte[(int) dataList[i].length()];
						fis.read(byData);
						map.put(AbstractMimeView.FILE_NAME, fileName);
						map.put(AbstractMimeView.FILE_DATA, byData);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}

		return map;
	}

	public Properties getPropertie() {
		try {
			propertie = new Properties();
			// 加载属性文件
			in = this.getClass().getResourceAsStream(StaticValues.PROP_NAME);
			propertie.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertie;
	}

	public void setPropertie(Properties propertie) {
		this.propertie = propertie;
	}

	/**
	 * 
	 * <功能简述> 取得文件夹大小 <功能详细描述>
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 *             [参数说明]
	 * 
	 * @return long [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		if (flist.length > 0) {
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		}
		return size;
	}

	/**
	 * 
	 * <功能简述> 转换文件大小 单位M <功能详细描述>
	 * 
	 * @param fileS
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws
	 *                [异常类型] [异常说明]
	 * @see [类、类#方法、类#成员]
	 */
	public double FormetFileSize(long fileS) {
		double fileSizeString = fileS / 1048576;
		return fileSizeString;
	}
}
