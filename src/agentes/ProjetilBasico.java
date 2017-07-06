package agentes;

public class ProjetilBasico extends AgenteBase {

    public ProjetilBasico() {
        tamanho = 2;
    }

    @Override
    protected void setup() {
        super.setup();
        time = (Time)getArguments()[0];
        angulo = (double)getArguments()[1];
    }

    // Varia o tipo da sprite de acordo com quem atirou
    @Override
    public String getNomeSprite() {
        return getClass().getSimpleName() + "_" + time.name();
    }

    @Override
    public void update(double delta) {
    }
}
