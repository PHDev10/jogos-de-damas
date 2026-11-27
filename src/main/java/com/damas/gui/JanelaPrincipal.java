package com.damas.gui;

import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.damas.objetos.Cor;
import com.damas.objetos.Jogo;

/**
 * Tela do jogo.
 * Responsável por reagir aos cliques feitos pelo jogador.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author Arthur Miranda Tavares {@link arthur.miranda@academico.ufpb.br}
 */

public class JanelaPrincipal extends JFrame {

    private static final int LIMITE_JOGADAS_SEM_COMER_PECA = 20;

    private Jogo jogo;
    private boolean primeiroClique;
    private CasaGUI casaClicadaOrigem;
    private CasaGUI casaClicadaDestino;

    public JanelaPrincipal() {

        initComponents();
        this.primeiroClique = true;
        this.casaClicadaOrigem = null;
        this.casaClicadaDestino = null;
        criarNovoJogo();

        // configura action listener para o menu novo
        menuNovo.addActionListener(e -> criarNovoJogo());

        // configura action listener para o menu status
        menuStatus.addActionListener(e -> exibirStatus());
        menuStatus.addActionListener(e -> exibirStatus());

        // configura action listener para o menu sair
        menuSair.addActionListener(e -> dispose());

        super.setLocationRelativeTo(null);
        super.setVisible(true);
        super.pack();
    }

    /**
     * Responde aos cliques realizados no tabuleiro.
     * 
     * @param casaClicada Casa que o jogador clicou.
     */
    public void reagir(CasaGUI casaClicada) {
        if (primeiroClique) {
            if (casaClicada.possuiPeca()) {
                casaClicadaOrigem = casaClicada;
                casaClicadaOrigem.destacar();
                primeiroClique = false;
            } else {
                JOptionPane.showMessageDialog(this, "Clique em uma peça.");
            }
        } else {
            casaClicadaDestino = casaClicada;
            jogo.moverPeca(casaClicadaOrigem.getPosicaoX(), casaClicadaOrigem.getPosicaoY(),
                    casaClicadaDestino.getPosicaoX(), casaClicadaDestino.getPosicaoY());
            casaClicadaOrigem.atenuar();
            primeiroClique = true;
            atualizar();
        }

        if (jogo.getJogadasSemComerPecas() == LIMITE_JOGADAS_SEM_COMER_PECA) {
            JOptionPane.showMessageDialog(this,
                    "FIM DE JOGO! \n" + LIMITE_JOGADAS_SEM_COMER_PECA + " Jogadas sem comer nenhuma peça!");
            criarNovoJogo();
        }

        Optional<Cor> ganhador = jogo.getGanhador();
        if (ganhador.isPresent()) {
            JOptionPane.showMessageDialog(this,
                    "FIM DE JOGO! \n" + jogo.getJogador(ganhador.get()).getNome() + " VENCEU!");
            criarNovoJogo();
        }
    }

    /**
     * Cria um novo jogo e atualiza o tabuleiro gráfico.
     */
    private void criarNovoJogo() {
        if (!primeiroClique) {
            primeiroClique = true;
            casaClicadaOrigem.atenuar();
        }
        jogo = new Jogo();
        atualizar();
    }

    private void exibirStatus() {
        JOptionPane.showMessageDialog(null, jogo.toString());
    }

    private void atualizar() {
        tabuleiroGUI.atualizar(jogo);
    }

    private void initComponents() {

        pnlLinhas = new javax.swing.JPanel();
        rotuloLinha7 = new javax.swing.JLabel();
        rotuloLinha6 = new javax.swing.JLabel();
        rotuloLinha5 = new javax.swing.JLabel();
        rotuloLinha4 = new javax.swing.JLabel();
        rotuloLinha3 = new javax.swing.JLabel();
        rotuloLinha2 = new javax.swing.JLabel();
        rotuloLinha1 = new javax.swing.JLabel();
        rotuloLinha0 = new javax.swing.JLabel();
        pnlColunas = new javax.swing.JPanel();
        rotuloColuna0 = new javax.swing.JLabel();
        rotuloColuna1 = new javax.swing.JLabel();
        rotuloColuna2 = new javax.swing.JLabel();
        rotuloColuna3 = new javax.swing.JLabel();
        rotuloColuna4 = new javax.swing.JLabel();
        rotuloColuna5 = new javax.swing.JLabel();
        rotuloColuna6 = new javax.swing.JLabel();
        rotuloColuna7 = new javax.swing.JLabel();
        tabuleiroGUI = new TabuleiroGUI(this);
        barraMenu = new javax.swing.JMenuBar();
        menuArquivo = new javax.swing.JMenu();
        menuNovo = new javax.swing.JMenuItem();
        separadorMenu1 = new javax.swing.JPopupMenu.Separator();
        menuStatus = new javax.swing.JMenuItem();
        separadorMenu2 = new javax.swing.JPopupMenu.Separator();
        menuSair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlLinhas.setLayout(new java.awt.GridLayout(8, 1));

        rotuloLinha7.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha7.setText("7");
        pnlLinhas.add(rotuloLinha7);

        rotuloLinha6.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha6.setText("6");
        pnlLinhas.add(rotuloLinha6);

        rotuloLinha5.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha5.setText("5");
        pnlLinhas.add(rotuloLinha5);

        rotuloLinha4.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha4.setText("4");
        pnlLinhas.add(rotuloLinha4);

        rotuloLinha3.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha3.setText("3");
        pnlLinhas.add(rotuloLinha3);

        rotuloLinha2.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha2.setText("2");
        pnlLinhas.add(rotuloLinha2);

        rotuloLinha1.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha1.setText("1");
        pnlLinhas.add(rotuloLinha1);

        rotuloLinha0.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
        rotuloLinha0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloLinha0.setText("0");
        pnlLinhas.add(rotuloLinha0);

        pnlColunas.setLayout(new java.awt.GridLayout(1, 8));

        rotuloColuna0.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna0.setText("0");
        pnlColunas.add(rotuloColuna0);

        rotuloColuna1.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna1.setText("1");
        pnlColunas.add(rotuloColuna1);

        rotuloColuna2.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna2.setText("2");
        pnlColunas.add(rotuloColuna2);

        rotuloColuna3.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna3.setText("3");
        pnlColunas.add(rotuloColuna3);

        rotuloColuna4.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna4.setText("4");
        pnlColunas.add(rotuloColuna4);

        rotuloColuna5.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna5.setText("5");
        pnlColunas.add(rotuloColuna5);

        rotuloColuna6.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna6.setText("6");
        pnlColunas.add(rotuloColuna6);

        rotuloColuna7.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
        rotuloColuna7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloColuna7.setText("7");
        pnlColunas.add(rotuloColuna7);

        menuArquivo.setText("Jogo");

        menuNovo.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuNovo.setText("Novo");
        menuArquivo.add(menuNovo);
        menuArquivo.add(separadorMenu1);

        menuStatus.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menuStatus.setText("Status");
        menuArquivo.add(menuStatus);
        menuArquivo.add(separadorMenu2);

        menuSair.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuSair.setText("Sair");
        menuArquivo.add(menuSair);

        barraMenu.add(menuArquivo);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlLinhas, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlColunas, javax.swing.GroupLayout.DEFAULT_SIZE, 576,
                                                Short.MAX_VALUE)
                                        .addComponent(tabuleiroGUI, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlLinhas, javax.swing.GroupLayout.PREFERRED_SIZE, 576,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tabuleiroGUI, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlColunas, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

        pack();
    }

    private javax.swing.JLabel rotuloLinha0;
    private javax.swing.JLabel rotuloLinha1;
    private javax.swing.JLabel rotuloLinha7;
    private javax.swing.JLabel rotuloLinha6;
    private javax.swing.JLabel rotuloLinha5;
    private javax.swing.JLabel rotuloLinha4;
    private javax.swing.JLabel rotuloLinha3;
    private javax.swing.JLabel rotuloLinha2;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JPopupMenu.Separator separadorMenu1;
    private javax.swing.JPopupMenu.Separator separadorMenu2;
    private javax.swing.JLabel rotuloColuna0;
    private javax.swing.JLabel rotuloColuna1;
    private javax.swing.JLabel rotuloColuna2;
    private javax.swing.JLabel rotuloColuna3;
    private javax.swing.JLabel rotuloColuna4;
    private javax.swing.JLabel rotuloColuna5;
    private javax.swing.JLabel rotuloColuna6;
    private javax.swing.JLabel rotuloColuna7;
    private javax.swing.JMenu menuArquivo;
    private javax.swing.JMenuItem menuNovo;
    private javax.swing.JMenuItem menuSair;
    private javax.swing.JMenuItem menuStatus;
    private javax.swing.JPanel pnlColunas;
    private javax.swing.JPanel pnlLinhas;
    private TabuleiroGUI tabuleiroGUI;
}
