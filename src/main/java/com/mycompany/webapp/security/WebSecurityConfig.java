package com.mycompany.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableMethodSecurity(securedEnabled = true) //secured를 사용할 수 있도록 설정
public class WebSecurityConfig {
   
   // JwtAuthenticationFilter 주입
   @Autowired
   private JwtAuthenticationFilter jwtAuthenticationFilter;
   // 인증 필터 체인을 관리 객체로 등록
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
      // Rest API에서 로그인 폼을 제공하지 않으므로 폼을 통한 로그인 인증을 하지 않도록 설정
      // 로그인 폼은 front-end에서 제공
      http.formLogin(config -> config.disable());
      // 로그아웃이 필요 없음 -> 브라우저에서 jwt 토큰 안 넘기면 됨
      // post로 form 요청하지 않음 오로지 JSON으로 전달하므로 _csrf (사용자 권한 도용 방지)를 하지 않음
      
      // REST API는 HttpSession을 사용하지 않음 -> 세션 사용하지 않도록 설정
      http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      
      
      //사이트 간 요청 위조 방지 비활성화(GET 방식 이외의 방식 요청은 _csrf 토큰을 요구하기 때문)
      http.csrf(config->config.disable());
      
      // CORS 설정(다른(크로스) 도메인에서 받은 인증 정보(AccessToken)로 요청할 경우 허가)
      // A 브라우저에서 받은 jwt 토큰을 통해서 B 브라우저에서 사용가능하게 하기 위해서 ex) 신세계 -> 이마트
      // A 브라우저는 설정할 필요 없지만 B 브라우저는 설정 해야 허가됨
      http.cors(config -> {});
      
      // JWT로 인증이 되도록 필터를 등록
      // accessToken이 인증처리 되면 아이디와 비밀번호를 인증처리 할 필요가 없어지기 때문에 아이디 검사 전에 위치 시킨다
      http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 아이디, 패스워드가 없이 인증 처리를 하려면 id, password 인증 전에 필터가 실행되야 함
      
      return http.build();
   }
   
   //권한 객체를 관리 객체로 등록
   @Bean
   public RoleHierarchy roleHierarchy() {
      RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
      hierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
      return hierarchy;
   }   
   //PreAuthorize 어노테이션의 표현식을 해석하는 객체 등록 
   @Bean
   public MethodSecurityExpressionHandler createExpressionHandler() {
     DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
       handler.setRoleHierarchy(roleHierarchy());
       return handler;
   }

   //다른 도메인(크로스 도메인) 설정
   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      
      // 요청 사이트 제한 -> ex) sinsege가 들어간 곳에서 온 요청은 허가 하겠다.
      //configuration.addAllowedOrigin("b.sinsega.com");
      
      // * 모든 사이트 허용하겠다는 의미
      configuration.addAllowedOrigin("*");
      
      // 요청 방식 제한 get, post, put, patch, delete
      configuration.addAllowedMethod("*");
   
      // 요청 헤더 제한 브라우저 종류 - user-agent 등등
      configuration.addAllowedHeader("*");
      
      // 모든 url에 대해 위 설정을 내용 적용
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**" , configuration);
      
      return source;
   }
   
}

