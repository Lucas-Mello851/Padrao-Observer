package br.com.leilao.observer;

import br.com.leilao.observer.model.Lance;
import br.com.leilao.observer.observer.ParticipanteLeilao;
import br.com.leilao.observer.observer.SistemaAuditoria;
import br.com.leilao.observer.observer.SistemaEmail;
import br.com.leilao.observer.subject.Leilao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do Padrão Observer - Sistema de Leilão")
class LeilaoObserverTest {

    private Leilao leilao;
    private ParticipanteLeilao ana;
    private ParticipanteLeilao bruno;
    private SistemaAuditoria auditoria;
    private SistemaEmail email;

    @BeforeEach
    void setUp() {
        leilao   = new Leilao("Notebook Gamer RTX 4070", 2000.00);
        ana      = new ParticipanteLeilao("Ana Silva");
        bruno    = new ParticipanteLeilao("Bruno Costa");
        auditoria = new SistemaAuditoria();
        email    = new SistemaEmail();
    }

    @Test
    @DisplayName("Deve registrar um observador corretamente")
    void deveRegistrarObservador() {
        leilao.registrarObservador(ana);
        assertEquals(1, leilao.getTotalObservadores());
    }

    @Test
    @DisplayName("Não deve registrar o mesmo observador duas vezes")
    void naoDeveRegistrarObservadorDuplicado() {
        leilao.registrarObservador(ana);
        leilao.registrarObservador(ana);
        assertEquals(1, leilao.getTotalObservadores());
    }

    @Test
    @DisplayName("Deve remover um observador corretamente")
    void deveRemoverObservador() {
        leilao.registrarObservador(ana);
        leilao.registrarObservador(bruno);
        leilao.removerObservador(ana);
        assertEquals(1, leilao.getTotalObservadores());
    }

    @Test
    @DisplayName("Observador removido não deve ser notificado")
    void observadorRemovidoNaoDeveSerNotificado() {
        leilao.registrarObservador(ana);
        leilao.registrarObservador(bruno);

        leilao.darLance(new Lance("Ana Silva", 2200.00));
        assertEquals(1, ana.getNotificacoes().size());

        leilao.removerObservador(ana);
        leilao.darLance(new Lance("Bruno Costa", 2500.00));

        assertEquals(1, ana.getNotificacoes().size());
        assertEquals(2, bruno.getNotificacoes().size());
    }

    @Test
    @DisplayName("Observer deve ser notificado ao dar lance válido")
    void deveNotificarObserverAoDarLance() {
        leilao.registrarObservador(ana);
        leilao.darLance(new Lance("Bruno Costa", 2500.00));

        assertEquals(1, ana.getNotificacoes().size());
    }

    @Test
    @DisplayName("Observer deve receber os dados corretos do lance")
    void deveReceberDadosCorretosDoLance() {
        leilao.registrarObservador(ana);
        leilao.darLance(new Lance("Bruno Costa", 2500.00));

        assertEquals("Notebook Gamer RTX 4070", ana.getUltimoProdutoNotificado());
        assertEquals(2500.00, ana.getUltimoLanceRecebido(), 0.001);
    }

    @Test
    @DisplayName("Todos os observers devem ser notificados simultaneamente")
    void todoObserversDevemSerNotificados() {
        leilao.registrarObservador(ana);
        leilao.registrarObservador(bruno);
        leilao.registrarObservador(auditoria);
        leilao.registrarObservador(email);

        leilao.darLance(new Lance("Carlos Melo", 2300.00));

        assertEquals(1, ana.getNotificacoes().size());
        assertEquals(1, bruno.getNotificacoes().size());
        assertEquals(1, auditoria.getRegistrosAuditoria().size());
        assertEquals(1, email.getTotalEmailsEnviados());
    }

    @Test
    @DisplayName("Deve acumular notificações em múltiplos lances")
    void deveAcumularNotificacoesEmMultiplosLances() {
        leilao.registrarObservador(ana);

        leilao.darLance(new Lance("Bruno Costa", 2200.00));
        leilao.darLance(new Lance("Carlos Melo", 2400.00));
        leilao.darLance(new Lance("Ana Silva", 2600.00));

        assertEquals(3, ana.getNotificacoes().size());
    }

    @Test
    @DisplayName("Lance abaixo do mínimo deve lançar exceção")
    void lanceBaixoDoMinimoDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> leilao.darLance(new Lance("Ana Silva", 1500.00)));
    }

    @Test
    @DisplayName("Lance abaixo do maior lance atual deve lançar exceção")
    void lanceBaixoDoMaiorLanceDeveLancarExcecao() {
        leilao.darLance(new Lance("Ana Silva", 2500.00));

        assertThrows(IllegalArgumentException.class,
                () -> leilao.darLance(new Lance("Bruno Costa", 2300.00)));
    }

    @Test
    @DisplayName("Lance igual ao maior lance atual deve lançar exceção")
    void lanceIgualAoMaiorLanceDeveLancarExcecao() {
        leilao.darLance(new Lance("Ana Silva", 2500.00));

        assertThrows(IllegalArgumentException.class,
                () -> leilao.darLance(new Lance("Bruno Costa", 2500.00)));
    }

    @Test
    @DisplayName("Lance inválido não deve notificar os observers")
    void lanceInvalidoNaoDeveNotificarObservers() {
        leilao.registrarObservador(ana);

        try {
            leilao.darLance(new Lance("Bruno Costa", 500.00));
        } catch (IllegalArgumentException ignored) {}

        assertEquals(0, ana.getNotificacoes().size());
    }

    @Test
    @DisplayName("Leilão encerrado não deve aceitar novos lances")
    void leilaoEncerradoNaoDeveAceitarLances() {
        leilao.darLance(new Lance("Ana Silva", 2200.00));
        leilao.encerrar();

        assertThrows(IllegalStateException.class,
                () -> leilao.darLance(new Lance("Bruno Costa", 2500.00)));
    }

    @Test
    @DisplayName("Após encerrar, o flag isEncerrado deve ser verdadeiro")
    void deveMarcarLeilaoComoEncerrado() {
        leilao.darLance(new Lance("Ana Silva", 2200.00));
        leilao.encerrar();

        assertTrue(leilao.isEncerrado());
    }

    @Test
    @DisplayName("Deve manter o maior lance correto após vários lances")
    void deveManteroMaiorLanceCorreto() {
        leilao.darLance(new Lance("Ana Silva", 2200.00));
        leilao.darLance(new Lance("Bruno Costa", 2800.00));
        leilao.darLance(new Lance("Ana Silva", 3100.00));

        assertEquals(3100.00, leilao.getMaiorLance().getValor(), 0.001);
        assertEquals("Ana Silva", leilao.getMaiorLance().getNomeParticipante());
    }

    @Test
    @DisplayName("Deve manter o histórico correto de todos os lances")
    void deveManteroHistoricoCorreto() {
        leilao.darLance(new Lance("Ana Silva", 2200.00));
        leilao.darLance(new Lance("Bruno Costa", 2500.00));
        leilao.darLance(new Lance("Carlos Melo", 2900.00));

        assertEquals(3, leilao.getHistoricoDeLances().size());
    }

    @Test
    @DisplayName("Sistema de e-mail deve registrar o último destinatário corretamente")
    void sistemaEmailDeveRegistrarDestinatarioCorreto() {
        leilao.registrarObservador(email);
        leilao.darLance(new Lance("Ana Silva", 2500.00));

        assertEquals("Ana Silva", email.getUltimoDestinatario());
    }

    @Test
    @DisplayName("Sistema de auditoria deve registrar todos os lances")
    void sistemaAuditoriaDeveRegistrarTodosLances() {
        leilao.registrarObservador(auditoria);

        leilao.darLance(new Lance("Ana Silva", 2200.00));
        leilao.darLance(new Lance("Bruno Costa", 2500.00));

        assertEquals(2, auditoria.getRegistrosAuditoria().size());
    }
}
