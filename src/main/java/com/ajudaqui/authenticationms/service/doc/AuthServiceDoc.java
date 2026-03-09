package com.ajudaqui.authenticationms.service.doc;

import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;

public interface AuthServiceDoc {

    /**
       * Realiza a autenticação do usuário com base no e-mail, senha e aplicação.
       *
       * <p>
       * Caso a aplicação não seja informada, define "bill-manager" como padrão.
       * Valida se o usuário está ativo, executa o processo de autenticação
       * pelo {@link AuthenticationManager} e, se bem-sucedido, armazena o
       * contexto de segurança e retorna o JWT correspondente.
       * </p>
       *
       * @param loginRequest dados de login contendo e-mail, senha e aplicação
       * @return {@link LoginResponse} com os dados do usuário na aplicação
       *         e o token JWT gerado
       * @throws MessageException caso a conta esteja desativada
       */
    LoginResponse authenticateUser(LoginRequest loginRequest);

    /**
       * Realiza o registro de um novo usuário na aplicação.
       *
       * <p>
       * Cria o usuário, gera um token de confirmação e envia por e-mail.
       * Em ambiente de desenvolvimento, ele vai como ativo automaticamente,
       * em produção, deve chamar o endpont confirmByToken passando o token para
       * ativar o usuario
       * Em produção, valida se a aplicação possui URL de registro configurada e,
       * caso o registro seja concluído, envia mensagem para fila (SQS) com os dados
       * adicionais.
       * </p>
       *
       * @param usersRegister objeto contendo os dados necessários para registro do
       *                      usuário,
       *                      incluindo campos adicionais utilizados na integração.
       * @return LoginResponse contendo os dados da aplicação do usuário e o JWT
       *         gerado para autenticação.
       * @throws BadRequestException caso esteja em produção e a aplicação não possua
       *                             URL de registro configurada.
       */
    LoginResponse registerUser(UsersRegister usersRegister);

}

