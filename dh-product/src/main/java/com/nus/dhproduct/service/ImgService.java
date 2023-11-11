package com.nus.dhproduct.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImgService {

  @Value("${minio.access_key}")
  public String accessKey;

  @Value("${minio.secret_key}")
  public String secretKey;

  @Value("${minio.bucket}")
  public String bucket;

  @Value("${minio.end_point}")
  public String endPoint;


  public String uploadImage(MultipartFile file) {
    String objectName = UUID.randomUUID().toString();
    try {
      InputStream fileInputStream = file.getInputStream();
      //1.创建minio链接客户端
      MinioClient minioClient = MinioClient.builder().credentials(accessKey, secretKey).endpoint(endPoint).build();
      //2.上传
      PutObjectArgs putObjectArgs = PutObjectArgs.builder()
          .object(objectName+".png")
          .contentType("image/jpg")
          .bucket(bucket)
          .stream(fileInputStream, fileInputStream.available(), -1)
          .build();
      minioClient.putObject(putObjectArgs);

    } catch (Exception ex) {
      return null;
    }
    return endPoint + "/" + bucket + "/" + objectName + ".png";
  }
}
