package com.jark.template.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author ponder
 */
@EnableWebFlux
@SpringBootApplication
public class GatewayAppApplication {

    public static void main(final String[] args) {
        SpringApplication.run(GatewayAppApplication.class, args);
    }

}
