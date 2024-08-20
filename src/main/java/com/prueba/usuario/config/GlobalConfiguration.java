package com.prueba.usuario.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.NoProviderFoundException;

@Configuration
@SpringBootApplication
@ComponentScan(
        basePackages = {
                "cl.prueba.usuario.controller",
                "cl.prueba.usuario.services"
        })
@Log4j2
public class GlobalConfiguration implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
                registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }

        @Bean
        public ReloadableResourceBundleMessageSource messageSource() {
                ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
                messageSource.setBasename("classpath:messages");
                messageSource.setDefaultEncoding("UTF-8");
                return messageSource;
        }



}
