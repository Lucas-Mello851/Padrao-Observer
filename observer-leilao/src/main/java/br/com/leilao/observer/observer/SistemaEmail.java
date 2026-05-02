package br.com.leilao.observer.observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SistemaEmail implements Observador {

    private final List<String> emailsEnviados;
    private String ultimoDestinatario;

    public SistemaEmail() {
        this.emailsEnviados = new ArrayList<>();
    }

    @Override
    public void atualizar(String nomeProduto, double lanceAtual, String nomeArrematante) {
        String email = String.format(
                "[E-MAIL] Para: %s@leilao.com | Assunto: Seu lance foi superado no produto '%s'! " +
                "| Novo valor: R$ %.2f por %s",
                nomeArrematante.toLowerCase().replace(" ", "."),
                nomeProduto, lanceAtual, nomeArrematante);

        emailsEnviados.add(email);
        ultimoDestinatario = nomeArrematante;
        System.out.println(email);
    }

    public List<String> getEmailsEnviados() {
        return Collections.unmodifiableList(emailsEnviados);
    }

    public String getUltimoDestinatario() {
        return ultimoDestinatario;
    }

    public int getTotalEmailsEnviados() {
        return emailsEnviados.size();
    }
}
