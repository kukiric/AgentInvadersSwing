package comportamentos;

import componentes.CanvasJogo;
import geral.Ator;
import geral.JadeHelper;
import geral.PausaGlobal;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import protocolos.GetProp;

/**
 * Coleta a posição de todos os outros agentes para desenhar na tela
 */
public class ComportamentoInterface extends ParallelBehaviour {

    private Map<AID, Ator> atores;
    private CanvasJogo canvas;
    
    public ComportamentoInterface(Agent agente, int intervalo, CanvasJogo canvas) {
        super(agente, ParallelBehaviour.WHEN_ALL);
        this.atores = new HashMap<>();
        this.canvas = canvas;
        addSubBehaviour(new ComportamentoAtualizador(agente, intervalo));
        addSubBehaviour(new ComportamentoRecebedor(agente));
    }

    // Pede novas informações aos atores e atualiza o canvas
    class ComportamentoAtualizador extends TickerBehaviour {

        public ComportamentoAtualizador(Agent agente, int intervalo) {
            super(agente, intervalo);
        }

        @Override
        protected void onTick() {
            // Respeita a pausa global
            if (PausaGlobal.pause) {
                return;
            }
            Agent agente = getAgent();
            // Busca todos os agentes no diretório facilitador
            List<AID> agentes = JadeHelper.instancia().buscarServico(agente, GetProp.nome());
            // Compõe a mensagem e envia ela
            ACLMessage msg = GetProp.criarMensagem(agente.getAID(), agentes, "definicaoAtor");
            agente.send(msg);
            // Atualiza o canvas
            canvas.atualizar(new ArrayList<Ator>(atores.values()));
        }
    }

    // Recebe informações dos atores e atualiza o mapa de informações
    class ComportamentoRecebedor extends CyclicBehaviour {

        public ComportamentoRecebedor(Agent agente) {
            super(agente);
        }

        @Override
        public void action() {
            // Recebe uma mensagem de cada vez
            MessageTemplate filtro = GetProp.getTemplateFiltro(ACLMessage.INFORM);
            ACLMessage msg = getAgent().receive(filtro);
            if (msg != null) {
                try {
                    Ator ator = (Ator)msg.getContentObject();
                    atores.put(msg.getSender(), ator);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            // Espera até a próxima mensagem
            else {
                block();
            }
        }
    }
}
