package com.damas.objetos;

class CapturaEmSequencia implements EstadoDoLance {

    private final Casa casa;

    CapturaEmSequencia(Casa casa) {
        this.casa = casa;
    }

    @Override
    public void mover(Jogo jogo, Casa origem, Casa destino) {
        if (!origem.equals(casa))
            return;
        if (!jogo.simularMovimentoEValidar(origem, destino))
            return;
        if (!jogo.haPecasACapturar())
            return;

        jogo.voltarAoLanceLivre();
        jogo.jogarLanceLivre(origem, destino);
    }

    @Override
    public Casa casaBloqueada() {
        return casa;
    }
}
