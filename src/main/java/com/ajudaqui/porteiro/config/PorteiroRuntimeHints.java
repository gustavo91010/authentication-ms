package com.ajudaqui.porteiro.config;

import com.ajudaqui.porteiro.config.dbmigrations.CreateDefaultApplicationMigration;
import com.ajudaqui.porteiro.config.dbmigrations.InitialSetupMigration;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class PorteiroRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    // Registra as classes de migração do Mongock para reflexão completa (incluindo construtores e métodos)
    hints.reflection().registerType(InitialSetupMigration.class, MemberCategory.values());
    hints.reflection().registerType(CreateDefaultApplicationMigration.class, MemberCategory.values());

    // Registra o arquivo de propriedades do Spring Cloud AWS como recurso
    hints.resources().registerPattern("io/awspring/cloud/core/SpringCloudClientConfiguration.properties");
  }
}
