package com.damas.objetos;

/**
 * Interface com os métodos abstratos das peças
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public interface Peca {

    /**
     * Movimenta a peca para uma nova casa: remove a peça da casa atual, coloca-a na casa de
     * destino e passa a considerar o destino como a sua casa atual. Não valida o movimento.
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

    /**
     * Indica se a peça, estando na casa informada, deve ser promovida a dama
     * @param casa {@code Casa} onde a peça está
     * @return {@code true} se a peça pode ser promovida nesta casa, {@code false} caso contrário
     */
    public boolean podeSerPromovida(Casa casa);
}
