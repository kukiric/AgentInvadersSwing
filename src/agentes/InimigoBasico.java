package agentes;

import geral.JadeHelper;
import geral.Time;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
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

public class InimigoBasico extends AgenteNave {

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
        this.tamanho = 30;
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
        moverPara(x, y);
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
        // Responde à morte de um aliado
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive(AgentInvaders.getTemplateFiltro());
                if (msg != null) {
                    try {
                        AgentInvaders.Evento evt = (AgentInvaders.Evento) msg.getContentObject();
                        if (evt.tipo == AgentInvaders.TipoEvento.OutraDestruida) {
                            InfoDestruida info = (InfoDestruida) evt.valor;
                            // Move para baixo se ela estiver na mesma coluna
                            if (info.coluna == coluna && info.linha > linha) {
                                yBase = yBase + 100;
                            }
                            // Remove a aliada da lista
                            aliados.remove(info.agente);
                        }
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

    @Override
    protected void takeDown() {
        super.takeDown();
        // Se retira do DF
        JadeHelper.instancia().removerServico(this);
        // Avisa os outros que esse morreu
        List<AID> listAliados = new ArrayList<>(aliados);
        ACLMessage msg = AgentInvaders.criarMensagem(getAID(), listAliados, AgentInvaders.TipoEvento.OutraDestruida, new InfoDestruida(linha, coluna, getAID()));
        send(msg);
    }

    @Override
    protected boolean podeAtirar() {
        // Baixa chance de atirar a cada tick, depende de quantos aliados sobraram
        int numAliados = aliados.size();
        double chance = 1.0 / (1000 - numAliados * 50);
        return rng.nextDouble() < chance;
    }

    @Override
    protected double anguloTiro() {
        // Para baixo (-180º)
        return Math.PI;
    }

    @Override
    public void update(double delta) {
        tempoMovimentacao += delta;
        // Anda pra trás e pra frente
        moverPara(xBase + Math.sin(tempoMovimentacao) * 125, yBase);
        super.update(delta);
    }
}
