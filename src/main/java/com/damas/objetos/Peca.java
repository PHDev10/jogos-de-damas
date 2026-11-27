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

    /**
     * @return {@code Cor.BRANCA} ou {@code Cor.VERMELHA}
     */
    public Cor getCor();

    public boolean isDama();

    public boolean podeSerPromovida(Casa casa);

    /**
     * <p>
     * Percorre as casas da casa de origem até a casa de destino, verifica se o
     * caminho é valido
     * para esta peça e adiciona as casas das peças capturadas à lista
     * {@code pecasAComer}
     * </p>
     * 
     * @param tabuleiro   {@code Tabuleiro} em jogo
     * @param origem      {@code Casa} de origem (onde esta peça está)
     * @param destino     {@code Casa} de destino
     * @param pecasAComer lista que recebe (ou tem limpa) as casas das peças a serem
     *                    comidas
     * @return {@code boolean} se a simulação ocorreu bem
     */
    public boolean simularMovimento(Tabuleiro tabuleiro, Casa origem, Casa destino, List<Casa> pecasAComer);

    /**
     * <p>
     * Percorre as casas do tabuleiro a partir da casa de origem indicada no sentido
     * dado
     * por {@code deltaX} e {@code deltaY} até o limite do tabuleiro.
     * </p>
     * 
     * @param tabuleiro {@code Tabuleiro} em jogo
     * @param origem    Casa de origem da peça
     * @param deltaX    {@code Tabuleiro.X_ESQUERDA} ou {@code Tabuleiro.X_DIREITA}
     * @param deltaY    {@code Tabuleiro.Y_BAIXO} ou {@code Tabuleiro.Y_CIMA}
     * @return
     *         {@code false} - se não há peça para comer
     *         <li>{@code false} - se houver mais de uma peça no caminho</li>
     *         <li>{@code false} - se houver peça de mesma cor no caminho</li>
     *         <li>{@code true} - se há peça para comer</li>
     */
    public boolean temCapturaNaDirecao(Tabuleiro tabuleiro, Casa origem, int deltaX, int deltaY);

    /**
     * <p>
     * Dispara o método {@code temCapturaNaDirecao()} no sentido
     * das quatro diagonais a partir da casa indicada.
     * </p>
     * 
     * @param tabuleiro {@code Tabuleiro} em jogo
     * @param origem    tipo {@code Casa} de onde vai partir a verificação
     * @return {@code true} Se há peça para comer em alguma diagonal
     */
    public boolean podeContinuarCapturando(Tabuleiro tabuleiro, Casa origem);
}
