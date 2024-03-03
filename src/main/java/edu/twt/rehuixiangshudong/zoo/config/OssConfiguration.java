package edu.twt.rehuixiangshudong.zoo.config;

import edu.twt.rehuixiangshudong.zoo.properties.AliOssProperties;
import edu.twt.rehuixiangshudong.zoo.util.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类 用于创建AliOssUtil对象
 */
@Configuration//添加configuration注解 可以用bean注解在程序启动时创建对象
@Slf4j
public class OssConfiguration {

    @Bean //启动时创建对象并交给spring容器管理
    @ConditionalOnMissingBean //确保只有一个AliOssUtil对象
    public AliOssUtil AliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
