package comportamentos;

import componentes.CanvasJogo;
import geral.Ator;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import servicos.GetProp;

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
        addSubBehaviour(new ComportamentoInscricao(agente, GetProp.nomeServico(), true, agentes -> {
            // E então manda uma inscrição direta para cada agente
            GetProp.enviarInscricao(agente, agentes);
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
