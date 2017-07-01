package agentes;

import jade.core.Agent;

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

    public String getNomeSprite() {
        return getClass().getSimpleName();
    }
}
