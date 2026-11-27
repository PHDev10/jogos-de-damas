package com.damas.gui;

import java.awt.Color;
import javax.swing.JPanel;

import com.damas.objetos.Casa;
import com.damas.objetos.Cor;
import com.damas.objetos.Jogo;
import com.damas.objetos.Pedra;
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

    public TabuleiroGUI(JanelaPrincipal janela) {
        this.janela = janela;
        initComponents();
        criarCasas();
    }

    /**
     * Preenche o tabuleiro com 64 casas
     */
    private void criarCasas() {
        casas = new CasaGUI[Tabuleiro.MAX_LINHAS][Tabuleiro.MAX_COLUNAS];
        // De cima para baixo
        for (int y = Tabuleiro.MAX_COLUNAS - 1; y >= 0; y--) {
            // Da esquerda para a direita
            for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
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
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                CasaGUI casaGUI = casas[x][y];
                
                Tabuleiro tabuleiro = jogo.getTabuleiro();
                Casa casa = tabuleiro.getCasa(x, y);
                if (casa.possuiPeca()) {
                    Pedra peca = casa.getPeca();

                    if (peca.getCor() == Cor.BRANCA) {
                        if (peca.isDama()) {
                            casaGUI.desenharDamaBranca();
                        } else {
                            casaGUI.desenharPedraBranca();
                        }
                    } else {
                        if (peca.isDama()) {
                            casaGUI.desenharDamaVermelha();
                        } else {
                            casaGUI.desenharPedraVermelha();
                        }
                    }
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
        setLayout(new java.awt.GridLayout(Tabuleiro.MAX_LINHAS, Tabuleiro.MAX_COLUNAS));
    }
}
