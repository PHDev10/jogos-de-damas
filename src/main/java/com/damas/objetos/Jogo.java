package com.damas.objetos;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Armazena o tabuleiro e responsavel por posicionar as pecas.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */
public class Jogo {

    private static final int PECAS_POR_JOGADOR = 12;
    private static final int PONTOS_PARA_VENCER = PECAS_POR_JOGADOR;

    private static final int PRIMEIRA_LINHA_BRANCAS = 0;
    private static final int ULTIMA_LINHA_BRANCAS = 2;
    private static final int PRIMEIRA_LINHA_VERMELHAS = 5;
    private static final int ULTIMA_LINHA_VERMELHAS = 7;

    private Tabuleiro tabuleiro;
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private Cor vezAtual = Cor.BRANCA;
    private int jogadas = 0;
    private int jogadasSemComerPeca = 0;
    private ArrayList<Casa> pecasAComer;
    private Casa casaBloqueadaOrigem;
    private final FabricaDePecas fabricaDePecas;

    public Jogo() {
        this(new FabricaDePecasPadrao());
    }

    public Jogo(FabricaDePecas fabricaDePecas) {
        this.fabricaDePecas = fabricaDePecas;
        tabuleiro = new Tabuleiro();
        pecasAComer = new ArrayList<Casa>();
        jogadorUm = new Jogador("player branco");
        jogadorDois = new Jogador("player vermelho");
        
        vezAtual = Cor.BRANCA;
        jogadas = 0;
        jogadasSemComerPeca = 0;
        casaBloqueadaOrigem = null;

        colocarPecas(tabuleiro);
    }
    
    /**
     * Realiza uma serie de passos para comandar uma peça na posição 
     * (origemX, origemY) fazer um movimento para (destinoX, destinoY).
     * 
     * @param origemX - {@code int} linha da Casa de origem.
     * @param origemY - {@code int} coluna da Casa de origem.
     * @param destinoX - {@code int} linha da Casa de destino.
     * @param destinoY - {@code int} coluna da Casa de destino.
     */
    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);
        Peca peca = origem.getPeca();

        if (casaBloqueadaOrigem == null) {
            if (peca.getCor() == getVez()) {

                if (peca.isMovimentoValido(destino)) {

                    if (simularMovimentoEValidar(origem, destino)) {                    

                        peca.mover(destino);

                        if (pecasAComer.size() > 0) {
                            comerPecas();
                            if (destino.getPeca().podeContinuarCapturando(tabuleiro, destino)) {
                                casaBloqueadaOrigem = destino;
                            } else {
                                trocarDeVez();
                            }
                        } else {
                            jogadasSemComerPeca++;
                            trocarDeVez();
                        }

                        jogadas++;
                        if (podeTransformarParaDama(destino)) transformarPedraParaDama(destino);
                    }
                }
            }
        } else {
            if ((origem.equals(casaBloqueadaOrigem))) {
                if(simularMovimentoEValidar(origem, destino)) {
                    if (pecasAComer.size() != 0) {
                        casaBloqueadaOrigem = null;
                        moverPeca(origemX, origemY, destinoX, destinoY);
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Percorre as casas da casa de origem clicada até a casa de destino clicada,
     * verifica se o caminho é valido e adiciona casas a variável {@code pecasAComer} 
     * </p> 
     * 
     * @param origem {@code Casa} de origem
     * @param destino {@code Casa} de destino
     * @return {@code boolean} se a simulação ocorreu bem
     */
    private boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        return origem.getPeca().simularMovimento(tabuleiro, origem, destino, pecasAComer);
    }

    /**
     * Limpa as peças na variável {@code ArrayList pecasAComer}, adiciona pontos ao jogador
     */
    private void comerPecas() {
        int pecasComidas = pecasAComer.size();

        getJogador(vezAtual).addPonto(pecasComidas);

        for (Casa casa : pecasAComer) {
            casa.removerPeca();
        }

        pecasAComer.clear();

        jogadasSemComerPeca = 0;
    }

    /**
     * Verifica se a pedra da casa pode virar dama.
     * @param casa {@code Casa}
     * @return {@code boolean}
     */
    private boolean podeTransformarParaDama(Casa casa) {
        Peca peca = casa.getPeca();

        return peca.podeSerPromovida(casa);
    }

    /**
     * Transforma a pedra da casa passada como parametro em dama
     * @param casa - tipo {@code Casa} contendo a peça a ser ser transformada.
     */
    private void transformarPedraParaDama(Casa casa) {
        Peca pedra = casa.getPeca();

        fabricaDePecas.criarDama(casa, pedra.getCor());
    }

    /**
     * Posiciona peças no tabuleiro.
     * Utilizado na inicialização do jogo.
     * @param tabuleiro - tipo {@code Tabuleiro} onde as peças serão posicionadas
     */
    public void colocarPecas(Tabuleiro tabuleiro) {

        // CRIA E PÕE AS PEÇAS NA PARTE INFERIOR DO TABULEIRO
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = PRIMEIRA_LINHA_BRANCAS; y <= ULTIMA_LINHA_BRANCAS; y++) {
                if((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    fabricaDePecas.criarPedra(casa, Cor.BRANCA);
                }
                
                else if ((x % 2 != 0) && (y % 2 != 0)){
                    Casa casa = tabuleiro.getCasa(x, y);
                    fabricaDePecas.criarPedra(casa, Cor.BRANCA);
                }
            }

        }
        // CRIA E POE AS PEÇAS NA PARTE SUPERIOR DO TABULEIRO
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = PRIMEIRA_LINHA_VERMELHAS; y <= ULTIMA_LINHA_VERMELHAS; y++) {
                if ((x % 2 != 0) && (y % 2 != 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    fabricaDePecas.criarPedra(casa, Cor.VERMELHA);
                }
                else if ((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    fabricaDePecas.criarPedra(casa, Cor.VERMELHA);
                }
            }
        }
    }

    /**
     * Troca a vez do jogador que pode mover no tabuleiro
     */
    public void trocarDeVez() {
        vezAtual = vezAtual.oposta();
    }

    /**
     * 
     * @return
     * {@code Optional.empty()} - Nenhum jogador
     * <li> {@code Optional.of(Cor.BRANCA)} - Jogador um </li>
     * <li> {@code Optional.of(Cor.VERMELHA)} - Jogador dois </li>
     */
    public Optional<Cor> getGanhador() {
        if (jogadorUm.getPontos() == PONTOS_PARA_VENCER) return Optional.of(Cor.BRANCA);
        if (jogadorDois.getPontos() == PONTOS_PARA_VENCER) return Optional.of(Cor.VERMELHA);
        return Optional.empty();
    }

    /**
     * @return o Tabuleiro em jogo.
     */
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
    
    public void setJogadorUm(Jogador jogador) {
        jogadorUm = jogador;
    }

    public void setJogadorDois(Jogador jogador) {
        jogadorDois = jogador;
    }

    public Jogador getJogadorUm() {
        return jogadorUm;
    }

    public Jogador getJogadorDois() {
        return jogadorDois;
    }

    /**
     * @param cor cor do jogador
     * @return o jogador um para {@code Cor.BRANCA} e o jogador dois para {@code Cor.VERMELHA}
     */
    public Jogador getJogador(Cor cor) {
        return cor == Cor.BRANCA ? jogadorUm : jogadorDois;
    }

    /**
     * @return
     *      {@code Cor.BRANCA} - jogador branco
     *  <li>{@code Cor.VERMELHA} - jogador vermelho </li>
     */
    public Cor getVez() {
        return vezAtual;
    }

    public int getJogadasSemComerPecas() {
        return jogadasSemComerPeca;
    }

    public int getJogada() {
        return jogadas;
    }

    public Casa getCasaBloqueada() {
        return casaBloqueadaOrigem;
    }

    @Override
    public String toString() {

        String retorno = "Vez: ";
        retorno += getJogador(getVez()).getNome();
        retorno += "\n";

        retorno += "Nº de jogadas: " + getJogada() + "\n";
        retorno += "Jogadas sem comer peça: " + getJogadasSemComerPecas() + "\n";
        retorno += "\n";
        retorno += "Informações do(a) jogador(a) " + jogadorUm.getNome() + "\n";
        retorno += "Pontos: " + jogadorUm.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (PECAS_POR_JOGADOR - jogadorDois.getPontos()) + "\n";
        retorno += "\n";        
        retorno += "Informações do(a) jogador(a) " + jogadorDois.getNome() + "\n";
        retorno += "Pontos: " + jogadorDois.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (PECAS_POR_JOGADOR - jogadorUm.getPontos()) + "\n";

        if (casaBloqueadaOrigem != null) {
            retorno += "\n";
            retorno += "Mova a peça na casa " + casaBloqueadaOrigem.getX() + ":" + casaBloqueadaOrigem.getY() + "!";
        }

        return retorno;
    }
}