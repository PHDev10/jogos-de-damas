package com.damas.objetos;

/**
 * Dama do jogo.
 * <p>Recebe uma casa e uma cor associada</p>
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public class Dama extends Pedra{

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
}
