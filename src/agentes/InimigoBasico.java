package agentes;

import geral.Time;
import java.util.Random;

public class InimigoBasico extends AgenteNave {

    private int xBase;
    private int yBase;
    private double tempoMovimentacao;
    private Random rng;

    public InimigoBasico() {
        super(100, 1, 1, 0);
        this.time = Time.Inimigo;
        this.rng = new Random();
        this.tamanho = 30;
    }

    @Override
    protected void setup() {
        super.setup();
        // Escolhe a posição base desejada de acordo com o seu id
        int id = (int)getArguments()[1];
        x = xBase = (id % 5) * 100 + 200;
        y = yBase = (id / 5) * 100 + 50;
        moverPara(x, y);
    }

    @Override
    protected boolean podeAtirar() {
        // Testa se a nave se encontra na linha de frente (abaixo das outras)
        if (y > 200) {
            // Baixa chance de atirar a cada tick
            return rng.nextDouble() < 1.0/200;
        }
        return false;
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
