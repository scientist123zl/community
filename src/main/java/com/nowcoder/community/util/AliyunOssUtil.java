package com.nowcoder.community.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Component
public class AliyunOssUtil {
    @Value("${Aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${Aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${Aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${Aliyun.oss.bucket.header.name}")
    private String headerBucketName;

    @Value("${Aliyun.oss.bucket.header.url}")
    private String headerBucketUrl;

    @Value("${Aliyun.oss.bucket.share.name}")
    private String shareBucketName;

    @Value("${Aliyun.oss.bucket.share.url}")
    private String shareBucketUrl;



    private OSS ossClient;

    private OSS getOSSClient() {
        if(ossClient==null){
            synchronized(AliyunOssUtil.class){
                if(ossClient==null){
                    ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                }
            }
        }
        return ossClient;
    }



    public String uploadHeaderImage(InputStream inputStream,String fileName) {
//        String generatedFileName;
//        String[] filePaths = fileName.split("\\.");
//        if (filePaths.length > 1) {
//            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
//        } else {
//            return null;
//        }

        PutObjectResult response = getOSSClient().putObject(headerBucketName, fileName, inputStream);
        if (response != null) {
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            URL url = getOSSClient().generatePresignedUrl(headerBucketName, fileName, expiration);
            return url.toString();
        } else {
            return null;
        }
    }

    public String uploadShareImage(InputStream inputStream,String fileName) {
//        String generatedFileName;
//        String[] filePaths = fileName.split("\\.");
//        if (filePaths.length > 1) {
//            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
//        } else {
//            return null;
//        }

        PutObjectResult response = getOSSClient().putObject(shareBucketName, fileName, inputStream);
        if (response != null) {
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            URL url = getOSSClient().generatePresignedUrl(headerBucketName, fileName, expiration);
            return url.toString();
        } else {
            return null;
        }

    }
}
