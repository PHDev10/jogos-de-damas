package com.damas.objetos;

/**
 * Cor de um jogador ou de uma peça do jogo.
 */
public enum Cor {
    BRANCA,
    VERMELHA;

    /**
     * @return a cor adversária: {@code BRANCA} para {@code VERMELHA} e vice-versa.
     */
    public Cor oposta() {
        return this == BRANCA ? VERMELHA : BRANCA;
    }
}
