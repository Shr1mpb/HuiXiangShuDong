package edu.twt.rehuixiangshudong.zoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "session.jwt")
@Data
public class JwtProperties
{
    /**
     * 管理端生成jwt令牌相关配置
     */
    private String SecretKey;
    private long Ttl;
    private String TokenName;
}

