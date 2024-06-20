package org.nitya.software.RealEstate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:test/RealEstate/src/main/resources/uploads/")
        .setCachePeriod(Integer.valueOf(10));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/401").setViewName("forward:/401.html");
        registry.addViewController("/403").setViewName("forward:/403.html");
        registry.addViewController("/404").setViewName("forward:/404.html");
        //registry.addViewController("/error").setViewName("forward:/error.html");
    }
}
