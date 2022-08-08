package com.cat.rmtj.controller;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.StringUtil;
import com.cat.rmtj.model.Shouli;

@RestController
@RequestMapping("/shouli")
public class ShouliHome extends BaseHome<Shouli>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2632363852244016655L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(HttpServletRequest request,@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Shouli entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			return ResultBean.getSucess(new Shouli());
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Shouli entity) throws Exception {
		if(StringUtil.isEmpty(entity.getSlsj())) {
			entity.setSlsj(CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance()));
		}
		baseService.save(entity);
		
		return ResultBean.getSucess(entity);
	}
}
