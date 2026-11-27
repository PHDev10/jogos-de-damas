package com.damas.objetos;

/**
 * Representa uma Peça do jogo.
 * Possui uma casa e uma cor associada.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */
public class Pedra implements Peca {

    protected Casa casa;
    private final Cor cor;

    /**
     * @param casa Objeto Casa
     * @param cor Cor da pedra (BRANCA ou VERMELHA)
     */
    public Pedra(Casa casa, Cor cor) {
        this.casa = casa;
        this.cor = cor;
        casa.colocarPeca(this);
    }
    
    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {

        // SENTIDO UNITÁRIO E DISTANCIA X E Y DA CASA ATUAL ATÉ A CASA DE DESTINO
        int distanciaX = Math.abs(destino.getX() - casa.getX());
        int distanciaY = Math.abs(destino.getY() - casa.getY());

        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        // REGRA DE MOVIMENTO NO CASO DA DISTÂNCIA SER DE 2 CASAS (MOVIMENTO DE COMER PEÇA)
        return (distanciaX <= 2 || distanciaY <= 2) && (distanciaX == distanciaY);
    }

    @Override
    public Cor getCor() {
        return cor;
    }

    @Override
    public boolean isDama() {
        return false;
    }
}
