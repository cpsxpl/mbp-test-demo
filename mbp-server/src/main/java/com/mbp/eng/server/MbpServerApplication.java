package com.mbp.eng.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${mbp.info.base-package}
@SpringBootApplication(scanBasePackages = {"${mbp.info.base-package}.server"})
public class MbpServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(MbpServerApplication.class, args);
    }

}
