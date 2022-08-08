package com.cat.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.CheckBean;
import com.cat.boot.jsonbean.PropBean;
import com.cat.boot.jsonbean.PropParamBean;
import com.cat.boot.jsonbean.QueryParmBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.jsonbean.TreeBean;
import com.cat.boot.jsonbean.TreeParmBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;

@RestController
@RequestMapping("/tree")
public class TreeController {
	
	@Autowired(required = true)
	public BaseService baseService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String getTree(TreeParmBean bean) throws Exception {
		List<PropParamBean> beans = new ArrayList<PropParamBean>();
		if (!StringUtil.isEmpty(bean.getRoot())) {
			beans.add(new PropParamBean("like", "and", StringUtil.isEmpty(bean.getKey()) ? "id" : bean.getKey(), bean.getRoot() + "%"));
		}
		List<Object[]> orgs = (List<Object[]>) baseService.getList(bean.getTable_name(), 
				StringUtil.isEmpty(bean.getSort()) ? "o.id asc" : bean.getSort(), true,
				StringUtil.isEmpty(bean.getProps()) ? "o.id,o.name,o.sc_id" : bean.getProps(), beans);

		if (!StringUtil.isListEmpty(orgs)) {
			List<TreeBean> tree = TreeBean.getTree(orgs, bean.getOther());
			return ResultBean.getSucess(tree);
		}
		return ResultBean.getResultBean("200", "", "");
	}
	
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/get_user", method = RequestMethod.GET)
	public String getTree_User(TreeParmBean bean) throws Exception {
		List<PropParamBean> beans = new ArrayList<PropParamBean>();
		if (!StringUtil.isEmpty(bean.getRoot())) {
			beans.add(new PropParamBean("like", "and", "code", bean.getRoot() + "%"));
		}
		List<Object[]> orgs = (List<Object[]>) baseService.getList("Org_Organ", "o.xh asc,o.id asc", true,
				"o.id,o.name,o.sc_id", beans);
		List<Object[]> persons = (List<Object[]>) baseService.getList("User", "system",
				"User_by_PostInformation",
				NameQueryUtil.setParams("leafs", Arrays.asList(new String[] { "LF001","LF002" })));
		if (!StringUtil.isListEmpty(persons)) {
			orgs.addAll(persons);
		}
		if (!StringUtil.isListEmpty(orgs)) {
			List<TreeBean> tree = TreeBean.getTree(orgs, bean.getOther());
			return ResultBean.getSucess(tree);
		}
		return ResultBean.getSucess(new ArrayList<TreeBean>());
	}
	
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(TreeParmBean bean) {
		List<Object[]> orgs = getL(bean);

		return toPropBean(orgs);
	}
	
	@RequestMapping(value = "/getList_CheckBox", method = RequestMethod.GET)
	public String getList_CheckBox(TreeParmBean bean) {
		List<Object[]> orgs = getL(bean);

		return toCheckBean(orgs);
	}
	
	@RequestMapping(value = "/getList_NQ", method = RequestMethod.GET)
	public String getList_NQ(QueryParmBean bean) {
		List<Object[]> orgs = getNq(bean);

		return toPropBean(orgs);
	}
	
	@RequestMapping(value = "/getList_NQ_CheckBox", method = RequestMethod.GET)
	public String getList_NQ_CheckBox(QueryParmBean bean) {
		List<Object[]> orgs = getNq(bean);
		return toCheckBean(orgs);
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getNq(QueryParmBean bean){
		List<Object[]> orgs = (List<Object[]>) baseService.getList(bean.getNamespace(), 
				bean.getXmlpath(),
				bean.getMethodname(),bean.getMap());
		return orgs;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getL(TreeParmBean bean){
		List<PropParamBean> beans = new ArrayList<PropParamBean>();
		if (!StringUtil.isEmpty(bean.getRoot())) {
			beans.add(new PropParamBean("like", "and", StringUtil.isEmpty(bean.getKey()) ? "id" : bean.getKey(), bean.getRoot() + "%"));
		}
		List<Object[]> orgs = (List<Object[]>) baseService.getList(bean.getTable_name(), 
				StringUtil.isEmpty(bean.getSort()) ? "o.id asc" : bean.getSort(), true,
				StringUtil.isEmpty(bean.getProps()) ? "o.id,o.name" : bean.getProps(), beans);
		return orgs;
	}
	
	private String toPropBean(List<Object[]> orgs) {
		if (!StringUtil.isListEmpty(orgs)) {
			List<PropBean> bs = new ArrayList<PropBean>();
			for (Object[] o : orgs) {
				PropBean b = new PropBean();
				if(o.length==3) {
					b.setOther(String.valueOf(o[2]));
				}
				b.setId(String.valueOf(o[0]));
				b.setName(String.valueOf(o[1]));
				bs.add(b);
			}
			return ResultBean.getSucess(bs);
		}
		
		return ResultBean.getResultBean("200", "", "");
	}
	
	private String toCheckBean(List<Object[]> orgs) {
		if (!StringUtil.isListEmpty(orgs)) {
			List<CheckBean> bs = new ArrayList<CheckBean>();
			for (Object[] o : orgs) {
				CheckBean b = new CheckBean();
				b.setLabel(String.valueOf(o[1]));
				b.setValue(String.valueOf(o[0]));
				bs.add(b);
			}
			return ResultBean.getSucess(bs);
		}
		
		return ResultBean.getResultBean("200", "", "");
	}
}
