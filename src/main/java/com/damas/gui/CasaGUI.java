package com.damas.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.net.URL;

import com.damas.objetos.Cor;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Interface Grafica de uma Casa no tabuleiro do jogo.
 *
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public class CasaGUI extends JButton {

    // Constantes 
    public static final Color COR_CLARA = new Color(255, 255, 250);
    public static final Color COR_ESCURA = new Color(87, 168, 124);
    private static final Color COR_DESTAQUE = new Color(0, 1, 0, 0.4f);

    // Icones das pecas
    private static final URL PEDRA_BRANCA_URL = CasaGUI.class.getResource("/resources/pedra_branca.png"); 
    private static final URL DAMA_BRANCA_URL = CasaGUI.class.getResource("/resources/dama_branca.png"); 
    private static final URL PEDRA_VERMELHA_URL = CasaGUI.class.getResource("/resources/pedra_vermelha.png"); 
    private static final URL DAMA_VERMELHA_URL = CasaGUI.class.getResource("/resources/dama_vermelha.png"); 

    private static final Icon PEDRA_BRANCA = new ImageIcon(PEDRA_BRANCA_URL);
    private static final Icon DAMA_BRANCA = new ImageIcon(DAMA_BRANCA_URL);
    private static final Icon PEDRA_VERMELHA = new ImageIcon(PEDRA_VERMELHA_URL);
    private static final Icon DAMA_VERMELHA = new ImageIcon(DAMA_VERMELHA_URL);

    // Tabela de ícones: [cor.ordinal()][dama ? 1 : 0]
    private static final Icon[][] ICONES = new Icon[Cor.values().length][2];
    static {
        ICONES[Cor.BRANCA.ordinal()][0] = PEDRA_BRANCA;
        ICONES[Cor.BRANCA.ordinal()][1] = DAMA_BRANCA;
        ICONES[Cor.VERMELHA.ordinal()][0] = PEDRA_VERMELHA;
        ICONES[Cor.VERMELHA.ordinal()][1] = DAMA_VERMELHA;
    }

    private int x;
    private int y;
    private Color cor;

    public CasaGUI(int x, int y, Color cor, TabuleiroGUI tabuleiro) {
        this.x = x;
        this.y = y;
        this.cor = cor;
        setIcon(null);

        // Layout e cor
        setBackground(cor);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(cor, 1));
        setContentAreaFilled(false);

        addActionListener(e -> tabuleiro.getJanela().reagir((CasaGUI) e.getSource()));
    }

    public int getPosicaoX() {
        return this.x;
    }

    public int getPosicaoY() {
        return this.y;
    }

    public void desenharPeca(Cor cor, boolean dama) {
        setIcon(ICONES[cor.ordinal()][dama ? 1 : 0]);
    }

    public void apagarPeca() {
        setIcon(null);
    }

    public boolean possuiPeca() {
        return getIcon() != null;
    }
    
    public void destacar() {
        setBackground(COR_DESTAQUE);
    }

    public void atenuar() {
        setBackground(cor);
    }

    /**
     * Pinta o componente com a cor de fundo, aceita valores RGBA
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
