package agentes;

import componentes.CanvasJogo;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import java.awt.Color;

/**
 * Responsabilidade:
 * - Coletar informações do ambiente de agentes e informar a interface gráfica (sistema externo)
 */
public class AgenteInterface extends Agent {
    CanvasJogo canvas;

    @Override
    protected void setup() {
        canvas = (CanvasJogo)getArguments()[0];
        addBehaviour(new TickerBehaviour(this, 10) {
            {
                setFixedPeriod(false);
            }
            @Override
            protected void onTick() {
                canvas.getRootPane().repaint();
            }
        });
    }
}
