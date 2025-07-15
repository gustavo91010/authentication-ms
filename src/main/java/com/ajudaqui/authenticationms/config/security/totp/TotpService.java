package com.ajudaqui.authenticationms.config.security.totp;

import static java.lang.String.format;

import com.atlassian.onetime.service.RandomSecretProvider;

import org.springframework.stereotype.Service;

@Service
public class TotpService {

  public String generatedSecret() {
    return new RandomSecretProvider().generateSecret().getBase32Encoded();
  }

  public String generatedQrCode(String email, String secret) {
    String issure = "Authentication MS";
    return format(
        "otpauth://totp/%s:%s?secret-%s&issuer=%s", issure, email, secret, issure);
  }
}
