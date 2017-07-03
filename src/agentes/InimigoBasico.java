package agentes;

public class InimigoBasico extends AgenteBase {
    int vida;

    public InimigoBasico() {
        time = Time.Inimigo;
    }

    @Override
    protected void setup() {
        rp.adicionarGetter("vida", () -> vida);
        super.setup();
    }
}
