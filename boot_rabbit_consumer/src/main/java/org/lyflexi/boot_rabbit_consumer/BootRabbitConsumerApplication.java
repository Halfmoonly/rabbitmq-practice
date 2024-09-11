package org.lyflexi.boot_rabbit_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//解决common打包失败问题
@ComponentScan(value = "org.lyflexi")
public class BootRabbitConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootRabbitConsumerApplication.class, args);
    }

}
