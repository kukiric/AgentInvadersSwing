package agentes;

import comportamentos.ComportamentoInscricao;
import geral.Ator;
import geral.JadeHelper;
import geral.Time;
import jade.core.AID;
import java.util.HashMap;
import java.util.Map;
import servicos.GetProp;

public class ProjetilBasico extends AgenteBase {

    private Map<Ator, AID> alvos;

    public ProjetilBasico() {
        this.tamanho = 2;
        this.alvos = new HashMap<>();
    }

    @Override
    protected void setup() {
        super.setup();
        // Faz a leitura dos parâmetros
        this.time = (Time)getArguments()[0];
        this.angulo = (double)getArguments()[1];
        this.x = (double)getArguments()[2];
        this.y = (double)getArguments()[3];
        // Procura todos os agentes disponíveis no DF
        send(JadeHelper.criaMensagemInscricao(this, GetProp.nomeServico(), getClass().getSimpleName()));
        // Recebe informações de todos os agentes de interesse (time oposto não neutro)
        addBehaviour(new ComportamentoInscricao(this, GetProp.nomeServico(), true, agentes -> {
            GetProp.enviarInscricao(this, agentes);
        }));
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
