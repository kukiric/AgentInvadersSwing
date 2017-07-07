package servicos;

import geral.JadeHelper;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
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
    public static final class Mensagem implements Serializable {
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
        msg.setProtocol(nomeServico());
        msg.setPerformative(ACLMessage.REQUEST);
        msg.setContent(prop);
        for (AID destinatario : destinatarios) {
            msg.addReceiver(destinatario);
        }
        msg.setSender(remetente);
        msg.addReplyTo(remetente);
        return msg;
    }

    /**
     * Retorna um template de filtro de mensagens que fazem parte do protocolo
     */
    public static MessageTemplate getTemplateFiltro(int performativa) {
        return new MessageTemplate((msg) -> {
            return nomeServico().equals(msg.getProtocol())
                && performativa == msg.getPerformative();
        });
    }

    /**
     * Envia a inscrição para vários agentes
     */
    public static void enviarInscricao(Agent agente, List<AID> destinatarios) {
        ACLMessage msg = GetProp.criarMensagemPedido(agente.getAID(), destinatarios, "");
        msg.setPerformative(ACLMessage.SUBSCRIBE);
        msg.setContent("true");
        agente.send(msg);
    }

    /**
     * Cancela a inscrição
     */
    public static void enviarCancelamento(Agent agente, List<AID> destinatarios) {
        ACLMessage msg = GetProp.criarMensagemPedido(agente.getAID(), destinatarios, "");
        msg.setPerformative(ACLMessage.SUBSCRIBE);
        msg.setContent("false");
        agente.send(msg);
    }

    public static String nomeServico() {
        return "ai_getProp";
    }

    public static ServiceDescription descricao(String tipo) {
        return JadeHelper.criarServico(nomeServico(), tipo);
    }
}
