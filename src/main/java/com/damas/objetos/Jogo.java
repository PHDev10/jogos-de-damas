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

    private final int PECAS_POR_JOGADOR = 12;
    private final int PONTOS_PARA_VENCER = PECAS_POR_JOGADOR;

    private Tabuleiro tabuleiro;
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private Cor vezAtual;
    private int jogadas;
    private int jogadasSemComerPeca;
    private ArrayList<Casa> pecasAComer;
    private EstadoDoLance estado;
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
        estado = new LanceLivre();

        colocarPecas(tabuleiro);
    }

    /**
     * Realiza uma serie de passos para comandar uma peça na posição
     * (origemX, origemY) fazer um movimento para (destinoX, destinoY).
     * 
     * @param origemX  - {@code int} linha da Casa de origem.
     * @param origemY  - {@code int} coluna da Casa de origem.
     * @param destinoX - {@code int} linha da Casa de destino.
     * @param destinoY - {@code int} coluna da Casa de destino.
     */
    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);

        estado.mover(this, origem, destino);
    }

    /**
     * Lance normal (sem captura obrigatória pendente): valida, move a peça, registra
     * captura ou lance simples, conta a jogada e promove a pedra se for o caso.
     * 
     * @param origem  {@code Casa} de origem
     * @param destino {@code Casa} de destino
     */
    void jogarLanceLivre(Casa origem, Casa destino) {
        Peca peca = origem.getPeca();

        if (peca.getCor() != getVez()) return;
        if (!peca.isMovimentoValido(destino)) return;
        if (!simularMovimentoEValidar(origem, destino)) return;

        peca.mover(destino);

        if (!pecasAComer.isEmpty()) {
            registrarCaptura(destino);
        } else {
            registrarLanceSemCaptura();
        }

        jogadas++;
        if (podeTransformarParaDama(destino))
            transformarPedraParaDama(destino);
    }

    /**
     * Come as peças capturadas e decide se o mesmo jogador continua (casa bloqueada)
     * ou se a vez troca. A peça em {@code destino} ainda é a pedra (a promoção vem depois).
     * 
     * @param destino {@code Casa} onde a peça terminou o lance
     */
    private void registrarCaptura(Casa destino) {
        comerPecas();
        if (destino.getPeca().podeContinuarCapturando(tabuleiro, destino)) {
            estado = new CapturaEmSequencia(destino);
        } else {
            trocarDeVez();
        }
    }

    /**
     * Lance sem captura: conta a jogada sem comer peça e troca a vez.
     */
    private void registrarLanceSemCaptura() {
        jogadasSemComerPeca++;
        trocarDeVez();
    }

    /**
     * @return {@code true} se a simulação do último movimento deixou peças a comer
     */
    boolean haPecasACapturar() {
        return !pecasAComer.isEmpty();
    }

    /**
     * Encerra a captura em sequência: volta ao estado de lance livre (sem limpar
     * {@code pecasAComer}).
     */
    void voltarAoLanceLivre() {
        estado = new LanceLivre();
    }

    /**
     * <p>
     * Percorre as casas da casa de origem clicada até a casa de destino clicada,
     * verifica se o caminho é valido e adiciona casas a variável
     * {@code pecasAComer}
     * </p>
     * 
     * @param origem  {@code Casa} de origem
     * @param destino {@code Casa} de destino
     * @return {@code boolean} se a simulação ocorreu bem
     */
    boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        return origem.getPeca().simularMovimento(tabuleiro, origem, destino, pecasAComer);
    }

    /**
     * Limpa as peças na variável {@code ArrayList pecasAComer}, adiciona pontos ao
     * jogador
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
     * 
     * @param casa {@code Casa}
     * @return {@code boolean}
     */
    private boolean podeTransformarParaDama(Casa casa) {
        Peca peca = casa.getPeca();

        return peca.podeSerPromovida(casa);
    }

    /**
     * Transforma a pedra da casa passada como parametro em dama
     * 
     * @param casa - tipo {@code Casa} contendo a peça a ser ser transformada.
     */
    private void transformarPedraParaDama(Casa casa) {
        Peca pedra = casa.getPeca();

        fabricaDePecas.criarDama(casa, pedra.getCor());
    }

    /**
     * Posiciona peças no tabuleiro.
     * Utilizado na inicialização do jogo.
     * 
     * @param tabuleiro - tipo {@code Tabuleiro} onde as peças serão posicionadas
     */
    public void colocarPecas(Tabuleiro tabuleiro) {
        new PosicionamentoInicial(fabricaDePecas).colocarPecas(tabuleiro);
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
     *         {@code Optional.empty()} - Nenhum jogador
     *         <li>{@code Optional.of(Cor.BRANCA)} - Jogador um</li>
     *         <li>{@code Optional.of(Cor.VERMELHA)} - Jogador dois</li>
     */
    public Optional<Cor> getGanhador() {
        if (jogadorUm.getPontos() == PONTOS_PARA_VENCER)
            return Optional.of(Cor.BRANCA);
        if (jogadorDois.getPontos() == PONTOS_PARA_VENCER)
            return Optional.of(Cor.VERMELHA);
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
     * @return o jogador um para {@code Cor.BRANCA} e o jogador dois para
     *         {@code Cor.VERMELHA}
     */
    public Jogador getJogador(Cor cor) {
        return cor == Cor.BRANCA ? jogadorUm : jogadorDois;
    }

    /**
     * @return
     *         {@code Cor.BRANCA} - jogador branco
     *         <li>{@code Cor.VERMELHA} - jogador vermelho</li>
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
        return estado.casaBloqueada();
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

        if (getCasaBloqueada() != null) {
            retorno += "\n";
            retorno += "Mova a peça na casa " + getCasaBloqueada().getX() + ":" + getCasaBloqueada().getY() + "!";
        }

        return retorno;
    }
}