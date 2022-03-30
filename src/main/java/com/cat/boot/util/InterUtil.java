package com.cat.boot.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Service;

@Service
public class InterUtil {
	
	public static String getUrl(String path,String... parms) {
		return path + "?" + setParams(parms);
	}

	public static String toget(String url) {
		System.out.println(url);
		UrlUtil util = new UrlUtil(url);
		try {
			return util.getHtml();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String topost(String url, String data) throws Exception {
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		RequestEntity entity = new StringRequestEntity(data, "application/json", "UTF-8");
		post.setRequestEntity(entity);
		httpclient.executeMethod(post);
		String info = null;
		int code = post.getStatusCode();
		if (code == HttpStatus.SC_OK) {
			info = new String(post.getResponseBodyAsString());
		}
		post.releaseConnection();
		return info;
	}

	public static String setParams(String... prams) {
        String url = "";
		for (int i = 0; i < prams.length; i++) {
			if (i % 2 == 0) {
				url = url + prams[i] + "=" + prams[i + 1];
			} else {
				url = url + "&";
			}
		}
		return url;
	}
}
