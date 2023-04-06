package com.jark.scheduler.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 定时任务模块
 * @author ponder
 */
@SpringBootApplication
public class SchedulerApplication {
    public static void main(final String[] args) {
        SpringApplication springApplication = new SpringApplication(SchedulerApplication.class);
        springApplication.setLazyInitialization(true);
        springApplication.run(args);
    }

}


