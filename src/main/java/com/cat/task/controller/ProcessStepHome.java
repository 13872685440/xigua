package com.cat.task.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.StringUtil;
import com.cat.task.model.ProcessStep;

@RestController
@RequestMapping("/processstep")
public class ProcessStepHome extends BaseHome<ProcessStep>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 281659191201833789L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			ProcessStep entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			ProcessStep entity = new ProcessStep();
			entity.setClc(baseService.getCode("Task_Process_Step", null, 4).trim());
			entity.setId(entity.getClc());
			return ResultBean.getSucess(entity);
		}
	}
	
	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String scId) {
		ProcessStep entity = new ProcessStep();
		ProcessStep superior = (ProcessStep) baseService.findById(ProcessStep.class, scId);
		entity.setScId(scId);
		entity.setScName(superior.getName());
		entity.setClc(baseService.getCode("Task_Process_Step", scId, 4).trim());
		entity.setId(scId + entity.getClc());

		return ResultBean.getSucess(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProcessStep entity) throws Exception {
		if(entity.getId().length()>4) {
			entity.setClc(entity.getId().substring(4,8));
		} else {
			entity.setClc(entity.getId());
		}
		if("1".equals(entity.getShr_type())){
			entity.setUserIds(StringUtil.listToString(entity.getUser_s()));
			
			entity.setRole_s(null);
			entity.setRole_type(null);
			entity.setOrganIds(null);
			entity.setLd_roles(null);
		} else if("2".equals(entity.getShr_type())) {
			entity.setUserIds(null);
			
			entity.setRoles(StringUtil.listToString(entity.getRole_s()));
			if("3".equals(entity.getRole_type())) {
				entity.setOrganIds(StringUtil.listToString(entity.getOrgan_s()));
			} else {
				entity.setOrgan_s(null);
			}
			entity.setLd_roles(null);
		} else if("3".equals(entity.getShr_type())) {
			entity.setUserIds(null);
			entity.setRole_s(null);
			entity.setRole_type(null);
			entity.setOrganIds(null);
			entity.setLd_roles(StringUtil.listToString(entity.getLd_role_s()));
		} else {
			entity.setUserIds(null);
			entity.setLd_roles(null);
			entity.setRole_s(null);
			entity.setRole_type(null);
			entity.setOrganIds(null);
		}
		baseService.save(entity);
		return ResultBean.getSucess(entity);
	}
}
