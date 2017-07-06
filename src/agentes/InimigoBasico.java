package agentes;

public class InimigoBasico extends AgenteBase {

    private int xBase;
    private int yBase;
    private double tempo;

    public int vida;

    public InimigoBasico() {
        time = Time.Inimigo;
    }

    @Override
    protected void setup() {
        super.setup();
        rp.adicionarGetter("vida", () -> vida);
        // Escolhe a posição base desejada de acordo com o seu id
        int id = (int)getArguments()[0];
        x = xBase = (id % 5) * 100 + 200;
        y = yBase = (id / 5) * 100 + 50;
    }

    @Override
    public void update(double delta) {
        tempo += delta;
        x = xBase + Math.sin(tempo) * 100;
    }
}
