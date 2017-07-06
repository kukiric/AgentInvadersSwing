package agentes;

import geral.Time;
import java.util.Random;

public class InimigoBasico extends AgenteNave {

    private int xBase;
    private int yBase;
    private double tempoMovimentacao;
    private Random rngTiro;

    public InimigoBasico() {
        super(100, 1, 1);
        this.time = Time.Inimigo;
        this.rngTiro = new Random();
    }

    @Override
    protected void setup() {
        super.setup();
        // Escolhe a posição base desejada de acordo com o seu id
        int id = (int)getArguments()[0];
        x = xBase = (id % 5) * 100 + 200;
        y = yBase = (id / 5) * 100 + 50;
    }

    @Override
    protected boolean podeAtirar() {
        // Baixa chance de atirar a cada tick
        return rngTiro.nextDouble() < 1.0/500;
    }

    @Override
    protected double anguloTiro() {
        // Para baixo (-180º)
        return Math.PI;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        tempoMovimentacao += delta;
        x = xBase + Math.sin(tempoMovimentacao) * 125;
    }
}
