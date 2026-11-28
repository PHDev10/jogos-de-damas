package com.damas.objetos;

import java.util.List;
import java.util.Optional;

public abstract class PecaBase implements Peca {

    protected Casa casa;
    private final Cor cor;

    protected PecaBase(Casa casa, Cor cor) {
        this.casa = casa;
        this.cor = cor;
        casa.colocarPeca(this);
    }

    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    @Override
    public Cor getCor() {
        return cor;
    }

    @Override
    public boolean simularMovimento(Tabuleiro tabuleiro, Casa origem, Casa destino, List<Casa> pecasAComer) {

        if (destino.getPeca() != null)
            return false;

        int sentidoX = (destino.getX() - origem.getX());
        int sentidoY = (destino.getY() - origem.getY());
        int distanciaX = Math.abs(sentidoX);
        int distanciaY = Math.abs(sentidoY);

        if ((distanciaX == 0) || (distanciaY == 0))
            return false;

        sentidoX = sentidoX / distanciaX;
        sentidoY = sentidoY / distanciaY;

        Optional<Boolean> resultadoAntecipado = regraEspecificaDoMovimento(tabuleiro, destino, sentidoX, sentidoY,
                distanciaX, distanciaY);
        if (resultadoAntecipado.isPresent())
            return resultadoAntecipado.get();

        return percorrerCaminho(tabuleiro, origem, destino, sentidoX, sentidoY, pecasAComer);
    }

    private boolean percorrerCaminho(Tabuleiro tabuleiro, Casa origem, Casa destino,
            int sentidoX, int sentidoY, List<Casa> pecasAComer) {
        int casasComPecaSeguidas = 0;
        int i = origem.getX();
        int j = origem.getY();

        while (i != destino.getX() && j != destino.getY()) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = tabuleiro.getCasa(i, j);
            Peca pecaAlvo = alvo.getPeca();

            if (pecaAlvo != null) {
                casasComPecaSeguidas += 1;

                if (getCor() == pecaAlvo.getCor()) {
                    pecasAComer.clear();
                    return false;
                }

            } else {

                if (casasComPecaSeguidas == 1) {
                    Casa casaCapturada = tabuleiro.getCasa((alvo.getX() - sentidoX), (alvo.getY() - sentidoY));
                    pecasAComer.add(casaCapturada);
                }
                casasComPecaSeguidas = 0;
            }

            if (casasComPecaSeguidas == 2) {
                pecasAComer.clear();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean podeContinuarCapturando(Tabuleiro tabuleiro, Casa origem) {

        return temCapturaNaDirecao(tabuleiro, origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_CIMA)
                || temCapturaNaDirecao(tabuleiro, origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA)
                || temCapturaNaDirecao(tabuleiro, origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_BAIXO)
                || temCapturaNaDirecao(tabuleiro, origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_BAIXO);
    }

    protected abstract Optional<Boolean> regraEspecificaDoMovimento(Tabuleiro tabuleiro, Casa destino,
            int sentidoX, int sentidoY, int distanciaX, int distanciaY);
}
