package br.com.leilao.observer.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Lance {

    private final String nomeParticipante;
    private final double valor;
    private final LocalDateTime momento;

    public Lance(String nomeParticipante, double valor) {
        this.nomeParticipante = nomeParticipante;
        this.valor = valor;
        this.momento = LocalDateTime.now();
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("Lance{participante='%s', valor=R$ %.2f, momento=%s}",
                nomeParticipante, valor, momento.format(fmt));
    }
}
