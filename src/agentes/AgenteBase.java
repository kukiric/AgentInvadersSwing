package agentes;

import geral.Ambiente;
import geral.Ator;
import geral.JadeHelper;
import geral.PausaGlobal;
import geral.Time;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import protocolos.AgentInvaders;

/**
 * Representa as propriedades básicas de todos os agentes físicos (não-gerenciadores) no sistema
 */
public abstract class AgenteBase extends Agent {

    protected Ambiente ambiente;

    public double x, y;
    public double tamanho;
    public double angulo;
    public Time time;

    AgenteBase() {
        this.time = Time.Neutro;
        // Configura a função update
        addBehaviour(new TickerBehaviour(this, 30) {
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
        ambiente = (Ambiente) getArguments()[0];
        ambiente.atualizarAtor(getAID(), getDefinicaoAtor());
    }

    @Override
    protected void takeDown() {
        // Remove do ambiente
        ambiente.removerAtor(getAID());
    }

    protected ServiceDescription[] getServicos() {
        ServiceDescription svc = new ServiceDescription();
        svc.addProtocols(AgentInvaders.nomeProtocolo());
        svc.setType(getClass().getSimpleName());
        return new ServiceDescription[] {
            
        };
    }

    public void update(double delta) {
        ambiente.atualizarAtor(getAID(), getDefinicaoAtor());
    }

    public String getNomeSprite() {
        return getClass().getSimpleName();
    }

    public Ator getDefinicaoAtor() {
        return new Ator(getAID(), getNomeSprite(), getClass().getSimpleName(), time, 1.0, x, y, angulo, 1.0, tamanho);
    }
}
