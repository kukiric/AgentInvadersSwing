package agentes;

import componentes.CanvasJogo;
import comportamentos.ComportamentoInterface;
import geral.PausaGlobal;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;

/**
 * Coleta informações sobre os agentes do sistema e passa para a interface gráfica
 */
public class AgenteInterface extends Agent {

    CanvasJogo canvas;

    @Override
    protected void setup() {
        canvas = (CanvasJogo)getArguments()[0];
        addBehaviour(new SequentialBehaviour(this) {
            {
                // Segura a interface até a primeira despausa
                addSubBehaviour(new SimpleBehaviour(myAgent) {
                    @Override
                    public void action() {
                        block(10);
                    }
                    @Override
                    public boolean done() {
                        return PausaGlobal.pause == false;
                    }
                });
                // Inicia em sequência
                addSubBehaviour(new ComportamentoInterface(myAgent, 16, canvas));
            }
        });
    }
}
