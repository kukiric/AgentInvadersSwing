package comportamentos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import protocolos.GetProp;

/**
 * Comportamento que trata o protocolo GetProp da aplicação
 */
public class ComportamentoGetProp extends CyclicBehaviour {

    public interface Getter extends Supplier<Serializable> {}

    private Map<String, Getter> getters;

    public ComportamentoGetProp(Agent agente) {
        super(agente);
        getters = new HashMap<>();
    }

    public void adicionarGetter(String prop, Getter getter) {
        getters.put(prop, getter);
    }

    @Override
    public void action() {
        // Busca mensagens compatíveis na fila de espera
        MessageTemplate filtro = GetProp.getTemplateFiltro(ACLMessage.REQUEST);
        ACLMessage msg = getAgent().receive(filtro);
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
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
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
