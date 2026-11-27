package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Caracteriza, direto nas peças, as regras de simulação do movimento e de captura
 * disponível (antes presas em Jogo). Tabuleiro montado só com a API pública
 * (x = coluna, y = linha).
 */
class RegrasDeCapturaDasPecasTest {

    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final List<Casa> pecasAComer = new ArrayList<>();

    private Casa casa(int x, int y) {
        return tabuleiro.getCasa(x, y);
    }

    private Peca criar(boolean dama, int x, int y, Cor cor) {
        return dama ? new Dama(casa(x, y), cor) : new Pedra(casa(x, y), cor);
    }

    private boolean simular(Peca peca, int origemX, int origemY, int destinoX, int destinoY) {
        return peca.simularMovimento(tabuleiro, casa(origemX, origemY), casa(destinoX, destinoY), pecasAComer);
    }

    // ------------------------------------------------------------------ Pedra.simularMovimento

    @Nested
    @DisplayName("Pedra.simularMovimento")
    class SimularMovimentoDaPedra {

        @ParameterizedTest(name = "pedra {0}: 1 casa no sentido de avanço")
        @EnumSource(Cor.class)
        void umaCasaNoSentidoDeAvancoEhValidaESemCaptura(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);

            assertTrue(simular(pedra, 3, 3, 4, 3 + d));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "pedra {0}: 1 casa para trás")
        @EnumSource(Cor.class)
        void umaCasaParaTrasEhInvalida(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);

            assertFalse(simular(pedra, 3, 3, 4, 3 - d));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "pedra {0}: 2 casas sobre inimiga")
        @EnumSource(Cor.class)
        void duasCasasSobreInimigaComDestinoLivreCapturaAInimiga(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);
            criar(false, 4, 3 + d, cor.oposta());

            assertTrue(simular(pedra, 3, 3, 5, 3 + 2 * d));
            assertEquals(1, pecasAComer.size());
            assertSame(casa(4, 3 + d), pecasAComer.get(0));
        }

        @ParameterizedTest(name = "pedra {0}: 2 casas com intermediária vazia")
        @EnumSource(Cor.class)
        void duasCasasComIntermediariaVaziaEhInvalida(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);

            assertFalse(simular(pedra, 3, 3, 5, 3 + 2 * d));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "pedra {0}: 2 casas sobre peça da própria cor")
        @EnumSource(Cor.class)
        void duasCasasSobrePecaDaPropriaCorEhInvalidaELimpaPecasAComer(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);
            criar(false, 4, 3 + d, cor);
            pecasAComer.add(casa(0, 0));

            assertFalse(simular(pedra, 3, 3, 5, 3 + 2 * d));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "pedra {0}: destino ocupado")
        @EnumSource(Cor.class)
        void destinoOcupadoEhInvalido(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);
            criar(false, 4, 3 + d, cor.oposta());

            assertFalse(simular(pedra, 3, 3, 4, 3 + d));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "pedra {0}: distância 3")
        @EnumSource(Cor.class)
        void distanciaTresEhInvalida(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);

            assertFalse(simular(pedra, 3, 3, 6, 3 + 3 * d));
            assertTrue(pecasAComer.isEmpty());
        }
    }

    // ------------------------------------------------------------------ Dama.simularMovimento

    @Nested
    @DisplayName("Dama.simularMovimento")
    class SimularMovimentoDaDama {

        @ParameterizedTest(name = "dama {0}: deslocamento longo livre")
        @EnumSource(Cor.class)
        void deslocamentoLongoLivreEhValidoESemCaptura(Cor cor) {
            Peca dama = criar(true, 0, 0, cor);

            assertTrue(simular(dama, 0, 0, 6, 6));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "dama {0}: captura longa")
        @EnumSource(Cor.class)
        void capturaLongaComUmaInimigaNoCaminhoGuardaACasaCapturada(Cor cor) {
            Peca dama = criar(true, 0, 0, cor);
            criar(false, 3, 3, cor.oposta());

            assertTrue(simular(dama, 0, 0, 6, 6));
            assertEquals(1, pecasAComer.size());
            assertSame(casa(3, 3), pecasAComer.get(0));
        }

        @ParameterizedTest(name = "dama {0}: duas peças seguidas")
        @EnumSource(Cor.class)
        void duasPecasSeguidasNoCaminhoEhInvalidoELimpaPecasAComer(Cor cor) {
            Peca dama = criar(true, 0, 0, cor);
            criar(false, 3, 3, cor.oposta());
            criar(false, 4, 4, cor.oposta());
            pecasAComer.add(casa(7, 0));

            assertFalse(simular(dama, 0, 0, 6, 6));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "dama {0}: peça da mesma cor no caminho")
        @EnumSource(Cor.class)
        void pecaDaMesmaCorNoCaminhoEhInvalidoELimpaPecasAComer(Cor cor) {
            Peca dama = criar(true, 0, 0, cor);
            criar(false, 3, 3, cor);
            pecasAComer.add(casa(7, 0));

            assertFalse(simular(dama, 0, 0, 6, 6));
            assertTrue(pecasAComer.isEmpty());
        }

        @ParameterizedTest(name = "dama {0}: destino ocupado")
        @EnumSource(Cor.class)
        void destinoOcupadoEhInvalido(Cor cor) {
            Peca dama = criar(true, 0, 0, cor);
            criar(false, 4, 4, cor.oposta());

            assertFalse(simular(dama, 0, 0, 4, 4));
        }
    }

    // ------------------------------------------------------------------ temCapturaNaDirecao

    @Nested
    @DisplayName("temCapturaNaDirecao (Pedra e Dama)")
    class TemCapturaNaDirecao {

        private boolean temCaptura(Peca peca, int x, int y, int deltaX, int deltaY) {
            return peca.temCapturaNaDirecao(tabuleiro, casa(x, y), deltaX, deltaY);
        }

        @ParameterizedTest(name = "dama={0}: inimiga adjacente com casa de trás livre")
        @CsvSource({"false", "true"})
        void inimigaAdjacenteComCasaDeTrasLivreTemCaptura(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.BRANCA);
            criar(false, 4, 4, Cor.VERMELHA);

            assertTrue(temCaptura(peca, 3, 3, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @ParameterizedTest(name = "dama={0}: inimiga adjacente com casa de trás ocupada")
        @CsvSource({"false", "true"})
        void inimigaAdjacenteComCasaDeTrasOcupadaNaoTemCaptura(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.BRANCA);
            criar(false, 4, 4, Cor.VERMELHA);
            criar(false, 5, 5, Cor.VERMELHA);

            assertFalse(temCaptura(peca, 3, 3, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @ParameterizedTest(name = "dama={0}: peça da mesma cor adjacente")
        @CsvSource({"false", "true"})
        void pecaDaMesmaCorAdjacenteNaoTemCaptura(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.BRANCA);
            criar(false, 4, 4, Cor.BRANCA);

            assertFalse(temCaptura(peca, 3, 3, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @ParameterizedTest(name = "dama={0}: borda do tabuleiro")
        @CsvSource({"false", "true"})
        void bordaDoTabuleiroNaoTemCapturaENaoLancaExcecao(boolean dama) {
            Peca noCanto = criar(dama, 7, 7, Cor.BRANCA);
            Peca noCantoOposto = criar(dama, 0, 0, Cor.VERMELHA);

            assertFalse(temCaptura(noCanto, 7, 7, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
            assertFalse(temCaptura(noCantoOposto, 0, 0, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_BAIXO));
        }

        @ParameterizedTest(name = "dama={0}: inimiga encostada na borda")
        @CsvSource({"false", "true"})
        void inimigaEncostadaNaBordaNaoTemCaptura(boolean dama) {
            Peca peca = criar(dama, 5, 5, Cor.BRANCA);
            criar(false, 6, 6, Cor.VERMELHA);
            criar(false, 7, 7, Cor.VERMELHA);

            assertFalse(temCaptura(peca, 5, 5, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @Test
        void damaComCasasVaziasAntesDaInimigaTemCaptura() {
            Peca dama = criar(true, 1, 1, Cor.BRANCA);
            criar(false, 4, 4, Cor.VERMELHA);

            assertTrue(temCaptura(dama, 1, 1, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @Test
        void damaComDuasPecasSeguidasNaoTemCaptura() {
            Peca dama = criar(true, 1, 1, Cor.BRANCA);
            criar(false, 4, 4, Cor.VERMELHA);
            criar(false, 5, 5, Cor.VERMELHA);

            assertFalse(temCaptura(dama, 1, 1, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @Test
        void damaNaoTemCapturaQuandoHaPecaDaPropriaCorAntesDaInimiga() {
            Peca dama = criar(true, 1, 1, Cor.BRANCA);
            criar(false, 2, 2, Cor.BRANCA);
            criar(false, 4, 4, Cor.VERMELHA);

            assertFalse(temCaptura(dama, 1, 1, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @Test
        void pedraSoEnxergaInimigaAdjacente() {
            Peca pedra = criar(false, 1, 1, Cor.BRANCA);
            criar(false, 4, 4, Cor.VERMELHA);

            assertFalse(temCaptura(pedra, 1, 1, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA));
        }

        @ParameterizedTest(name = "dama={0}: direção sem peças")
        @CsvSource({"false", "true"})
        void direcaoSemPecasNaoTemCaptura(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.BRANCA);

            assertFalse(temCaptura(peca, 3, 3, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_BAIXO));
        }
    }

    // ------------------------------------------------------------------ podeContinuarCapturando

    @Nested
    @DisplayName("podeContinuarCapturando (Pedra e Dama)")
    class PodeContinuarCapturando {

        @ParameterizedTest(name = "dama={0}: só a última direção (esquerda/baixo) tem captura")
        @CsvSource({"false", "true"})
        void verdadeiroQuandoSoAUltimaDasQuatroDirecoesTemCaptura(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.BRANCA);
            criar(false, 2, 2, Cor.VERMELHA);

            assertTrue(peca.podeContinuarCapturando(tabuleiro, casa(3, 3)));
        }

        @ParameterizedTest(name = "dama={0}: nenhuma direção tem captura")
        @CsvSource({"false", "true"})
        void falsoQuandoNenhumaDirecaoTemCaptura(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.BRANCA);

            assertFalse(peca.podeContinuarCapturando(tabuleiro, casa(3, 3)));
        }

        @ParameterizedTest(name = "dama={0}: só a primeira direção (esquerda/cima) tem captura")
        @CsvSource({"false", "true"})
        void verdadeiroQuandoSoAPrimeiraDasQuatroDirecoesTemCaptura(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.VERMELHA);
            criar(false, 2, 4, Cor.BRANCA);

            assertTrue(peca.podeContinuarCapturando(tabuleiro, casa(3, 3)));
        }

        @ParameterizedTest(name = "pedra {0} detecta captura para trás")
        @EnumSource(Cor.class)
        void pedraDetectaCapturaParaTrasComportamentoPreservado(Cor cor) {
            int d = cor.direcaoAvanco();
            Peca pedra = criar(false, 3, 3, cor);
            criar(false, 4, 3 - d, cor.oposta());

            assertTrue(pedra.podeContinuarCapturando(tabuleiro, casa(3, 3)));
        }

        @ParameterizedTest(name = "dama={0}: peça da mesma cor adjacente")
        @CsvSource({"false", "true"})
        void falsoComPecaDaMesmaCorAdjacente(boolean dama) {
            Peca peca = criar(dama, 3, 3, Cor.BRANCA);
            criar(false, 4, 4, Cor.BRANCA);

            assertFalse(peca.podeContinuarCapturando(tabuleiro, casa(3, 3)));
        }
    }
}
