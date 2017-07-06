package agentes;

import geral.Direcao;

public class ProjetilBasico extends AgenteBase {

    public Direcao direcao;

    public ProjetilBasico() {
        tamanho = 2;
    }

    @Override
    protected void setup() {
        super.setup();
        time = (Time)getArguments()[0];
        direcao = (Direcao)getArguments()[1];
        rp.adicionarGetter("direcao", () -> direcao);
    }

    // Varia o tipo da sprite de acordo com quem atirou
    @Override
    public String getNomeSprite() {
        return getClass().getSimpleName() + "_" + time.name();
    }

    @Override
    public void update() {
    }
}
