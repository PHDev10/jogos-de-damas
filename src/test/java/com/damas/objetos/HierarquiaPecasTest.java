package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Caracteriza a hierarquia: Pedra e Dama são irmãs (ambas PecaBase e Peca);
 * Dama não é mais uma Pedra.
 */
class HierarquiaPecasTest {

    private final Tabuleiro tabuleiro = new Tabuleiro();

    @Test
    void pedraEPecaEPecaBase() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(0, 0), Cor.BRANCA);

        assertInstanceOf(Peca.class, pedra);
        assertInstanceOf(PecaBase.class, pedra);
    }

    @Test
    void damaEPecaEPecaBase() {
        Dama dama = new Dama(tabuleiro.getCasa(1, 1), Cor.BRANCA);

        assertInstanceOf(Peca.class, dama);
        assertInstanceOf(PecaBase.class, dama);
    }

    @Test
    void damaNaoEPedra() {
        Object dama = new Dama(tabuleiro.getCasa(1, 1), Cor.VERMELHA);

        assertFalse(dama instanceof Pedra);
    }

    @Test
    void pedraNaoEDama() {
        Object pedra = new Pedra(tabuleiro.getCasa(0, 0), Cor.VERMELHA);

        assertFalse(pedra instanceof Dama);
    }

    @Test
    void casaGuardaEDevolvePecaDeQualquerEspecie() {
        Casa casaPedra = tabuleiro.getCasa(0, 0);
        Casa casaDama = tabuleiro.getCasa(1, 1);

        Peca pedra = new Pedra(casaPedra, Cor.BRANCA);
        Peca dama = new Dama(casaDama, Cor.VERMELHA);

        assertSame(pedra, casaPedra.getPeca());
        assertSame(dama, casaDama.getPeca());
    }

    @Test
    void moverAtravesDoContratoPecaAtualizaAsCasasParaAsDuasEspecies() {
        Peca pedra = new Pedra(tabuleiro.getCasa(0, 0), Cor.BRANCA);
        Peca dama = new Dama(tabuleiro.getCasa(4, 4), Cor.BRANCA);

        pedra.mover(tabuleiro.getCasa(1, 1));
        dama.mover(tabuleiro.getCasa(7, 7));

        assertFalse(tabuleiro.getCasa(0, 0).possuiPeca());
        assertFalse(tabuleiro.getCasa(4, 4).possuiPeca());
        assertSame(pedra, tabuleiro.getCasa(1, 1).getPeca());
        assertSame(dama, tabuleiro.getCasa(7, 7).getPeca());
    }

    @Test
    void pecaMovidaPassaAConsiderarODestinoComoSuaCasaAtual() {
        Peca dama = new Dama(tabuleiro.getCasa(0, 0), Cor.BRANCA);

        dama.mover(tabuleiro.getCasa(2, 2));

        assertTrue(dama.isMovimentoValido(tabuleiro.getCasa(5, 5)));
        assertFalse(dama.isMovimentoValido(tabuleiro.getCasa(0, 5)));
    }

    @Test
    void corEEspecieSaoIndependentes() {
        Peca[] pecas = {
            new Pedra(tabuleiro.getCasa(0, 0), Cor.BRANCA),
            new Pedra(tabuleiro.getCasa(1, 1), Cor.VERMELHA),
            new Dama(tabuleiro.getCasa(2, 2), Cor.BRANCA),
            new Dama(tabuleiro.getCasa(3, 3), Cor.VERMELHA)
        };
        Cor[] cores = {Cor.BRANCA, Cor.VERMELHA, Cor.BRANCA, Cor.VERMELHA};
        boolean[] damas = {false, false, true, true};

        for (int i = 0; i < pecas.length; i++) {
            assertEquals(cores[i], pecas[i].getCor());
            assertEquals(damas[i], pecas[i].isDama());
        }
    }
}
