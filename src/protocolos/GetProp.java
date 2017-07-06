package protocolos;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.List;

/**
 * Interface para o protocolo específico da aplicação de leitura de propriedades via troca de mensagens
 */
public class GetProp {
    /**
     * Cria uma mensagem pedindo o valor de alguma propriedade para determinados agentes
     */
    public static ACLMessage criarMensagem(AID remetente, List<AID> destinatarios, String prop) {
        ACLMessage msg = new ACLMessage();
        msg.setLanguage("Java");
        msg.setProtocol("ai_getProp");
        msg.setPerformative(ACLMessage.REQUEST);
        msg.setContent(prop);
        for (AID destinatario : destinatarios) {
            msg.addReceiver(destinatario);
        }
        msg.addReplyTo(remetente);
        return msg;
    }

    /**
     * Retorna um template de filtro de mensagens que fazem parte do protocolo
     */
    public static MessageTemplate getTemplateFiltro(int performativa) {
        return new MessageTemplate((msg) -> {
            return msg.getProtocol().equals(nome())
                && msg.getPerformative() == performativa;
        });
    }

    /**
     * Retorna o nome do protocolo
     */
    public static String nome() {
        return "ai_getProp";
    }
}
