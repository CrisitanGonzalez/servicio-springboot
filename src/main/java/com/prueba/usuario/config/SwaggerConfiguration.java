package com.prueba.usuario.config;


import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(basePackages = {"cl.prueba.usuario.controller"})
@EnableSwagger2
public class SwaggerConfiguration {
    @Autowired
    private Environment env;

    @Bean
    public Docket loginApi(ReloadableResourceBundleMessageSource messageSource) {

        return new Docket(DocumentationType.SWAGGER_2).groupName("Prueba").apiInfo(apiInfo(messageSource))
                .forCodeGeneration(true).select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework"))).build()
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_UTF8_VALUE)).enableUrlTemplating(false)
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo(ReloadableResourceBundleMessageSource messageSource) {
        return new ApiInfoBuilder().title("Api - Prueba")
                .version("1.0.0")
                .description("Hola soy un api de pruebas").build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().deepLinking(true).displayOperationId(true).defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1).defaultModelRendering(ModelRendering.EXAMPLE)
                .docExpansion(DocExpansion.NONE).filter(true).displayRequestDuration(true).maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA).showExtensions(false).tagsSorter(TagsSorter.ALPHA)
                .operationsSorter(OperationsSorter.METHOD)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS).validatorUrl(null).build();
    }

}