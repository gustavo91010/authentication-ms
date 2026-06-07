package com.ajudaqui.porteiro.controller.doc;

import java.util.List;

import com.ajudaqui.porteiro.dto.ApplicationDto;
import com.ajudaqui.porteiro.dto.HttpAplications;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Applications", description = "Gerenciamento de aplicações")
public interface ApplicationsControllerDoc {

  @Operation(summary = "Registra uma aplicação e torna o usuário Moderador, permitido a quem tem acesso master")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Aplicação criada"),
      @ApiResponse(responseCode = "400", description = "Erro de validação")
  })
  ResponseEntity<HttpAplications> register(String authorization, ApplicationDto applicationDto);

  @Operation(summary = "Listar usuários por aplicação", description = "Retorna todos os usuários vinculados a uma aplicação específica. Acesso permitido apenas para MODERATOR.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado"),
      @ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
  })
  ResponseEntity<HttpAplications> getByAppName(String jwtToken, String appName);

  @Operation(summary = "Listar todas as aplicações", description = "Retorna a lista de todas as aplicações cadastradas no sistema. "
      + "Permitido a qyem tem o acesso master")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado")
  })
  ResponseEntity<List<HttpAplications>> allApplications(String authorization);

  @Operation(summary = "Buscar aplicação por ID", description = "Retorna os dados de uma aplicação específica pelo ID. "
      + "Acesso permitido apenas para MODERATOR.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Aplicação encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado"),
      @ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
  })
  ResponseEntity<HttpAplications> getById(String jwtToken, String applicationId);

  ResponseEntity<HttpAplications> update(String jwtToken, String applicationId, ApplicationDto dto);

}
