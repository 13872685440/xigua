package com.cat.boot.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

public final class UrlUtil {

	private String url;
	private String bookmark = "";
	private String charset = null;
	private java.util.Map<String, String> hm = null;

	@SuppressWarnings("unused")
	private UrlUtil() {
	}

	/**
	 * 以默认的utf-8编码的形式构建一个Url类的实例.
	 *
	 * @param url
	 *            要处理的URL资源地址。
	 */
	public UrlUtil(String url) {
		this(url, "utf-8");
	}

	/**
	 * 构建一个Url类的实例.
	 *
	 * @param url
	 *            要处理的URL资源地址。
	 * @param charset
	 *            指定数据处理的编码类型
	 */
	public UrlUtil(String url, String charset) {
		if (url.indexOf("#") == -1) {
			this.url = url;
		} else {
			bookmark = url.substring(url.indexOf("#"));
			this.url = url.substring(0, url.indexOf("#"));
		}
		this.charset = charset;
	}

	/**
	 * 从Url对象所指向的URL资源下载文件并保存到指定的文件.
	 *
	 * @param file
	 *            要保存的文件.
	 * @return File 下载保存之后的文件对象。
	 * @throws IOException
	 *             当发生I/O读写异常，或者所下载保存的文件扩展名为本系统禁止的扩展名时，将抛出此异常.
	 */
	@SuppressWarnings("unused")
	public File download(File file) throws IOException {
		String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
		URL urlX = new URL(this.toString());
		BufferedInputStream bis = new BufferedInputStream(urlX.openStream());
		byte[] bytes = new byte[100];
		FileOutputStream bos = new FileOutputStream(file);
		int len;
		while ((len = bis.read(bytes)) > 0) {
			bos.write(bytes, 0, len);
		}
		bis.close();
		bos.flush();
		bos.close();
		return file;
	}

	/**
	 * 从URL地址中删除一个或多个参数变量.
	 *
	 * @param names
	 *            要删除的参数变量的名称，如果要删除多个变量，变量名称之间用逗号，分号或空隔分隔。
	 * @return Url 当前的Url对象
	 */
	public UrlUtil del(String names) {
		String nameA[] = names.replaceAll(" ", "").split(",");
		for (String n : nameA) {
			if (n == null || n.equals("")) {
				continue;
			}
			String s = n + "=";
			if (url.indexOf("?" + s) != -1) {
				int iEnd = url.indexOf("&", url.indexOf("?" + s));
				if (iEnd == -1) {
					url = url.substring(0, url.indexOf("?"));
				} else {
					url = url.substring(0, url.indexOf("?") + 1) + url.substring(iEnd + 1);
				}
			} else if (url.indexOf("&" + s) != -1) {
				int iEnd = url.indexOf("&", url.indexOf("&" + s) + s.length() + 1);
				if (iEnd == -1) {
					url = url.substring(0, url.indexOf("&" + s));
				} else {
					url = url.substring(0, url.indexOf("&" + s)) + url.substring(iEnd);
				}
			}
		}
		return this;
	}

	/**
	 * 获取URL中的指定参数变量的值.
	 *
	 * @param name
	 *            要获取的变量值，如果该变量不存在，将返回空字符串("").
	 * @return String 要获取的变量字符串名称。
	 */
	public String get(String name) throws Exception {
		String rtn;
		String s = name + "=";
		String b;
		if (url.indexOf("?" + s) != -1) {
			b = "?" + s;
		} else if (url.indexOf("&" + s) != -1) {
			b = "&" + s;
		} else {
			return "";
		}
		int iEnd = url.indexOf("&", url.indexOf(b) + b.length());
		if (iEnd == -1) {
			rtn = url.substring(url.indexOf(b) + b.length());
		} else {
			rtn = url.substring(url.indexOf(b) + b.length(), iEnd) + "";
		}
		try {
			return charset == null ? rtn : java.net.URLDecoder.decode(rtn, charset);
		} catch (UnsupportedEncodingException e) {
			throw new Exception("未知的编码类型" + charset);
		}
	}

	/**
	 * 获取URL地址中的锚信息. 如果未找到锚,将返回空字符串("").
	 *
	 * @return String 锚字符串（“锚”是指URL地址“#”后面的字符）
	 */
	@SuppressWarnings("unused")
	public String getAnchor() {
		return bookmark;
	}

	public UrlUtil post(String name, String value) throws Exception {
		if (hm == null) {
			hm = new java.util.HashMap<String, String>();
		}
		try {
			hm.put(name, charset == null ? value : java.net.URLEncoder.encode(value, charset));
		} catch (UnsupportedEncodingException e) {
			throw new Exception("未知的编码类型" + charset);
		}
		return this;
	}

	public UrlUtil set(String name, String value) throws Exception {
		try {
			if (charset != null) {
				value = java.net.URLEncoder.encode(value, charset);
			}
		} catch (UnsupportedEncodingException e) {
			throw new Exception("未知的编码类型" + charset);
		}
		String s = name + "=";
		String b = null;
		if (url.indexOf("?" + s) != -1) {
			b = "?" + s;
		} else if (url.indexOf("&" + s) != -1) {
			b = "&" + s;
		}
		if (b == null) {
			url += (url.indexOf("?") == -1 ? "?" : "&") + s + value;
		} else {
			int iEnd = url.indexOf("&", url.indexOf(b) + b.length());
			if (iEnd == -1) {
				url = url.substring(0, url.indexOf(b) + b.length()) + value;
			} else {
				url = url.substring(0, url.indexOf(b) + b.length()) + value + url.substring(iEnd);
			}
		}
		return this;
	}

	/**
	 * 添加或修改URL中的变量的值. 如果指定的变量存在，将修改其值，如果不存在，将添加该变量.
	 *
	 * @param name
	 *            要设置或添加的变量名称.
	 * @param value
	 *            要设置的变量的整型值.
	 * @return Url 当前的Url对象。
	 */
	public UrlUtil set(String name, int value) throws Exception {
		return set(name, String.valueOf(value));
	}

	/**
	 * 用于设置URL地址的文件路径信息. 即改变URL的问号(?)前面的字符串.
	 *
	 * @param fileVPath
	 *            要设置的URL文件路径信息.如果该参数中含有问号(?),将取问号前面的字符串进行操作.
	 * @return Url 当前的Url对象。
	 */
	@SuppressWarnings("unused")
	public UrlUtil setF(String fileVPath) {
		if (fileVPath.indexOf("?") != -1) {
			fileVPath = fileVPath.substring(0, fileVPath.indexOf("?"));
		}
		if (url.indexOf("?") == -1) {
			url = fileVPath;
		} else {
			url = fileVPath + url.substring(url.indexOf("?"));
		}
		return this;
	}

	/**
	 * 向Url对象所指向的URL资源发送请求(POST或GET),并获取返回的HTML文本数据. <br/>
	 * 说明：如果在调用此方法之前已经调用过{@link #post(String, String)}方法,那么系统将以POST方式发送请求,
	 * 否则将使用默认的GET方式发送请求.
	 *
	 * @return String 返回所请求URL资源的HTML内容.
	 *         如果返回的内容出现乱码,请检查构造此Url对象时所使用的charset参数值是否与所请求的URL资源所使用的编码一致.
	 */
	public String getHtml() throws IOException {
		if (hm == null) {
			return sendGet();
		} else {
			return sendPost();
		}
	}

	/**
	 * 返回将要(或者已经)向URL地址POST过去的数据内容.
	 *
	 * @return 形如：name1=value1&name2=value2 ... 格式组成的字符串.
	 *         其中的value值已经根据构造方法所指定的charset进行了编码处理.
	 * @see #post(String, String)
	 */
	public String getPost() {
		if (hm == null) {
			return "";
		}
		String s = hm.toString().replaceAll(", ", "&");
		return s.substring(1, s.length() - 1);
	}

	/**
	 * 返回当前Url对象所表示的URL地址字符串.
	 *
	 * @return String URL地址字符串.
	 */
	@Override
	public String toString() {
		return url + bookmark;
	}

	private String sendGet() throws IOException {
		URL urlX = new URL(this.toString());
		InputStream in = urlX.openStream();
		String content = pipe(in, charset);
		in.close();
		return content;
	}

	private String sendPost() throws IOException {
		String tCharset = charset == null ? "utf-8" : charset;
		String data = getPost();
		URL urlX = new URL(url);
		URLConnection conn = urlX.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), tCharset);
		out.write(data);
		out.flush();
		out.close();
		InputStream in = conn.getInputStream();
		String content = pipe(in, tCharset);
		in.close();
		return content;
	}

	public static String postJson(String url, String data) throws Exception {
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

	private static String pipe(InputStream in, String charset) throws IOException {
		StringBuilder s = new StringBuilder();
		if (charset == null || "".equals(charset)) {
			charset = "utf-8";
		}
		String rLine;
		BufferedReader bReader = new BufferedReader(new InputStreamReader(in, charset));
		while ((rLine = bReader.readLine()) != null) {
			int str_len = rLine.length();
			if (str_len > 0) {
				s.append(rLine);
			}
		}
		in.close();
		return s.toString();
	}
}