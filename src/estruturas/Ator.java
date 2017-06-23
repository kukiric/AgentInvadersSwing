package estruturas;

/**
 * Representa um agente físico qualquer no sistema, como a nave do jogador ou um inimigo
 * @author ricardo
 */
public class Ator {
    
    public enum Tipo {
        Jogador,
        Inimigo,
        Healer
    }

    private final Tipo tipo;
    public int x, y;

    public Ator(Tipo tipo) {
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }
}
