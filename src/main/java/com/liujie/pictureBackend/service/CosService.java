package com.liujie.pictureBackend.service;

import com.liujie.pictureBackend.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
@Slf4j
@Component
public class CosService {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    // ... 一些操作 COS 的方法

    //上传对象
    // 创建 TransferManager 实例，这个实例用来后续调用高级接口
    TransferManager createTransferManager() {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);
        return transferManager;
    }

    /**
     * 上传本地文件
     * @param key
     * @param file
     * @return
     * @throws CosClientException
     * @throws CosServiceException
     */
    public PutObjectResult putObject(String key, File file) throws CosClientException, CosServiceException {
        // 调用 COS 接口之前必须保证本进程存在一个 COSClient 实例，如果没有则创建
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        //String bucketName = "examplebucket-1250000000";
        // 对象键(Key)是对象在存储桶中的唯一标识。
        //String key = "exampleobject";
        // 本地文件路径
        // String localFilePath = "/path/to/localFile";
        // File localFile = new File(localFilePath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        //putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        PutObjectResult putObjectResult=new PutObjectResult();
        try {
            putObjectResult = cosClient.putObject(putObjectRequest);
        } catch (Exception e) {
            log.error("上传cos错误,错误原因:{}",e.getMessage());
            throw new CosServiceException(e.getMessage());
        } finally {
            // 确认本进程不再使用 cosClient 实例之后，关闭即可(注册成bean不用关闭)
            //cosClient.shutdown();
        }
        return putObjectResult;
    }

    /**
     * 下载对象到本地
     * @param key
     * @return
     * @throws CosClientException
     * @throws CosServiceException
     */
    public COSObject getObject(String key) throws CosClientException, CosServiceException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        //如果存储桶开启了版本控制功能，需要下载指定版本的对象，可通过 setVersionId 函数指定对象的版本号
        //getObjectRequest.setVersionId("versionId");
        return cosClient.getObject(getObjectRequest);
    }


}

