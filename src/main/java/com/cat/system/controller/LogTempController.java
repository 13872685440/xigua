package com.cat.system.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cat.boot.catconst.RedisConst;
import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.IResultConvert;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQueryRedis;
import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.StringUtil;

@SuppressWarnings("rawtypes")
@RestController
@Scope("prototype")
@RequestMapping("/logtemp")
public class LogTempController extends BaseNqtQueryRedis implements IResultConvert{

	/**
	 * 
	 */
	private static final long serialVersionUID = -55052767065446327L;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		if(parms.getParams().containsKey("name")) {
			excuteQuery(parms.getParams().get("name") + "_log_temp_"+CalendarUtil.getYyyyMmDd(Calendar.getInstance()),RedisConst.log_db,parms);
		} else {
			excuteQuery("log_temp_"+CalendarUtil.getYyyyMmDd(Calendar.getInstance()),RedisConst.log_db,parms);
		}
		return ResultBean.getSucess(new QueryResultBean(parms, results));
	}

	@Override
	public List toResult(List ls) {
		List<JSONObject> os = new ArrayList<JSONObject>();
		if(!StringUtil.isListEmpty(ls)) {
			for (Object object : ls) {
				JSONObject o = JSONObject.parseObject((String)object);
				os.add(o);
			}
		}
		return os;
	}
}
