package com.liujie.pictureBackend.service;

import com.liujie.pictureBackend.config.CosClientConfig;
import com.qcloud.cos.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;


/**
 * 统一图片文件处理类
 */
@Service
@Slf4j
public class FilePictureService {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private CosService cosService;

    //文件上传
    public PutObjectResult uploadPicture(String key, File file)
    {

    }
    //文件下载

    //图片识别
}

