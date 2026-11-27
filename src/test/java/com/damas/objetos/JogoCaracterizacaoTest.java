package com.damas.objetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Golden master: joga partidas aleatórias determinísticas (sementes 1..200) usando só a
 * API pública e compara cada lance aceito com o arquivo
 * {@code src/test/resources/caracterizacao/partidas.txt}, gerado a partir do código atual.
 *
 * <p>Para regravar: {@code mvn test -Dtest=JogoCaracterizacaoTest -Dgolden.gravar=true}.</p>
 */
class JogoCaracterizacaoTest {

    private static final int PRIMEIRA_SEMENTE = 1;
    private static final int ULTIMA_SEMENTE = 200;
    private static final int MAX_LANCES = 400;
    /** Nas primeiras sementes a assinatura completa é gravada (diagnóstico); nas demais, um hash dela. */
    private static final int SEMENTES_COM_ASSINATURA_COMPLETA = 10;
    private static final int MAX_DISTANCIA = 7;

    private static final String RECURSO = "/caracterizacao/partidas.txt";
    private static final Path ARQUIVO = Path.of("src", "test", "resources", "caracterizacao", "partidas.txt");

    private static final int[][] DIAGONAIS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    /** Contadores para provar que o golden master exercita o que importa. */
    private static class Cobertura {
        int lances;
        int partidas;
        int capturasSimples;
        int capturasEmSequencia;
        int ganhosDeDoisOuMaisPontos;
        int promocoes;
        int capturasDeDama;
    }

    // ------------------------------------------------------------------ teste

    @Test
    void partidasAleatoriasDeterministicasReproduzemOGoldenMaster() throws IOException {
        Cobertura cobertura = new Cobertura();
        List<String> obtido = jogarTodasAsPartidas(cobertura);

        verificarCobertura(cobertura);

        if (Boolean.getBoolean("golden.gravar")) {
            Files.createDirectories(ARQUIVO.getParent());
            Files.write(ARQUIVO, (String.join("\n", obtido) + "\n").getBytes(StandardCharsets.UTF_8));
            System.out.println("Golden master gravado: " + ARQUIVO + " (" + obtido.size() + " lances, "
                    + Files.size(ARQUIVO) + " bytes)");
        } else {
            compararComGolden(obtido);
        }
        System.out.println("Cobertura do golden master: partidas=" + cobertura.partidas + " lances=" + cobertura.lances
                + " capturasSimples=" + cobertura.capturasSimples
                + " capturasEmSequencia=" + cobertura.capturasEmSequencia
                + " ganhosDe2OuMaisPontos=" + cobertura.ganhosDeDoisOuMaisPontos
                + " promocoes=" + cobertura.promocoes
                + " capturasDeDama=" + cobertura.capturasDeDama);
    }

    private void verificarCobertura(Cobertura c) {
        assertTrue(c.capturasSimples > 0, "nenhuma captura simples ocorreu");
        assertTrue(c.capturasEmSequencia > 0, "nenhuma captura em sequência ocorreu");
        assertTrue(c.ganhosDeDoisOuMaisPontos > 0, "nenhum ganho de 2+ pontos num lance ocorreu");
        assertTrue(c.promocoes > 0, "nenhuma promoção a dama ocorreu");
        assertTrue(c.capturasDeDama > 0, "nenhuma captura feita por dama ocorreu");
    }

    private void compararComGolden(List<String> obtido) throws IOException {
        List<String> esperado = lerGolden();
        int n = Math.min(esperado.size(), obtido.size());
        for (int i = 0; i < n; i++) {
            if (!esperado.get(i).equals(obtido.get(i))) {
                String[] e = esperado.get(i).split(";", 4);
                fail("Divergência na seed " + e[0] + ", índice do lance " + e[1] + " (linha " + (i + 1) + ")"
                        + "\n  esperado: " + esperado.get(i)
                        + "\n  obtido:   " + obtido.get(i));
            }
        }
        assertEquals(esperado.size(), obtido.size(), "quantidade de lances aceitos diferente do golden master");
    }

    private List<String> lerGolden() throws IOException {
        try (InputStream in = JogoCaracterizacaoTest.class.getResourceAsStream(RECURSO)) {
            assertNotNull(in, "golden master ausente; grave com -Dgolden.gravar=true");
            String texto = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            List<String> linhas = new ArrayList<>();
            for (String linha : texto.split("\n")) {
                if (!linha.isEmpty()) linhas.add(linha);
            }
            return linhas;
        }
    }

    // ------------------------------------------------------------------ simulação

    private List<String> jogarTodasAsPartidas(Cobertura cobertura) {
        List<String> linhas = new ArrayList<>();
        for (int seed = PRIMEIRA_SEMENTE; seed <= ULTIMA_SEMENTE; seed++) {
            jogarPartida(seed, linhas, cobertura);
            cobertura.partidas++;
        }
        return linhas;
    }

    private void jogarPartida(int seed, List<String> linhas, Cobertura cobertura) {
        Jogo jogo = new Jogo();
        Random random = new Random(seed);
        int indice = 0;

        while (indice < MAX_LANCES && jogo.getGanhador().isEmpty()) {
            List<int[]> candidatos = candidatos(jogo);
            Collections.shuffle(candidatos, random);

            String antes = assinatura(jogo);
            boolean aceito = false;

            for (int[] c : candidatos) {
                boolean eraDama = jogo.getTabuleiro().getCasa(c[0], c[1]).getPeca().isDama();
                int pontosAntes = jogo.getJogadorUm().getPontos() + jogo.getJogadorDois().getPontos();
                int damasAntes = contarDamas(jogo);

                jogo.moverPeca(c[0], c[1], c[2], c[3]);

                String depois = assinatura(jogo);
                if (depois.equals(antes)) continue;

                int ganho = jogo.getJogadorUm().getPontos() + jogo.getJogadorDois().getPontos() - pontosAntes;
                registrarCobertura(cobertura, jogo, eraDama, ganho, damasAntes);

                linhas.add(seed + ";" + indice + ";" + c[0] + ":" + c[1] + ">" + c[2] + ":" + c[3] + ";"
                        + (seed <= SEMENTES_COM_ASSINATURA_COMPLETA ? depois : hash(depois)));
                cobertura.lances++;
                indice++;
                aceito = true;
                break;
            }

            if (!aceito) break;
        }
    }

    private void registrarCobertura(Cobertura cobertura, Jogo jogo, boolean eraDama, int ganho, int damasAntes) {
        boolean bloqueada = jogo.getCasaBloqueada() != null;
        if (ganho > 0 && !bloqueada) cobertura.capturasSimples++;
        if (bloqueada) cobertura.capturasEmSequencia++;
        if (ganho >= 2) cobertura.ganhosDeDoisOuMaisPontos++;
        if (contarDamas(jogo) > damasAntes) cobertura.promocoes++;
        if (ganho > 0 && eraDama) cobertura.capturasDeDama++;
    }

    /** Candidatos (origem -> destino): 4 diagonais até 7 casas dentro do tabuleiro. */
    private List<int[]> candidatos(Jogo jogo) {
        List<int[]> lista = new ArrayList<>();
        Casa bloqueada = jogo.getCasaBloqueada();
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                Casa casa = jogo.getTabuleiro().getCasa(x, y);
                Peca peca = casa.getPeca();
                if (peca == null) continue;
                if (bloqueada != null ? casa != bloqueada : peca.getCor() != jogo.getVez()) continue;

                for (int[] d : DIAGONAIS) {
                    for (int k = 1; k <= MAX_DISTANCIA; k++) {
                        int dx = x + d[0] * k;
                        int dy = y + d[1] * k;
                        if (dx < 0 || dx >= Tabuleiro.MAX_LINHAS || dy < 0 || dy >= Tabuleiro.MAX_COLUNAS) break;
                        lista.add(new int[] {x, y, dx, dy});
                    }
                }
            }
        }
        return lista;
    }

    private int contarDamas(Jogo jogo) {
        int total = 0;
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < Tabuleiro.MAX_COLUNAS; y++) {
                Peca peca = jogo.getTabuleiro().getCasa(x, y).getPeca();
                if (peca != null && peca.isDama()) total++;
            }
        }
        return total;
    }

    /** Estado completo: tabuleiro (linha 7 primeiro), vez, pontos, jogadas, jogadas sem comer, casa bloqueada. */
    private String assinatura(Jogo jogo) {
        StringBuilder sb = new StringBuilder();
        for (int y = Tabuleiro.MAX_COLUNAS - 1; y >= 0; y--) {
            for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
                Peca peca = jogo.getTabuleiro().getCasa(x, y).getPeca();
                if (peca == null) {
                    sb.append('.');
                } else {
                    char c = peca.getCor() == Cor.BRANCA ? 'b' : 'v';
                    sb.append(peca.isDama() ? Character.toUpperCase(c) : c);
                }
            }
            if (y > 0) sb.append('/');
        }
        Casa bloqueada = jogo.getCasaBloqueada();
        sb.append('|').append(jogo.getVez())
          .append('|').append(jogo.getJogadorUm().getPontos())
          .append('|').append(jogo.getJogadorDois().getPontos())
          .append('|').append(jogo.getJogada())
          .append('|').append(jogo.getJogadasSemComerPecas())
          .append('|').append(bloqueada == null ? "-" : bloqueada.getX() + ":" + bloqueada.getY());
        return sb.toString();
    }

    private String hash(String texto) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(String.format("%02x", bytes[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
