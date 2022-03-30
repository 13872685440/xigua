package com.cat.dictionary.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.PropParamBean;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.dictionary.jsonbean.DictionaryParmBean;
import com.cat.dictionary.model.Dictionary;

@RestController
@Scope("prototype")
@RequestMapping("/dictionary")
public class DictionaryQuery extends BaseNqtQuery<Dictionary> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8470029593371118819L;

	@Override
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, results));
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id,@RequestParam String app_code) {
		return super.delete(id,app_code);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String get(DictionaryParmBean bean) {
		// 查询根目录
		Dictionary d = (Dictionary) baseService.getSimple("Dictionary", "", true,
				NameQueryUtil.setParams("typeCode", bean.getTypeCode(), "scId", null));
		if (d == null) {
			return ResultBean.getResultBean("400", "查询参数错误", null);
		}
		List<PropParamBean> beans = new ArrayList<PropParamBean>();
		if (!StringUtil.isListEmpty(bean.getFilter())) {
			beans.add(new PropParamBean("not in", "and", "id", bean.getFilter(d.getTypeCode())));
		}
		beans.add(new PropParamBean("=", "and", "typeCode", bean.getTypeCode()));
		beans.add(new PropParamBean("", "and", "scId", "not null"));
		List<Dictionary> ds = (List<Dictionary>) baseService.getList("Dictionary", "o.xh asc", true, beans);
		return ResultBean.getSucess(ds);
	}
}
