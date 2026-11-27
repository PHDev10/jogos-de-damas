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

    private Tabuleiro tabuleiro;
    private final Placar placar;
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
        placar = new Placar(new Jogador("player branco"), new Jogador("player vermelho"));

        vezAtual = Cor.BRANCA;
        jogadas = 0;
        jogadasSemComerPeca = 0;
        estado = new LanceLivre();

        colocarPecas(tabuleiro);
    }

    /**
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

    void jogarLanceLivre(Casa origem, Casa destino) {
        Peca peca = origem.getPeca();

        if (peca.getCor() != getVez())
            return;
        if (!peca.isMovimentoValido(destino))
            return;
        if (!simularMovimentoEValidar(origem, destino))
            return;

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

    private void registrarCaptura(Casa destino) {
        comerPecas();
        if (destino.getPeca().podeContinuarCapturando(tabuleiro, destino)) {
            estado = new CapturaEmSequencia(destino);
        } else {
            trocarDeVez();
        }
    }

    private void registrarLanceSemCaptura() {
        jogadasSemComerPeca++;
        trocarDeVez();
    }


    boolean haPecasACapturar() {
        return !pecasAComer.isEmpty();
    }

    void voltarAoLanceLivre() {
        estado = new LanceLivre();
    }

    /**
     * @param origem  {@code Casa} de origem
     * @param destino {@code Casa} de destino
     * @return {@code boolean} se a simulação ocorreu bem
     */
    boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        return origem.getPeca().simularMovimento(tabuleiro, origem, destino, pecasAComer);
    }

    private void comerPecas() {
        int pecasComidas = pecasAComer.size();

        getJogador(vezAtual).addPonto(pecasComidas);

        for (Casa casa : pecasAComer) {
            casa.removerPeca();
        }

        pecasAComer.clear();

        jogadasSemComerPeca = 0;
    }

    private boolean podeTransformarParaDama(Casa casa) {
        Peca peca = casa.getPeca();

        return peca.podeSerPromovida(casa);
    }

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
        return placar.getGanhador();
    }

    /**
     * @return o Tabuleiro em jogo.
     */
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void setJogadorUm(Jogador jogador) {
        placar.setJogadorUm(jogador);
    }

    public void setJogadorDois(Jogador jogador) {
        placar.setJogadorDois(jogador);
    }

    public Jogador getJogadorUm() {
        return placar.getJogadorUm();
    }

    public Jogador getJogadorDois() {
        return placar.getJogadorDois();
    }

    /**
     * @return o jogador um para {@code Cor.BRANCA} e o jogador dois para
     */
    public Jogador getJogador(Cor cor) {
        return placar.getJogador(cor);
    }

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
        retorno += "Informações do(a) jogador(a) " + getJogadorUm().getNome() + "\n";
        retorno += "Pontos: " + getJogadorUm().getPontos() + "\n";
        retorno += "Nº de peças restantes: " + placar.pecasRestantes(Cor.BRANCA) + "\n";
        retorno += "\n";
        retorno += "Informações do(a) jogador(a) " + getJogadorDois().getNome() + "\n";
        retorno += "Pontos: " + getJogadorDois().getPontos() + "\n";
        retorno += "Nº de peças restantes: " + placar.pecasRestantes(Cor.VERMELHA) + "\n";

        if (getCasaBloqueada() != null) {
            retorno += "\n";
            retorno += "Mova a peça na casa " + getCasaBloqueada().getX() + ":" + getCasaBloqueada().getY() + "!";
        }

        return retorno;
    }
}