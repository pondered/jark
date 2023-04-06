package com.jark.upms.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ponder
 */
@SpringBootApplication(scanBasePackages = {"com.jark.upms.**"})
public class UpmsApplication {

    public static void main(final String[] args) {
        SpringApplication springApplication = new SpringApplication(UpmsApplication.class);
        springApplication.setLazyInitialization(true);
        springApplication.run(args);
    }
}


