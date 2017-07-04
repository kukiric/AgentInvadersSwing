package agentes;

public class InimigoBasico extends AgenteBase {
    int vida;

    public InimigoBasico() {
        time = Time.Inimigo;
    }

    @Override
    protected void setup() {
        super.setup();
        rp.adicionarGetter("vida", () -> vida);
    }
}
