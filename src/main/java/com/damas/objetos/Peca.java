package com.damas.objetos;

/**
 * Interface com os métodos abstratos das peças
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public interface Peca {

    /**
     * Movimenta a peca para uma nova casa. Antes de trocar de casa a Peca armazena a casa no
     * campo ultimaCasa, isso servirá para verificar qual sentido em que a peça se moveu para
     * implementar a lógica de comer peças.
     * @param destino nova casa que ira conter esta peca.
     */
    public void mover(Casa destino);
    
    /**
     * Implementa a regra de movimento da peça
     * @param destino - tipo {@code Casa} destino da peça
     * @return {@code boolean}
     */
    public boolean isMovimentoValido(Casa destino);

    /**
     * Retorna a cor da peça
     * @return {@code Cor.BRANCA} ou {@code Cor.VERMELHA}
     */
    public Cor getCor();

    /**
     * Indica a espécie da peça
     * @return {@code true} se a peça é uma {@code Dama}, {@code false} se é uma {@code Pedra}
     */
    public boolean isDama();
}
