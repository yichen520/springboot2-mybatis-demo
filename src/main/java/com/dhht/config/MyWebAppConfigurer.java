//package com.dhht.config;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//public class MyWebAppConfigurer
//        extends WebMvcConfigurerAdapter {
//    /**
//     *
//     * @Title:  文件上传路径映射
//     * @Description:储存在  /uploadfile/** 路径下的文件映射到/home/项目名/uploadfile/
//     *
//     */
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/group1/**").addResourceLocations("file:C:/temp/");
//        super.addResourceHandlers(registry);
//    }
//
//}