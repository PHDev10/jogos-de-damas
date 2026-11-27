package com.damas.objetos;

interface EstadoDoLance {
    void mover(Jogo jogo, Casa origem, Casa destino);

    Casa casaBloqueada();
}
