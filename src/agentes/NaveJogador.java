package agentes;

import geral.Time;
import java.util.Random;

public final class NaveJogador extends AgenteNave {

    private double tempoMovimentacao;
    private final double inicioX = 400;
    private final double inicioY = 550;
    private Random rng;
    
    public NaveJogador() {
        super(200, 3, 2.0, 0.2);
        this.rng = new Random();
        this.time = Time.Jogador;
        this.velocidade = 300;
        this.tamanho = 30;
    }

    private int getNumeroInimigos() {
        return (int)ambiente.getAllAtores().stream()
                .filter(a -> a.tipo.equals(InimigoBasico.class.getSimpleName()))
                .count();
    }

    @Override
    protected void setup() {
        super.setup();
        // Posição inicial do jogador
        x = inicioX;
        y = inicioY;
    }

    @Override
    protected boolean podeAtirar() {
        // Somente enquanto existirem inimigos
        return getNumeroInimigos() > 0;
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
//        List<Ator> projeteis = ambiente.getColisoes(x, y, 100, outro -> {
//            return outro.tipo.equals(ProjetilBasico.class.getSimpleName())
//                && outro.time != this.time;
//        });
//        for (Ator a : projeteis) {
//            // TODO: lógica de desvio
//        }
        // Movimentação básica
        tempoMovimentacao += delta * 1.73;
        moverPara(inicioX + Math.sin(tempoMovimentacao) * 400, inicioY);
    }
}
