package cn.dgg.CRM365.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


import cn.dgg.CRM365.domain.resourcesManage.Messages;
import cn.dgg.CRM365.util.orm.ICommonDAO;

public class SendMessage {
	
	
	
	/**
     * 
     *<功能简述>
     * 短信发送调用方法
     *<功能详细描述>
     * @param content 发送短信内容
     * @param sendtime 发送短信时间
     * @param phonelist 要发送客户电话
     * @param request
     * @return [参数说明]
     * 
     * @return ModelAndView [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */	
	public static String infoSend(String phonelist,String content,String sendtime){
			String sTotalString = "";
			try {
				content += "【成都叁陆伍投资管理有限公司】" ;
				String scontent = URLEncoder.encode(content, "utf-8");
				String sURL="http://www.infotoall.com:8088/smshttp/infoSend?account=365tz&password=123456&content="+scontent;
				sURL+="&sendtime="+sendtime+"&phonelist="+phonelist+"&taskId=365tz_"+sendtime+"_http_122121";
				java.net.URL l_url = new java.net.URL(sURL);
				java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
				l_connection.connect();
				InputStream l_urlStream = l_connection.getInputStream();
				java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
				String sCurrentLine = "";
				while ((sCurrentLine = l_reader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return sTotalString;
		}
	/**
     * 
     *<功能简述>
     * 记录短信发送信息
     *<功能详细描述>
     * @param loginId 发送人
     * @param chientName 接受短信客户
     * @param content 发送时间
     * @return [参数说明]
     * 
     * @return ModelAndView [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */	
   public static boolean saveMessages(ICommonDAO<Messages> mdao,String loginId,String chientName,String content) {
	   boolean falg = true;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dq = sdf.format(new Date());
			Messages messages = new Messages();
			messages.setSender(loginId);
			messages.setReceivedBy(chientName);
			messages.setContent(content);
			messages.setSendtime(dq);
			mdao.save(messages);
		} catch (Exception e) {
			falg = false;
			System.out.println("添加短信记录异常");
			e.printStackTrace();
		}
	   return falg;
   }
}
