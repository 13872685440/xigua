package com.cat.boot.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.ExcelImportUtil;
import com.cat.boot.util.RadomUtil;
import com.cat.boot.util.StringUtil;

@Controller
@Scope("prototype")
@RequestMapping("/import")
public class ImportController {

	@Autowired
	private BaseService baseService;

	@RequestMapping(value = "/improtExcel", method = { RequestMethod.POST })
	public void improtExcel(@RequestParam(value = "fileField") MultipartFile file,
			@RequestParam(value = "table") String table, @RequestParam(value = "keyid") String keyid,
			@RequestParam(value = "type") String type, HttpServletResponse response) throws IOException {
		try {
			List<Map<String, Object>> lists = ExcelImportUtil.readXls(file);
			long i = 0;
			if (!StringUtil.isListEmpty(lists)) {
				// 如果是覆盖导入
				List<String> keys = new ArrayList<String>();
				if ("1".equals(type)) {
					Map<String, Object> tmp = lists.get(0);
					for (String key : tmp.keySet()) {
						String value = (String) tmp.get(key);
						if (!StringUtil.isEmpty(value) && "key".equals(value)) {
							keys.add(key);
						}
					}
				}
				lists.remove(0);
				for (Map<String, Object> map : lists) {
					boolean flag = save(table, keyid, map, keys);
					if (!flag) {
						i++;
					}
				}
			}
			if (i == 0) {
				response.getWriter().print("ok");
			} else {
				response.getWriter().print(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("error");
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean save(String table, String keyid, Map<String, Object> map, List<String> keys) {
		try {
			// 如果keys不为空 先删除 后插入
			if (!StringUtil.isListEmpty(keys)) {
				// 拼接delete字符串
				String deletesql = "delete " + table + " o where (1=1)";
				for (String key : keys) {
					deletesql = deletesql + " and o." + key + "=:" + key;
				}
				Query query = baseService.query(deletesql, true);
				for (String key : keys) {
					query.setParameter(key, map.get(key));
				}
				baseService.noAnnotationUpdate(query);
			}
			// 拼接insert字符串
			String sql = "insert into " + table + " (";
			for (String key : map.keySet()) {
				sql = sql + key + ",";
			}
			boolean hasId = false;
			if (!map.keySet().contains(keyid)) {
				hasId = true;
			}
			if (hasId) {
				sql = sql + "ID,";
			}
			sql = sql + "BE_CT,BE_crt_Uid,BE_ver) values (";
			for (String key : map.keySet()) {
				sql = sql + ":" + key + ",";
			}
			if (hasId) {
				sql = sql + ":id,";
			}
			sql = sql + ":ct,:uid,:ver) ";
			Query query = baseService.query(sql, true);
			if (hasId) {
				query.setParameter("id", RadomUtil.radom(22)
						+ String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(3, 13));
			}
			for (String key : map.keySet()) {
				query.setParameter(key, map.get(key));
			}
			query.setParameter("ct", CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance()))
					.setParameter("uid", "System").setParameter("ver", 0);
			baseService.noAnnotationUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
