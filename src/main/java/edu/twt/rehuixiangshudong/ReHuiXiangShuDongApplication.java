package edu.twt.rehuixiangshudong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan//扫描servlet组件 此处指的过滤器
@EnableScheduling//启用定时任务
public class ReHuiXiangShuDongApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReHuiXiangShuDongApplication.class, args);
    }

}
