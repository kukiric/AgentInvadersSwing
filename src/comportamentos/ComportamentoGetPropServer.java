package comportamentos;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import servicos.GetProp;

/**
 * Comportamento que trata o protocolo GetProp da aplicação
 */
public final class ComportamentoGetPropServer extends ParallelBehaviour {

    public interface Getter extends Supplier<Serializable> {}

    private Set<AID> inscritos;
    private Map<String, Getter> getters;

    public ComportamentoGetPropServer(Agent agente) {
        super(agente, ParallelBehaviour.WHEN_ALL);
        inscritos = new HashSet<>();
        getters = new HashMap<>();
        addSubBehaviour(new RespondeInscricao(agente));
        addSubBehaviour(new RespondeProp(agente));
    }

    public void adicionarGetter(String prop, Getter getter) {
        getters.put(prop, getter);
    }

    public void notificarProp(String prop, Serializable valor) {
        // Envia uma mensagem com a propriedade para todos os inscritos
        ACLMessage msg = new ACLMessage();
        msg.setPerformative(ACLMessage.INFORM);
        msg.setSender(myAgent.getAID());
        msg.setLanguage("Java");
        msg.setProtocol(GetProp.nomeServico());
        msg.setInReplyTo(prop + ":SUBSCRIPTION");
        try {
            msg.setContentObject(valor);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        for (AID agente : inscritos) {
            msg.addReceiver(agente);
        }
        myAgent.send(msg);
    }

    final class RespondeInscricao extends CyclicBehaviour {

        public RespondeInscricao(Agent agente) {
            super(agente);
        }

        @Override
        public void action() {
            // Busca mensagens compatíveis na fila de espera
            MessageTemplate filtro = GetProp.getTemplateFiltro(ACLMessage.SUBSCRIBE);
            ACLMessage msg = myAgent.receive(filtro);
            // Trata a mensagem se ela existir
            if (msg != null) {
                ACLMessage resp = msg.createReply();
                resp.setInReplyTo(":SUBSCRIBE");
                // Verifica o conteúdo da mensagem
                if (msg.getContent().equalsIgnoreCase("true")) {
                    inscritos.add(msg.getSender());
                    resp.setPerformative(ACLMessage.AGREE);
                }
                else if (msg.getContent().equalsIgnoreCase("false")) {
                    inscritos.remove(msg.getSender());
                    resp.setPerformative(ACLMessage.AGREE);
                }
                // Senão, responde com um erro
                else {
                    resp.setPerformative(ACLMessage.REFUSE);
                }
                myAgent.send(resp);
            }
            // Senão, fica em espera até a próxima mensagem
            else {
                block();
            }
        }
    }

    // Responde aos pedidos de nova propriedade
    final class RespondeProp extends CyclicBehaviour {

        public RespondeProp(Agent agente) {
            super(agente);
        }

        @Override
        public void action() {
            // Busca mensagens compatíveis na fila de espera
            MessageTemplate filtro = GetProp.getTemplateFiltro(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(filtro);
            // Trata a mensagem se ela existir
            if (msg != null) {
                String nomeProp = msg.getContent();
                Getter getter = getters.get(nomeProp);
                ACLMessage resp = msg.createReply();
                resp.setInReplyTo(msg.getContent());
                // Responde a mensagem como válida se a propriedade existir
                if (getter != null) {
                    Serializable prop = getter.get();
                    resp.setOntology(prop.getClass().getTypeName());
                    try {
                        resp.setContentObject(prop);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    resp.setPerformative(ACLMessage.INFORM);
                }
                // Senão, responde com um erro
                else {
                    resp.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                }
                myAgent.send(resp);
            }
            // Senão, fica em espera até a próxima mensagem
            else {
                block();
            }
        }
    }
}
