package com.jark.template.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ponder
 */
@SpringBootApplication(scanBasePackages = {"com.jark.template.**"})
public class TemplateApplication {

    public static void main(final String[] args) {
        SpringApplication springApplication = new SpringApplication(TemplateApplication.class);
        springApplication.setLazyInitialization(true);
        springApplication.run(args);
    }
}


