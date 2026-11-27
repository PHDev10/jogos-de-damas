package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CasaTest {

    @Test
    void casaNovaGuardaPosicaoEIniciaSemPeca() {
        Casa casa = new Casa(3, 5);

        assertEquals(3, casa.getX());
        assertEquals(5, casa.getY());
        assertNull(casa.getPeca());
        assertFalse(casa.possuiPeca());
    }

    @Test
    void colocarPecaFazACasaPossuirAPeca() {
        Casa casa = new Casa(0, 0);
        Pedra pedra = new Pedra(new Casa(1, 1), Cor.BRANCA);

        casa.colocarPeca(pedra);

        assertTrue(casa.possuiPeca());
        assertSame(pedra, casa.getPeca());
    }

    @Test
    void removerPecaLiberaACasa() {
        Casa casa = new Casa(2, 2);
        new Pedra(casa, Cor.VERMELHA);

        casa.removerPeca();

        assertFalse(casa.possuiPeca());
        assertNull(casa.getPeca());
    }

    @Test
    void removerPecaEmCasaVaziaNaoFazNada() {
        Casa casa = new Casa(2, 2);

        casa.removerPeca();

        assertFalse(casa.possuiPeca());
    }

    @Test
    void colocarPecaSubstituiPecaAnteriorSemValidacao() {
        Casa casa = new Casa(4, 4);
        Pedra primeira = new Pedra(casa, Cor.BRANCA);
        Pedra segunda = new Pedra(casa, Cor.VERMELHA);

        assertSame(segunda, casa.getPeca());
        assertFalse(casa.getPeca() == primeira);
    }

    @Test
    void colocarPecaAceitaNull() {
        Casa casa = new Casa(4, 4);
        new Pedra(casa, Cor.BRANCA);

        casa.colocarPeca(null);

        assertFalse(casa.possuiPeca());
    }
}
