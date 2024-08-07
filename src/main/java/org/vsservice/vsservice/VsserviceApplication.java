package org.vsservice.vsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class VsserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VsserviceApplication.class, args);
    }

}
