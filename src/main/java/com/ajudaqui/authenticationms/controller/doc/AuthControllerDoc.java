package com.ajudaqui.authenticationms.controller.doc;

import javax.validation.Valid;

import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticação", description = "Endpoints responsáveis por autenticação e registro de usuários")
@RequestMapping("/auth")
public interface AuthControllerDoc {

    @Operation(
        summary = "Realizar login",
        description = "Autentica o usuário e retorna JWT e dados de acesso."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Credenciais inválidas")
    })
    @PostMapping("/signin")
    ResponseEntity<LoginResponse> authenticateUser(
        @Valid @RequestBody LoginRequest loginRequest
    );



    @Operation(
        summary = "Confirmar token",
        description = "Confirma o token enviado para validação de conta. "
            + "Requer autenticação com ROLE_USER."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token confirmado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Token inválido"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/confirm-token")
    ResponseEntity<?> confirmToken(

        @Parameter(description = "Token JWT no formato Bearer")
        @RequestHeader("Authorization") String jwtToken,

        @Parameter(description = "Token de confirmação recebido por email", example = "123456")
        @RequestParam String token
    );



    @Operation(
        summary = "Registrar usuário",
        description = "Realiza o cadastro de um novo usuário no sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping("/signup")
    ResponseEntity<LoginResponse> registerUser(
        @Valid @RequestBody UsersRegister usersRegister
    );
}
