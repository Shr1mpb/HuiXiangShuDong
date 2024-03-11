package edu.twt.rehuixiangshudong;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan//扫描servlet组件 此处指的过滤器
@EnableScheduling//启用定时任务
public class ReHuiXiangShuDongApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReHuiXiangShuDongApplication.class, args);
    }

//配置SSL相关
    //SpringBoot-v2.0+配置方式
    @Bean
    public ServletWebServerFactory servletContainer() {
        //创建Tomcat服务器工厂实例
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        //添加此tomcat实例其它连接参数
        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    /**
     * 配置tomcat自定义连接参数
     */
    private Connector createHTTPConnector() {
        //Connector port有两种运行模式(NIO和APR)，选择NIO模式：protocol="org.apache.coyote.http11.Http11NioProtocol"
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        //启用http（80）端口
        connector.setScheme("http");
        //设置安全连接标志，该标志将被分配给通过该连接接收的请求
        //secure新的安全连接标志
        //如果connector.setSecure(true)，则http使用http, https使用https; 分离状态，因此设置false
        connector.setSecure(false);
        //http默认端口
        connector.setPort(80);
        //重定向证书端口443，便于http自动跳转https
        connector.setRedirectPort(443);
        return connector;
    }
}


