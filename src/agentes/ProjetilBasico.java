package agentes;

import geral.Ator;
import geral.Time;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.Collections;
import java.util.List;
import protocolos.AgentInvaders;

public class ProjetilBasico extends AgenteBase {

    public ProjetilBasico() {
        this.tamanho = 3;
    }

    @Override
    protected void setup() {
        super.setup();
        // Faz a leitura dos parâmetros
        time = (Time)getArguments()[1];
        angulo = (double)getArguments()[2];
        x = (double)getArguments()[3];
        y = (double)getArguments()[4];
    }

    @Override
    protected void takeDown() {
        super.takeDown();
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
        // Remove e envia mensagem se chegar perto de algum ator do outro time
        else {
            List<Ator> colisoes = ambiente.getColisoes(x, y, tamanho, ator -> ator.time != this.time && ator.time != Time.Neutro);
            if (colisoes.size() > 0) {
                List<AID> alvo = Collections.singletonList(colisoes.get(0).agenteDono);
                ACLMessage msg = AgentInvaders.criarMensagem(getAID(), alvo, AgentInvaders.TipoEvento.Dano, 25);
                send(msg);
                doDelete();
            }
        }
    }
}
