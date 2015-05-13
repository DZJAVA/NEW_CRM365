package cn.dgg.CRM365.web.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.dgg.CRM365.domain.authority.User;
import cn.dgg.CRM365.domain.resources.AutoAssign;
import cn.dgg.CRM365.domain.resourcesManage.Client;
import cn.dgg.CRM365.util.commonUtil.DBManager;
import cn.dgg.CRM365.util.commonUtil.StaticValues;
import cn.dgg.CRM365.util.orm.ICommonDAO;

@SuppressWarnings("all")
@RequestMapping("/time")
@Controller
public class TimingAssign {
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Object> objDao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<AutoAssign> autoDao;
	/**
	 * 每天早上九点定时分配客户
	 */
	@RequestMapping("timeTest")
	public void timingAssign(){
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if(day != 7 && day != 1){
			String hql = "select c.id from Client c where c.assignTime = '' and c.clientSourse.name = '购买数据' and c.clientStatus = '2'";
			String userHql = "select x.id from XzShituAllocation x where x.qiyong = 2 and x.jiedan = 1 and x.depjiedan = '1'";
			String autoHql = "from AutoAssign a";
			Connection conn = null;
			PreparedStatement ps = null;
			String nowTime = StaticValues.format.format(new Date());
			boolean autoFlag = true;
			try {
				conn = DBManager.getConnection();//获取jdbc连接
				autoFlag = conn.getAutoCommit();
				conn.setAutoCommit(false);//设置不能自动提交
				ps = conn.prepareStatement("update dgg_client set follower = ?, assignTime = ? where id = ?");
				List<AutoAssign> autoList = autoDao.findAll(autoHql);
				List<Object> xList = objDao.findAll(userHql);
				List<Object> list = objDao.findAll(hql);
				List<Object> assignList = new ArrayList<Object>();//装配已经分配过的客户list
				User user = null;
				int num = 0;
				int allNum = 0;//总的需要分配的客户数
				int flag = 0;
				if(autoList.size() > 0){
					for(int j = 0; j < autoList.size(); j++){
						flag++;
						user = autoList.get(j).getUser();
						if(user.getIsOrNotEnable() == 1 || user.getSignStatus() == 0 || 
								"0".equals(user.getDepartment().getOrderStatus())){
							continue;
						}else{
							allNum = list.size();
							num = autoList.get(j).getNum();
							if(num >= allNum){//只能补第一个用户的客户差
								for(Object obj : list){
									ps.setLong(1, user.getId());
									ps.setString(2, nowTime);
									ps.setLong(3, Long.parseLong(obj.toString()));
									ps.addBatch();
									ps.clearParameters();
								}
								if(num == allNum){//刚刚分配完，删除中间表里的记录
									ps.addBatch("delete from dgg_autoAssign where user = " + user.getId());
								}else{//更新中间表里的客户差数量
									ps.addBatch("update dgg_autoAssign set num = "+ (num-allNum) +" where user = " + user.getId());
								}
								ps.executeBatch();
								conn.commit();
								ps.clearBatch();
								break;//分配结束
							}else{
								for(int i = 0; i < num; i++){
									ps.setLong(1, user.getId());
									ps.setString(2, nowTime);
									ps.setLong(3, Long.parseLong(list.get(i).toString()));
									ps.addBatch();
									assignList.add(list.get(i));
									ps.clearParameters();
								}
								ps.addBatch("delete from dgg_autoAssign where user = " + user.getId());
								list.removeAll(assignList);
								assignList.clear();
							}
						}
						if(flag%5 == 0 || j == autoList.size() - 1){
							ps.executeBatch();
							conn.commit();
							ps.clearBatch();
						}
					}
				}
				if(list.size() > 0){//每个接单员工的分配系数为20
					assignByCoefficient(xList, list, ps, conn, assignList);
				}
			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally{
				try {
					if(conn != null){
						conn.setAutoCommit(autoFlag);//恢复以前的提交状态
						conn.close();
					}
					if(ps != null){
						ps.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 按系数分配
	 */
	public void assignByCoefficient(List<Object> xList, List<Object> list, PreparedStatement ps, 
			Connection conn, List<Object> assignList) throws Exception{
		Long id = null;
		int allNum = 0;
		String nowTime = StaticValues.format.format(new Date());
		int flag = 0;
		for(int i = 0; i < xList.size(); i++){//给每个接单的员工分配
			flag++;
			id = Long.parseLong(xList.get(i).toString());//接单员工id
			allNum = list.size();
			if(allNum <= 20){
				if(allNum == 0){
					ps.addBatch("insert into dgg_autoAssign(user, num) values("+id+","+20+")");
				}else{
					for(Object o : list){
						ps.setLong(1, id);
						ps.setString(2, nowTime);
						ps.setLong(3, Long.parseLong(o.toString()));
						ps.addBatch();
						ps.clearParameters();
					}
				}
				if(allNum > 0){//分配完了，全部清除
					if((20 - allNum) > 0){
						ps.addBatch("insert into dgg_autoAssign(user, num) values("+id+","+(20 - allNum)+")");
					}
					list.clear();
				}
			}else{
				for(int j = 0; j < 20; j++){
					ps.setLong(1, id);
					ps.setString(2, nowTime);
					ps.setLong(3, Long.parseLong(list.get(j).toString()));
					ps.addBatch();
					assignList.add(list.get(j));
					ps.clearParameters();
				}
				list.removeAll(assignList);
				assignList.clear();
			}
			if(flag%5 == 0 || i == xList.size() - 1){
				ps.executeBatch();
				conn.commit();
				ps.clearBatch();
			}
		}
	}
}
