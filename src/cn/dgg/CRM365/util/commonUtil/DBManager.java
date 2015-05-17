package cn.dgg.CRM365.util.commonUtil;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
	
	private static final String URL ;
	private static final String USER ;
	private static final String PASSWORD ;
	private static final String DRIVER;
	static{
		URL = PropertiesUtil.getValue("systemValue", "jdbc.url");
		USER = PropertiesUtil.getValue("systemValue", "jdbc.username");
		PASSWORD = PropertiesUtil.getValue("systemValue", "jdbc.password");
		DRIVER = PropertiesUtil.getValue("systemValue", "jdbc.driverClassName");
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
			System.out.println(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
