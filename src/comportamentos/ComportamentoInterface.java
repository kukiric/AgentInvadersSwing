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
    private static MessageTemplate filtroAss = GetProp.getTemplateFiltro(ACLMessage.AGREE);
    private static MessageTemplate filtroSub = new MessageTemplate((msg) -> "fipa-subscribe".equals(msg.getProtocol()));

    private Map<AID, Ator> atores;
    private CanvasJogo canvas;
    
    public ComportamentoInterface(Agent agente, int intervalo, CanvasJogo canvas) {
        super(agente, ParallelBehaviour.WHEN_ALL);
        this.atores = new HashMap<>();
        this.canvas = canvas;
        // Se inscreve no diretório facilitador para todos os agentes que expõem propriedades
        addSubBehaviour(new OneShotBehaviour(agente) {
            @Override
            public void action() {
                ACLMessage msg = JadeHelper.instancia().criaMensagemInscricao(agente, GetProp.nome());
                agente.send(msg);
            }
        });
        // Recebe notificações do DF e pede inscrição aos agentes
        addSubBehaviour(new CyclicBehaviour(agente) {
            @Override
            public void action() {
                // Recebe uma mensagem de cada vez
                ACLMessage msg = myAgent.receive(filtroSub);
                if (msg != null) {
                    // Decodifica a mensagem
                    try {
                        DFAgentDescription[] notificacoes = DFService.decodeNotification(msg.getContent());
                        // Pede para receber notificações diretas dos agentes
                        List<AID> agentes = Stream.of(notificacoes).map(n -> n.getName()).collect(Collectors.toList());
                        ACLMessage msgSub = GetProp.criarMensagemPedido(myAgent.getAID(), agentes, "");
                        msgSub.setPerformative(ACLMessage.SUBSCRIBE);
                        msgSub.setContent("true");
                        myAgent.send(msgSub);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
                // Espera até a próxima mensagem
                else {
                    block();
                }
            }
        });
        // Consome as mensagens de concordância de assinatura
        addSubBehaviour(new CyclicBehaviour(agente) {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive(filtroAss);
                block();
            }
        });
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
