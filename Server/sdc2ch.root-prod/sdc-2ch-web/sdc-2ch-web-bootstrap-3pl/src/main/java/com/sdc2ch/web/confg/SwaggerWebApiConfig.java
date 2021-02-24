package com.sdc2ch.web.confg;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerWebApiConfig {

    @Bean
    public Docket api() {
    	

		
		ParameterBuilder aParameterBuilder = new ParameterBuilder();
		aParameterBuilder.name("authorization").modelRef(new ModelRef("string")).parameterType("header").required(true)
				.build();
		List<springfox.documentation.service.Parameter> aParameters = new ArrayList<Parameter>();
		aParameters.add(aParameterBuilder.build());
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.sdc2ch"))
				.paths(PathSelectors.any()).build().pathMapping("")
				.globalOperationParameters(aParameters);

    }
}
