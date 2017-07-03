package agentes;

public class NaveJogador extends AgenteBase {
    int vida;

    public NaveJogador() {
        time = Time.Jogador;
    }

    protected void setup() {
        rp.adicionarGetter("vida", () -> vida);
        super.setup();
    }
}
