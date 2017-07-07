package agentes;

import comportamentos.ComportamentoRespondeCura;
import geral.Ator;
import geral.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class NaveJogador extends AgenteNave {

    private double tempoMovimentacao;
    private final double inicioX = 400;
    private final double inicioY = 550;
    private Random rng;

    public NaveJogador() {
        super(100, 3, 2.0, 0.2);
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
        // Pede a cura quando disponível
        addBehaviour(new ComportamentoRespondeCura(this, () -> vida, () -> vidaMax, () -> getDefinicaoAtor()));
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
        List<Ator> projeteisEsquerda = new ArrayList<>();
        List<Ator> projeteisDireita = new ArrayList<>();
        // Desvia para a direção com menos projéteis
        List<Ator> projeteis = ambiente.getColisoes(x, y, 50, outro -> {
            return outro.tipo.equals(ProjetilBasico.class.getSimpleName())
                && outro.time != this.time;
        });
        for (Ator a : projeteis) {
            // Divide os projeteis
            if (a.x < this.x) {
                projeteisEsquerda.add(a);
            }
            else {
                projeteisDireita.add(a);
            }
        }
        // Sem preferência
        if (projeteisEsquerda.size() == projeteisDireita.size()) {
            tempoMovimentacao += delta;
            moverPara(inicioX + Math.sin(tempoMovimentacao) * 200, inicioY);
        }
        // Desvia para a esquerda
        else if (projeteisEsquerda.size() < projeteisDireita.size()) {
            moverPara(x - 50, inicioY);
        }
        // Desvia para a direita
        else {
            moverPara(x + 50, inicioY);
        }
    }
}
