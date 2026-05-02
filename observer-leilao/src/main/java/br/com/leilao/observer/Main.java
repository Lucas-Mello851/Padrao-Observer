package br.com.leilao.observer;

import br.com.leilao.observer.model.Lance;
import br.com.leilao.observer.observer.ParticipanteLeilao;
import br.com.leilao.observer.observer.SistemaAuditoria;
import br.com.leilao.observer.observer.SistemaEmail;
import br.com.leilao.observer.subject.Leilao;

public class Main {

    public static void main(String[] args) {

        System.out.println(" ");
        System.out.println("  SISTEMA DE LEILÃO ONLINE — Padrão Observer");
        System.out.println("\n");

        Leilao leilao = new Leilao("Notebook Gamer RTX 4070", 2000.00);

        ParticipanteLeilao ana    = new ParticipanteLeilao("Ana Silva");
        ParticipanteLeilao bruno  = new ParticipanteLeilao("Bruno Costa");
        ParticipanteLeilao carlos = new ParticipanteLeilao("Carlos Melo");
        SistemaAuditoria   auditoria = new SistemaAuditoria();
        SistemaEmail       email     = new SistemaEmail();

        leilao.registrarObservador(ana);
        leilao.registrarObservador(bruno);
        leilao.registrarObservador(carlos);
        leilao.registrarObservador(auditoria);
        leilao.registrarObservador(email);

        System.out.println("> Leilão iniciado! Produto: " + leilao.getNomeProduto());
        System.out.println("> Lance mínimo: R$ " + leilao.getLanceMinimo());
        System.out.println("> Observadores registrados: " + leilao.getTotalObservadores());
        System.out.println();

        System.out.println(" Lance 1: Ana dá R$ 2.200,00 ");
        leilao.darLance(new Lance("Ana Silva", 2200.00));

        System.out.println("\n Carlos se desinscreve (não quer mais acompanhar) ");
        leilao.removerObservador(carlos);

        System.out.println("\n Lance 2: Bruno dá R$ 2.500,00 ");
        leilao.darLance(new Lance("Bruno Costa", 2500.00));

        System.out.println("\n Lance 3: Ana dá R$ 2.800,00 ");
        leilao.darLance(new Lance("Ana Silva", 2800.00));

        System.out.println("\n Lance 4: Bruno tenta dar R$ 2.700,00 (inválido) ");
        try {
            leilao.darLance(new Lance("Bruno Costa", 2700.00));
        } catch (IllegalArgumentException e) {
            System.out.println("[RECUSADO] " + e.getMessage());
        }

        System.out.println("\n Lance 5: Bruno dá R$ 3.100,00 ");
        leilao.darLance(new Lance("Bruno Costa", 3100.00));

        leilao.encerrar();

        auditoria.exibirRelatorio();

        System.out.println(" HISTÓRICO DE LANCES ");
        leilao.getHistoricoDeLances().forEach(System.out::println);
        System.out.println("\n");

        System.out.println("E-mails disparados: " + email.getTotalEmailsEnviados());
        System.out.println("Notificações recebidas por Ana: " + ana.getNotificacoes().size());
        System.out.println("Notificações recebidas por Bruno: " + bruno.getNotificacoes().size());
        System.out.println("Carlos (removido) recebeu: " + carlos.getNotificacoes().size() + " notificações após remoção");
    }
}
