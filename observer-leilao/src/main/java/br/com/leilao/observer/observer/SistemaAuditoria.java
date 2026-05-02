package br.com.leilao.observer.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SistemaAuditoria implements Observador {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final List<String> registrosAuditoria;

    public SistemaAuditoria() {
        this.registrosAuditoria = new ArrayList<>();
    }

    @Override
    public void atualizar(String nomeProduto, double lanceAtual, String nomeArrematante) {
        String registro = String.format(
                "[AUDITORIA | %s] Produto: %-25s | Arrematante: %-20s | Valor: R$ %10.2f",
                LocalDateTime.now().format(FORMATTER),
                nomeProduto,
                nomeArrematante,
                lanceAtual);

        registrosAuditoria.add(registro);
        System.out.println(registro);
    }

    public void exibirRelatorio() {
        System.out.println("\n RELATÓRIO DE AUDITORIA ");
        if (registrosAuditoria.isEmpty()) {
            System.out.println("Nenhum lance registrado.");
        } else {
            registrosAuditoria.forEach(System.out::println);
        }
        System.out.println("Total de lances auditados: " + registrosAuditoria.size());
        System.out.println("\n");
    }

    public List<String> getRegistrosAuditoria() {
        return Collections.unmodifiableList(registrosAuditoria);
    }
}
