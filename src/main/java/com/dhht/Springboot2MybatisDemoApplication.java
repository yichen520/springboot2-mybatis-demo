package com.dhht;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@MapperScan("com.dhht.dao")
@EnableAspectJAutoProxy(exposeProxy = true)
@Configuration
public class Springboot2MybatisDemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Springboot2MybatisDemoApplication.class, args);
	}

//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer(){
//		return new EmbeddedServletContainerCustomizer() {
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//				container.setSessionTimeout(1800);//单位为S
//			}
//		};
//	}

	//设置单文件不能大于10M
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//单个文件最大
		factory.setMaxFileSize("50480KB"); //KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("504800KB");
		return factory.createMultipartConfig();
	}
}
