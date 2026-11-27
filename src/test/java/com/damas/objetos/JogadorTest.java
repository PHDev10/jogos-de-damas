package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class JogadorTest {

    @Test
    void nomeDefaultEAnonimo() {
        assertEquals("Anônimo", Jogador.DEFAULT_NAME);
    }

    @Test
    void jogadorNovoGuardaNomeEComecaComZeroPontos() {
        Jogador jogador = new Jogador("Pedro");

        assertEquals("Pedro", jogador.getNome());
        assertEquals(0, jogador.getPontos());
    }

    @Test
    void nomeCom16CaracteresEAceito() {
        Jogador jogador = new Jogador("1234567890123456");

        assertEquals("1234567890123456", jogador.getNome());
    }

    @Test
    void nomeComMaisDe16CaracteresViraAnonimo() {
        Jogador jogador = new Jogador("12345678901234567");

        assertEquals("Anônimo", jogador.getNome());
    }

    @Test
    void nomeVazioEAceito() {
        assertEquals("", new Jogador("").getNome());
    }

    @Test
    void nomeNuloLancaNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Jogador(null));
    }

    @Test
    void setNomeNaoAlteraONomeComportamentoAtual() {
        Jogador jogador = new Jogador("Original");

        jogador.setNome("Novo");

        assertEquals("Original", jogador.getNome());
    }

    @Test
    void setNomeNaoAlteraONomeMesmoComNomeInvalido() {
        Jogador jogador = new Jogador("Original");

        jogador.setNome("um nome com mais de dezesseis caracteres");

        assertEquals("Original", jogador.getNome());
    }

    @Test
    void setNomeComNuloLancaNullPointerException() {
        Jogador jogador = new Jogador("Original");

        assertThrows(NullPointerException.class, () -> jogador.setNome(null));
    }

    @Test
    void addPontoSemArgumentoIncrementaEmUm() {
        Jogador jogador = new Jogador("Pedro");

        jogador.addPonto();
        jogador.addPonto();

        assertEquals(2, jogador.getPontos());
    }

    @Test
    void addPontoComArgumentoSomaOValor() {
        Jogador jogador = new Jogador("Pedro");

        jogador.addPonto(5);
        jogador.addPonto(3);

        assertEquals(8, jogador.getPontos());
    }

    @Test
    void addPontoComValorNegativoSubtraiSemValidacao() {
        Jogador jogador = new Jogador("Pedro");
        jogador.addPonto(5);

        jogador.addPonto(-2);

        assertEquals(3, jogador.getPontos());
    }
}
