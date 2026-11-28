package com.damas.objetos;

import java.util.List;

/**
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public interface Peca {

    /**
     * @param destino nova casa que ira conter esta peca.
     */
    public void mover(Casa destino);

    /**
     * @param destino - tipo {@code Casa} destino da peça
     * @return {@code boolean}
     */
    public boolean isMovimentoValido(Casa destino);

    public Cor getCor();

    public boolean isDama();

    public boolean podeSerPromovida(Casa casa);

    public boolean simularMovimento(Tabuleiro tabuleiro, Casa origem, Casa destino, List<Casa> pecasAComer);

    public boolean temCapturaNaDirecao(Tabuleiro tabuleiro, Casa origem, int deltaX, int deltaY);

    public boolean podeContinuarCapturando(Tabuleiro tabuleiro, Casa origem);
}
