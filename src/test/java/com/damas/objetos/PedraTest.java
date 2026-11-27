package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class PedraTest {

    private final Tabuleiro tabuleiro = new Tabuleiro();

    private Pedra pedraEm44() {
        return new Pedra(tabuleiro.getCasa(4, 4), Cor.BRANCA);
    }

    @Test
    void construtorPosicionaAPedraNaCasa() {
        Casa casa = tabuleiro.getCasa(1, 1);

        Pedra pedra = new Pedra(casa, Cor.VERMELHA);

        assertSame(pedra, casa.getPeca());
        assertEquals(Cor.VERMELHA, pedra.getCor());
        assertFalse(pedra.isDama());
    }

    @Test
    void moverTiraAPedraDaCasaAntigaEColoqueNaNova() {
        Pedra pedra = pedraEm44();
        Casa origem = tabuleiro.getCasa(4, 4);
        Casa destino = tabuleiro.getCasa(5, 5);

        pedra.mover(destino);

        assertNull(origem.getPeca());
        assertSame(pedra, destino.getPeca());
    }

    @Test
    void moverNaoValidaOMovimento() {
        Pedra pedra = pedraEm44();
        Casa longe = tabuleiro.getCasa(0, 7);

        pedra.mover(longe);

        assertSame(pedra, longe.getPeca());
        assertFalse(tabuleiro.getCasa(4, 4).possuiPeca());
    }

    /**
     * Tabela observada de Pedra.isMovimentoValido a partir de (4,4).
     * Não considera direção (branca/vermelha) nem ocupação de casas: apenas
     * diagonais de distância 1 ou 2 são válidas.
     */
    @ParameterizedTest(name = "pedra (4,4)->({0},{1}) = {2}")
    @CsvSource({
            "5,5,true",
            "3,3,true",
            "5,3,true",
            "3,5,true",
            "6,6,true",
            "2,2,true",
            "6,2,true",
            "2,6,true",
            "7,7,false",
            "1,1,false",
            "0,0,false",
            "1,7,false",
            "7,1,false",
            "4,4,false",
            "4,5,false",
            "5,4,false",
            "0,4,false",
            "4,0,false",
            "6,5,false",
            "5,6,false",
            "7,6,false"
    })
    void isMovimentoValidoDaPedraConformeObservado(int x, int y, boolean esperado) {
        Pedra pedra = pedraEm44();

        assertEquals(esperado, pedra.isMovimentoValido(tabuleiro.getCasa(x, y)));
    }

    @Test
    void pedraAceitaMovimentoDeDuasCasasParaTrasSemVerificarDirecao() {
        Pedra branca = new Pedra(tabuleiro.getCasa(4, 4), Cor.BRANCA);
        Pedra vermelha = new Pedra(tabuleiro.getCasa(2, 2), Cor.VERMELHA);

        assertTrue(branca.isMovimentoValido(tabuleiro.getCasa(2, 2)));
        assertTrue(vermelha.isMovimentoValido(tabuleiro.getCasa(4, 4)));
    }

    @Test
    void movimentoValidoDependeApenasDaCasaDaPedraNaoDoDestinoOcupado() {
        Pedra pedra = pedraEm44();
        new Pedra(tabuleiro.getCasa(5, 5), Cor.VERMELHA);

        assertTrue(pedra.isMovimentoValido(tabuleiro.getCasa(5, 5)));
    }

    @Test
    void pedraBrancaTemCorBranca() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 0), Cor.BRANCA);

        assertEquals(Cor.BRANCA, pedra.getCor());
    }

    @Test
    void pedraVermelhaTemCorVermelha() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 0), Cor.VERMELHA);

        assertEquals(Cor.VERMELHA, pedra.getCor());
    }

    @ParameterizedTest(name = "cor {0}")
    @EnumSource(Cor.class)
    void getCorDevolveACorInformadaNoConstrutor(Cor cor) {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 0), cor);

        assertEquals(cor, pedra.getCor());
    }

    @ParameterizedTest(name = "pedra {0} nao e dama")
    @EnumSource(Cor.class)
    void pedraNaoEDama(Cor cor) {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 0), cor);

        assertFalse(pedra.isDama());
    }

    @Test
    void pedraBrancaPodeSerPromovidaNaLinha7() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 6), Cor.BRANCA);

        assertTrue(pedra.podeSerPromovida(tabuleiro.getCasa(1, 7)));
    }

    @Test
    void pedraBrancaNaoPodeSerPromovidaNaLinha6() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 5), Cor.BRANCA);

        assertFalse(pedra.podeSerPromovida(tabuleiro.getCasa(1, 6)));
    }

    @Test
    void pedraBrancaNaoPodeSerPromovidaNaLinhaDePromocaoDaVermelha() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(1, 1), Cor.BRANCA);

        assertFalse(pedra.podeSerPromovida(tabuleiro.getCasa(0, 0)));
    }

    @Test
    void pedraVermelhaPodeSerPromovidaNaLinha0() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(2, 1), Cor.VERMELHA);

        assertTrue(pedra.podeSerPromovida(tabuleiro.getCasa(1, 0)));
    }

    @Test
    void pedraVermelhaNaoPodeSerPromovidaNaLinha1() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(2, 2), Cor.VERMELHA);

        assertFalse(pedra.podeSerPromovida(tabuleiro.getCasa(1, 1)));
    }

    @Test
    void pedraVermelhaNaoPodeSerPromovidaNaLinhaDePromocaoDaBranca() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(1, 6), Cor.VERMELHA);

        assertFalse(pedra.podeSerPromovida(tabuleiro.getCasa(0, 7)));
    }

    @Test
    void podeSerPromovidaOlhaApenasParaACasaInformadaENaoParaAPosicaoDaPedra() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 0), Cor.BRANCA);

        assertTrue(pedra.podeSerPromovida(tabuleiro.getCasa(5, 7)));
        assertFalse(pedra.podeSerPromovida(tabuleiro.getCasa(0, 0)));
    }
}
