package agentes;

import geral.Ator;
import geral.Time;
import java.util.List;
import java.util.Random;

public class NaveJogador extends AgenteNave {

    private Random rng;
    
    public NaveJogador() {
        super(200, 3, 2.0, 0.2);
        this.rng = new Random();
        this.time = Time.Jogador;
        this.tamanho = 30;
    }

    @Override
    protected void setup() {
        super.setup();
        // Posição inicial do jogador
        x = 400;
        y = 550;
        moverPara(x, y);
    }

    @Override
    protected boolean podeAtirar() {
        // Sempre
        return true;
    }

    @Override
    protected double anguloTiro() {
        // Para cima, com um pouco de imprecisão
        return rng.nextDouble() * 0.1 - 0.05;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        // Desvia balas e tenta acompanhar o movimento dos inimigos
        List<Ator> projeteis = ambiente.getColisoes(x, y, 100, outro -> {
            return outro.tipo.equals(ProjetilBasico.class.getSimpleName())
                && outro.time != this.time;
        });
        for (Ator a : projeteis) {
            // Desviar
        }
    }
}
