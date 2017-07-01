package geral;

import agentes.AgenteBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Responsabilidade:
 * - Encapsular as propriedades de todas as variáveis e agentes relevantes do sistema
 * @author ricardo
 */
public class Ambiente {
    private final List<AgenteBase> agentes;

    public Ambiente(Collection<AgenteBase> atores) {
        this.agentes = new ArrayList<>();
        this.agentes.addAll(atores);
    }

    public List<AgenteBase> getAtores() {
        return agentes;
    }
}
