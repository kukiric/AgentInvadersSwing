package comportamentos;

import componentes.CanvasJogo;
import geral.Ator;
import geral.JadeHelper;
import geral.PausaGlobal;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import protocolos.GetProp;

/**
 * Coleta a posição de todos os outros agentes para desenhar na tela
 */
public final class ComportamentoInterface extends ParallelBehaviour {

    private static MessageTemplate filtroProp = GetProp.getTemplateFiltro(ACLMessage.INFORM);

    private Map<AID, Ator> atores;
    private CanvasJogo canvas;
    
    public ComportamentoInterface(Agent agente, int intervalo, CanvasJogo canvas) {
        super(agente, ParallelBehaviour.WHEN_ALL);
        this.atores = new HashMap<>();
        this.canvas = canvas;
        // Se inscreve no diretório facilitador para todos os agentes que expõem propriedades
        addSubBehaviour(new ComportamentoInscricao(agente, GetProp.nomeProtocolo(), true, agentes -> {
            ACLMessage msgSub = GetProp.criarMensagemPedido(myAgent.getAID(), agentes, "");
            msgSub.setPerformative(ACLMessage.SUBSCRIBE);
            msgSub.setContent("true");
            myAgent.send(msgSub);
        }));
        // Trata o recebimento de atualizações dos agentes
        addSubBehaviour(new ComportamentoGetPropClient(agente, msg -> {
            // Ator morreu, remove ele
            if (msg.prop.startsWith("morto") && (Boolean)msg.valor == true) {
                atores.remove(msg.agente);
            }
            // Nova posição, atualiza a tabela
            else if (msg.prop.startsWith("definicaoAtor")) {
                atores.put(msg.agente, (Ator)msg.valor);
            }
        }));
        // Atualiza a interface com os dados periodicamente
        addSubBehaviour(new TickerBehaviour(agente, intervalo) {
            @Override
            protected void onTick() {
                canvas.atualizar(new ArrayList<>(atores.values()));
            }
        });
    }
}
