package org.lyflexi.boot_rabbit_producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//解决common打包失败问题
@ComponentScan(value = "org.lyflexi")
public class BootRabbitProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootRabbitProducerApplication.class, args);
    }

}
