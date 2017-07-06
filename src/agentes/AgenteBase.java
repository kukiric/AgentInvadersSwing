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
    public double x, y;
    public double tamanho;
    public double angulo;
    public Time time;

    AgenteBase() {
        time = Time.Neutro;
        rp = new ComportamentoGetProp(this);
        // Configura as propriedades base do ator
        rp.adicionarGetter("x", () -> x);
        rp.adicionarGetter("y", () -> y);
        rp.adicionarGetter("tamanho", () -> tamanho);
        rp.adicionarGetter("angulo", () -> angulo);
        rp.adicionarGetter("time", () -> time);
        rp.adicionarGetter("definicaoAtor", () -> getDefinicaoAtor());
        // Conmfigura a função update
        addBehaviour(new TickerBehaviour(this, 16) {
            { setFixedPeriod(true); }
            @Override
            protected void onTick() {
                // Respeita a pausa global
                if (!PausaGlobal.pause) {
                    update(getPeriod() / 1000.0);
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

    public abstract void update(double delta);

    public String getNomeSprite() {
        return getClass().getSimpleName();
    }

    public Ator getDefinicaoAtor() {
        return new Ator(getNomeSprite(), x, y, angulo, 0.5);
    }
}
