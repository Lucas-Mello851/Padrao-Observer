package br.com.leilao.observer.subject;

import br.com.leilao.observer.model.Lance;
import br.com.leilao.observer.observer.Observador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leilao implements Sujeito {

    private final String nomeProduto;
    private final double lanceMinimo;
    private final List<Observador> observadores;
    private final List<Lance> historicoDeLances;
    private Lance maiorLance;
    private boolean encerrado;

    public Leilao(String nomeProduto, double lanceMinimo) {
        this.nomeProduto = nomeProduto;
        this.lanceMinimo = lanceMinimo;
        this.observadores = new ArrayList<>();
        this.historicoDeLances = new ArrayList<>();
        this.encerrado = false;
    }

    @Override
    public void registrarObservador(Observador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores() {
        for (Observador obs : observadores) {
            obs.atualizar(nomeProduto, maiorLance.getValor(), maiorLance.getNomeParticipante());
        }
    }

    public void darLance(Lance lance) {
        if (encerrado) {
            throw new IllegalStateException("Leilão encerrado. Não é possível dar mais lances.");
        }
        if (lance.getValor() < lanceMinimo) {
            throw new IllegalArgumentException(
                    String.format("Lance mínimo é R$ %.2f. Lance de R$ %.2f recusado.",
                            lanceMinimo, lance.getValor()));
        }
        if (maiorLance != null && lance.getValor() <= maiorLance.getValor()) {
            throw new IllegalArgumentException(
                    String.format("Lance deve ser maior que o atual (R$ %.2f). Lance de R$ %.2f recusado.",
                            maiorLance.getValor(), lance.getValor()));
        }

        historicoDeLances.add(lance);
        maiorLance = lance;
        notificarObservadores();
    }

    public void encerrar() {
        this.encerrado = true;
        System.out.printf("%n[LEILÃO ENCERRADO] Produto: %s | Vencedor: %s | Valor: R$ %.2f%n",
                nomeProduto,
                maiorLance != null ? maiorLance.getNomeParticipante() : "Sem lances",
                maiorLance != null ? maiorLance.getValor() : 0.0);
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public double getLanceMinimo() {
        return lanceMinimo;
    }

    public Lance getMaiorLance() {
        return maiorLance;
    }

    public boolean isEncerrado() {
        return encerrado;
    }

    public List<Lance> getHistoricoDeLances() {
        return Collections.unmodifiableList(historicoDeLances);
    }

    public int getTotalObservadores() {
        return observadores.size();
    }
}
