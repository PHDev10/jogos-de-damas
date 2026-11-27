package com.damas.objetos;

public enum Cor {
    BRANCA,
    VERMELHA;

    private static final int DIRECAO_AVANCO_BRANCAS = 1;
    private static final int DIRECAO_AVANCO_VERMELHAS = -1;

    private static final int LINHA_PROMOCAO_BRANCAS = 7;
    private static final int LINHA_PROMOCAO_VERMELHAS = 0;

    public Cor oposta() {
        return this == BRANCA ? VERMELHA : BRANCA;
    }

    public int direcaoAvanco() {
        return this == BRANCA ? DIRECAO_AVANCO_BRANCAS : DIRECAO_AVANCO_VERMELHAS;
    }

    public int linhaPromocao() {
        return this == BRANCA ? LINHA_PROMOCAO_BRANCAS : LINHA_PROMOCAO_VERMELHAS;
    }
}
