package servicos;

import geral.JadeHelper;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.Serializable;
import java.util.List;

public class EventoJogo {

    public enum Tipo {
        Dano,
        Cura,
        Fim,
        Destruida
    }

    public static final class Mensagem implements Serializable {
        public final Tipo tipo;
        public final Serializable valor;

        public Mensagem(Tipo tipo, Serializable valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    public static MessageTemplate getTemplateFiltro(int performativa) {
        return new MessageTemplate((msg) -> {
            return nomeServico().equals(msg.getProtocol())
                && performativa == msg.getPerformative();
        });
    }

    public static ACLMessage criarMensagem(AID remetente, List<AID> destinatarios, Tipo tipo, Serializable parametro) {
        ACLMessage msg = new ACLMessage();
        msg.setLanguage("Java");
        msg.setProtocol(nomeServico());
        msg.setPerformative(ACLMessage.INFORM);
        try {
            msg.setContentObject(new Mensagem(tipo, parametro));
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

    public static String nomeServico() {
        return "ai_evento";
    }

    public static ServiceDescription descricao(String tipo) {
        return JadeHelper.criarServico(nomeServico(), tipo);
    }
}
