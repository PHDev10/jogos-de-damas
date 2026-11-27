package com.damas.objetos;

/**
 * Cria as peças do jogo. A peça criada já é colocada na casa informada
 * (como fazem os construtores das peças).
 */
public interface FabricaDePecas {

    /**
     * @param casa {@code Casa} onde a pedra será colocada
     * @param cor cor da pedra
     * @return a pedra criada, já colocada na casa
     */
    Peca criarPedra(Casa casa, Cor cor);

    /**
     * @param casa {@code Casa} onde a dama será colocada (substitui a peça que estiver nela)
     * @param cor cor da dama
     * @return a dama criada, já colocada na casa
     */
    Peca criarDama(Casa casa, Cor cor);
}
