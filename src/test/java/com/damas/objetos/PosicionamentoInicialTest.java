package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Caracteriza a ordem e as posições das 24 pedras criadas pelo jogo novo. */
class PosicionamentoInicialTest {

    /** Ordem observada: x externo, y interno; brancas antes das vermelhas. */
    private static final List<String> ESPERADO = List.of(
            "0,0,BRANCA",
            "0,2,BRANCA",
            "1,1,BRANCA",
            "2,0,BRANCA",
            "2,2,BRANCA",
            "3,1,BRANCA",
            "4,0,BRANCA",
            "4,2,BRANCA",
            "5,1,BRANCA",
            "6,0,BRANCA",
            "6,2,BRANCA",
            "7,1,BRANCA",
            "0,6,VERMELHA",
            "1,5,VERMELHA",
            "1,7,VERMELHA",
            "2,6,VERMELHA",
            "3,5,VERMELHA",
            "3,7,VERMELHA",
            "4,6,VERMELHA",
            "5,5,VERMELHA",
            "5,7,VERMELHA",
            "6,6,VERMELHA",
            "7,5,VERMELHA",
            "7,7,VERMELHA");

    private static class FabricaRegistradora implements FabricaDePecas {
        private final FabricaDePecas padrao = new FabricaDePecasPadrao();
        final List<String> pedras = new ArrayList<>();
        int damas = 0;

        @Override
        public Peca criarPedra(Casa casa, Cor cor) {
            pedras.add(casa.getX() + "," + casa.getY() + "," + cor);
            return padrao.criarPedra(casa, cor);
        }

        @Override
        public Peca criarDama(Casa casa, Cor cor) {
            damas++;
            return padrao.criarDama(casa, cor);
        }
    }

    @Test
    void jogoNovoCriaAs24PedrasNasMesmasCasasENaMesmaOrdem() {
        FabricaRegistradora fabrica = new FabricaRegistradora();

        new Jogo(fabrica);

        assertEquals(ESPERADO, fabrica.pedras);
        assertEquals(0, fabrica.damas);
    }

    @Test
    void colocarPecasPublicoRepeteAMesmaSequenciaEmOutroTabuleiro() {
        FabricaRegistradora fabrica = new FabricaRegistradora();
        Jogo jogo = new Jogo(fabrica);
        fabrica.pedras.clear();

        jogo.colocarPecas(new Tabuleiro());

        assertEquals(ESPERADO, fabrica.pedras);
        assertTrue(fabrica.damas == 0);
    }
}
