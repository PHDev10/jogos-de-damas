package com.damas.objetos;

/**
 * Estado do lance do {@code Jogo}: lance livre ou captura em sequência obrigatória.
 */
interface EstadoDoLance {

    /**
     * Trata um pedido de movimento de acordo com o estado atual.
     * 
     * @param jogo    {@code Jogo} em andamento
     * @param origem  {@code Casa} de origem
     * @param destino {@code Casa} de destino
     */
    void mover(Jogo jogo, Casa origem, Casa destino);

    /**
     * @return a {@code Casa} cuja peça deve continuar capturando, ou {@code null} se não há
     */
    Casa casaBloqueada();
}
