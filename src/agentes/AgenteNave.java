package agentes;

import geral.Ator;
import geral.JadeHelper;

/**
 * Representa uma nave que pode atirar, levar dano, e se mover
 */
public abstract class AgenteNave extends AgenteBase {

    private int tirosPorRajada;
    private double tempoPorRajada;
    private double tempoTiro;
    private int contadorTiro;

    public int vidaMax;
    public int vida;

    public AgenteNave(int vidaMax, int tirosPorRajada, double tempoPorRajada) {
        this.tirosPorRajada = tirosPorRajada;
        this.tempoPorRajada = tempoPorRajada;
        this.tempoTiro = 0;
        this.contadorTiro = 0;
        this.vidaMax = vidaMax;
        this.vida = this.vidaMax;
    }

    @Override
    protected void setup() {
        super.setup();
        rp.adicionarGetter("vida", () -> vida);
    }

    protected abstract boolean podeAtirar();

    protected abstract double anguloTiro();

    @Override
    public void update(double delta) {
        tempoTiro += delta;
        if (tempoTiro > tempoPorRajada && podeAtirar()) {
            String nomeProjetil = "projetil_" + getAID().getLocalName() + "_" + (contadorTiro++);
            Object[] opcoesProjetil = new Object[] {
                this.time,
                anguloTiro(),
                x,
                y
            };
            JadeHelper.instancia().criaAgente(nomeProjetil, "agentes.ProjetilBasico", opcoesProjetil);
            tempoTiro = 0;
        }
        super.update(delta);
    }

    @Override
    public Ator getDefinicaoAtor() {
        // Converte o valor da vida para uma escala de 0 a 1
        double fVida = (double) vida / vidaMax;
        fVida = fVida < 0 ? 0 : fVida;
        fVida = fVida > 1 ? 1 : fVida;
        return new Ator(getNomeSprite(), time, fVida, x, y, angulo, 0.5);
    }
}
