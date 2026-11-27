package com.damas.gui;

import java.awt.Color;
import javax.swing.JPanel;

import com.damas.objetos.Casa;
import com.damas.objetos.Jogo;
import com.damas.objetos.Peca;
import com.damas.objetos.Tabuleiro;

/**
 * Interface Grafica do Tabuleiro do jogo.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 */
public class TabuleiroGUI extends JPanel {

    private JanelaPrincipal janela;
    private CasaGUI[][] casas;
    private final Tabuleiro medidas = new Tabuleiro();

    public TabuleiroGUI(JanelaPrincipal janela) {
        this.janela = janela;
        initComponents();
        criarCasas();
    }

    /**
     * Preenche o tabuleiro com 64 casas
     */
    private void criarCasas() {
        casas = new CasaGUI[medidas.MAX_LINHAS][medidas.MAX_COLUNAS];
        // De cima para baixo
        for (int y = medidas.MAX_COLUNAS - 1; y >= 0; y--) {
            // Da esquerda para a direita
            for (int x = 0; x < medidas.MAX_LINHAS; x++) {
                Color cor = calcularCor(x, y);
                CasaGUI casa = new CasaGUI(x, y, cor, this);
                casas[x][y] = casa;
                add(casa);
            }
        }
    }

    private Color calcularCor(int x, int y) {
        return (x % 2 + y % 2) % 2 == 0 ? CasaGUI.COR_ESCURA : CasaGUI.COR_CLARA;
    }

    public void atualizar(Jogo jogo) {
        for (int x = 0; x < medidas.MAX_LINHAS; x++) {
            for (int y = 0; y < medidas.MAX_COLUNAS; y++) {
                CasaGUI casaGUI = casas[x][y];
                
                Tabuleiro tabuleiro = jogo.getTabuleiro();
                Casa casa = tabuleiro.getCasa(x, y);
                if (casa.possuiPeca()) {
                    Peca peca = casa.getPeca();

                    casaGUI.desenharPeca(peca.getCor(), peca.isDama());
                }
                else {
                    casaGUI.apagarPeca();
                }
            }
        }
    }

    public JanelaPrincipal getJanela() {
        return janela;
    }

    private void initComponents() {
        setLayout(new java.awt.GridLayout(medidas.MAX_LINHAS, medidas.MAX_COLUNAS));
    }
}
