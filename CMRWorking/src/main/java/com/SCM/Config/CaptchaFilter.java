package com.SCM.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.SCM.Helper.validationHelper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CaptchaFilter extends OncePerRequestFilter {
	
	@Autowired
	validationHelper validationhelper;
	
	
	private final AuthenticationFailureHandler authenticationFailureHandler;
	
	
	public CaptchaFilter(@Autowired AuthenticationFailureHandler authenticationFailureHandler ) {
		this.authenticationFailureHandler =  authenticationFailureHandler;
	}
	
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if("/do-login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
			
			String token = request.getParameter("cf-turnstile-response");
			
			if(!validationhelper.isValidCaptcha(token, request)) {
				
				AuthenticationException captchaFailed = new BadCredentialsException("CAPTCHA verification failed!!!");
				authenticationFailureHandler.onAuthenticationFailure(request, response, captchaFailed);
				return;
			}
				
		}
		
		
		filterChain.doFilter(request, response);
		
		
	}	
}
