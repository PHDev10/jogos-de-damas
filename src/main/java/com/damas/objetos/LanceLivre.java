package com.damas.objetos;

/**
 * Estado sem captura pendente: qualquer peça da vez pode jogar.
 */
class LanceLivre implements EstadoDoLance {

    @Override
    public void mover(Jogo jogo, Casa origem, Casa destino) {
        jogo.jogarLanceLivre(origem, destino);
    }

    @Override
    public Casa casaBloqueada() {
        return null;
    }
}
