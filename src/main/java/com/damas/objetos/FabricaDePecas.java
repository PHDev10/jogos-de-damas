package com.damas.objetos;

public interface FabricaDePecas {
    Peca criarPedra(Casa casa, Cor cor);

    Peca criarDama(Casa casa, Cor cor);
}
