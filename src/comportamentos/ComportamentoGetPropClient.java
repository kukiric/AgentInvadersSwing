package comportamentos;

import geral.Ator;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import protocolos.GetProp;

/**
 * Permite a receber notificações do protocolo GetProp em qualquer ator, executando uma função sempre que é recebida uma atualização
 */
public class ComportamentoGetPropClient extends CyclicBehaviour {

    private static MessageTemplate filtro = GetProp.getTemplateFiltro(ACLMessage.INFORM);

    public Consumer<GetProp.Mensagem> listener;

    public ComportamentoGetPropClient(Agent agente, Consumer<GetProp.Mensagem> listener) {
        super(agente);
        this.listener = listener;
    }

    @Override
    public void action() {
        // Recebe uma mensagem de cada vez
        ACLMessage msg = getAgent().receive(filtro);
        if (msg != null) {
            // Decodifica a mensagem
            try {
                Serializable obj = msg.getContentObject();
                listener.accept(new GetProp.Mensagem(msg.getSender(), msg.getInReplyTo(), obj));
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
