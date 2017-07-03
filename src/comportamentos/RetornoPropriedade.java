package comportamentos;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Responsabilidade:
 * - Tratar qualquer mensagem que peça o valor de alguma proriedade interna desse agente
 */
public class RetornoPropriedade extends CyclicBehaviour {

    private Map<String, Supplier> getters;

    public RetornoPropriedade() {
        getters = new HashMap<>();
    }

    public void adicionarGetter(String prop, Supplier getter) {
        getters.put(prop, getter);
    }

    @Override
    public void action() {
        // Busca mensagens compatíveis na fila de espera
        MessageTemplate filtro = new MessageTemplate((ACLMessage msg) -> {
            return msg.getProtocol().equals("ai_getProp") && msg.getPerformative() == ACLMessage.REQUEST;
        });
        ACLMessage msg = getAgent().receive(filtro);
        // Trata a mensagem se ela existir
        if (msg != null) {
            String nomeProp = msg.getContent();
            Supplier getter = getters.get(nomeProp);
            ACLMessage resp = msg.createReply();
            // Responde a mensagem como válida se a propriedade existir
            if (getter != null) {
                Object prop = getter.get();
                resp.setOntology(prop.getClass().getTypeName());
                resp.setContent(prop.toString());
                resp.setPerformative(ACLMessage.INFORM);
            }
            // Senão, responde com um erro
            else {
                resp.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            }
            getAgent().send(resp);
        }
        // Senão, fica em espera até a próxima mensagem
        else {
            block();
        }
    }
}
