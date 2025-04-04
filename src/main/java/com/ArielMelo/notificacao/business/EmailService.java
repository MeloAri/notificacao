package com.ArielMelo.notificacao.business;

import com.ArielMelo.notificacao.Infrastructure.exceptions.EmailException;
import com.ArielMelo.notificacao.business.dto.TarefasDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.email.remetente}")
    public String remetente;

    @Value("${envio.email.nome-remetente}")
    private String nomeRemetente;


    public void enviaEmail(TarefasDTO dto) {
        if (dto == null || dto.getEmailUsuario() == null) {
            throw new IllegalArgumentException("DTO ou email do usuário não pode ser nulo");
        }

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            try {
                helper.setFrom(new InternetAddress(remetente, nomeRemetente));
            } catch (UnsupportedEncodingException e) {
                // Fallback para versão sem nome se houver problema com encoding
                helper.setFrom(remetente);
            }

            helper.setTo(InternetAddress.parse(dto.getEmailUsuario()));
            helper.setSubject("Notificação de Tarefa");

            Context context = new Context();
            context.setVariable("nomeTarefa", dto.getNomeTarefa());
            context.setVariable("dataEvento", dto.getDataEvento());
            context.setVariable("descricao", dto.getDescricao());

            String template = templateEngine.process("notificacao", context);
            helper.setText(template, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailException("Erro ao enviar o email", e);
        }
    }
}