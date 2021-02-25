package com.donat.crypto.reporter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final Environment environment;

    /**
     * DI constructor
     *
     * @param environment DI bean
     */
    public WebConfiguration(Environment environment) {
        this.environment = environment;
    }

}
