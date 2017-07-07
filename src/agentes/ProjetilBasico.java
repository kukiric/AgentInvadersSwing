package agentes;

import comportamentos.ComportamentoGetPropClient;
import comportamentos.ComportamentoInscricao;
import geral.Ator;
import geral.JadeHelper;
import geral.Time;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import servicos.EventoJogo;
import servicos.GetProp;

public class ProjetilBasico extends AgenteBase {

    private List<AID> pontosInscricao;

    public ProjetilBasico() {
        this.tamanho = 4.5;
        this.pontosInscricao = new ArrayList<>();
    }

    @Override
    protected void setup() {
        super.setup();
        // Faz a leitura dos parâmetros
        time = (Time)getArguments()[0];
        angulo = (double)getArguments()[1];
        x = (double)getArguments()[2];
        y = (double)getArguments()[3];
        // Procura todos os agentes disponíveis no DF
        send(JadeHelper.criaMensagemInscricaoDF(this, GetProp.nomeServico(), getClass().getSimpleName()));
        // Recebe informações de todos os agentes de interesse (time oposto não neutro)
        addBehaviour(new ComportamentoInscricao(this, GetProp.nomeServico(), true, agentes -> {
            GetProp.enviarInscricao(this, agentes);
        }));
        // Trata as mensagens recebidas durante a inscrição
        addBehaviour(new ComportamentoGetPropClient(this, msg -> {
            if (msg.prop.equals("definicaoAtor")) {
                Ator ator = (Ator) msg.valor;
                // Imediatamente remove os agentes que não são de interesse (neutros ou do mesmo time)
                if (ator.time == this.time || ator.time == Time.Neutro) {
                    GetProp.enviarCancelamento(this, Collections.singletonList(msg.agente));
                    return;
                }
                // Danifica um ator se atingí-lo
                double distancia = Math.sqrt(Math.pow(ator.x - this.x, 2) + Math.pow(ator.y - this.y, 2));
                if (distancia < this.tamanho + ator.tamanho) {
                    ACLMessage dmg = EventoJogo.criarMensagem(getAID(), Collections.singletonList(msg.agente), EventoJogo.Tipo.Dano, 25);
                    send(dmg);
                    doDelete();
                }
            }
        }));
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        // Remove as inscrições ativas quando morrer
        GetProp.enviarCancelamento(this, pontosInscricao);
    }

    // Varia o tipo da sprite de acordo com quem atirou
    @Override
    public String getNomeSprite() {
        return getClass().getSimpleName() + "_" + time.name();
    }

    @Override
    public void update(double delta) {
        // Move o projétil na direção que ele aponta
        x += Math.sin(angulo) * delta * 200;
        y -= Math.cos(angulo) * delta * 200;
        // Atualiza com a nova posição
        super.update(delta);
        // Remove o projétil se ele se encontrar fora da tela
        if (x < -50 || x > 850 || y < -50 || y > 650) {
            doDelete();
        }
    }
}
