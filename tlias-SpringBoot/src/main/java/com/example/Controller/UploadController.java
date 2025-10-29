package com.example.Controller;

import com.example.Entity.AliOSSProperties;
import com.example.Entity.Result;
import com.example.Utils.AliyunOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UploadController {

//    /**
//     * 文件上传-本地存储
//     * @param username
//     * @param age
//     * @param file
//     * @return
//     */
//    @PostMapping("/upload")
//    public Result upload(String username, Integer  age, MultipartFile file) throws IOException {
//        //不改文件名
////        file.transferTo(new File("D:/"+ file.getOriginalFilename()));//保存文件到本地
////        return Result.success(file.getOriginalFilename());
//
//        //随机生成新文件名
//        String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        file.transferTo(new File("D:/"+ newFileName));
//        return Result.success(newFileName);
//    }


    /*
    阿里云文件上传
     */

//    //阿里云配置信息,在application.properties中配置
//    @Value("${aliyun.endpoint}")
//    private String endpoint;
//    @Value("${aliyun.bucketName}")
//    private String bucketName;

    //阿里云配置信息，在实体类中配置
    @Autowired
    private AliOSSProperties aliOSSProperties;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        //获取原始文件名，截取后缀。
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = AliyunOSSUtils.upload(aliOSSProperties.getEndpoint(),aliOSSProperties.getBucketName(), file.getBytes(), extName);
        return Result.success(url);//返回文件上传后的URL地址
    }
}
