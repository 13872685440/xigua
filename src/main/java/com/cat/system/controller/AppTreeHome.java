package com.cat.system.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.AppTree;

@RestController
@RequestMapping("/apptree")
public class AppTreeHome extends BaseHome<AppTree> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6987335446356310743L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			AppTree entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			AppTree entity = new AppTree();
			entity.setClc(baseService.getCode("SYS_App_Tree", null, 4).trim());
			entity.setId(entity.getClc());
			return ResultBean.getSucess(entity);
		}
	}

	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String scId) {
		AppTree entity = new AppTree();
		AppTree superior = (AppTree) baseService.findById(AppTree.class, scId);
		entity.setScId(scId);
		entity.setScName(superior.getName());
		entity.setClc(baseService.getCode("SYS_App_Tree", scId, 4).trim());
		entity.setId(scId + entity.getClc());

		return ResultBean.getSucess(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(AppTree entity) throws Exception {
		if (!StringUtil.isEmpty(entity.getScId())) {
			AppTree sup = (AppTree) baseService.findById(AppTree.class, entity.getScId());

			entity.setLxh(sup.getLxh() * 10 + entity.getXh());
		} else {
			entity.setLxh(entity.getXh().longValue());
		}
		if(!StringUtil.isListEmpty(entity.getMulti_apes())) {
			entity.setMulti_ape(StringUtil.listToString(entity.getMulti_apes()));
		}
		entity.setWn(StringUtil.isEmpty(entity.getScWnName()) ? entity.getName() : entity.getScWnName() + "_" + entity.getName());
		baseService.save(entity);
		return ResultBean.getSucess(entity);
	}

}
