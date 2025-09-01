package com.SCM.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class userConfig {

	
    // 1. User detail service impl custom class as bean Userdetailsservice spring interface used to load user data
    @Bean
    protected UserDetailsService getUDS() {
        return new UDetailServiceImpl();
    }


    // 2. Password encoder

    @Bean
    protected BCryptPasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }



    // 3. Bean for DaoAuthenticationProvider

    @Bean
    protected DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(getUDS());
        provider.setPasswordEncoder(passEncoder());
        return provider;
    }


    // this works insted of configure method which contains authentication manager builder
    // what this does This returns the AuthenticationManager auto-configured by
    // Spring Boot, which internally uses your registered UserDetailsService,
    // DaoAuthenticationProvider, and PasswordEncoder — we need to define them as @Bean 's
    @Bean
    protected AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
    	return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }
    
    
    
    @Bean 
    protected CaptchaFilter captchaFilter(AuthenticationFailureHandler authenticationFailureHandler) {
    	return new CaptchaFilter(authenticationFailureHandler);
    }
    
    

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.
                authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/signup","/login","/CSS/**", "/JS/**", "/Images/**","/changePass/**").permitAll()
                        .anyRequest().authenticated()          
                )
                .formLogin(form -> form				// new methods to give form specification older ones are depricated
                		.loginPage("/login")			 // 👉 Your custom login page
                		.loginProcessingUrl("/do-login").permitAll()		   // 👉 Form POST action
                		.defaultSuccessUrl("/user/index?loginSuccess=true", true)		 // 👉 Where to go after successful login
                		.failureUrl("/login?error=true")                     // 👉 On failure
                		.permitAll()
                		)
                .logout(logout -> logout
                		.logoutUrl("/logout")
                		.logoutSuccessUrl("/login?logout=true")
                		.permitAll()
                		)
                .addFilterBefore(captchaFilter(authenticationFailureHandler()), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf-> csrf.disable());

        // CSRF = Cross-Site Request forgery :- type of web security vulnerability where
        // a malicious website tricks a logged-in user into performing unwanted actions
        // on another website (usually where the user is authenticated).

        
        return http.build();
    }

}
