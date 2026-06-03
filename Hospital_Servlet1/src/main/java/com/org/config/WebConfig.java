package com.org.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
                InternalResourceViewResolver resolver = new InternalResourceViewResolver();
                resolver.setPrefix("/");
                resolver.setSuffix(".jsp");
                resolver.setViewClass(JstlView.class);
                registry.viewResolver(resolver);
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/assets/**")
                                .addResourceLocations("classpath:/static/assets/", "/assets/");

                registry.addResourceHandler("/img/**")
                                .addResourceLocations("classpath:/static/img/", "/img/");

                registry.addResourceHandler("/component/**")
                                .addResourceLocations("classpath:/static/component/", "/component/");

                registry.addResourceHandler("/**")
                                .addResourceLocations("classpath:/META-INF/resources/",
                                                "classpath:/resources/",
                                                "classpath:/static/",
                                                "classpath:/public/",
                                                "/");
        }

}