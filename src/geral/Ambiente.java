package geral;

import jade.core.AID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Representa todas as informações relevante aos agentes físicos do sistema
 */
public class Ambiente {

    private Map<AID, Ator> atores;

    public Ambiente() {
        this.atores = new HashMap<>();
    }

    public void atualizarAtor(AID agente, Ator ator) {
        atores.put(agente, ator);
    }

    public void removerAtor(AID agente) {
        atores.remove(agente);
    }

    public List<Ator> getAllAtores() {
        return new ArrayList<Ator>(atores.values());
    }

    public List<Ator> getColisoes(double x, double y, double tamanho, Function<Ator, Boolean> filtro) {
        ArrayList<Ator> resultados = new ArrayList<>();
        for (Ator a : atores.values()) {
            // Checagem de distância
            if (distancia(x, y, a.x, a.y) <= tamanho + a.tamanho) {
                // Filtro custom
                if (filtro.apply(a)) {
                    resultados.add(a);
                }
            }
        }
        return resultados;
    }

    public static double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
