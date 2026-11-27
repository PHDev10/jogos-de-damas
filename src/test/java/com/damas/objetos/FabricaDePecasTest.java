package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FabricaDePecasTest {

    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final FabricaDePecas fabrica = new FabricaDePecasPadrao();

    /** Fábrica de teste: delega à padrão e registra as chamadas. */
    private static class FabricaRegistradora implements FabricaDePecas {
        private final FabricaDePecas padrao = new FabricaDePecasPadrao();
        final List<Cor> pedras = new ArrayList<>();
        final List<Cor> damas = new ArrayList<>();

        @Override
        public Peca criarPedra(Casa casa, Cor cor) {
            pedras.add(cor);
            return padrao.criarPedra(casa, cor);
        }

        @Override
        public Peca criarDama(Casa casa, Cor cor) {
            damas.add(cor);
            return padrao.criarDama(casa, cor);
        }
    }

    @ParameterizedTest(name = "criarPedra {0}")
    @EnumSource(Cor.class)
    void criarPedraDevolvePedraDaCorPedidaEColocaNaCasa(Cor cor) {
        Casa casa = tabuleiro.getCasa(2, 2);

        Peca peca = fabrica.criarPedra(casa, cor);

        assertInstanceOf(Pedra.class, peca);
        assertEquals(cor, peca.getCor());
        assertFalse(peca.isDama());
        assertSame(peca, casa.getPeca());
    }

    @ParameterizedTest(name = "criarDama {0}")
    @EnumSource(Cor.class)
    void criarDamaDevolveDamaDaCorPedidaEColocaNaCasa(Cor cor) {
        Casa casa = tabuleiro.getCasa(3, 3);

        Peca peca = fabrica.criarDama(casa, cor);

        assertInstanceOf(Dama.class, peca);
        assertEquals(cor, peca.getCor());
        assertTrue(peca.isDama());
        assertSame(peca, casa.getPeca());
    }

    @Test
    void criarDamaSobreCasaOcupadaPorPedraSubstituiAPeca() {
        Casa casa = tabuleiro.getCasa(4, 4);
        Peca pedra = fabrica.criarPedra(casa, Cor.BRANCA);

        Peca dama = fabrica.criarDama(casa, Cor.BRANCA);

        assertSame(dama, casa.getPeca());
        assertNotSame(pedra, casa.getPeca());
        assertTrue(casa.getPeca().isDama());
    }

    @Test
    void jogoComFabricaInjetadaCriaAs24PedrasIniciaisESemDamas() {
        FabricaRegistradora registro = new FabricaRegistradora();

        new Jogo(registro);

        assertEquals(12, registro.pedras.stream().filter(c -> c == Cor.BRANCA).count());
        assertEquals(12, registro.pedras.stream().filter(c -> c == Cor.VERMELHA).count());
        assertEquals(24, registro.pedras.size());
        assertTrue(registro.damas.isEmpty());
    }

    @Test
    void promocaoPedeExatamenteUmaDamaDaCorDaPedraPromovida() {
        FabricaRegistradora registro = new FabricaRegistradora();
        Jogo jogo = new Jogo(registro);
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                jogo.getTabuleiro().getCasa(x, y).removerPeca();
            }
        }
        new Pedra(jogo.getTabuleiro().getCasa(1, 6), Cor.BRANCA);
        new Pedra(jogo.getTabuleiro().getCasa(7, 7), Cor.VERMELHA);

        jogo.moverPeca(1, 6, 2, 7);

        assertEquals(List.of(Cor.BRANCA), registro.damas);
        assertTrue(jogo.getTabuleiro().getCasa(2, 7).getPeca().isDama());
    }

    @Test
    void jogoSemArgumentosMontaOMesmoTabuleiroInicialQueOJogoComFabricaInjetada() {
        Jogo padrao = new Jogo();
        Jogo injetado = new Jogo(new FabricaRegistradora());

        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                Peca a = padrao.getTabuleiro().getCasa(x, y).getPeca();
                Peca b = injetado.getTabuleiro().getCasa(x, y).getPeca();
                assertEquals(a == null, b == null);
                if (a != null) {
                    assertEquals(a.getCor(), b.getCor());
                    assertEquals(a.isDama(), b.isDama());
                }
            }
        }
    }
}
