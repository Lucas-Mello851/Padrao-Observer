package br.com.leilao.observer.observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticipanteLeilao implements Observador {

    private final String nome;
    private final List<String> notificacoes;
    private String ultimoProdutoNotificado;
    private double ultimoLanceRecebido;

    public ParticipanteLeilao(String nome) {
        this.nome = nome;
        this.notificacoes = new ArrayList<>();
    }

    @Override
    public void atualizar(String nomeProduto, double lanceAtual, String nomeArrematante) {
        this.ultimoProdutoNotificado = nomeProduto;
        this.ultimoLanceRecebido = lanceAtual;

        String msg = String.format(
                "[%s] Novo lance no produto '%s': R$ %.2f por %s",
                nome, nomeProduto, lanceAtual, nomeArrematante);

        notificacoes.add(msg);
        System.out.println(msg);
    }

    public String getNome() {
        return nome;
    }

    public List<String> getNotificacoes() {
        return Collections.unmodifiableList(notificacoes);
    }

    public String getUltimoProdutoNotificado() {
        return ultimoProdutoNotificado;
    }

    public double getUltimoLanceRecebido() {
        return ultimoLanceRecebido;
    }

    @Override
    public String toString() {
        return "ParticipanteLeilao{nome='" + nome + "'}";
    }
}
