package com.dxdp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.dxdp.mapper")
@SpringBootApplication
public class DxDianPingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DxDianPingApplication.class, args);
    }

}
