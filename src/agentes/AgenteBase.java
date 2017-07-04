package agentes;

import comportamentos.RetornoPropriedade;
import geral.Ator;
import geral.JadeHelper;
import jade.core.Agent;
import java.util.Random;

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
    public float tamanho;

    AgenteBase() {
        Random rand = new Random();
        x = rand.nextInt(400);
        y = rand.nextInt(400);
        time = Time.Neutro;
        rp = new RetornoPropriedade();
        rp.adicionarGetter("x", () -> x);
        rp.adicionarGetter("y", () -> y);
        rp.adicionarGetter("time", () -> time);
        rp.adicionarGetter("tamanho", () -> tamanho);
        rp.adicionarGetter("definicaoAtor", () -> getDefinicaoAtor());
    }

    @Override
    protected void setup() {
        JadeHelper.instancia().registrarServico(this, "ai_getProp");
        addBehaviour(rp);
    }

    @Override
    protected void takeDown() {
        JadeHelper.instancia().removerServico(this);
    }

    public String getNomeSprite() {
        return getClass().getSimpleName();
    }

    public Ator getDefinicaoAtor() {
        return new Ator(getNomeSprite(), x, y);
    }
}
