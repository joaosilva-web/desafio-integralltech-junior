package com.integralltech.chamados.exception;

public class IaIndisponivelException extends RuntimeException {

    public IaIndisponivelException(String mensagem) {
        super("Serviço de IA indisponível: " + mensagem);
    }
}
