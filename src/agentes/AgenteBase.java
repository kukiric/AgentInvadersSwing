package agentes;

import comportamentos.ComportamentoGetProp;
import geral.Ator;
import geral.JadeHelper;
import geral.PausaGlobal;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import protocolos.GetProp;

/**
 * Representa as propriedades básicas de todos os agentes físicos (não-gerenciadores) no sistema
 */
public abstract class AgenteBase extends Agent {

    public enum Time {
        Jogador,
        Inimigo,
        Neutro
    }

    protected ComportamentoGetProp rp;
    public int x, y;
    public Time time;
    public double tamanho;
    public double angulo;

    AgenteBase() {
        time = Time.Neutro;
        rp = new ComportamentoGetProp(this);
        // Configura as propriedades base do ator
        rp.adicionarGetter("x", () -> x);
        rp.adicionarGetter("y", () -> y);
        rp.adicionarGetter("time", () -> time);
        rp.adicionarGetter("tamanho", () -> tamanho);
        rp.adicionarGetter("angulo", () -> angulo);
        rp.adicionarGetter("definicaoAtor", () -> getDefinicaoAtor());
        // Conmfigura a função update
        addBehaviour(new TickerBehaviour(this, 30) {
            @Override
            protected void onTick() {
                // Respeita a pausa global
                if (!PausaGlobal.pause) {
                    update();
                }
            }
        });
    }

    @Override
    protected void setup() {
        JadeHelper.instancia().registrarServico(this, GetProp.nome());
        addBehaviour(rp);
    }

    @Override
    protected void takeDown() {
        JadeHelper.instancia().removerServico(this);
    }

    public abstract void update();

    public String getNomeSprite() {
        return getClass().getSimpleName();
    }

    public Ator getDefinicaoAtor() {
        return new Ator(getNomeSprite(), x, y, angulo, 0.5);
    }
}
