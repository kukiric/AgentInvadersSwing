package agentes;

import componentes.CanvasJogo;
import comportamentos.ComportamentoInterface;
import jade.core.Agent;

/**
 * Coleta informações sobre os agentes do sistema e passa para a interface gráfica
 */
public class AgenteInterface extends Agent {

    CanvasJogo canvas;

    @Override
    protected void setup() {
        canvas = (CanvasJogo)getArguments()[0];
        addBehaviour(new ComportamentoInterface(this, 16, canvas));
    }
}
