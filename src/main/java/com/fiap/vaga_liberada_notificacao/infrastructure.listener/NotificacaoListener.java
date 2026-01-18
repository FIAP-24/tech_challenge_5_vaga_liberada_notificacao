package com.fiap.vaga_liberada_notificacao.infrastructure.listener;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoListener {

    @SqsListener("notificacao-agenda-queue")
    public void receberNotificacao(String mensagem) {
        System.out.println("=================================================");
        System.out.println("NOVA MENSAGEM RECEBIDA VIA SQS:");
        System.out.println(mensagem);
        System.out.println("=================================================");


    }
}