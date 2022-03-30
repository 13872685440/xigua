package com.cat.system.controller;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.jsonbean.TreeParmBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.jsonbean.AddressBean;
import com.cat.system.jsonbean.AddressListBean;
import com.cat.system.model.Organ;
import com.cat.system.model.User;

@RestController
@Scope("prototype")
@RequestMapping("/organ")
public class OrganQuery extends BaseNqtQuery<Organ>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5992956687999194584L;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, results));
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id,@RequestParam String app_code) {
		return super.delete(id,app_code);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getDatas", method = RequestMethod.GET)
	public String getDatas(TreeParmBean bean) throws Exception {
		List<Organ> os = (List<Organ>)
				baseService.getList("Organ", "o.xh asc", true,NameQueryUtil.setParams("orgtype",bean.getRoot()));
		return ResultBean.getSucess(os);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAppDatas", method = RequestMethod.GET)
	public String getAppDatas(TreeParmBean pbean) throws Exception {
		AddressListBean bean = new AddressListBean();
		if(!StringUtil.isEmpty(pbean.getRoot())) {
			// 查询是否有 下级
			List<Organ> organs = (List<Organ>)baseService.
					getList("Organ", "o.xh asc", true, NameQueryUtil.setParams("scId",pbean.getRoot()));
			if(!StringUtil.isListEmpty(organs)) {
				bean.setOrgans(AddressBean.setThis(organs));
			}
			// 查询机构下的人员
			List<User> persons =
					(List<User>)baseService.getList("Organ", "system", "User_by_Organ", NameQueryUtil.setParams("organId",pbean.getRoot()));
			if(!StringUtil.isListEmpty(persons)) {
				bean.setPersons(AddressBean.setThis2(persons));
			}
		} else {
			// 查询一级机构
			List<Organ> organs = (List<Organ>)baseService.
					getList("Organ", "o.xh asc", true, NameQueryUtil.setParams("scId",null));
			if(!StringUtil.isListEmpty(organs)) {
				bean.setOrgans(AddressBean.setThis(organs));
			}
		}
		return ResultBean.getSucess(bean);
	}
}
