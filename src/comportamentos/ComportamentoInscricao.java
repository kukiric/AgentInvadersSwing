package comportamentos;

import geral.JadeHelper;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import protocolos.GetProp;

/**
 * Inscreve o agente em algum protocolo através do Diretório Facilitador e trata as mensagens recebidas
 */
public class ComportamentoInscricao extends SequentialBehaviour {

    public ComportamentoInscricao(Agent agente, String protocolo, Consumer<List<AID>> listener) {
        // Filtro de mensagens
        MessageTemplate filtro = new MessageTemplate(msg -> "fipa-subscribe".equals(msg.getProtocol()));
        // Inicia a inscrição
        addSubBehaviour(new OneShotBehaviour(agente) {
            @Override
            public void action() {
                ACLMessage msg = JadeHelper.instancia().criaMensagemInscricao(agente, GetProp.nomeProtocolo());
                agente.send(msg);
            }
        });
        // Recebe as notificações
        addSubBehaviour(new CyclicBehaviour(agente) {
            @Override
            public void action() {
                // Recebe uma mensagem de cada vez
                ACLMessage msg = myAgent.receive(filtro);
                if (msg != null) {
                    // Decodifica a mensagem
                    try {
                        DFAgentDescription[] notificacoes = DFService.decodeNotification(msg.getContent());
                        // Pede para receber notificações diretas dos agentes
                        List<AID> agentes = Stream.of(notificacoes).map(n -> n.getName()).collect(Collectors.toList());
                        listener.accept(agentes);
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
    }
    
}
