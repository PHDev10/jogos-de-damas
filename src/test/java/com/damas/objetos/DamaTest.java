package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DamaTest {

    private final Tabuleiro tabuleiro = new Tabuleiro();

    private Dama damaEm44() {
        return new Dama(tabuleiro.getCasa(4, 4), Cor.BRANCA);
    }

    @Test
    void damaEUmaPecaDaCorInformada() {
        Dama branca = new Dama(tabuleiro.getCasa(0, 0), Cor.BRANCA);
        Dama vermelha = new Dama(tabuleiro.getCasa(1, 1), Cor.VERMELHA);

        assertInstanceOf(PecaBase.class, branca);
        assertEquals(Cor.BRANCA, branca.getCor());
        assertEquals(Cor.VERMELHA, vermelha.getCor());
        assertTrue(branca.isDama());
        assertTrue(vermelha.isDama());
    }

    @Test
    void construtorPosicionaADamaNaCasaSubstituindoAPedra() {
        Casa casa = tabuleiro.getCasa(2, 2);
        new Pedra(casa, Cor.BRANCA);

        Dama dama = new Dama(casa, Cor.BRANCA);

        assertSame(dama, casa.getPeca());
    }

    @Test
    void moverDamaFuncionaComoMoverPedra() {
        Dama dama = damaEm44();
        Casa destino = tabuleiro.getCasa(7, 7);

        dama.mover(destino);

        assertNull(tabuleiro.getCasa(4, 4).getPeca());
        assertSame(dama, destino.getPeca());
    }

    /**
     * Tabela observada de Dama.isMovimentoValido a partir de (4,4):
     * qualquer casa na mesma diagonal é válida, inclusive a própria casa.
     */
    @ParameterizedTest(name = "dama (4,4)->({0},{1}) = {2}")
    @CsvSource({
            "5,5,true",
            "6,6,true",
            "7,7,true",
            "3,3,true",
            "2,2,true",
            "1,1,true",
            "0,0,true",
            "5,3,true",
            "6,2,true",
            "7,1,true",
            "3,5,true",
            "2,6,true",
            "1,7,true",
            "4,4,true",
            "4,5,false",
            "5,4,false",
            "6,5,false",
            "7,6,false",
            "5,6,false",
            "0,4,false",
            "4,0,false",
            "7,4,false"
    })
    void isMovimentoValidoDaDamaConformeObservado(int x, int y, boolean esperado) {
        Dama dama = damaEm44();

        assertEquals(esperado, dama.isMovimentoValido(tabuleiro.getCasa(x, y)));
    }

    @Test
    void damaConsideraValidaAPropriaCasaDeOrigem() {
        Dama dama = damaEm44();

        assertTrue(dama.isMovimentoValido(tabuleiro.getCasa(4, 4)));
    }

    @Test
    void damaAceitaDistanciasQuePedraRejeita() {
        Pedra pedra = new Pedra(tabuleiro.getCasa(4, 4), Cor.BRANCA);
        Dama dama = new Dama(tabuleiro.getCasa(0, 0), Cor.BRANCA);
        Casa distante = tabuleiro.getCasa(7, 7);

        assertEquals(false, pedra.isMovimentoValido(distante));
        assertTrue(dama.isMovimentoValido(distante));
    }

    @Test
    void damaBrancaTemCorBranca() {
        Dama dama = new Dama(tabuleiro.getCasa(0, 0), Cor.BRANCA);

        assertEquals(Cor.BRANCA, dama.getCor());
    }

    @Test
    void damaVermelhaTemCorVermelha() {
        Dama dama = new Dama(tabuleiro.getCasa(0, 0), Cor.VERMELHA);

        assertEquals(Cor.VERMELHA, dama.getCor());
    }

    @Test
    void damaSeIdentificaComoDamaMesmoQuandoVistaComoPecaBaseOuPeca() {
        PecaBase comoPecaBase = new Dama(tabuleiro.getCasa(0, 0), Cor.BRANCA);
        Peca comoPeca = new Dama(tabuleiro.getCasa(1, 1), Cor.VERMELHA);

        assertTrue(comoPecaBase.isDama());
        assertTrue(comoPeca.isDama());
    }

    @ParameterizedTest(name = "dama branca nunca e promovida (linha {0})")
    @CsvSource({"0", "1", "2", "3", "4", "5", "6", "7"})
    void damaBrancaNaoPodeSerPromovidaEmNenhumaLinha(int linha) {
        Dama dama = new Dama(tabuleiro.getCasa(0, 0), Cor.BRANCA);

        assertFalse(dama.podeSerPromovida(tabuleiro.getCasa(3, linha)));
    }

    @ParameterizedTest(name = "dama vermelha nunca e promovida (linha {0})")
    @CsvSource({"0", "1", "2", "3", "4", "5", "6", "7"})
    void damaVermelhaNaoPodeSerPromovidaEmNenhumaLinha(int linha) {
        Dama dama = new Dama(tabuleiro.getCasa(0, 0), Cor.VERMELHA);

        assertFalse(dama.podeSerPromovida(tabuleiro.getCasa(3, linha)));
    }
}
