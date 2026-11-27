package com.damas.objetos;

import java.util.List;
import java.util.Optional;

/**
 * Parte comum a todas as peças do jogo (Pedra e Dama): a casa onde a peça está
 * e a sua cor. As regras específicas de cada espécie ficam nas subclasses.
 */
public abstract class PecaBase implements Peca {

    protected Casa casa;
    private final Cor cor;

    /**
     * @param casa Objeto Casa
     * @param cor Cor da peça (BRANCA ou VERMELHA)
     */
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
        int casasComPecaSeguidas = 0;

        if (destino.getPeca() != null) return false;

        // SENTIDO DO MOVIMENTO E DISTÂNCIA DO MOVIMENTO
        int sentidoX = (destino.getX() - origem.getX());
        int sentidoY = (destino.getY() - origem.getY());
        int distanciaX = Math.abs(sentidoX);
        int distanciaY = Math.abs(sentidoY);

        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        sentidoX = sentidoX/distanciaX;
        sentidoY = sentidoY/distanciaY;

        // REGRA ESPECÍFICA DE CADA ESPÉCIE DE PEÇA (SE JÁ DEFINE O RESULTADO, NÃO PERCORRE O CAMINHO)
        Optional<Boolean> resultadoAntecipado =
            regraEspecificaDoMovimento(tabuleiro, destino, sentidoX, sentidoY, distanciaX, distanciaY);
        if (resultadoAntecipado.isPresent()) return resultadoAntecipado.get();

        //PERCORRER AS CASAS E VERIFICAR:
        // 1 - SE HÁ MAIS DE UMA PEÇA SEGUIDA NO CAMINHO (VERDADEIRO RETORNA FALSO)
        // 2 - SE HÁ UMA PEÇA NO CAMINHO E É DA MESMA COR (VERDADEIRO RETORNA FALSO)
        int i = origem.getX();
        int j = origem.getY();

        while (!((i == destino.getX()) || (j == destino.getY()))) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = tabuleiro.getCasa(i, j);
            Peca pecaAlvo = alvo.getPeca();

            if (!(pecaAlvo == null)) {
                casasComPecaSeguidas += 1;

                // VE SE TEM UMA PECA DO MESMO TIPO NO CAMNHO, CASO TENHA, RETORNA FALSE
                if (getCor() == pecaAlvo.getCor()) {
                    if (pecasAComer.size() > 0) pecasAComer.clear();
                    return false;
                }

            } else {

                // VE SE HÁ PEÇA PARA COMER NO CAMINHO E PASSAR A CASA À COLEÇÃO pecasAComer() PARA DEPOIS COME-LAS
                if (casasComPecaSeguidas == 1) {
                    Casa casaCapturada = tabuleiro.getCasa((alvo.getX() - sentidoX), (alvo.getY() - sentidoY));
                    pecasAComer.add(casaCapturada);
                }
                casasComPecaSeguidas = 0;
            }

            if (casasComPecaSeguidas == 2) {
                if (pecasAComer.size() > 0) pecasAComer.clear();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean podeContinuarCapturando(Tabuleiro tabuleiro, Casa origem) {

        return temCapturaNaDirecao(tabuleiro, origem, tabuleiro.X_ESQUERDA, tabuleiro.Y_CIMA)
            || temCapturaNaDirecao(tabuleiro, origem, tabuleiro.X_DIREITA, tabuleiro.Y_CIMA)
            || temCapturaNaDirecao(tabuleiro, origem, tabuleiro.X_DIREITA, tabuleiro.Y_BAIXO)
            || temCapturaNaDirecao(tabuleiro, origem, tabuleiro.X_ESQUERDA, tabuleiro.Y_BAIXO);
    }

    /**
     * Gancho do {@code simularMovimento}: a regra de movimento própria de cada espécie, aplicada
     * antes de percorrer o caminho.
     * @return {@code Optional.empty()} para seguir e percorrer o caminho; ou o resultado
     *         final da simulação, quando a regra já o define
     */
    protected abstract Optional<Boolean> regraEspecificaDoMovimento(Tabuleiro tabuleiro, Casa destino,
            int sentidoX, int sentidoY, int distanciaX, int distanciaY);
}
