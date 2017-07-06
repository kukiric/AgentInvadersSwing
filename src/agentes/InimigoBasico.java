package agentes;

import geral.PausaGlobal;
import jade.core.behaviours.TickerBehaviour;
import java.util.Random;

public class InimigoBasico extends AgenteBase {
    int vida;

    public InimigoBasico() {
        time = Time.Inimigo;
    }
    
    Random rand = new Random();
    double velX = rand.nextDouble() * 5;
    double velY = rand.nextDouble() * 5;
    double velA = rand.nextDouble() * 0.2;

    @Override
    protected void setup() {
        super.setup();
        rp.adicionarGetter("vida", () -> vida);
        int id = (int)getArguments()[0];
        // Escolhe a posição desejada de acordo com seu id
        //x = 40;
        //y = 40;
    }

    @Override
    public void update() {
        x += velX;
        y += velY;
        angulo += velA;
    }
}
