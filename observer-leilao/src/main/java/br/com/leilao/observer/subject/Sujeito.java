package br.com.leilao.observer.subject;

import br.com.leilao.observer.observer.Observador;

public interface Sujeito {

    void registrarObservador(Observador observador);

    void removerObservador(Observador observador);

    void notificarObservadores();
}
