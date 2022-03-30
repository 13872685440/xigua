package com.cat.dictionary.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.PropParamBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.dictionary.model.Dictionary;

@RestController
@RequestMapping("/dictionary")
public class DictionaryHome extends BaseHome<Dictionary> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7833244390007651087L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Dictionary entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			Dictionary entity = new Dictionary();
			entity.setClc(baseService.getCode("Data_Dictionary", null, 4).trim());
			entity.setId(entity.getClc());
			return ResultBean.getSucess(entity);
		}
	}

	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String scId) {
		Dictionary entity = new Dictionary();
		Dictionary superior = (Dictionary) baseService.findById(Dictionary.class, scId);
		entity.setScId(scId);
		entity.setScName(superior.getName());
		entity.setClc(baseService.getCode("Data_Dictionary", scId, 4).trim());
		entity.setId(scId + entity.getClc());
		entity.setTypeCode(superior.getTypeCode());

		return ResultBean.getSucess(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Dictionary entity) throws Exception {
		entity.setWn(StringUtil.isEmpty(entity.getScWnName()) ? entity.getName() : entity.getScWnName() + "_" + entity.getName());
		baseService.save(entity);
		baseService.update4Prop("Data_Dictionary", true, NameQueryUtil.setParams("type_code", entity.getTypeCode()),
				NameQueryUtil.getBeans(new PropParamBean("like", "and", "id", entity.getId() + "%")));
		return ResultBean.getSucess("sucess");
	}

}
