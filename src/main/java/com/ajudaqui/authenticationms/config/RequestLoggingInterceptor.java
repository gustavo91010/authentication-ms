package com.ajudaqui.authenticationms.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

  @Autowired
  private JwtUtils jwtUtils;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (handler instanceof HandlerMethod) {

      String authHeader = "";

      if (request.getHeader("Authorization") != null) {
        String auth = request.getHeader("Authorization");
        if (auth.contains("Bearer "))
          auth = jwtUtils.getEmailFromJwtToken(auth);
        authHeader = "| identifer: " + auth;
      }

      HandlerMethod method = (HandlerMethod) handler;
      String methodName = method.getMethod().getName();
      String path = request.getRequestURI();
      String httpMethod = request.getMethod();
      String ip = request.getRemoteAddr();
      logger.info("{} | {} | {} : [{}] {} {}", ip, methodName, httpMethod, path, authHeader);
    }

    return true;
  }

}
