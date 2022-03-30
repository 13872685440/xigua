package com.cat.file.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cat.boot.catconst.RedisConst;
import com.cat.boot.config.JedisUtil;
import com.cat.boot.config.MinioTemplate;
import com.cat.boot.exception.CatException;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.file.jsonbean.FileBean;
import com.cat.file.jsonbean.FileInfoBean;
import com.cat.file.jsonbean.ResultFileBean;
import com.cat.file.jsonbean.UploadBean;
import com.cat.file.model.FileInfo;
import com.cat.file.model.FileRecord;
import com.cat.file.util.FileUtils;

@RestController
@RequestMapping("/miniofile")
public class MinioFileController {

	@Autowired
	private BaseService baseService;
	
	@Autowired
	public JedisUtil jedisUtil;
	
	@Autowired
    private MinioTemplate minioTemplate;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String load(UploadBean bean) throws CatException, IOException {
		Map<Object, Object> maps = NameQueryUtil.setParams("ebcn", bean.getEbcn(), "keyValue", StringUtil.isListEmpty(bean.getKeyValues()) ? 
				bean.getKeyValue() : bean.getKeyValues(),
				"sign", bean.getSign());
		List<FileRecord> datas = (List<FileRecord>) baseService.getList("FileRecord", "o.ct asc", true, maps);
		List<FileInfoBean> beans = new ArrayList<FileInfoBean>();
		if(!StringUtil.isListEmpty(datas)) {
			for (FileRecord wd : datas) {
				String path = minioTemplate.getObjectURL(jedisUtil.getMinioPath("uploadpath"),wd.getFileInfo().getUrlName(),60*60);
				FileInfoBean fileinfobean = new FileInfoBean();
				if(!bean.isIsapp()) {
					fileinfobean = FileInfoBean.setThis(wd);
				} else {
					fileinfobean.setId(wd.getId());
					fileinfobean.setName(wd.getFileInfo().getOriginalName());
					fileinfobean.setFile_ext(wd.getFileInfo().getFileExt());
				}
				fileinfobean.setUrl(path);
				beans.add(fileinfobean);
			}
			return ResultBean.getSucess(beans);
		} else {
			return ResultBean.getSucess(beans);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile file, UploadBean bean)
			throws CatException, IOException {
		FileBean filebean = new FileBean(file);
		FileRecord wd = 
				saveFile(response, bean, filebean);
		if(!StringUtil.isEmpty(bean.getKeyProp()) && "flutter".equals(bean.getKeyProp())) {
			if (wd!=null) {
				String path = minioTemplate.getObjectURL(jedisUtil.getMinioPath("uploadpath"),
						wd.getFileInfo().getUrlName(),60*60);
				FileInfoBean fileinfobean = new FileInfoBean();
				if(!bean.isIsapp()) {
					fileinfobean = FileInfoBean.setThis(wd);
				} else {
					fileinfobean.setId(wd.getId());
					fileinfobean.setName(wd.getFileInfo().getOriginalName());
					fileinfobean.setFile_ext(wd.getFileInfo().getFileExt());
					fileinfobean.setName(URLEncoder.encode(wd.getFileInfo().getOriginalName(),"UTF-8"));
				}
				fileinfobean.setUrl(path);
				return ResultBean.getSucess(fileinfobean);
			} else {
				return ResultBean.getResultBean("400", "上传失败", "");
			}
		} else {
			if (wd!=null) {
				return ResultFileBean.getSucess(FileInfoBean.setThis(wd));
			} else {
				return ResultFileBean.getError();
			}
		}
	}
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @RequestParam String blwdId, @RequestParam String isOnLine)
			throws Exception {
		FileRecord wd = (FileRecord) baseService.findById(FileRecord.class, blwdId);
		if (wd == null) {
			return;
		} else {
			InputStream in = minioTemplate.getObject(jedisUtil.getMinioPath("uploadpath"),wd.getFileInfo().getUrlName());
			FileInfo info = wd.getFileInfo();
			FileUtils.download(response, in, info.getOriginalName(),isOnLine);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public String deleteFile(@RequestParam String id) {
		FileRecord data = (FileRecord) baseService.findById(FileRecord.class, id);
		FileInfo info = (FileInfo)data.getFileInfo();
		minioTemplate.removeObject(jedisUtil.getMinioPath("uploadpath"), info.getUrlName());
		
		baseService.delete(data);
		baseService.delete(info);
		
		return ResultBean.getSucess("");
	}
	
	@SuppressWarnings("unchecked")
	public String deleteFile_Entity(String keyValue,String ebcn) {
		List<FileRecord> datas = (List<FileRecord>) baseService.getList("FileRecord", null, true
				,NameQueryUtil.setParams("keyValue",keyValue,"ebcn",ebcn));
		if(!StringUtil.isListEmpty(datas)) {
			for (FileRecord data : datas) {
				FileInfo info = (FileInfo)data.getFileInfo();
				minioTemplate.removeObject(jedisUtil.getMinioPath("uploadpath"), info.getUrlName());
				
				baseService.noAnnotationDelete(data);
				baseService.noAnnotationDelete(info);
			}
		}
		
		return ResultBean.getSucess("");
	}
	
	@RequestMapping(value = "/viewimage", method = RequestMethod.GET)
	public void viewimage(@RequestParam String id,@RequestParam String token, HttpServletResponse response) throws Exception {
		if (StringUtil.isEmpty(token) || !jedisUtil.exists(token,RedisConst.token_db)) {

			CatException.responseData(response, 401, "认证超时");
			throw new CatException("认证超时");
		}
		InputStream in = null;
		try {
			FileRecord wd = (FileRecord) baseService.findById(FileRecord.class, id);
			if (wd == null) {
				return;
			} else {
				
			}
			in = minioTemplate.getObject(jedisUtil.getMinioPath("uploadpath"),wd.getFileInfo().getUrlName());
				
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = in.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void replaceTmpId(String tmpId, String entityId, String ebcn) {
		baseService.update("FileRecord", "file", "FileRecord_replaceTmpId",
				NameQueryUtil.setParams("entityId", entityId, "tmpId", tmpId, "ebcn", ebcn));
	}

	private FileRecord saveFile(HttpServletResponse response, UploadBean bean, FileBean filebean) throws IOException {
		String path = bean.getEbcn() + "/" + bean.getKeyValue() + "/"
				+ Calendar.getInstance().getTimeInMillis() + "/" + filebean.getTitle();
		String sign = bean.getSign();
		String uploadDir = jedisUtil.getMinioPath("uploadpath");

		return saveWd(response, filebean, path, uploadDir, sign, bean);
	}

	private FileRecord saveWd(HttpServletResponse response, FileBean filebean, String path, String uploadDir, String sign,
			UploadBean bean) throws IOException {
		byte[] bytes = filebean.getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		
		minioTemplate.putObject(uploadDir, path , inputStream);
		
		FileInfo fileInfo = new FileInfo();

		fileInfo.setOriginalName(filebean.getTitle());
		fileInfo.setUrlName(path);
		fileInfo.setSize(Double.valueOf(filebean.getSize()));
		baseService.save(fileInfo, false);

		FileRecord fileRecord = new FileRecord();
		fileRecord.setSign(sign);
		fileRecord.setFile_id(fileInfo.getId());
		
		fileRecord.setEbcn(bean.getEbcn());

		fileRecord.setKeyValue(bean.getKeyValue());

		baseService.save(fileRecord, false);

		return fileRecord;
	}
}
