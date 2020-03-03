package com.weiwu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {


    // 文档的地址 http://127.0.0.1:8088/swagger-ui.html 原路径
    // 新皮肤 http://127.0.0.1:8088/doc.html
    // 配置swagger2核心配置
    @Bean
    public Docket createRestApi() {

        // 指定API类型为 swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())     // 用于定义API文档汇总信息
                // 配置swagger2扫描的包
                .select().apis(RequestHandlerSelectors.basePackage("com.weiwu.controller"))
                .paths(PathSelectors.any())   // 所有controller
                .build();
    }

    // API 文档信息及联系人信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货电商平台接口API")
                .contact(new Contact("immoc", "https://www.amuyu.cn", "weiwu0904@163.com"))
                .description("专为天天吃货提供的API文档")
                .version("1.0.0")  //文档版本号
                .termsOfServiceUrl("https://www.amuyu.cn")  //网站地址
                .build();
    }
}
