package com.damas.objetos;

import java.util.Optional;

/**
 * Representa uma Peça do jogo.
 * Possui uma casa e uma cor associada.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */
public class Pedra extends PecaBase {

    /**
     * @param casa Objeto Casa
     * @param cor Cor da pedra (BRANCA ou VERMELHA)
     */
    public Pedra(Casa casa, Cor cor) {
        super(casa, cor);
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {

        // SENTIDO UNITÁRIO E DISTANCIA X E Y DA CASA ATUAL ATÉ A CASA DE DESTINO
        int distanciaX = Math.abs(destino.getX() - casa.getX());
        int distanciaY = Math.abs(destino.getY() - casa.getY());

        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        // REGRA DE MOVIMENTO NO CASO DA DISTÂNCIA SER DE 2 CASAS (MOVIMENTO DE COMER PEÇA)
        return (distanciaX <= 2 || distanciaY <= 2) && (distanciaX == distanciaY);
    }

    @Override
    public boolean isDama() {
        return false;
    }

    @Override
    public boolean podeSerPromovida(Casa casa) {
        return casa.getY() == getCor().linhaPromocao();
    }

    @Override
    public boolean temCapturaNaDirecao(Tabuleiro tabuleiro, Casa origem, int deltaX, int deltaY) {

        int x = origem.getX();
        int y = origem.getY();

        x += deltaX;
        y += deltaY;

        try {

            Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();

            if (!( pecaAtual == null)) {

                if (tabuleiro.getCasa((x + deltaX), (y + deltaY)).getPeca() != null) {
                    return false;
                }

                // VERIFICA SE A PEÇA NO CAMINHO É DA MESMA COR
                if (getCor() == pecaAtual.getCor()) {
                    return false;
                }

                return true;
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    protected Optional<Boolean> regraEspecificaDoMovimento(Tabuleiro tabuleiro, Casa destino,
            int sentidoX, int sentidoY, int distanciaX, int distanciaY) {

        // REGRA DE MOVIMENTO DAS PEDRAS NO TABULEIRO CASO A DISTÂNCIA ATÉ A CASA CLICADA SEJA DE 2 BLOCOS
        if (distanciaX == 2 && distanciaY == 2) {

            Casa casaIntermediaria = tabuleiro.getCasa((destino.getX() - sentidoX), (destino.getY() - sentidoY));
            if (casaIntermediaria.getPeca() == null) return Optional.of(false);
            return Optional.empty();
        }

        // REGRA DE MOVIMENTO DAS PEDRAS NO TABULEIRO CASO A DISTÂNCIA ATÉ A CASA CLICADA SEJA DE 1 BLOCO
        if ((distanciaX == 1 || distanciaY == 1) && (distanciaX == distanciaY) && sentidoY == getCor().direcaoAvanco()) {
            return Optional.of(true);
        } else {
            return Optional.of(false);
        }
    }
}
