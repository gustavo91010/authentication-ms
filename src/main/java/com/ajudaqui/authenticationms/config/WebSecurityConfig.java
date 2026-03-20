package com.ajudaqui.authenticationms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ajudaqui.authenticationms.config.security.jwt.AuthEntryPointJwt;
import com.ajudaqui.authenticationms.config.security.jwt.AuthTokenFilter;
import com.ajudaqui.authenticationms.config.security.jwt.CustomAccessDeniedHandler;
import com.ajudaqui.authenticationms.config.security.service.UserDetailsServiceImpl;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;
  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

  // ... (beans mantidos igual)

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling()
        // O JWT EntryPoint só será usado se NÃO for rota de admin
        .defaultAuthenticationEntryPointFor(unauthorizedHandler, request -> !request.getRequestURI().startsWith("/admin/"))
        .accessDeniedHandler(accessDeniedHandler())
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .antMatchers("/admin/**").hasAuthority("ROLE_MODERATOR") 
        .antMatchers("/**").permitAll() 
        .anyRequest().authenticated()
        .and()
        .httpBasic(); // Ativa o popup de login para quem não cair no JWT EntryPoint
    
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
