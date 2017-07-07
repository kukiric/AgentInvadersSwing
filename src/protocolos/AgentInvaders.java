package protocolos;

import geral.JadeHelper;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.List;

public class AgentInvaders {

    public enum TipoEvento {
        Dano,
        Cura,
        OutraDestruida
    }

    public static final class Evento implements Serializable {
        public final TipoEvento tipo;
        public final Serializable valor;

        public Evento(TipoEvento tipo, Serializable valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    public static MessageTemplate getTemplateFiltro() {
        return new MessageTemplate((msg) -> {
            return nomeProtocolo().equals(msg.getProtocol())
                && ACLMessage.INFORM == msg.getPerformative();
        });
    }

    public static ACLMessage criarMensagem(AID remetente, List<AID> destinatarios, TipoEvento tipo, Serializable parametro) {
        ACLMessage msg = new ACLMessage();
        msg.setLanguage("Java");
        msg.setProtocol(nomeProtocolo());
        msg.setPerformative(ACLMessage.INFORM);
        try {
            msg.setContentObject(new Evento(tipo, parametro));
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        for (AID destinatario : destinatarios) {
            msg.addReceiver(destinatario);
        }
        msg.setSender(remetente);
        msg.addReplyTo(remetente);
        return msg;
    }

    public static String nomeProtocolo() {
        return "agentinvaders-generic";
    }
}
