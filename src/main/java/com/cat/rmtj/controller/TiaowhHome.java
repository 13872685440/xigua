package com.cat.rmtj.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.StringUtil;
import com.cat.rmtj.model.Tiaowh;

@RestController
@RequestMapping("/tiaowh")
public class TiaowhHome extends BaseHome<Tiaowh>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -921396825886502260L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Tiaowh entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			Tiaowh entity = new Tiaowh();
			entity.setClc(baseService.getCode("rmtj_tiaowh", null, 4).trim());
			entity.setId(entity.getClc());
			return ResultBean.getSucess(entity);
		}
	}
	
	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String scId) {
		Tiaowh entity = new Tiaowh();
		Tiaowh superior = (Tiaowh) baseService.findById(Tiaowh.class, scId);
		entity.setScId(scId);
		entity.setScName(superior.getName());
		entity.setClc(baseService.getCode("rmtj_tiaowh", scId, 4).trim());
		entity.setId(scId + entity.getClc());

		return ResultBean.getSucess(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Tiaowh entity) throws Exception {
		//entity.setWn(StringUtil.isEmpty(entity.getScWnName()) ? entity.getName() : entity.getScWnName() + "_" + entity.getName());
		baseService.save(entity);
		return ResultBean.getSucess(entity);
	}
}
