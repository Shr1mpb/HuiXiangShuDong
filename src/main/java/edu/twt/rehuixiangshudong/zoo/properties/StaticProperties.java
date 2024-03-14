package edu.twt.rehuixiangshudong.zoo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "static")
@Component
@Data
public class StaticProperties {
    /**
     * 静态资源url
     */
    private String url;
}
