package com.cat.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.boot.util.StringUtil;
import com.cat.system.jsonbean.UserBean;
import com.cat.system.model.PostInformation;
import com.cat.system.model.User;

@RestController
@Scope("prototype")
@RequestMapping("/user")
public class UserQuery extends BaseNqtQuery<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 601478503436290539L;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		/*List<String> userIds = new ArrayList<String>();
		if(!parms.hasparm("code")) {
			parms.getParams().put("code", "0001");
		}
		if(parms.hasparm("code")) {
			String code = (String)parms.getParams().get("code");
			if(code!=null) {
				if(code.length() == 4 || code.length() == 8 || code.length() == 12) {
					// 机构查询
					String sql = 
							"select o.user_id from Org_Post_Information o where o.isleaf in ('LF001','LF002') and o.organ_id like '" + code + "%'";
					userIds = baseService.query(sql, true).list();
				} else {
					PostInformation pi = (PostInformation)baseService.findById(PostInformation.class, code);
					// 人员查询
					userIds.add(pi.getUserId());
				}
			}
		}
		if(StringUtil.isListEmpty(userIds)) {
			userIds.add("xxxxxxxxxxxxxxxxxxxx");
		}
		parms.getParams().put("ids", userIds);*/
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, UserBean.setThis(results)));
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id,@RequestParam String app_code) {
		return super.delete(id,app_code);
	}
}
