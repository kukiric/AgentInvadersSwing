package agentes;

import componentes.CanvasJogo;
import comportamentos.ComportamentoInterface;
import geral.Ator;
import jade.core.Agent;
import java.util.List;

/**
 * Responsabilidade:
 * - Coletar informações do ambiente de agentes e informar a interface gráfica (sistema externo)
 */
public class AgenteInterface extends Agent {

    CanvasJogo canvas;

    @Override
    protected void setup() {
        canvas = (CanvasJogo)getArguments()[0];
        addBehaviour(new ComportamentoInterface(this, canvas));
    }
}
