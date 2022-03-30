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
import com.cat.system.model.Organ;

@RestController
@RequestMapping("/organ")
public class OrganHome extends BaseHome<Organ>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -921396825886502260L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Organ entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			Organ entity = new Organ();
			entity.setClc(baseService.getCode("Org_Organ", null, 4).trim());
			entity.setId(entity.getClc());
			return ResultBean.getSucess(entity);
		}
	}
	
	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String scId) {
		Organ entity = new Organ();
		Organ superior = (Organ) baseService.findById(Organ.class, scId);
		entity.setScId(scId);
		entity.setScName(superior.getName());
		entity.setClc(baseService.getCode("Org_Organ", scId, 4).trim());
		entity.setId(scId + entity.getClc());

		return ResultBean.getSucess(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Organ entity) throws Exception {
		entity.setWn(StringUtil.isEmpty(entity.getScWnName()) ? entity.getName() : entity.getScWnName() + "_" + entity.getName());
		baseService.save(entity);
		return ResultBean.getSucess(entity);
	}
}
