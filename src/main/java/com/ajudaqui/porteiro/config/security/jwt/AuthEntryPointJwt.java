package com.ajudaqui.porteiro.config.security.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    
    // Se for rota de admin, deixa o httpBasic disparar o popup do navegador
    if (request.getRequestURI().startsWith("/admin/")) {
        response.addHeader("WWW-Authenticate", "Basic realm=\"Admin Dashboard\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        return;
    }

    response.setCharacterEncoding("utf-8");
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().print("{ \"message\": \"" + "Não autorizado" + "\" }");
    logger.error("Usuario não autorizado!");
  }

}
