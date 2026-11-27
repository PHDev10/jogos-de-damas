package com.damas.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;

import org.junit.jupiter.api.Test;

import com.damas.objetos.Cor;
import com.damas.objetos.Jogo;
import com.damas.objetos.Pedra;
import com.damas.objetos.Tabuleiro;

/** Usa só componentes leves (sem JFrame), então roda sem exibir janelas. */
class DesenhoDasPecasTest {

    private static CasaGUI casaGUI(TabuleiroGUI gui, int x, int y) {
        for (Component c : gui.getComponents()) {
            CasaGUI casa = (CasaGUI) c;
            if (casa.getPosicaoX() == x && casa.getPosicaoY() == y) {
                return casa;
            }
        }
        throw new AssertionError("casa não encontrada");
    }

    private static Icon icone(Cor cor, boolean dama) {
        CasaGUI casa = new CasaGUI(0, 0, Color.WHITE, null);
        casa.desenharPeca(cor, dama);
        return casa.getIcon();
    }

    @Test
    void asQuatroCombinacoesDeCorEEspecieTemIconesDiferentes() {
        Set<Icon> icones = new HashSet<>();
        for (Cor cor : Cor.values()) {
            icones.add(icone(cor, false));
            icones.add(icone(cor, true));
        }

        assertEquals(4, icones.size());
        assertNotNull(icone(Cor.BRANCA, false));
    }

    @Test
    void chamadasRepetidasDevolvemOMesmoIcone() {
        for (Cor cor : Cor.values()) {
            assertSame(icone(cor, false), icone(cor, false));
            assertSame(icone(cor, true), icone(cor, true));
        }
    }

    @Test
    void apagarPecaZeraOIcone() {
        CasaGUI casa = new CasaGUI(0, 0, Color.WHITE, null);
        casa.desenharPeca(Cor.VERMELHA, true);

        casa.apagarPeca();

        assertNull(casa.getIcon());
    }

    @Test
    void atualizarComJogoNovoDesenha12PedrasBrancas12VermelhasE40CasasVazias() {
        TabuleiroGUI gui = new TabuleiroGUI(null);

        gui.atualizar(new Jogo());

        int brancas = 0;
        int vermelhas = 0;
        int vazias = 0;
        for (Component c : gui.getComponents()) {
            Icon icone = ((CasaGUI) c).getIcon();
            if (icone == null) vazias++;
            else if (icone == icone(Cor.BRANCA, false)) brancas++;
            else if (icone == icone(Cor.VERMELHA, false)) vermelhas++;
        }
        assertEquals(12, brancas);
        assertEquals(12, vermelhas);
        assertEquals(40, vazias);
    }

    @Test
    void depoisDaPromocaoACasaDaDamaUsaOIconeDeDamaDaCorCorreta() {
        Jogo jogo = new Jogo();
        for (int x = 0; x < jogo.getTabuleiro().MAX_LINHAS; x++) {
            for (int y = 0; y < jogo.getTabuleiro().MAX_COLUNAS; y++) {
                jogo.getTabuleiro().getCasa(x, y).removerPeca();
            }
        }
        new Pedra(jogo.getTabuleiro().getCasa(1, 6), Cor.BRANCA);
        new Pedra(jogo.getTabuleiro().getCasa(7, 7), Cor.VERMELHA);
        jogo.moverPeca(1, 6, 2, 7);
        TabuleiroGUI gui = new TabuleiroGUI(null);

        gui.atualizar(jogo);

        assertSame(icone(Cor.BRANCA, true), casaGUI(gui, 2, 7).getIcon());
        assertSame(icone(Cor.VERMELHA, false), casaGUI(gui, 7, 7).getIcon());
        assertNotSame(icone(Cor.BRANCA, false), casaGUI(gui, 2, 7).getIcon());
        assertNull(casaGUI(gui, 1, 6).getIcon());
    }
}
