package agentes;

import comportamentos.ComportamentoRespondeCura;
import geral.JadeHelper;
import geral.Time;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import protocolos.AgentInvaders;

public final class InimigoBasico extends AgenteNave {

    // Junta as informações de qual name morreu
    private static class InfoDestruida implements Serializable {
        public final int linha;
        public final int coluna;
        public final AID agente;

        public InfoDestruida(int linha, int coluna, AID agente) {
            this.linha = linha;
            this.coluna = coluna;
            this.agente = agente;
        }
    }

    private Set<AID> aliados;
    private int xBase;
    private int yBase;
    private int linha;
    private int coluna;
    private double tempoMovimentacao;
    private Random rng;

    public InimigoBasico() {
        super(100, 1, 1, 0);
        this.aliados = new HashSet<>();
        this.time = Time.Inimigo;
        this.rng = new Random();
        this.velocidade = 100;
        this.tamanho = 30;
    }

    private boolean jogadorVivo() {
        return ambiente.getAllAtores().stream().anyMatch(a -> a.tipo.equals(NaveJogador.class.getSimpleName()));
    }

    @Override
    protected void setup() {
        super.setup();
        // Escolhe a posição base desejada de acordo com o seu id
        int id = (int)getArguments()[1];
        linha = id / 5;
        coluna = id % 5;
        x = xBase = coluna * 100 + 200;
        y = yBase = linha * 100 + 50;
        // Se registra no DF para encontrar seus aliados
        JadeHelper.instancia().registrarServico(this, "indice", "inimigo", AgentInvaders.nomeProtocolo());
        // E busca os outros
        ACLMessage msg = JadeHelper.criaMensagemInscricaoDF(this, "indice", "inimigo", AgentInvaders.nomeProtocolo());
        addBehaviour(new SubscriptionInitiator(this, msg) {
            @Override
            protected void handleInform(ACLMessage inform) {
                // Adiciona os aliados na lista
                try {
                    DFAgentDescription[] agentes = DFService.decodeNotification(inform.getContent());
                    for (DFAgentDescription agente : agentes) {
                        aliados.add(agente.getName());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
        // Pede a cura quando disponível
        addBehaviour(new ComportamentoRespondeCura(this, () -> vida, () -> vidaMax, () -> getDefinicaoAtor()));
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        // Se retira do DF
        JadeHelper.instancia().removerServico(this);
        // Avisa os outros que esse morreu
        List<AID> listaAliados = new ArrayList<>(aliados);
        ACLMessage msg = AgentInvaders.criarMensagem(getAID(), listaAliados, AgentInvaders.TipoEvento.OutraDestruida, new InfoDestruida(linha, coluna, getAID()));
        send(msg);
    }

    @Override
    protected boolean podeAtirar() {
        // Atira somente enquanto o jogador ainda estiver vivo
        if (jogadorVivo()) {
            // Baixa chance de atirar a cada tick, depende de quantos aliados sobraram
            int numAliados = aliados.size();
            double chance = 1.0 / (200 - (15 - numAliados) * 15);
            return rng.nextDouble() < chance;
        }
        return false;
    }

    @Override
    protected double anguloTiro() {
        // Para baixo (-180º)
        return Math.PI;
    }

    @Override
    protected void tratarEvento(AgentInvaders.Evento evento) {
        super.tratarEvento(evento);
        if (evento.tipo == AgentInvaders.TipoEvento.OutraDestruida) {
            InfoDestruida info = (InfoDestruida) evento.valor;
            // Move para baixo se ela estiver na mesma coluna
            if (info.coluna == coluna && info.linha > linha) {
                yBase = yBase + 100;
            }
            // Remove a aliada da lista
            aliados.remove(info.agente);
        }
    }

    @Override
    public void update(double delta) {
        // Anda de um lado para o outro enquanto o jogador existir
        if (jogadorVivo()) {
            tempoMovimentacao += delta;
            moverPara(xBase + Math.sin(tempoMovimentacao) * 125, yBase);
        }
        // Sai do mapa por baixo quando ele morrer
        else {
            moverPara(xBase, 2000);
        }
        super.update(delta);
    }
}
