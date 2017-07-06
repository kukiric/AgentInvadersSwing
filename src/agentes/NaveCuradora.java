package agentes;

public class NaveCuradora extends AgenteNave {

    public NaveCuradora() {
        super(100, 0, 0);
    }

    @Override
    protected void setup() {
        super.setup();
        x = -400;
        y = 200;
    }

    @Override
    protected boolean podeAtirar() {
        // Nunca atira
        return false;
    }

    @Override
    protected double anguloTiro() {
        // Não importa
        return 0.0;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }
}
