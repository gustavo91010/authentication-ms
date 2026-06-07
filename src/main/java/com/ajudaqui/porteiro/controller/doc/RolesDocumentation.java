package com.ajudaqui.porteiro.controller.doc;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autorização", description = "Perfis de acesso do sistema:\n\n" +
    "- ROLE_ADMIN: Pode gerenciar os usuários registrados na aplicação.\n" +
    "- ROLE_MODERATOR: Pode gerenciar a aplicação e os usuários.\n" +
    "- ROLE_USER: Pode acessar funcionalidades básicas e confirmar token.")
public interface RolesDocumentation {
}
