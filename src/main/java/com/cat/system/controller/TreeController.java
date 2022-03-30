package com.cat.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
				NameQueryUtil.setParams("leafs", Arrays.asList(new String[] { "在职" })));
		if (!StringUtil.isListEmpty(persons)) {
			orgs.addAll(persons);
		}
		if (!StringUtil.isListEmpty(orgs)) {
			List<TreeBean> tree = TreeBean.getTree(orgs, bean.getOther());
			return ResultBean.getSucess(tree);
		}
		return ResultBean.getSucess(new ArrayList<TreeBean>());
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	public String getList(TreeParmBean bean) {
		List<PropParamBean> beans = new ArrayList<PropParamBean>();
		if (!StringUtil.isEmpty(bean.getRoot())) {
			beans.add(new PropParamBean("like", "and", StringUtil.isEmpty(bean.getKey()) ? "id" : bean.getKey(), bean.getRoot() + "%"));
		}
		List<Object[]> orgs = (List<Object[]>) baseService.getList(bean.getTable_name(), 
				StringUtil.isEmpty(bean.getSort()) ? "o.id asc" : bean.getSort(), true,
				StringUtil.isEmpty(bean.getProps()) ? "o.id,o.name" : bean.getProps(), beans);

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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getList_NQ", method = RequestMethod.GET)
	public String getList_NQ(QueryParmBean bean) {
		List<Object[]> orgs = (List<Object[]>) baseService.getList(bean.getNamespace(), 
				bean.getXmlpath(),
				bean.getMethodname(),bean.getMap());

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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getList_S", method = RequestMethod.GET)
	public String getList_S(QueryParmBean bean) {
		List<String> orgs = (List<String>) baseService.getList(bean.getNamespace(), 
				bean.getXmlpath(),
				bean.getMethodname(),bean.getMap());

		if (!StringUtil.isListEmpty(orgs)) {
			
			return ResultBean.getSucess(orgs);
		}
		
		return ResultBean.getSucess(new ArrayList<String>());
	}
}
