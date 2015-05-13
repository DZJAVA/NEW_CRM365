package cn.dgg.CRM365.util.commonUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager {
	
	private static final String URL ;
	private static final String USER ;
	private static final String PASSWORD ;
	private static final String DRIVER;
	//������
	static{
		URL = PropertiesUtil.getValue("systemValue", "url");
		USER = PropertiesUtil.getValue("systemValue", "user");
		PASSWORD = PropertiesUtil.getValue("systemValue", "password");
		DRIVER = PropertiesUtil.getValue("systemValue", "driver");
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	//获得jdbc连接
	public static Connection getConnection() throws Exception{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	public static void close(Connection con) throws Exception{
		if(con != null){
			if(!con.isClosed()){
				con.close();
			}
		}
	}
	public void add(String i){
		i = "sre";
	}
	//关闭jdbc连接
	public static void main(String[] args) {
		try {
			Connection conn = DBManager.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into dgg_clientdifrecord(userid,clientDif) values(?,?)");
			ps.clearBatch();
			Long start = System.currentTimeMillis();
			System.out.println("开始时间：" + start);
			String str;
			for(int i = 1; i < 10001; i++){
				str = String.valueOf(i);
				ps.setString(1, str);
				ps.setString(2, str);
				ps.addBatch();
//				if(i%100 == 0){
//					ps.executeBatch();
//					ps.clearBatch();
//				}
			}
			ps.executeBatch();
//			conn.commit();
			Long end = System.currentTimeMillis();
			System.out.println("结束时间：" + end);
			System.out.println("时间差：" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
