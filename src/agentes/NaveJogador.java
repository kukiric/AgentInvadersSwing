package agentes;

import geral.Time;

public class NaveJogador extends AgenteNave {

    public int vida;
    
    public NaveJogador() {
        super(500, 3, 1.0);
        this.time = Time.Jogador;
        this.tamanho = 25;
    }

    @Override
    protected void setup() {
        super.setup();
        // Posição inicial do jogador
        x = 400;
        y = 550;
    }

    @Override
    protected boolean podeAtirar() {
        // Sempre
        return true;
    }

    @Override
    protected double anguloTiro() {
        // Para cima
        return 0.0;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }
}
