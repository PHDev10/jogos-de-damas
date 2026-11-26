package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CorTest {

    @Test
    void opostaDeBrancaEVermelha() {
        assertEquals(Cor.VERMELHA, Cor.BRANCA.oposta());
    }

    @Test
    void opostaDeVermelhaEBranca() {
        assertEquals(Cor.BRANCA, Cor.VERMELHA.oposta());
    }

    @Test
    void opostaDaOpostaVoltaALorigem() {
        for (Cor cor : Cor.values()) {
            assertEquals(cor, cor.oposta().oposta());
        }
    }

    @Test
    void enumTemApenasBrancaEVermelha() {
        assertEquals(2, Cor.values().length);
        assertEquals(Cor.BRANCA, Cor.valueOf("BRANCA"));
        assertEquals(Cor.VERMELHA, Cor.valueOf("VERMELHA"));
    }
}
