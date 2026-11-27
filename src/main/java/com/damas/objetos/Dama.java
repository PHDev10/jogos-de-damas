package com.damas.objetos;

import java.util.Optional;

/**
 * Dama do jogo.
 * <p>Recebe uma casa e uma cor associada</p>
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public class Dama extends PecaBase {

    /**
     * @param casa Objeto Casa
     * @param cor Cor da dama (BRANCA ou VERMELHA)
     */
    public Dama(Casa casa, Cor cor) {
        super(casa, cor);
    }

    @Override
    public boolean isDama() {
        return true;
    }

    @Override
    public boolean podeSerPromovida(Casa casa) {
        return false;
    }

    /**
     * Movimento da Dama que pode andar várias casas na diagonal
     * @param destino
     * @return boolean. True se puder ser movida e false se não 
     */
   @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY()));

        return distanciaX == distanciaY;
    }

    @Override
    public boolean temCapturaNaDirecao(Tabuleiro tabuleiro, Casa origem, int deltaX, int deltaY) {

        int x = origem.getX();
        int y = origem.getY();
        int pecasSeguidasNoCaminho = 0;

        while (!foraDoTabuleiro(x, y)) {
            x += deltaX;
            y += deltaY;

            try {
                Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();

                if (!( pecaAtual == null)) {

                    pecasSeguidasNoCaminho += 1;

                    // VERIFICA SE HÁ ALGUMA PEÇA DO MESMO TIPO NO CAMINHO SE SIM, RETORNA FALSE;
                    if (getCor() == pecaAtual.getCor()) {
                        return false;
                    }
                } else {

                    if (pecasSeguidasNoCaminho == 1) {
                        return true;
                    }

                    if (pecasSeguidasNoCaminho == 2) {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    /**
     * @param x coluna
     * @param y linha
     * @return {@code true} se a posição está fora do tabuleiro
     */
    private boolean foraDoTabuleiro(int x, int y) {
        return x < 0 || x >= Tabuleiro.MAX_LINHAS || y < 0 || y >= Tabuleiro.MAX_COLUNAS;
    }

    @Override
    protected Optional<Boolean> regraEspecificaDoMovimento(Tabuleiro tabuleiro, Casa destino,
            int sentidoX, int sentidoY, int distanciaX, int distanciaY) {
        return Optional.empty();
    }
}
