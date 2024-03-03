package edu.twt.rehuixiangshudong.zoo.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyuncs.exceptions.ClientException;
import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.exception.FileUploadException;
import edu.twt.rehuixiangshudong.zoo.exception.OssObjectDeleteFailedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;


@AllArgsConstructor
@Slf4j
@Data
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes 上传的文件.getBytes
     * @param objectName 上传的文件名
     * @return 返回文件路劲
     */
    public String upload(byte[] bytes, String objectName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            throw new FileUploadException(MessageConstant.UPLOAD_FAILED);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }

    /**
     * 文件删除
     * 添加文件删除 保证在oss存储中 每个用户只对应至多n个文件 n代表与用户有关的文件的数量
     * 没有返回值 没有抛异常则证明删除成功
     */
    public void delete(String objectName) throws Exception {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, objectName);
        } catch (Exception e) {
            throw new OssObjectDeleteFailedException(MessageConstant.OSSFILE_DELETE_FAILED);
        }  finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}

