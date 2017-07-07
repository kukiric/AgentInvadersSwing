package agentes;

import geral.Ator;
import geral.JadeHelper;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import servicos.EventoJogo;

/**
 * Representa uma nave que pode atirar, levar dano, e se mover
 */
public abstract class AgenteNave extends AgenteBase {

    private int tirosPorRajada;
    private double tempoPorRajada;
    private double tempoPorTiro;
    private double contadorTempo;
    private int contadorTiro;

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
        this.velocidade = 100;
        this.vidaMax = vidaMax;
        this.vida = this.vidaMax;
    }

    @Override
    protected void setup() {
        super.setup();
        rp.adicionarGetter("vida", () -> vida);
        // Trata mensagens de evento
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive(EventoJogo.getTemplateFiltro());
                if (msg != null) {
                    EventoJogo.Mensagem info = null;
                    try {
                        trataEvento((EventoJogo.Mensagem) msg.getContentObject());
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

    protected void recebeDano(int qtd) {
        vida -= qtd;
        if (vida > vidaMax) {
            vida = vidaMax;
        }
        else if (vida <= 0) {
            doDelete();
        }
    }

    protected void trataEvento(EventoJogo.Mensagem evento) {
        switch (evento.tipo) {
            case Dano:
                recebeDano((Integer) evento.valor);
                break;
            case Cura:
                recebeDano(0 - ((Integer) evento.valor));
                break;
        }
    }

    protected void doMover(double delta) {
        // Testa a proximidade do local de destino
        if (Double.compare(x, xDestino) != 0 || Double.compare(y, yDestino) != 0) {
            // Ângulo da diferença de vetores
            double angulo = Math.atan2(y - yDestino, x - xDestino) - Math.PI / 2;
            // Teorema de Pitágoras
            double distancia = Math.sqrt(Math.pow(x - xDestino, 2) + Math.pow(y - yDestino, 2));
            // Avanço na direção
            double deltaX =  Math.sin(angulo) * delta * velocidade;
            double deltaY = -Math.cos(angulo) * delta * velocidade;
            // Clamping (corte no intervalo)
            deltaX = deltaX >  distancia ?  distancia : deltaX;
            deltaX = deltaX < -distancia ? -distancia : deltaX;
            deltaY = deltaY >  distancia ?  distancia : deltaY;
            deltaY = deltaY < -distancia ? -distancia : deltaY;
            x += deltaX;
            y += deltaY;
        }
    }

    protected void moverPara(double x, double y) {
        xDestino = x;
        yDestino = y;
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
        return new Ator(getNomeSprite(), time, fVida, x, y, angulo, 0.5, tamanho);
    }
}
