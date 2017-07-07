package comportamentos;

import agentes.NaveCuradora;
import geral.Ator;
import geral.JadeHelper;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import java.util.function.Supplier;
import protocolos.AgentInvaders;

public class ComportamentoRespondeCura extends SequentialBehaviour {

    private Supplier<Integer> getVida;
    private Supplier<Integer> getVidaMax;
    private Supplier<Ator> getAtor;
    
    public ComportamentoRespondeCura(Agent agente, Supplier<Integer> getVida, Supplier<Integer> getVidaMax, Supplier<Ator> getAtor) {
        super(agente);
        this.getVida = getVida;
        this.getVidaMax = getVidaMax;
        this.getAtor = getAtor;
        // Inscreve-se no DF
        ACLMessage msg = JadeHelper.criaMensagemInscricaoDF(myAgent, "indice", "cura", AgentInvaders.nomeProtocolo());
        addSubBehaviour(new SubscriptionInitiator(agente, msg) {
            @Override
            protected void handleInform(ACLMessage inform) {
                try {
                    DFAgentDescription[] dfds = DFService.decodeNotification(inform.getContent());
                    // Checa se esse agente precisa de vida
                    int vida = getVida.get();
                    if (vida < getVidaMax.get()) {
                        // Responde à primeira nave curadora
                        NaveCuradora.PedidoCura pedido = new NaveCuradora.PedidoCura(vida, getAtor.get());
                        AgentInvaders.enviarMensagem(myAgent, dfds[0].getName(), AgentInvaders.TipoEvento.PedidoCura, pedido);
                    }
                }
                catch (FIPAException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}
