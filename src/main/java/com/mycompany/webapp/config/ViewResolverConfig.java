package com.mycompany.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//springboot가 시작될 때, 사용되는 설정 객체로 사용
@Configuration
public class ViewResolverConfig {
	@Bean //리턴된 객체를 관리객체로 만들어 주는 어노테이션
	public ViewResolver internalResourceViewResolover() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

}
