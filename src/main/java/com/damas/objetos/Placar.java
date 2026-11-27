package com.damas.objetos;

import java.util.Optional;

/**
 * Guarda os dois jogadores e sabe responder sobre pontos: quem ganhou e quantas
 * peças restam a cada um.
 */
class Placar {

    private final int PECAS_POR_JOGADOR = 12;
    private final int PONTOS_PARA_VENCER = PECAS_POR_JOGADOR;

    private Jogador jogadorUm;
    private Jogador jogadorDois;

    /**
     * @param jogadorUm   jogador das peças brancas
     * @param jogadorDois jogador das peças vermelhas
     */
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

    /**
     * @param cor cor do jogador
     * @return o jogador um para {@code Cor.BRANCA} e o jogador dois para {@code Cor.VERMELHA}
     */
    Jogador getJogador(Cor cor) {
        return cor == Cor.BRANCA ? jogadorUm : jogadorDois;
    }

    /**
     * @return {@code Optional.empty()} se ninguém atingiu os pontos para vencer; senão a cor
     *         do vencedor (confere o jogador um primeiro)
     */
    Optional<Cor> getGanhador() {
        if (jogadorUm.getPontos() == PONTOS_PARA_VENCER)
            return Optional.of(Cor.BRANCA);
        if (jogadorDois.getPontos() == PONTOS_PARA_VENCER)
            return Optional.of(Cor.VERMELHA);
        return Optional.empty();
    }

    /**
     * @param cor cor do jogador
     * @return peças restantes do jogador: o total menos os pontos do adversário
     */
    int pecasRestantes(Cor cor) {
        return PECAS_POR_JOGADOR - getJogador(cor.oposta()).getPontos();
    }
}
