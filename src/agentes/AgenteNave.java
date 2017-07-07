package agentes;

import geral.Ator;
import geral.JadeHelper;
import geral.Time;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import protocolos.AgentInvaders;

/**
 * Representa uma nave que pode atirar, levar dano, e se mover
 */
public abstract class AgenteNave extends AgenteBase {

    private int tirosPorRajada;
    private double tempoPorRajada;
    private double tempoPorTiro;
    private double contadorTempo;
    private int contadorTiro;
    private boolean movendo;

    protected double xDestino;
    protected double yDestino;
    protected double velocidade;

    public int vidaMax;
    public int vida;

    public AgenteNave(int vidaMax, int tirosPorRajada, double tempoPorRajada, double tempoPorTiro) {
        this.tirosPorRajada = tirosPorRajada;
        this.tempoPorRajada = tempoPorRajada;
        this.tempoPorTiro = tempoPorTiro;
        this.contadorTempo = 0;
        this.contadorTiro = 0;
        this.movendo = false;
        this.velocidade = 100;
        this.vidaMax = vidaMax;
        this.vida = this.vidaMax;
    }

    @Override
    protected void setup() {
        super.setup();
        // Trata mensagens de evento
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive(AgentInvaders.getTemplateFiltro());
                if (msg != null) {
                    AgentInvaders.Evento info = null;
                    try {
                        tratarEvento((AgentInvaders.Evento) msg.getContentObject());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
                else {
                    block();
                }
            }
        });
    }

    protected abstract boolean podeAtirar();

    protected abstract double anguloTiro();

    protected void receberDano(int qtd) {
        vida -= qtd;
        if (vida > vidaMax) {
            vida = vidaMax;
        }
        else if (vida <= 0) {
            doDelete();
        }
    }

    protected void tratarEvento(AgentInvaders.Evento evento) {
        switch (evento.tipo) {
            case Dano:
                receberDano((Integer) evento.valor);
                break;
            case Cura:
                receberDano(0 - ((Integer) evento.valor));
                break;
        }
    }

    private void doMover(double delta) {
        if (movendo) {
            // Testa a proximidade do local de destino
            if (Double.compare(x, xDestino) != 0 || Double.compare(y, yDestino) != 0) {
                // Ângulo da diferença de vetores
                double anguloMover = Math.atan2(y - yDestino, x - xDestino) - Math.PI / 2;
                // Teorema de Pitágoras
                double distancia = Math.sqrt(Math.pow(x - xDestino, 2) + Math.pow(y - yDestino, 2));
                // Avanço na direção
                double deltaX =  Math.sin(anguloMover) * delta * velocidade;
                double deltaY = -Math.cos(anguloMover) * delta * velocidade;
                // Clamping (corte no intervalo)
                deltaX = deltaX >  distancia ?  distancia : deltaX;
                deltaX = deltaX < -distancia ? -distancia : deltaX;
                deltaY = deltaY >  distancia ?  distancia : deltaY;
                deltaY = deltaY < -distancia ? -distancia : deltaY;
                x += deltaX;
                y += deltaY;
            }
            else {
                movendo = false;
            }
        }
    }

    protected void moverPara(double x, double y) {
        xDestino = x;
        yDestino = y;
        movendo = true;
    }

    protected void pararMovimento() {
        movendo = false;
    }

    @Override
    public void update(double delta) {
        contadorTempo += delta;
        // Esepera o tempo de rajada a cada X tiros de rajada
        // BUG: Aqui gera um erro de divisão por zero se tirosPorRajada for 0
        double tempoEspera = contadorTiro % tirosPorRajada == 0 ? tempoPorRajada : tempoPorTiro;
        if (contadorTempo > tempoEspera && podeAtirar()) {
            String nomeProjetil = "projetil_" + getAID().getLocalName() + "_" + (contadorTiro++);
            Object[] opcoesProjetil = new Object[] {
                this.ambiente,
                this.time,
                anguloTiro(),
                x,
                y
            };
            JadeHelper.instancia().criarAgente(nomeProjetil, "agentes.ProjetilBasico", opcoesProjetil);
            contadorTempo = 0;
        }
        doMover(delta);
        super.update(delta);
    }

    @Override
    public Ator getDefinicaoAtor() {
        // Converte o valor da vida para uma escala de 0 a 1
        double fVida = (double) vida / vidaMax;
        return new Ator(getAID(), getNomeSprite(), getClass().getSimpleName(), time, fVida, x, y, angulo, 0.5, tamanho);
    }
}
