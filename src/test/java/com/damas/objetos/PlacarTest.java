package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class PlacarTest {

    private final Jogador um = new Jogador("um");
    private final Jogador dois = new Jogador("dois");
    private final Placar placar = new Placar(um, dois);

    @Test
    void getJogadorDevolveUmParaBrancaEDoisParaVermelha() {
        assertSame(um, placar.getJogador(Cor.BRANCA));
        assertSame(dois, placar.getJogador(Cor.VERMELHA));
        assertSame(um, placar.getJogadorUm());
        assertSame(dois, placar.getJogadorDois());
    }

    @Test
    void setJogadoresSubstituemOsJogadoresDoPlacar() {
        Jogador novoUm = new Jogador("novo um");
        Jogador novoDois = new Jogador("novo dois");

        placar.setJogadorUm(novoUm);
        placar.setJogadorDois(novoDois);

        assertSame(novoUm, placar.getJogador(Cor.BRANCA));
        assertSame(novoDois, placar.getJogador(Cor.VERMELHA));
    }

    @Test
    void semPontosParaVencerNaoHaGanhador() {
        um.addPonto(11);
        dois.addPonto(11);

        assertTrue(placar.getGanhador().isEmpty());
    }

    @Test
    void jogadorUmCom12PontosEBranca() {
        um.addPonto(12);

        assertEquals(Optional.of(Cor.BRANCA), placar.getGanhador());
    }

    @Test
    void jogadorDoisCom12PontosEVermelha() {
        dois.addPonto(12);

        assertEquals(Optional.of(Cor.VERMELHA), placar.getGanhador());
    }

    @Test
    void empateEm12ConferePrimeiroOJogadorUm() {
        um.addPonto(12);
        dois.addPonto(12);

        assertEquals(Optional.of(Cor.BRANCA), placar.getGanhador());
    }

    @Test
    void pecasRestantesSaoOTotalMenosOsPontosDoAdversario() {
        um.addPonto(5);
        dois.addPonto(3);

        assertEquals(12 - 3, placar.pecasRestantes(Cor.BRANCA));
        assertEquals(12 - 5, placar.pecasRestantes(Cor.VERMELHA));
    }
}
