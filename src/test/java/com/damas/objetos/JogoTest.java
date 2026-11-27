package com.damas.objetos;

import static com.damas.objetos.JogoTestSupport.casa;
import static com.damas.objetos.JogoTestSupport.contarPecas;
import static com.damas.objetos.JogoTestSupport.contarTodasAsPecas;
import static com.damas.objetos.JogoTestSupport.damaBranca;
import static com.damas.objetos.JogoTestSupport.damaVermelha;
import static com.damas.objetos.JogoTestSupport.jogoVazio;
import static com.damas.objetos.JogoTestSupport.pedraBranca;
import static com.damas.objetos.JogoTestSupport.pedraVermelha;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class JogoTest {

    /** Fotografia do estado observável do jogo, para comparar "antes" e "depois". */
    private static String estado(Jogo jogo) {
        StringBuilder sb = new StringBuilder();
        sb.append("vez=").append(jogo.getVez());
        sb.append(" jogadas=").append(jogo.getJogada());
        sb.append(" semComer=").append(jogo.getJogadasSemComerPecas());
        sb.append(" pontos=").append(jogo.getJogadorUm().getPontos())
          .append('/').append(jogo.getJogadorDois().getPontos());
        Casa bloqueada = jogo.getCasaBloqueada();
        sb.append(" bloqueada=").append(bloqueada == null ? "null" : bloqueada.getX() + ":" + bloqueada.getY());
        sb.append(" pecas=");
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                Pedra peca = jogo.getTabuleiro().getCasa(x, y).getPeca();
                if (peca != null) {
                    sb.append('(').append(x).append(',').append(y).append(')')
                      .append(peca.getCor() == Cor.BRANCA ? 'B' : 'V').append(peca instanceof Dama ? 'D' : 'P');
                }
            }
        }
        return sb.toString();
    }

    private static void assertMovimentoRejeitado(Jogo jogo, int ox, int oy, int dx, int dy) {
        String antes = estado(jogo);
        jogo.moverPeca(ox, oy, dx, dy);
        assertEquals(antes, estado(jogo));
    }

    // ------------------------------------------------------------------ jogo novo

    @Nested
    @DisplayName("Jogo novo")
    class JogoNovo {

        private final Jogo jogo = new Jogo();

        @Test
        void jogoNovoTem12PecasBrancasE12Vermelhas() {
            assertEquals(12, contarPecas(jogo, Cor.BRANCA, false));
            assertEquals(12, contarPecas(jogo, Cor.VERMELHA, false));
            assertEquals(0, contarPecas(jogo, Cor.BRANCA, true));
            assertEquals(0, contarPecas(jogo, Cor.VERMELHA, true));
            assertEquals(24, contarTodasAsPecas(jogo));
        }

        @Test
        void pecasBrancasComecamNasTresPrimeirasLinhasEmCasasDeSomaPar() {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 3; y++) {
                    Pedra peca = jogo.getTabuleiro().getCasa(x, y).getPeca();
                    if ((x + y) % 2 == 0) {
                        assertNotNull(peca, "esperava peça branca em " + x + "," + y);
                        assertEquals(Cor.BRANCA, peca.getCor());
                        assertFalse(peca.isDama());
                    } else {
                        assertNull(peca, "esperava casa vazia em " + x + "," + y);
                    }
                }
            }
        }

        @Test
        void pecasVermelhasComecamNasTresUltimasLinhasEmCasasDeSomaPar() {
            for (int x = 0; x < 8; x++) {
                for (int y = 5; y < 8; y++) {
                    Pedra peca = jogo.getTabuleiro().getCasa(x, y).getPeca();
                    if ((x + y) % 2 == 0) {
                        assertNotNull(peca, "esperava peça vermelha em " + x + "," + y);
                        assertEquals(Cor.VERMELHA, peca.getCor());
                        assertFalse(peca.isDama());
                    } else {
                        assertNull(peca, "esperava casa vazia em " + x + "," + y);
                    }
                }
            }
        }

        @Test
        void linhasDoMeioComecamVazias() {
            for (int x = 0; x < 8; x++) {
                assertFalse(jogo.getTabuleiro().getCasa(x, 3).possuiPeca());
                assertFalse(jogo.getTabuleiro().getCasa(x, 4).possuiPeca());
            }
        }

        @Test
        void contadoresIniciais() {
            assertEquals(Cor.BRANCA, jogo.getVez());
            assertEquals(0, jogo.getJogada());
            assertEquals(0, jogo.getJogadasSemComerPecas());
            assertNull(jogo.getCasaBloqueada());
        }

        @Test
        void pontosIniciaisEGanhadorNenhum() {
            assertEquals(0, jogo.getJogadorUm().getPontos());
            assertEquals(0, jogo.getJogadorDois().getPontos());
            assertEquals(Optional.empty(), jogo.getGanhador());
        }

        @Test
        void nomesPadraoDosJogadores() {
            assertEquals("player branco", jogo.getJogadorUm().getNome());
            assertEquals("player vermelho", jogo.getJogadorDois().getNome());
        }

        @Test
        void setJogadoresSubstituemOsJogadores() {
            Jogador um = new Jogador("Ana");
            Jogador dois = new Jogador("Beto");

            jogo.setJogadorUm(um);
            jogo.setJogadorDois(dois);

            assertSame(um, jogo.getJogadorUm());
            assertSame(dois, jogo.getJogadorDois());
        }

        @Test
        void trocarDeVezAlternaEntre1e2() {
            jogo.trocarDeVez();
            assertEquals(Cor.VERMELHA, jogo.getVez());
            jogo.trocarDeVez();
            assertEquals(Cor.BRANCA, jogo.getVez());
        }

        @Test
        void colocarPecasPublicoAdicionaPecasEmTabuleiroVazio() {
            Tabuleiro outro = new Tabuleiro();

            jogo.colocarPecas(outro);

            assertTrue(outro.getCasa(0, 0).possuiPeca());
            assertEquals(Cor.BRANCA, outro.getCasa(0, 0).getPeca().getCor());
            assertFalse(outro.getCasa(0, 0).getPeca().isDama());
            assertEquals(Cor.VERMELHA, outro.getCasa(1, 5).getPeca().getCor());
            assertFalse(outro.getCasa(1, 5).getPeca().isDama());
        }
    }

    // ------------------------------------------------------------ movimento simples

    @Nested
    @DisplayName("Movimento simples")
    class MovimentoSimples {

        @Test
        void pedraBrancaAvancaUmaCasaEAlternaAVez() {
            Jogo jogo = new Jogo();
            Pedra peca = casa(jogo, 0, 2).getPeca();

            jogo.moverPeca(0, 2, 1, 3);

            assertNull(casa(jogo, 0, 2).getPeca());
            assertSame(peca, casa(jogo, 1, 3).getPeca());
            assertEquals(Cor.VERMELHA, jogo.getVez());
            assertEquals(1, jogo.getJogada());
            assertEquals(1, jogo.getJogadasSemComerPecas());
            assertNull(jogo.getCasaBloqueada());
            assertEquals(0, jogo.getJogadorUm().getPontos());
        }

        @Test
        void pedraVermelhaAvancaUmaCasaNaVezDoVermelho() {
            Jogo jogo = new Jogo();
            jogo.moverPeca(0, 2, 1, 3);
            Pedra peca = casa(jogo, 1, 5).getPeca();

            jogo.moverPeca(1, 5, 0, 4);

            assertNull(casa(jogo, 1, 5).getPeca());
            assertSame(peca, casa(jogo, 0, 4).getPeca());
            assertEquals(Cor.BRANCA, jogo.getVez());
            assertEquals(2, jogo.getJogada());
            assertEquals(2, jogo.getJogadasSemComerPecas());
        }

        @Test
        void pedraBrancaAvancaParaADireitaEParaAEsquerda() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(3, 3, 4, 4);
            assertTrue(casa(jogo, 4, 4).possuiPeca());

            jogo.moverPeca(7, 7, 6, 6);
            jogo.moverPeca(4, 4, 3, 5);
            assertTrue(casa(jogo, 3, 5).possuiPeca());
            assertEquals(3, jogo.getJogada());
        }

        // ---- rejeições: estado inalterado

        @Test
        void movimentoForaDaVezEhRejeitado() {
            Jogo jogo = new Jogo();

            assertMovimentoRejeitado(jogo, 1, 5, 0, 4);
        }

        @Test
        void movimentoDaBrancaNaVezDoVermelhoEhRejeitado() {
            Jogo jogo = new Jogo();
            jogo.moverPeca(0, 2, 1, 3);

            assertMovimentoRejeitado(jogo, 2, 2, 3, 3);
        }

        @Test
        void destinoOcupadoEhRejeitado() {
            Jogo jogo = new Jogo();

            assertMovimentoRejeitado(jogo, 0, 0, 1, 1);
        }

        @Test
        void pedraBrancaNaoAndaParaTras() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 3, 4);
            pedraVermelha(jogo, 7, 7);

            assertMovimentoRejeitado(jogo, 3, 4, 2, 3);
            assertMovimentoRejeitado(jogo, 3, 4, 4, 3);
        }

        @Test
        void pedraVermelhaNaoAndaParaTras() {
            Jogo jogo = jogoVazio();
            pedraVermelha(jogo, 3, 4);
            pedraBranca(jogo, 7, 0);
            jogo.trocarDeVez();

            assertMovimentoRejeitado(jogo, 3, 4, 2, 5);
            assertMovimentoRejeitado(jogo, 3, 4, 4, 5);
        }

        @Test
        void movimentoNaoDiagonalEhRejeitado() {
            Jogo jogo = new Jogo();

            assertMovimentoRejeitado(jogo, 0, 2, 0, 3); // vertical
            assertMovimentoRejeitado(jogo, 0, 2, 2, 2); // horizontal (destino ocupado em jogo novo)
            assertMovimentoRejeitado(jogo, 0, 2, 2, 3); // "cavalo"
        }

        @Test
        void movimentoParaAPropriaCasaEhRejeitado() {
            Jogo jogo = new Jogo();

            assertMovimentoRejeitado(jogo, 0, 2, 0, 2);
        }

        @Test
        void distanciaDeDuasCasasSemPecaParaComerEhRejeitada() {
            Jogo jogo = new Jogo();

            assertMovimentoRejeitado(jogo, 0, 2, 2, 4);
        }

        @Test
        void distanciaDeDuasCasasSobrePecaPropriaEhRejeitada() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraBranca(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            assertMovimentoRejeitado(jogo, 2, 2, 4, 4);
        }

        @Test
        void distanciaDeDuasCasasSobreInimigoComDestinoOcupadoEhRejeitada() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 4, 4);
            pedraVermelha(jogo, 7, 7);

            assertMovimentoRejeitado(jogo, 2, 2, 4, 4);
        }

        @Test
        void distanciaDeTresCasasParaPedraEhRejeitada() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 7, 7);

            assertMovimentoRejeitado(jogo, 2, 2, 5, 5);
        }

        @Test
        void movimentoRejeitadoNaoConsomeAVezNemPontos() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(2, 2, 2, 3);
            jogo.moverPeca(2, 2, 2, 2);

            assertEquals(Cor.BRANCA, jogo.getVez());
            assertEquals(0, jogo.getJogada());
        }

        // ---- exceções do código atual

        @Test
        void moverAPartirDeCasaVaziaLancaNullPointerException() {
            Jogo jogo = new Jogo();

            assertThrows(NullPointerException.class, () -> jogo.moverPeca(0, 3, 1, 4));
        }

        @Test
        void moverParaForaDoTabuleiroLancaArrayIndexOutOfBounds() {
            Jogo jogo = new Jogo();

            assertThrows(ArrayIndexOutOfBoundsException.class, () -> jogo.moverPeca(0, 2, -1, 3));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> jogo.moverPeca(0, 2, 8, 3));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> jogo.moverPeca(8, 2, 7, 3));
        }
    }

    // ---------------------------------------------------------------- captura de pedra

    @Nested
    @DisplayName("Captura por pedra")
    class CapturaPedra {

        @Test
        void pedraBrancaCapturaSemSequenciaPontuaETrocaAVez() {
            Jogo jogo = jogoVazio();
            Pedra branca = pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(2, 2, 4, 4);

            assertNull(casa(jogo, 2, 2).getPeca());
            assertNull(casa(jogo, 3, 3).getPeca());
            assertSame(branca, casa(jogo, 4, 4).getPeca());
            assertEquals(1, jogo.getJogadorUm().getPontos());
            assertEquals(0, jogo.getJogadorDois().getPontos());
            assertEquals(0, jogo.getJogadasSemComerPecas());
            assertEquals(1, jogo.getJogada());
            assertEquals(Cor.VERMELHA, jogo.getVez());
            assertNull(jogo.getCasaBloqueada());
        }

        @Test
        void pedraVermelhaCapturaSemSequenciaPontuaETrocaAVez() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 0, 0);
            pedraBranca(jogo, 3, 3);
            Pedra vermelha = pedraVermelha(jogo, 4, 4);
            jogo.trocarDeVez();

            jogo.moverPeca(4, 4, 2, 2);

            assertNull(casa(jogo, 3, 3).getPeca());
            assertSame(vermelha, casa(jogo, 2, 2).getPeca());
            assertEquals(0, jogo.getJogadorUm().getPontos());
            assertEquals(1, jogo.getJogadorDois().getPontos());
            assertEquals(0, jogo.getJogadasSemComerPecas());
            assertEquals(Cor.BRANCA, jogo.getVez());
            assertNull(jogo.getCasaBloqueada());
        }

        @Test
        void capturaZeraOContadorDeJogadasSemComer() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraBranca(jogo, 0, 0);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(0, 0, 1, 1);
            jogo.moverPeca(7, 7, 6, 6);
            assertEquals(2, jogo.getJogadasSemComerPecas());

            jogo.moverPeca(2, 2, 4, 4);

            assertEquals(0, jogo.getJogadasSemComerPecas());
            assertEquals(3, jogo.getJogada());
        }

        @Test
        void capturaComSequenciaPossivelBloqueiaACasaEMantemAVez() {
            Jogo jogo = jogoVazio();
            Pedra branca = pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 5, 5);
            pedraVermelha(jogo, 7, 0);

            jogo.moverPeca(2, 2, 4, 4);

            assertSame(branca, casa(jogo, 4, 4).getPeca());
            assertSame(casa(jogo, 4, 4), jogo.getCasaBloqueada());
            assertEquals(Cor.BRANCA, jogo.getVez());
            assertEquals(1, jogo.getJogada());
            assertEquals(1, jogo.getJogadorUm().getPontos());
            assertEquals(0, jogo.getJogadasSemComerPecas());
        }

        @Test
        void comCasaBloqueadaOutraPecaNaoPodeSerMovida() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraBranca(jogo, 0, 0);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 5, 5);
            pedraVermelha(jogo, 7, 1);
            jogo.moverPeca(2, 2, 4, 4);

            assertMovimentoRejeitado(jogo, 0, 0, 1, 1);
        }

        @Test
        void comCasaBloqueadaPecaDoAdversarioNaoPodeSerMovida() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 5, 5);
            pedraVermelha(jogo, 7, 1);
            jogo.moverPeca(2, 2, 4, 4);

            assertMovimentoRejeitado(jogo, 7, 1, 6, 0);
        }

        @Test
        void comCasaBloqueadaPassoSimplesDaPecaBloqueadaEhIgnorado() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 5, 5);
            pedraVermelha(jogo, 7, 0);
            jogo.moverPeca(2, 2, 4, 4);

            assertMovimentoRejeitado(jogo, 4, 4, 3, 5);
            assertMovimentoRejeitado(jogo, 4, 4, 5, 3);
        }

        @Test
        void continuarACapturaLiberaACasaETrocaAVezQuandoNaoHaMaisCapturas() {
            Jogo jogo = jogoVazio();
            Pedra branca = pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 5, 5);
            pedraVermelha(jogo, 7, 0);
            jogo.moverPeca(2, 2, 4, 4);

            jogo.moverPeca(4, 4, 6, 6);

            assertNull(casa(jogo, 5, 5).getPeca());
            assertSame(branca, casa(jogo, 6, 6).getPeca());
            assertNull(jogo.getCasaBloqueada());
            assertEquals(Cor.VERMELHA, jogo.getVez());
            assertEquals(2, jogo.getJogada());
            assertEquals(0, jogo.getJogadasSemComerPecas());
        }

        /**
         * COMPORTAMENTO ESTRANHO (registrado, não corrigido): ao continuar uma captura em
         * sequência a partir da casa bloqueada, Jogo.moverPeca chama simularMovimentoEValidar
         * duas vezes (uma no ramo "bloqueado" e outra na chamada recursiva), e a mesma peça
         * entra duas vezes em pecasAComer. Resultado: 1 peça capturada vale 2 pontos.
         * Aqui: 1 ponto da primeira captura + 2 pelo segundo salto = 3 pontos para 2 peças.
         */
        @Test
        void capturaEmSequenciaDuplicaOsPontosDaPecaCapturadaNaContinuacao() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 5, 5);
            pedraVermelha(jogo, 7, 0);

            jogo.moverPeca(2, 2, 4, 4);
            assertEquals(1, jogo.getJogadorUm().getPontos());

            jogo.moverPeca(4, 4, 6, 6);

            assertEquals(3, jogo.getJogadorUm().getPontos());
            assertEquals(1, contarPecas(jogo, Cor.VERMELHA, false));
        }

        @Test
        void pedraTambemCapturaParaTrasPoisONaoVerificaDirecaoNaCaptura() {
            Jogo jogo = jogoVazio();
            Pedra branca = pedraBranca(jogo, 4, 4);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(4, 4, 2, 2);

            assertNull(casa(jogo, 3, 3).getPeca());
            assertSame(branca, casa(jogo, 2, 2).getPeca());
            assertEquals(1, jogo.getJogadorUm().getPontos());
            assertEquals(Cor.VERMELHA, jogo.getVez());
        }

        @Test
        void pedraNaoCapturaPecaPropriaNemSaltaDuasPecas() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            // salto de 3 casas não é permitido para pedra
            assertMovimentoRejeitado(jogo, 2, 2, 5, 5);
        }
    }

    // ------------------------------------------------------------------------- dama

    @Nested
    @DisplayName("Dama")
    class DamaNoJogo {

        @Test
        void damaBrancaMoveLivrementeVariasCasasEmQualquerDirecao() {
            Jogo jogo = jogoVazio();
            Dama dama = damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(0, 0, 5, 5);
            assertSame(dama, casa(jogo, 5, 5).getPeca());
            assertEquals(Cor.VERMELHA, jogo.getVez());
            assertEquals(1, jogo.getJogadasSemComerPecas());

            jogo.moverPeca(7, 7, 6, 6);
            jogo.moverPeca(5, 5, 2, 2);

            assertSame(dama, casa(jogo, 2, 2).getPeca());
            assertEquals(3, jogo.getJogada());
            assertEquals(3, jogo.getJogadasSemComerPecas());
        }

        @Test
        void damaNaoSeMoveForaDaDiagonal() {
            Jogo jogo = jogoVazio();
            damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 7, 7);

            assertMovimentoRejeitado(jogo, 0, 0, 0, 5);
            assertMovimentoRejeitado(jogo, 0, 0, 3, 5);
        }

        @Test
        void damaCapturaUmaPecaADistancia() {
            Jogo jogo = jogoVazio();
            Dama dama = damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 2, 2);
            pedraVermelha(jogo, 7, 1);

            jogo.moverPeca(0, 0, 5, 5);

            assertNull(casa(jogo, 2, 2).getPeca());
            assertSame(dama, casa(jogo, 5, 5).getPeca());
            assertEquals(1, jogo.getJogadorUm().getPontos());
            assertEquals(0, jogo.getJogadasSemComerPecas());
            assertEquals(Cor.VERMELHA, jogo.getVez());
            assertNull(jogo.getCasaBloqueada());
        }

        @Test
        void damaCapturaDuasPecasNoMesmoMovimentoQuandoHaCasaVaziaEntreElas() {
            Jogo jogo = jogoVazio();
            damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 2, 2);
            pedraVermelha(jogo, 4, 4);
            pedraVermelha(jogo, 7, 1);

            jogo.moverPeca(0, 0, 5, 5);

            assertNull(casa(jogo, 2, 2).getPeca());
            assertNull(casa(jogo, 4, 4).getPeca());
            assertEquals(2, jogo.getJogadorUm().getPontos());
            assertEquals(Cor.VERMELHA, jogo.getVez());
        }

        @Test
        void damaNaoPulaDuasPecasAdjacentes() {
            Jogo jogo = jogoVazio();
            damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 1);

            assertMovimentoRejeitado(jogo, 0, 0, 5, 5);
        }

        @Test
        void damaNaoPulaPecaPropria() {
            Jogo jogo = jogoVazio();
            damaBranca(jogo, 0, 0);
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 7, 1);

            assertMovimentoRejeitado(jogo, 0, 0, 5, 5);
        }

        @Test
        void damaCapturaEFicaBloqueadaQuandoHaNovaCapturaPossivel() {
            Jogo jogo = jogoVazio();
            Dama dama = damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 2, 2);
            pedraVermelha(jogo, 5, 3);
            pedraVermelha(jogo, 7, 1);

            jogo.moverPeca(0, 0, 4, 4);

            assertSame(dama, casa(jogo, 4, 4).getPeca());
            assertSame(casa(jogo, 4, 4), jogo.getCasaBloqueada());
            assertEquals(Cor.BRANCA, jogo.getVez());
            assertEquals(1, jogo.getJogadorUm().getPontos());
        }

        @Test
        void damaVermelhaCapturaNaVezDoVermelho() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 7, 0);
            Dama dama = damaVermelha(jogo, 5, 2);
            pedraBranca(jogo, 3, 4);
            jogo.trocarDeVez();

            jogo.moverPeca(5, 2, 2, 5);

            assertNull(casa(jogo, 3, 4).getPeca());
            assertSame(dama, casa(jogo, 2, 5).getPeca());
            assertEquals(1, jogo.getJogadorDois().getPontos());
            assertEquals(Cor.BRANCA, jogo.getVez());
        }

        @Test
        void damaSobreCasaOcupadaEhRejeitada() {
            Jogo jogo = jogoVazio();
            damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 4, 4);

            assertMovimentoRejeitado(jogo, 0, 0, 4, 4);
        }
    }

    // ----------------------------------------------------------------------- promoção

    @Nested
    @DisplayName("Promoção a dama")
    class Promocao {

        @Test
        void pedraBrancaViraDamaAoChegarEmY7() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 1, 6);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(1, 6, 2, 7);

            Pedra peca = casa(jogo, 2, 7).getPeca();
            assertInstanceOf(Dama.class, peca);
            assertEquals(Cor.BRANCA, peca.getCor());
            assertTrue(peca.isDama());
            assertEquals(Cor.VERMELHA, jogo.getVez());
        }

        @Test
        void pedraVermelhaViraDamaAoChegarEmY0() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 0, 0);
            pedraVermelha(jogo, 2, 1);
            jogo.trocarDeVez();

            jogo.moverPeca(2, 1, 1, 0);

            Pedra peca = casa(jogo, 1, 0).getPeca();
            assertInstanceOf(Dama.class, peca);
            assertEquals(Cor.VERMELHA, peca.getCor());
            assertTrue(peca.isDama());
            assertEquals(Cor.BRANCA, jogo.getVez());
        }

        @Test
        void pedraNaoViraDamaForaDaUltimaLinha() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 1, 5);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(1, 5, 2, 6);

            Pedra peca = casa(jogo, 2, 6).getPeca();
            assertFalse(peca instanceof Dama);
            assertEquals(Cor.BRANCA, peca.getCor());
            assertFalse(peca.isDama());
        }

        @Test
        void promocaoPorCapturaPontuaEViraDama() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 1, 5);
            pedraVermelha(jogo, 2, 6);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(1, 5, 3, 7);

            Pedra peca = casa(jogo, 3, 7).getPeca();
            assertInstanceOf(Dama.class, peca);
            assertEquals(Cor.BRANCA, peca.getCor());
            assertTrue(peca.isDama());
            assertEquals(1, jogo.getJogadorUm().getPontos());
            assertEquals(Cor.VERMELHA, jogo.getVez());
        }

        @Test
        void damaJaPromovidaNaoEhRepromovida() {
            Jogo jogo = jogoVazio();
            Dama dama = damaBranca(jogo, 0, 0);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(0, 0, 3, 3);

            assertSame(dama, casa(jogo, 3, 3).getPeca());
        }
    }

    // ----------------------------------------------------------------------- fim de jogo

    @Nested
    @DisplayName("Fim de jogo")
    class FimDeJogo {

        @Test
        void ganhadorEhUmQuandoBrancoChegaA12Pontos() {
            Jogo jogo = jogoVazio();
            jogo.getJogadorUm().addPonto(11);
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);
            assertEquals(Optional.empty(), jogo.getGanhador());

            jogo.moverPeca(2, 2, 4, 4);

            assertEquals(12, jogo.getJogadorUm().getPontos());
            assertEquals(Optional.of(Cor.BRANCA), jogo.getGanhador());
        }

        @Test
        void ganhadorEhDoisQuandoVermelhoChegaA12Pontos() {
            Jogo jogo = jogoVazio();
            jogo.getJogadorDois().addPonto(11);
            pedraBranca(jogo, 7, 7);
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            jogo.trocarDeVez();
            assertEquals(Optional.empty(), jogo.getGanhador());

            jogo.moverPeca(3, 3, 1, 1);

            assertEquals(12, jogo.getJogadorDois().getPontos());
            assertEquals(Optional.of(Cor.VERMELHA), jogo.getGanhador());
        }

        @ParameterizedTest(name = "pontos brancos={0}, vermelhos={1} -> ganhador {2}")
        @CsvSource({
                "0,0,NENHUM",
                "11,11,NENHUM",
                "12,0,BRANCA",
                "0,12,VERMELHA",
                "12,12,BRANCA",
                "13,0,NENHUM",
                "0,13,NENHUM",
                "13,12,VERMELHA"
        })
        void getGanhadorSoReconheceExatamente12Pontos(int pontosUm, int pontosDois, String esperado) {
            Jogo jogo = new Jogo();
            jogo.getJogadorUm().addPonto(pontosUm);
            jogo.getJogadorDois().addPonto(pontosDois);

            Optional<Cor> ganhadorEsperado = esperado.equals("NENHUM")
                    ? Optional.empty()
                    : Optional.of(Cor.valueOf(esperado));
            assertEquals(ganhadorEsperado, jogo.getGanhador());
        }

        @Test
        void jogoNaoImpedeMovimentosDepoisDeHaverGanhador() {
            Jogo jogo = jogoVazio();
            jogo.getJogadorUm().addPonto(12);
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(2, 2, 3, 3);

            assertEquals(Optional.of(Cor.BRANCA), jogo.getGanhador());
            assertTrue(casa(jogo, 3, 3).possuiPeca());
            assertEquals(1, jogo.getJogada());
        }
    }

    // ------------------------------------------------------------------ jogador por cor

    @Nested
    @DisplayName("Jogador por cor e vez")
    class JogadorPorCor {

        @Test
        void vezInicialEBranca() {
            assertEquals(Cor.BRANCA, new Jogo().getVez());
        }

        @Test
        void vezAlternaEntreBrancaEVermelha() {
            Jogo jogo = new Jogo();

            jogo.trocarDeVez();
            assertEquals(Cor.VERMELHA, jogo.getVez());

            jogo.trocarDeVez();
            assertEquals(Cor.BRANCA, jogo.getVez());
        }

        @Test
        void ganhadorEhVazioEmJogoNovo() {
            assertEquals(Optional.empty(), new Jogo().getGanhador());
            assertFalse(new Jogo().getGanhador().isPresent());
        }

        @Test
        void getJogadorBrancaDevolveJogadorUmEVermelhaDevolveJogadorDois() {
            Jogo jogo = new Jogo();

            assertSame(jogo.getJogadorUm(), jogo.getJogador(Cor.BRANCA));
            assertSame(jogo.getJogadorDois(), jogo.getJogador(Cor.VERMELHA));
        }

        @Test
        void getJogadorAcompanhaSetJogadorUmESetJogadorDois() {
            Jogo jogo = new Jogo();
            Jogador um = new Jogador("Ana");
            Jogador dois = new Jogador("Beto");

            jogo.setJogadorUm(um);
            jogo.setJogadorDois(dois);

            assertSame(um, jogo.getJogador(Cor.BRANCA));
            assertSame(dois, jogo.getJogador(Cor.VERMELHA));
            assertSame(um, jogo.getJogadorUm());
            assertSame(dois, jogo.getJogadorDois());
        }

        @Test
        void pontoDaCapturaVaiParaOJogadorDaCorDaVez() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(2, 2, 4, 4);

            assertEquals(1, jogo.getJogador(Cor.BRANCA).getPontos());
            assertEquals(0, jogo.getJogador(Cor.VERMELHA).getPontos());
        }

        @Test
        void pontoDeJogadorSubstituidoVaiParaONovoJogador() {
            Jogo jogo = jogoVazio();
            Jogador novoBranco = new Jogador("Ana");
            jogo.setJogadorUm(novoBranco);
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 7, 7);

            jogo.moverPeca(2, 2, 4, 4);

            assertEquals(1, novoBranco.getPontos());
        }

        @Test
        void ganhadorBrancoEhIdentificadoPelaCor() {
            Jogo jogo = new Jogo();
            jogo.getJogador(Cor.BRANCA).addPonto(12);

            assertEquals(Optional.of(Cor.BRANCA), jogo.getGanhador());
        }

        @Test
        void ganhadorVermelhoEhIdentificadoPelaCor() {
            Jogo jogo = new Jogo();
            jogo.getJogador(Cor.VERMELHA).addPonto(12);

            assertEquals(Optional.of(Cor.VERMELHA), jogo.getGanhador());
        }

        @Test
        void quandoAmbosTem12PontosOGanhadorEBrancoPorPrecedencia() {
            Jogo jogo = new Jogo();
            jogo.getJogadorUm().addPonto(12);
            jogo.getJogadorDois().addPonto(12);

            assertEquals(Optional.of(Cor.BRANCA), jogo.getGanhador());
        }

        @Test
        void moverPecaDaCorForaDaVezContinuaSendoRejeitado() {
            Jogo jogo = jogoVazio();
            pedraVermelha(jogo, 3, 4);
            pedraBranca(jogo, 7, 0);

            assertMovimentoRejeitado(jogo, 3, 4, 2, 3);

            jogo.trocarDeVez();
            jogo.moverPeca(3, 4, 2, 3);

            assertTrue(casa(jogo, 2, 3).possuiPeca());
            assertEquals(Cor.BRANCA, jogo.getVez());
        }

        @Test
        void damaTambemSoMoveNaVezDaSuaCor() {
            Jogo jogo = jogoVazio();
            damaVermelha(jogo, 0, 0);
            pedraBranca(jogo, 7, 0);

            assertMovimentoRejeitado(jogo, 0, 0, 3, 3);
        }
    }

    // ----------------------------------------------------------------------- toString

    @Nested
    @DisplayName("Jogo.toString()")
    class ToStringDoJogo {

        @Test
        void textoDoStatusEmJogoNovo() {
            String esperado = "Vez: player branco\n"
                    + "Nº de jogadas: 0\n"
                    + "Jogadas sem comer peça: 0\n"
                    + "\n"
                    + "Informações do(a) jogador(a) player branco\n"
                    + "Pontos: 0\n"
                    + "Nº de peças restantes: 12\n"
                    + "\n"
                    + "Informações do(a) jogador(a) player vermelho\n"
                    + "Pontos: 0\n"
                    + "Nº de peças restantes: 12\n";

            assertEquals(esperado, new Jogo().toString());
        }

        @Test
        void textoDoStatusComCasaBloqueada() {
            Jogo jogo = jogoVazio();
            pedraBranca(jogo, 2, 2);
            pedraVermelha(jogo, 3, 3);
            pedraVermelha(jogo, 5, 5);
            jogo.moverPeca(2, 2, 4, 4);

            String esperado = "Vez: player branco\n"
                    + "Nº de jogadas: 1\n"
                    + "Jogadas sem comer peça: 0\n"
                    + "\n"
                    + "Informações do(a) jogador(a) player branco\n"
                    + "Pontos: 1\n"
                    + "Nº de peças restantes: 12\n"
                    + "\n"
                    + "Informações do(a) jogador(a) player vermelho\n"
                    + "Pontos: 0\n"
                    + "Nº de peças restantes: 11\n"
                    + "\n"
                    + "Mova a peça na casa 4:4!";

            assertEquals(esperado, jogo.toString());
        }

        @Test
        void textoMostraONomeDoJogadorDaVezAposTrocarDeVez() {
            Jogo jogo = new Jogo();
            jogo.setJogadorUm(new Jogador("Ana"));
            jogo.setJogadorDois(new Jogador("Beto"));
            jogo.moverPeca(0, 2, 1, 3);

            String texto = jogo.toString();

            assertTrue(texto.startsWith("Vez: Beto\n"));
            assertTrue(texto.contains("Informações do(a) jogador(a) Ana\n"));
            assertTrue(texto.contains("Informações do(a) jogador(a) Beto\n"));
            assertTrue(texto.contains("Nº de jogadas: 1\n"));
            assertTrue(texto.contains("Jogadas sem comer peça: 1\n"));
        }
    }
}
