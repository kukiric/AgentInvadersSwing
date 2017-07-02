package agentes;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;

/**
 * Responsabilidade:
 * - Representar as propriedades básicas de um agente físico qualquer no sistema
 * @author ricardo
 */
public abstract class AgenteBase extends Agent {

    public enum Time {
        Jogador,
        Inimigo,
        Neutro
    }

    public int x, y;
    public Time time;

    @Override
    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addLanguages("pt_BR");
        dfd.addProtocols("ai_getPos");
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String getNomeSprite() {
        return getClass().getSimpleName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
