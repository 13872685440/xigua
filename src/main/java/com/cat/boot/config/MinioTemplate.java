package com.cat.boot.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

@Component
@PropertySource("classpath:minio.properties")
public class MinioTemplate implements InitializingBean {
    /**
     * minio地址+端口号
     */
    @Value("${minio.url}")
    private  String url;
    /**
     * minio用户名
     */
    @Value("${minio.accessKey}")
    private  String accessKey;
    /**
     * minio密码
     */
    @Value("${minio.secretKey}")
    private  String secretKey;
    /**
     * 文件桶的名称
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.minioClient = new MinioClient.Builder().endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) {
    	try {
    		boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    		if (!isExist) {
    			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    		}
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    public List<Bucket> getAllBuckets() {
    	try {
    		return minioClient.listBuckets();
        }catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    public Optional<Bucket> getBucket(String bucketName) {
    	try {
    		return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    	}catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) {
    	try {
    		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    	}catch(Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    public List getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
    	try {
	        List<Item> list = new ArrayList<>();
	        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
	        if (objectsIterator != null) {
	            Iterator<Result<Item>> iterator = objectsIterator.iterator();
	            if (iterator != null) {
	                while (iterator.hasNext()) {
	                    Result<Item> result = iterator.next();
	                    Item item = result.get();
	                    list.add(item);
	                }
	            }
	        }
	
	        return list;
    	}catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
    	try {
    		if (StringUtils.isEmpty(bucketName)) {
    			bucketName = this.bucketName;
    		}
    		String x = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires).method(Method.GET).build());
    		System.out.println(x);
    		return x;   	
    	}catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public GetObjectResponse  getObject(String bucketName, String objectName) {
    	try {
	        if (StringUtils.isEmpty(bucketName)) {
	            bucketName = this.bucketName;
	        }
	        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    	}catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * 获取文件
     * @param bucketName
     * @param objectName
     * @return
     */
    public StatObjectResponse statObject(String bucketName, String objectName) {
    	try {
	        if (StringUtils.isEmpty(bucketName)) {
	            bucketName = this.bucketName;
	        }
	        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    	}catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName,InputStream stream){
    	try {
	        if (StringUtils.isEmpty(bucketName)) {
	            bucketName = this.bucketName;
	        }
	        createBucket(bucketName);
	        //minioClient.deleteBucketEncryption(DeleteBucketEncryptionArgs.builder().bucket(bucketName).build());
	        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, -1, 1024 * 1024 * 10).contentType("image/jpeg").build());
    	}catch(Exception e) {
        	e.printStackTrace();
        }
    }


    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) {
    	try {
	    	if (StringUtils.isEmpty(bucketName)) {
	            bucketName = this.bucketName;
	        }
	        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    	}catch(Exception e) {
        	e.printStackTrace();
        }
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
  
}