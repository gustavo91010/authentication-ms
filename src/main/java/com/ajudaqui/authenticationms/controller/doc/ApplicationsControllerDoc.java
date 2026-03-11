package com.ajudaqui.authenticationms.controller.doc;

import java.util.List;

import com.ajudaqui.authenticationms.dto.ApplicationDto;
import com.ajudaqui.authenticationms.dto.HttpAplications;
import com.ajudaqui.authenticationms.dto.HttpUsersAppData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Applications", description = "Gerenciamento de aplicações")
public interface ApplicationsControllerDoc {

  @Operation(summary = "Registra uma aplicação e torna o usuário Moderador")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Aplicação criada"),
      @ApiResponse(responseCode = "400", description = "Erro de validação")
  })
  @PostMapping("")
  ResponseEntity<HttpAplications> regsiter(String authorization, ApplicationDto appicationDto);

  @Operation(summary = "Listar usuários por aplicação", description = "Retorna todos os usuários vinculados a uma aplicação específica. Acesso permitido apenas para MODERATOR.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado"),
      @ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
  })
  @GetMapping("/name/{appName}")
  ResponseEntity<List<HttpUsersAppData>> getUsersByApp(
      @Parameter(description = "Token JWT no formato Bearer") @RequestHeader("Authorization") String jwtToken,
      @Parameter(description = "Nome da aplicação", example = "vem-pro-culto") @PathVariable String appName);

  @Operation(summary = "Listar todas as aplicações", description = "Retorna a lista de todas as aplicações cadastradas no sistema. "
      + "Acesso permitido apenas para ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado")
  })
  @GetMapping("/all")
  ResponseEntity<List<HttpAplications>> allApplications(String authorization);

  @Operation(summary = "Buscar aplicação por ID", description = "Retorna os dados de uma aplicação específica pelo ID. "
      + "Acesso permitido apenas para ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Aplicação encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado"),
      @ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
  })

  @GetMapping("/id/{applicationId}")
  ResponseEntity<HttpAplications> getById(
      @Parameter(description = "Token JWT no formato Bearer") @RequestHeader("Authorization") String jwtToken,
      @Parameter(description = "ID da aplicação", example = "1") @PathVariable Long applicationId);

  @PutMapping("/id/{applicationId}")
  ResponseEntity<HttpAplications> update(
      @Parameter(description = "Token JWT no formato Bearer do ADMIN da aplicação") @RequestHeader("Authorization") String jwtToken,
      @Parameter(description = "ID da aplicação", example = "1") @PathVariable Long applicationId, ApplicationDto dto);

}
