package agentes;

import componentes.CanvasJogo;
import geral.Ambiente;
import geral.JadeHelper;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import protocolos.AgentInvaders;

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
        // Se inscreve no DF para receber o ambiente
        ACLMessage msg = JadeHelper.criaMensagemInscricaoDF(this, "interface", AgentInvaders.nomeProtocolo());
        addBehaviour(new SubscriptionInitiator(this, msg) {
            @Override
            protected void handleInform(ACLMessage inform) {
                System.out.println("Recebida informação: " + inform.getContent());
            }
        });
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
