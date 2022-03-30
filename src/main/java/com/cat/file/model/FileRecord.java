package com.cat.file.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;

@Entity
@Table(name = "File_Record")
@BatchSize(size = 50)
public class FileRecord extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7972267901041557010L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/** 上传标记 用于进行简易上传（不使用YeWuBanLiZlLx）时 同一个ebcn和keyvalue上传时进行标记位置 */
	/** 使用A1-A99 B1-B99的标记方式 A表示文档存储在服务器上 B表示文档存储在FTP上 */
	@Column(name = "a_sign", length = 200)
	private String sign;
	
	@Column(length = 40)
	private String file_id;
	
	/** 序号 */
	@Column
	private long xh = 10;

	/** 实体Bean类名 Entity Bean Class Name */
	@Column(nullable = false, length = 200)
	private String ebcn;

	/** 实体Bean关键域值 */
	@Column(nullable = false, length = 80)
	private String keyValue;

	/** 用来记录上传时的毫秒值 用来排序 */
	@Column(nullable = true, length = 20)
	private String timeInMillis;
	
	@Transient
	private FileInfo fileInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFile_id() {
		return file_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public long getXh() {
		return xh;
	}

	public void setXh(long xh) {
		this.xh = xh;
	}

	public String getEbcn() {
		return ebcn;
	}

	public void setEbcn(String ebcn) {
		this.ebcn = ebcn;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getTimeInMillis() {
		return timeInMillis;
	}

	public void setTimeInMillis(String timeInMillis) {
		this.timeInMillis = timeInMillis;
	}

	public FileInfo getFileInfo() {
		if(!StringUtil.isEmpty(this.file_id)) {
			FileInfo file = (FileInfo)getService().findById(FileInfo.class, this.file_id);
			return file;
		}
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
