package com.cat.dictionary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.boot.util.ExcelImportUtil;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.dictionary.model.Canton;
import com.cat.file.jsonbean.ResultFileBean;

@RestController
@Scope("prototype")
@RequestMapping("/canton")
public class CantonQuery extends BaseNqtQuery<Canton>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5757013997340700553L;
	
	@Override
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		if(StringUtil.isMapContainsKey(parms.getParams(), "code")) {
			String code = (String)parms.getParams().get("code");
			if(code.endsWith("000000")) {
				parms.getParams().replace("code", code.substring(0, 6));
			} else if(code.endsWith("000")) {
				parms.getParams().replace("code", code.substring(0, 9));
			}
		}
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, results));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id) {
		// 查询是否有下级
		List<Canton> cantons = (List<Canton>)baseService.getList("Canton", null, true,NameQueryUtil.setParams("scId",id));
		if(!StringUtil.isListEmpty(cantons)) {
			return ResultBean.getResultBean("400", "", "有下级关联，不能删除");
		}
		return super.delete(id);
	}
	
	@RequestMapping(value = "/improt", method = { RequestMethod.POST })
	public String improt(@RequestParam(value = "file") MultipartFile file) throws Exception {
		List<Map<String, Object>> lists = ExcelImportUtil.readXls2(file);
		if(!StringUtil.isListEmpty(lists)) {
			for (Map<String, Object> map : lists) {
				String extendField = (String)map.get("WGGL010040");
				String id = (String)map.get("WGGL010030");
				String name = (String)map.get("WGGL010020");
				Canton canton = new Canton();
				canton.setId(id);
				canton.setName(name);
				canton.setWn(name);
				canton.setLeaf(false);
				canton.setExtendField(extendField);
				if("002".equals(extendField)) {
					// 第二级 找寻其上级
					String scId = id.substring(0, 6) + "000000";
					canton.setScId(scId);
				} else if("003".equals(extendField)){
					// 第三级 找寻其上级
					String scId = id.substring(0, 9) + "000";
					canton.setScId(scId);
					canton.setLeaf(true);
				}
				baseService.noAnnotationSave(canton, false);
			}
		}
		return ResultFileBean.getSucess("导入成功");
	}

}
