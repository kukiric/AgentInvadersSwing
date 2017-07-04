package agentes;

import componentes.CanvasJogo;
import comportamentos.RetornoPropriedade;
import geral.Ator;
import geral.JadeHelper;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidade:
 * - Coletar informações do ambiente de agentes e informar a interface gráfica (sistema externo)
 */
public class AgenteInterface extends Agent {
    CanvasJogo canvas;
    List<Ator> atores;

    @Override
    protected void setup() {
        canvas = (CanvasJogo)getArguments()[0];
        addBehaviour(new ComportamentoInterface());
    }
    
    class ComportamentoInterface extends TickerBehaviour {
        public ComportamentoInterface() {
            super(AgenteInterface.this, 30);
        }

        @Override
        protected void onTick() {
            // Limpa todas as mensagens pendentes
            while (receive() != null) {}
            // Busca todos os agentes no diretório facilitador
            List<AID> agentes = JadeHelper.instancia().buscarServico(getAgent(), "ai_getProp");
            atores = new ArrayList<>(agentes.size());
            // Pede informações à todos os agentes
            ACLMessage msg = RetornoPropriedade.criarMensagem("definicaoAtor");
            for (AID agente : agentes) {
                msg.addReceiver(agente);
            }
            msg.addReplyTo(getAID());
            send(msg);
        }
    }
}
