package protocolos;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class AgentInvaders {

    public enum TipoEvento {
        Dano,
        Cura,
        PedidoCura,
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
        // Seta o tipo do evento na ontologia para visualização
        msg.setOntology(tipo.name());
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

    public static void enviarMensagem(Agent agente, AID destinatario, TipoEvento tipo, Serializable parametro) {
        ACLMessage msg = criarMensagem(destinatario, Collections.singletonList(destinatario), tipo, parametro);
        agente.send(msg);
    }

    public static String nomeProtocolo() {
        return "agentinvaders-generic";
    }
}
