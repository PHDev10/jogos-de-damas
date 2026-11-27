package com.damas.objetos;

/**
 * Posiciona as pedras na configuração inicial do jogo, usando uma
 * {@code FabricaDePecas} para criá-las.
 */
class PosicionamentoInicial {

    private final int LINHAS_INICIAIS_POR_JOGADOR = 3;

    private final FabricaDePecas fabricaDePecas;

    /**
     * @param fabricaDePecas fábrica usada para criar as pedras
     */
    PosicionamentoInicial(FabricaDePecas fabricaDePecas) {
        this.fabricaDePecas = fabricaDePecas;
    }

    /**
     * Posiciona peças no tabuleiro: brancas na parte inferior, depois vermelhas na superior.
     * 
     * @param tabuleiro - tipo {@code Tabuleiro} onde as peças serão posicionadas
     */
    void colocarPecas(Tabuleiro tabuleiro) {

        // CRIA E PÕE AS PEÇAS NA PARTE INFERIOR DO TABULEIRO
        colocarPedras(tabuleiro, Cor.BRANCA, 0, LINHAS_INICIAIS_POR_JOGADOR - 1);
        // CRIA E POE AS PEÇAS NA PARTE SUPERIOR DO TABULEIRO
        colocarPedras(tabuleiro, Cor.VERMELHA, Tabuleiro.MAX_COLUNAS - LINHAS_INICIAIS_POR_JOGADOR,
                Tabuleiro.MAX_COLUNAS - 1);
    }

    /**
     * Cria pedras da cor informada nas casas de coluna e linha com a mesma paridade,
     * percorrendo x (externo) e y (interno).
     * 
     * @param tabuleiro   {@code Tabuleiro} onde as pedras serão posicionadas
     * @param cor         cor das pedras
     * @param linhaInicial primeira linha (y) a preencher, inclusive
     * @param linhaFinal   última linha (y) a preencher, inclusive
     */
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
