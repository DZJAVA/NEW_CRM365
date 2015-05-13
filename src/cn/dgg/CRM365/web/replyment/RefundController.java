package cn.dgg.CRM365.web.replyment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dgg.CRM365.domain.replyment.LoanDetail;
import cn.dgg.CRM365.domain.replyment.Rcount;
import cn.dgg.CRM365.domain.resourcesManage.Client;
import cn.dgg.CRM365.util.mvc.MvcUtil;
import cn.dgg.CRM365.util.orm.ICommonDAO;
import cn.dgg.CRM365.util.orm.SqlBuilder;
import cn.dgg.CRM365.util.page.GridLoadParams;
import cn.dgg.CRM365.util.page.Pagination;

/**
 * 还款明细
  * <功能简述>
  * <功能详细描述>
  * 
  * @author  chenqin
  * @version  [版本号, Dec 17, 2012]
  * @see  [相关类/方法]
  * @since  [产品/模块版本]
 */
@SuppressWarnings("all")
@RequestMapping("/refund")
@Controller
public class RefundController {
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<LoanDetail> dao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Client> cdao;
	
	@Autowired
	@Qualifier("commonDAOProxy")
	ICommonDAO<Rcount> rdao;

	@RequestMapping("/jumpPage.do")
	public ModelAndView jumpPage() {
		return new ModelAndView("replyment/refundDetailInfo");
	}
	
	/**
	 * 根据贷款明细查找还款记录信息
	  *<功能简述>
	  *<功能详细描述>
	  * @param gridLoadParams
	  * @param request
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/findByLoanD")
	public ModelAndView findByLoanD(@ModelAttribute("params")
			GridLoadParams gridLoadParams, HttpServletRequest request,@RequestParam("lid") String lid){
		JSONObject jsonObject = new JSONObject();
		Pagination pagination = new Pagination();
		pagination.set(gridLoadParams.getStart(), gridLoadParams.getLimit());
		String hql = "from Rcount r where r.loanDetail.id ="+Long.parseLong(lid);
		try {
			List<Rcount> rcounts = rdao.findAll(hql);
			jsonObject.element("totalCount", pagination.getTotalResults());
			JSONArray data = new JSONArray();
			if (rcounts.size() > 0) {
				for (Rcount field : rcounts) {
					JSONObject item = new JSONObject();
					item.element("id", MvcUtil.toJsonString(field.getId()));  
					item.element("number", MvcUtil.toJsonString(field.getNumber()));  
					item.element("rSum", MvcUtil.toJsonString(field.getLoanDetail().getMonthMoney()));  
					item.element("rTime", MvcUtil.toJsonString(field.getPayTime()));  
					item.element("factRTime", MvcUtil.toJsonString(field.getFactRTime()));  
					if(field.getStatus().equals("0")){
						item.element("status", "未还款");
					}if(field.getStatus().equals("1")){
						item.element("status", "已还款");
					}
					data.add(item);
				}
			}
			jsonObject.element("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
	}
	
	/**
	 * 修改状态为已还款
	  *<功能简述>
	  *<功能详细描述>
	  * @return [参数说明]
	  * 
	  * @return ModelAndView [返回类型说明]
	  * @exception throws [异常类型] [异常说明]
	  * @see [类、类#方法、类#成员]
	 */
	@RequestMapping("/updateRefund")
	public ModelAndView updateRefund(HttpServletRequest request,@RequestParam("id") String id,@RequestParam("_factRTime") String _factRTime){
		JSONObject jsonObject = new JSONObject();
		String hql = "from Rcount r where r.id = ?";
		try {
			List<Rcount> rcounts = rdao.findByHql(hql,new Object[]{Long.parseLong(id)});
			if(rcounts.size()>0){
				Rcount rcount = rcounts.get(0);
				rcount.setStatus("1");
				rcount.setFactRTime(_factRTime);
				rdao.update(rcount);
				jsonObject.element("success", true);
				jsonObject.element("msg", "修改还款状态成功！");
				return MvcUtil.jsonObjectModelAndView(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.element("failure", true);
			jsonObject.element("msg", "修改失败！");
			return MvcUtil.jsonObjectModelAndView(jsonObject);
		}
		return MvcUtil.jsonObjectModelAndView(jsonObject);
		
	} 

}
