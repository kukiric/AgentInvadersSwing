package estruturas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Responsabilidade:
 * - Encapsular as propriedades de todos os objetos do ambiente relevantes à interface gráfica
 * @author ricardo
 */
public class ContainerAmbiente {
    private final List<Ator> atores;

    public ContainerAmbiente(Collection<Ator> atores) {
        this.atores = new ArrayList<>();
        this.atores.addAll(atores);
    }

    public List<Ator> getAtores() {
        return atores;
    }
}
