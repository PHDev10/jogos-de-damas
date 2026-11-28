package com.damas.objetos;

class PosicionamentoInicial {

    private final int LINHAS_INICIAIS_POR_JOGADOR = 3;

    private final FabricaDePecas fabricaDePecas;

    PosicionamentoInicial(FabricaDePecas fabricaDePecas) {
        this.fabricaDePecas = fabricaDePecas;
    }

    void colocarPecas(Tabuleiro tabuleiro) {
        colocarPedras(tabuleiro, Cor.BRANCA, 0, LINHAS_INICIAIS_POR_JOGADOR - 1);
        colocarPedras(tabuleiro, Cor.VERMELHA, Tabuleiro.MAX_COLUNAS - LINHAS_INICIAIS_POR_JOGADOR,
                Tabuleiro.MAX_COLUNAS - 1);
    }

    private void colocarPedras(Tabuleiro tabuleiro, Cor cor, int linhaInicial, int linhaFinal) {
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = linhaInicial; y <= linhaFinal; y++) {
                if (x % 2 == y % 2) {
                    fabricaDePecas.criarPedra(tabuleiro.getCasa(x, y), cor);
                }
            }
        }
    }
}
