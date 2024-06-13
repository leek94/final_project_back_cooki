package com.mycompany.webapp.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	@Autowired
		private JwtProvider jwtProvider;
	@Autowired
		private AppUserDetailsService appUserDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException { {
		//요청 헤더에서 accessToken얻기
		String accessToken = null;
		/* HttpServletRequest httpServletRequest = (HttpServletRequest) request; */
		String headerValue = request.getHeader("Authorization");
		if(headerValue !=null && headerValue.startsWith("Bearer")) {
		//bearer 인덱스 이후부터 끝까지 
			accessToken= headerValue.substring(7);
			log.info(accessToken);
		}
		
		//queryString으로 전달된 accessToken얻기
		//<img src="/board/battach/1?accessToken=xxx"/>와 같이 전달됐을 경우
		if(accessToken==null) {
			if(request.getParameter("accessToken")!=null) {
				accessToken=request.getParameter("accessToken");
			}
		}
		
		
		if(accessToken!=null) {
			//AccessToken 유효성 검사 
			Jws<Claims> jws = jwtProvider.validateToken(accessToken);
			if(jws!= null) {
				log.info("accessToken이 유효함");
				String userId=jwtProvider.getUserId(jws);
				log.info("userId: "+userId);
				
				//사용자의 상세 정보 가져오기
				UserDetails userDetails = (AppUserDetails)appUserDetailsService.loadUserByUsername(userId);
				
				//인증 객체 얻기
				Authentication authentication = 
						new UsernamePasswordAuthenticationToken(userDetails,null ,userDetails.getAuthorities());
				//스프링 시큐리티에 인증 객체 설정
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else {
				//유효하지 않을 경우
				log.info("accessToken이 유효지 않음");
	
			}
		}
			
		//다음 필터를 실행 
		filterChain.doFilter(request, response);
			}
	}
	

}
