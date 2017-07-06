package agentes;

import geral.PausaGlobal;
import jade.core.behaviours.TickerBehaviour;
import java.util.Random;

public class InimigoBasico extends AgenteBase {
    int vida;

    public InimigoBasico() {
        time = Time.Inimigo;
    }

    @Override
    protected void setup() {
        super.setup();
        rp.adicionarGetter("vida", () -> vida);
        int id = (int)getArguments()[0];
        // Escolhe a posição desejada de acordo com seu id
        //x = 40;
        //y = 40;
        // Teste de renderização
        Random rand = new Random();
        double velX = rand.nextDouble() * 5;
        double velY = rand.nextDouble() * 5;
        double velA = rand.nextDouble() * 0.2;
        addBehaviour(new TickerBehaviour(this, 30) {
            @Override
            protected void onTick() {
                if (!PausaGlobal.pause) {
                    x += velX;
                    y += velY;
                    angulo += velA;
                }
            }
        });
    }
}
