package com.ArielMelo.notificacao.business.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "envio.email")
public class MailConfig {
    private String remetente;
    private String nomeRemetente;
}
