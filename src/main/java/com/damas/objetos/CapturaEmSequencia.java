package com.damas.objetos;

/**
 * Estado com captura obrigatória pendente: só a peça da casa bloqueada pode jogar,
 * e só em lance de captura.
 */
class CapturaEmSequencia implements EstadoDoLance {

    private final Casa casa;

    /**
     * @param casa {@code Casa} da peça que deve continuar capturando
     */
    CapturaEmSequencia(Casa casa) {
        this.casa = casa;
    }

    @Override
    public void mover(Jogo jogo, Casa origem, Casa destino) {
        if (!origem.equals(casa)) return;
        if (!jogo.simularMovimentoEValidar(origem, destino)) return;
        if (!jogo.haPecasACapturar()) return;

        jogo.voltarAoLanceLivre();
        jogo.jogarLanceLivre(origem, destino);
    }

    @Override
    public Casa casaBloqueada() {
        return casa;
    }
}
