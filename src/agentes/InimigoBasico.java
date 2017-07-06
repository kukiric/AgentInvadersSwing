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
        int id = (int)getArguments()[0];
        // Escolhe a posição desejada de acordo com seu id
        x = 40;
        y = 40;
    }
}
