package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TabuleiroTest {

    @Test
    void constantesDoTabuleiro() {
        assertEquals(8, Tabuleiro.MAX_LINHAS);
        assertEquals(8, Tabuleiro.MAX_COLUNAS);
        assertEquals(-1, Tabuleiro.X_ESQUERDA);
        assertEquals(1, Tabuleiro.X_DIREITA);
        assertEquals(-1, Tabuleiro.Y_BAIXO);
        assertEquals(1, Tabuleiro.Y_CIMA);
    }

    @Test
    void tabuleiroTem64CasasComAsCoordenadasCorretas() {
        Tabuleiro tabuleiro = new Tabuleiro();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Casa casa = tabuleiro.getCasa(x, y);
                assertNotNull(casa);
                assertEquals(x, casa.getX());
                assertEquals(y, casa.getY());
                assertFalse(casa.possuiPeca());
            }
        }
    }

    @Test
    void getCasaSempreRetornaAMesmaInstancia() {
        Tabuleiro tabuleiro = new Tabuleiro();

        assertSame(tabuleiro.getCasa(3, 4), tabuleiro.getCasa(3, 4));
    }

    @Test
    void getCasaForaDosLimitesLancaArrayIndexOutOfBounds() {
        Tabuleiro tabuleiro = new Tabuleiro();

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> tabuleiro.getCasa(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> tabuleiro.getCasa(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> tabuleiro.getCasa(8, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> tabuleiro.getCasa(0, 8));
    }

    @Test
    void colocarERemoverPecaPeloTabuleiro() {
        Tabuleiro tabuleiro = new Tabuleiro();
        Casa casa = tabuleiro.getCasa(5, 6);

        Pedra pedra = new Pedra(casa, Peca.PEDRA_VERMELHA);
        assertSame(pedra, tabuleiro.getCasa(5, 6).getPeca());

        tabuleiro.getCasa(5, 6).removerPeca();
        assertFalse(tabuleiro.getCasa(5, 6).possuiPeca());
    }
}
