package com.damas.objetos;

import java.util.Optional;

class Placar {

    private final int PECAS_POR_JOGADOR = 12;
    private final int PONTOS_PARA_VENCER = PECAS_POR_JOGADOR;

    private Jogador jogadorUm;
    private Jogador jogadorDois;

    Placar(Jogador jogadorUm, Jogador jogadorDois) {
        this.jogadorUm = jogadorUm;
        this.jogadorDois = jogadorDois;
    }

    Jogador getJogadorUm() {
        return jogadorUm;
    }

    Jogador getJogadorDois() {
        return jogadorDois;
    }

    void setJogadorUm(Jogador jogador) {
        jogadorUm = jogador;
    }

    void setJogadorDois(Jogador jogador) {
        jogadorDois = jogador;
    }

    Jogador getJogador(Cor cor) {
        return cor == Cor.BRANCA ? jogadorUm : jogadorDois;
    }

    Optional<Cor> getGanhador() {
        if (jogadorUm.getPontos() == PONTOS_PARA_VENCER)
            return Optional.of(Cor.BRANCA);
        if (jogadorDois.getPontos() == PONTOS_PARA_VENCER)
            return Optional.of(Cor.VERMELHA);
        return Optional.empty();
    }

    int pecasRestantes(Cor cor) {
        return PECAS_POR_JOGADOR - getJogador(cor.oposta()).getPontos();
    }
}
