package agentes;

import comportamentos.ComportamentoGetPropServer;
import geral.Ator;
import geral.JadeHelper;
import geral.PausaGlobal;
import geral.Time;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import protocolos.GetProp;

/**
 * Representa as propriedades básicas de todos os agentes físicos (não-gerenciadores) no sistema
 */
public abstract class AgenteBase extends Agent {

    protected ComportamentoGetPropServer rp;
    public double x, y;
    public double tamanho;
    public double angulo;
    public Time time;

    AgenteBase() {
        time = Time.Neutro;
        rp = new ComportamentoGetPropServer(this);
        rp.adicionarGetter("definicaoAtor", () -> getDefinicaoAtor());
        // Configura a função update
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
        rp.notificarProp("morto", new Boolean(true));
    }

    public void update(double delta) {
        rp.notificarProp("definicaoAtor", getDefinicaoAtor());
    }

    public String getNomeSprite() {
        return getClass().getSimpleName();
    }

    public Ator getDefinicaoAtor() {
        return new Ator(getNomeSprite(), time, 1.0, x, y, angulo, 0.5);
    }
}
