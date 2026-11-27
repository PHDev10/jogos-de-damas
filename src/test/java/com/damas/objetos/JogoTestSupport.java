package com.damas.objetos;

/**
 * Utilitários de teste: monta posições específicas usando apenas a API pública
 * do domínio.
 */
final class JogoTestSupport {

    private JogoTestSupport() {
    }

    /** Cria um jogo e remove TODAS as peças da posição inicial. */
    static Jogo jogoVazio() {
        Jogo jogo = new Jogo();
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                jogo.getTabuleiro().getCasa(x, y).removerPeca();
            }
        }
        return jogo;
    }

    static Pedra pedraBranca(Jogo jogo, int x, int y) {
        return new Pedra(jogo.getTabuleiro().getCasa(x, y), Cor.BRANCA);
    }

    static Pedra pedraVermelha(Jogo jogo, int x, int y) {
        return new Pedra(jogo.getTabuleiro().getCasa(x, y), Cor.VERMELHA);
    }

    static Dama damaBranca(Jogo jogo, int x, int y) {
        return new Dama(jogo.getTabuleiro().getCasa(x, y), Cor.BRANCA);
    }

    static Dama damaVermelha(Jogo jogo, int x, int y) {
        return new Dama(jogo.getTabuleiro().getCasa(x, y), Cor.VERMELHA);
    }

    static Casa casa(Jogo jogo, int x, int y) {
        return jogo.getTabuleiro().getCasa(x, y);
    }

    /** Conta as peças de uma cor e espécie (pedra ou dama) no tabuleiro. */
    static int contarPecas(Jogo jogo, Cor cor, boolean dama) {
        int total = 0;
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                Pedra peca = jogo.getTabuleiro().getCasa(x, y).getPeca();
                if (peca != null && peca.getCor() == cor && peca.isDama() == dama) {
                    total++;
                }
            }
        }
        return total;
    }

    /** Conta todas as peças do tabuleiro. */
    static int contarTodasAsPecas(Jogo jogo) {
        int total = 0;
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                if (jogo.getTabuleiro().getCasa(x, y).possuiPeca()) {
                    total++;
                }
            }
        }
        return total;
    }
}
