package agentes;

import comportamentos.RetornoPropriedade;
import geral.JadeHelper;
import jade.core.Agent;

/**
 * Responsabilidade:
 * - Representar as propriedades básicas de um agente físico qualquer no sistema
 */
public abstract class AgenteBase extends Agent {

    public enum Time {
        Jogador,
        Inimigo,
        Neutro
    }

    protected RetornoPropriedade rp;
    public int x, y;
    public Time time;

    AgenteBase() {
        rp = new RetornoPropriedade();
        rp.adicionarGetter("x", () -> x);
        rp.adicionarGetter("y", () -> y);
    }

    @Override
    protected void setup() {
        JadeHelper.instancia().registrarServico(this, "ai_getProp");
        addBehaviour(rp);
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
