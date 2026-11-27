package com.damas.objetos;

/**
 * Fábrica padrão: cria {@code Pedra} e {@code Dama}.
 */
public class FabricaDePecasPadrao implements FabricaDePecas {

    @Override
    public Peca criarPedra(Casa casa, Cor cor) {
        return new Pedra(casa, cor);
    }

    @Override
    public Peca criarDama(Casa casa, Cor cor) {
        return new Dama(casa, cor);
    }
}
