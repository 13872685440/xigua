package com.cat.dictionary.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.StringUtil;
import com.cat.dictionary.model.Canton;

@RestController
@RequestMapping("/canton")
public class CantonHome extends BaseHome<Canton>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2818650234529546380L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Canton entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			return ResultBean.getSucess(new Canton());
		}
	}
	
	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String id) {
		Canton entity = new Canton();

		entity.setScId(id);

		return ResultBean.getSucess(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Canton entity) throws Exception {
		// 如果没有上级
		if(StringUtil.isEmpty(entity.getScId())) {
			entity.setLeaf(false);
			entity.setExtendField("001");
		} else {
			Canton c = (Canton)baseService.findById(Canton.class, entity.getScId());
			if("002".equals(c.getExtendField())){
				entity.setLeaf(true);
				entity.setExtendField("003");
			} else if("001".equals(c.getExtendField())){
				entity.setLeaf(false);
				entity.setExtendField("002");
			}
		}
		baseService.save(entity);
		return ResultBean.getSucess("sucess");
	}
}
