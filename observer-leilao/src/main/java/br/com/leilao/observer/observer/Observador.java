package br.com.leilao.observer.observer;

public interface Observador {

    void atualizar(String nomeProduto, double lanceAtual, String nomeArrematante);
}
