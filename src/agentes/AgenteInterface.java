package agentes;

import componentes.CanvasJogo;
import comportamentos.ComportamentoInterface;
import geral.Ator;
import jade.core.Agent;
import java.util.List;

/**
 * Coleta informações sobre os agentes do sistema e passa para a interface gráfica
 */
public class AgenteInterface extends Agent {

    CanvasJogo canvas;

    @Override
    protected void setup() {
        canvas = (CanvasJogo)getArguments()[0];
        addBehaviour(new ComportamentoInterface(this, 30, canvas));
    }
}
