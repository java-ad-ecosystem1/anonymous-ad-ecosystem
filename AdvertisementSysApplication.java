package com.advertisement_sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.advertisement_sys.dao")
public class AdvertisementSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvertisementSysApplication.class, args);
    }

}
