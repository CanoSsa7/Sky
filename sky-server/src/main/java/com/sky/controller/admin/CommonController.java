package com.sky.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author CanoSsa7
 */
@RestControllerAdvice
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    Result<String> fileUpload(@RequestPart("file") MultipartFile file) throws IOException, ClientException {
        try {
            String originalFileName = file.getOriginalFilename();
            //截取原始文件名后缀
            String format = null;
            if (originalFileName != null) {
                format = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            //创建文件上传后的名字
            String newName = UUID.randomUUID().toString() + format;
            //上传文件，获取文件的公网url
            String fileUrl = aliOssUtil.upload(file.getBytes(), newName);
            return Result.success(fileUrl);
        }catch (Exception e){
            log.info("上传失败");
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

    }

}
