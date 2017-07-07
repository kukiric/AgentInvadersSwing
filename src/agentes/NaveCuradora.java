package agentes;

import geral.Ambiente;
import geral.Ator;
import geral.JadeHelper;
import java.io.Serializable;
import java.util.Random;
import protocolos.AgentInvaders;

public final class NaveCuradora extends AgenteNave {

    public final static class PedidoCura implements Serializable {
        public final int vida;
        public final Ator quem;

        public PedidoCura(int vida, Ator quem) {
            this.vida = vida;
            this.quem = quem;
        }
    }

    enum Estado {
        Esperando,
        Buscando,
        Curando,
        Voltando
    };

    private Estado estado;
    private Random rng;
    private boolean listado;
    private Ator alvo;
    private int alvoVida;
    private int curado;
    // Tempo aleatório
    private final double minTempo = 10;
    private final double maxTempo = 20;
    // Começa fora da tela
    private final double inicioX = -400;
    private final double inicioY = 200;
    private double tempoNext;
    private double tempo;

    public NaveCuradora() {
        super(100, 1, 1, 1);
        this.estado = Estado.Esperando;
        this.rng = new Random();
        this.listado = false;
        this.velocidade = 500;
        this.tempo = 0;
    }

    @Override
    protected void setup() {
        super.setup();
        x = inicioX;
        y = inicioY;
        tempoNext = rng.nextDouble() * (maxTempo - minTempo) + minTempo;
    }

    @Override
    protected boolean podeAtirar() {
        // Nunca atira
        return false;
    }

    @Override
    protected double anguloTiro() {
        // Não importa
        return 0.0;
    }

    @Override
    protected void tratarEvento(AgentInvaders.Evento evento) {
        super.tratarEvento(evento);
        // Recebe pedidos de cura
        if (evento.tipo == AgentInvaders.TipoEvento.PedidoCura) {
            PedidoCura pedido = (PedidoCura) evento.valor;
            // Grava essa nave se ela precisar da maior cura
            if (alvo == null || pedido.vida < alvoVida) {
                alvo = pedido.quem;
            }
        }
    }

    @Override
    public void update(double delta) {
        switch(estado) {
            case Esperando: {
                // Checa se é hora de oferecer serviços de cura
                tempo += delta;
                if (tempo > tempoNext) {
                    estado = Estado.Buscando;
                    tempoNext = rng.nextDouble() * (maxTempo - minTempo) + minTempo;
                }
                break;
            }
            case Buscando: {
                // Lista o serviço no DF
                if (!listado) {
                    JadeHelper.instancia().registrarServico(this, "indice", "cura", AgentInvaders.nomeProtocolo());
                    listado = true;
                    tempo = 0;
                }
                tempo += delta;
                // Dois segundos no máximo em modo de busca
                if (tempo > 2.0) {
                    if (alvo != null) {
                        estado = Estado.Curando;
                        tempo = 0;
                    }
                    // Volta se ninguém pediu cura :(
                    else {
                        estado = Estado.Voltando;
                    }
                    // Remove do DF
                    JadeHelper.instancia().removerServico(this);
                    listado = false;
                }
                break;
            }
            case Curando: {
                tempo += delta;
                moverPara(alvo.x, alvo.y);
                // Está dentro da distância de cura, ou passou do limite de tempo?
                if (Ambiente.distancia(x, y, alvo.x, alvo.y) < 1.0 || tempo > 5.0) {
                    // Cura o agente em 100 pontos de vida
                    AgentInvaders.enviarMensagem(this, alvo.agenteDono, AgentInvaders.TipoEvento.Cura, 100);
                    // E volta
                    estado = Estado.Voltando;
                }
            }
            case Voltando: {
                // Volta para o início
                if (Ambiente.distancia(x, y, inicioX, inicioY) > 1.0) {
                    moverPara(inicioX, inicioY);
                }
                // Muda para o próximo estado
                else {
                    estado = Estado.Esperando;
                    tempoNext = rng.nextDouble() * (maxTempo - minTempo) + minTempo;
                    tempo = 0;
                }
            }
        }
        super.update(delta);
    }
}
