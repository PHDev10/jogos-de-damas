package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PedraTest {

    private final Tabuleiro tabuleiro = new Tabuleiro();

    private Pedra pedraEm44() {
        return new Pedra(tabuleiro.getCasa(4, 4), Peca.PEDRA_BRANCA);
    }

    @Test
    void construtorPosicionaAPedraNaCasa() {
        Casa casa = tabuleiro.getCasa(1, 1);

        Pedra pedra = new Pedra(casa, Peca.PEDRA_VERMELHA);

        assertSame(pedra, casa.getPeca());
        assertEquals(Peca.PEDRA_VERMELHA, pedra.getTipo());
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

    @Test
    void tiposDasPecas() {
        assertEquals(0, Peca.PEDRA_BRANCA);
        assertEquals(1, Peca.DAMA_BRANCA);
        assertEquals(2, Peca.PEDRA_VERMELHA);
        assertEquals(3, Peca.DAMA_VERMELHA);
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
        Pedra branca = new Pedra(tabuleiro.getCasa(4, 4), Peca.PEDRA_BRANCA);
        Pedra vermelha = new Pedra(tabuleiro.getCasa(2, 2), Peca.PEDRA_VERMELHA);

        assertTrue(branca.isMovimentoValido(tabuleiro.getCasa(2, 2)));
        assertTrue(vermelha.isMovimentoValido(tabuleiro.getCasa(4, 4)));
    }

    @Test
    void movimentoValidoDependeApenasDaCasaDaPedraNaoDoDestinoOcupado() {
        Pedra pedra = pedraEm44();
        new Pedra(tabuleiro.getCasa(5, 5), Peca.PEDRA_VERMELHA);

        assertTrue(pedra.isMovimentoValido(tabuleiro.getCasa(5, 5)));
    }
}
