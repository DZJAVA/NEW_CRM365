package cn.dgg.CRM365.util.commonUtil;

import java.io.File;

/**
 * 
 * <功能简述> 上传公共文件路径，添加上传文件路径，创建文件名
 * 
 * @author 谢小明
 * @version [第一次创建时间, 2012-11-19 10：12]
 * @see [相关类/方法]
 */
public class CreateFilePath {

	private String GJDWFile = "C:\\GJDWFILe";// 最上层文件夹路径

	/**
	 * 指定上传文件的上级文件名和当前文件名
	 * 
	 * 谢小明 [2012-11-19 13:42]
	 * 
	 * @param abovePath
	 *            上级文件夹路径
	 * @param belowPath
	 *            当前文件夹路径
	 */
	public String addFileUpload(String abovePath, String belowPath) {
		// File[] roots = File.listRoots();
		// for (int i = 0; i < roots.length; i++) {
		// System.out.println(roots[i]);
		// }
		// if (roots.length > 0) {
		// contextPath = roots[0].toString();
		// }

		File file = new File(GJDWFile);
		// 判断GJDWFile文件夹是否存在，如果不存在则创建文件夹
		if (!file.exists()) {
			// 创建文件夹
			file.mkdir();
		}
		if (abovePath == null || "".equals(abovePath)) {
			GJDWFile = GJDWFile;
		} else {
			GJDWFile = GJDWFile + "\\" + abovePath;
		}
		file = new File(GJDWFile);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}
		if (belowPath == null || "".equals(belowPath)) {
			GJDWFile = GJDWFile;
		} else {
			GJDWFile = GJDWFile + "\\" + belowPath;
		}
		file = new File(GJDWFile);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}
		return GJDWFile;
	}

	/**
	 * 上传附件，必须保存当前用户ID，当前上传父类类型，当前上传父类ID
	 * 
	 * 谢小明 [2012-11-19 14：30]
	 * 
	 * @param userFileName
	 *            当前用户Id
	 * @param parentClassType
	 *            当前上传父类类型
	 * @param parentClassId
	 *            当前上传父类ID
	 */
	public String addFileUpload(String userId, String parentClassId,
			String parentClassType) {
		// File[] roots = File.listRoots();
		// for (int i = 0; i < roots.length; i++) {
		// System.out.println(roots[i]);
		// }
		// if (roots.length > 0) {
		// contextPath = roots[0].toString();
		// }
		// 封装所有参数路径，放进集合
		String[] fileList = { GJDWFile, userId, parentClassType, parentClassId };
		// 集合长度
		long num = fileList.length;
		// 如果有参数为空，那么就返回null
		for (int i = 0; i < num; i++) {
			if (null == fileList[i] || "".equals(fileList[i])) {
				return null;
			}
		}
		// 根据参数，循环创建文件夹
		for (int i = 0; i < num; i++) {
			// 循环创建文件夹路径
			if (fileList[i] == GJDWFile || GJDWFile.equals(fileList[i])) {
				GJDWFile = GJDWFile;
			} else {
				GJDWFile = GJDWFile + "\\" + fileList[i];
			}
			File file = new File(GJDWFile);
			// 判断GJDWFile文件夹是否存在，如果不存在则创建文件夹
			if (!file.exists()) {
				// 创建文件夹
				file.mkdir();
			}
		}
		return GJDWFile;
	}
	// 测试 addFileUpload方法
	// public static void main(String[] args) {
	// CreateFilePath cfp=new CreateFilePath();
	// System.out.println(cfp.addFileUpload("456", "", "43"));
	// }
}
