package com.cat.demo.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.StringUtil;
import com.cat.demo.model.DemoTree;

@RestController
@RequestMapping("/demotree")
public class DemoTreeHome extends BaseHome<DemoTree>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2071460009742632727L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			DemoTree entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			DemoTree entity = new DemoTree();
			entity.setId(baseService.getCode("Demo_Tree", null, 4).trim());
			entity.setClc(entity.getId());
			return ResultBean.getSucess(entity);
		}
	}
	
	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String scId) {
		DemoTree entity = new DemoTree();
		DemoTree superior = (DemoTree) baseService.findById(DemoTree.class, scId);
		entity.setScId(scId);
		entity.setScName(superior.getInput());
		entity.setClc(baseService.getCode("Demo_Tree", scId, 4).trim());
		entity.setId(scId + entity.getClc());

		return ResultBean.getSucess(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(DemoTree entity) throws Exception {
		if(!StringUtil.isListEmpty(entity.getMul_selects())) {
			entity.setMul_select(StringUtil.listToString(entity.getMul_selects()));
		}
		if(!StringUtil.isListEmpty(entity.getMul_trees())) {
			entity.setMul_tree(StringUtil.listToString(entity.getMul_trees()));
		}
		if(!StringUtil.isListEmpty(entity.getCheckboxs())) {
			entity.setCheckbox(StringUtil.listToString(entity.getCheckboxs()));
		}
		baseService.save(entity);
		
		minioFileController.replaceTmpId(entity.getTmpId(), entity.getId(), "com.cat.demo.model.DemoTree");
		return ResultBean.getSucess(entity);
	}
}
