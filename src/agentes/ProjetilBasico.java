package agentes;

import geral.Time;

public class ProjetilBasico extends AgenteBase {

    public ProjetilBasico() {
        tamanho = 2;
    }

    @Override
    protected void setup() {
        super.setup();
        this.time = (Time)getArguments()[0];
        this.angulo = (double)getArguments()[1];
        this.x = (double)getArguments()[2];
        this.y = (double)getArguments()[3];
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
