package com.damas.objetos;

/**
 * Cor de um jogador ou de uma peça do jogo.
 */
public enum Cor {
    BRANCA,
    VERMELHA;

    private static final int DIRECAO_AVANCO_BRANCAS = 1;
    private static final int DIRECAO_AVANCO_VERMELHAS = -1;

    private static final int LINHA_PROMOCAO_BRANCAS = 7;
    private static final int LINHA_PROMOCAO_VERMELHAS = 0;

    /**
     * @return a cor adversária: {@code BRANCA} para {@code VERMELHA} e vice-versa.
     */
    public Cor oposta() {
        return this == BRANCA ? VERMELHA : BRANCA;
    }

    /**
     * @return o sentido (no eixo Y) em que as pedras desta cor avançam: +1 para
     *         {@code BRANCA} e -1 para {@code VERMELHA}.
     */
    public int direcaoAvanco() {
        return this == BRANCA ? DIRECAO_AVANCO_BRANCAS : DIRECAO_AVANCO_VERMELHAS;
    }

    /**
     * @return a linha (coordenada Y) em que as pedras desta cor viram dama: 7 para
     *         {@code BRANCA} e 0 para {@code VERMELHA}.
     */
    public int linhaPromocao() {
        return this == BRANCA ? LINHA_PROMOCAO_BRANCAS : LINHA_PROMOCAO_VERMELHAS;
    }
}
