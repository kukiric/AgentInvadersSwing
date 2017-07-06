package protocolos;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.List;

/**
 * Interface para o protocolo específico da aplicação de leitura de propriedades via troca de mensagens
 */
public class GetProp {

    /**
     * Mensagem empacotada para resposta de inscrição
     */
    public static class Mensagem {
        public final AID agente;
        public final String prop;
        public final Serializable valor;

        public Mensagem(AID agente, String prop, Serializable valor) {
            this.agente = agente;
            this.prop = prop;
            this.valor = valor;
        }
    }

    /**
     * Cria uma mensagem pedindo o valor de alguma propriedade
     */
    public static ACLMessage criarMensagemPedido(AID remetente, List<AID> destinatarios, String prop) {
        ACLMessage msg = new ACLMessage();
        msg.setLanguage("Java");
        msg.setProtocol(GetProp.nome());
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
            return nome().equals(msg.getProtocol())
                && performativa == msg.getPerformative();
        });
    }

    /**
     * Retorna o nome do protocolo
     */
    public static String nome() {
        return "ai_getProp";
    }
}
