package agentes;

import componentes.CanvasJogo;
import geral.Ambiente;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * Coleta informações sobre os agentes do sistema e passa para a interface gráfica
 */
public class AgenteInterface extends Agent {

    private CanvasJogo canvas;
    private Ambiente ambiente;

    @Override
    protected void setup() {
        ambiente = (Ambiente) getArguments()[0];
        canvas = (CanvasJogo) getArguments()[1];
        // Atualiza a interface com os dados periodicamente
        addBehaviour(new TickerBehaviour(this, 30) {
            @Override
            protected void onTick() {
                if (ambiente != null) {
                    canvas.atualizar(ambiente.getAllAtores());
                }
            }
        });
    }
}
