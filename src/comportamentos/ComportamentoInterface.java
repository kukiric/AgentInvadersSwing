package comportamentos;

import componentes.CanvasJogo;
import geral.Ator;
import geral.JadeHelper;
import geral.PausaGlobal;
import jade.core.AID;
import jade.core.Agent;
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
public class ComportamentoInterface extends TickerBehaviour {

    private boolean enviando;
    private Map<AID, Ator> atores;
    
    public ComportamentoInterface(Agent agente, CanvasJogo canvas) {
        super(agente, 30);
        enviando = true;
        atores = new HashMap<>();
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
        if (enviando) {
            // Limpa todas as mensagens pendentes
            while (agente.receive() != null) {}
            // Compõe a mensagem e envia ela
            ACLMessage msg = GetProp.criarMensagem(agente.getAID(), agentes, "definicaoAtor");
            agente.send(msg);
            enviando = false;
        }
        else {
            // Recebe todas as mensagens
            MessageTemplate filtro = GetProp.getTemplateFiltro(ACLMessage.INFORM);
            ACLMessage msg = agente.receive(filtro);
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
            else {
                block();
            }
        }
    }
}
