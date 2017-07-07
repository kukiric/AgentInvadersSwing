package agentes;

import geral.Ator;
import jade.core.AID;
import java.util.HashMap;
import java.util.Map;

public class NaveCuradora extends AgenteNave {

    private Map<AID, Ator> alvos;

    public NaveCuradora() {
        super(100, 1, 1, 1);
        this.alvos = new HashMap<>();
    }

    @Override
    protected void setup() {
        super.setup();
        // Começa fora da tela
        x = -400;
        y = 200;
        moverPara(x, y);
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
