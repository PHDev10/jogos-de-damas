package com.damas.objetos;

/**
 * Parte comum a todas as peças do jogo (Pedra e Dama): a casa onde a peça está
 * e a sua cor. As regras específicas de cada espécie ficam nas subclasses.
 */
public abstract class PecaBase implements Peca {

    protected Casa casa;
    private final Cor cor;

    /**
     * @param casa Objeto Casa
     * @param cor Cor da peça (BRANCA ou VERMELHA)
     */
    protected PecaBase(Casa casa, Cor cor) {
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
    public Cor getCor() {
        return cor;
    }
}
