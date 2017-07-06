package geral;

import java.io.Serializable;

/**
 * Representa um objeto no canvas
 */
public class Ator implements Serializable {
    public String nomeSprite;
    public Direcao dir;
    public int x;
    public int y;

    public Ator(String nomeSprite, int x, int y) {
        this.nomeSprite = nomeSprite;
        this.dir = Direcao.Cima;
        this.x = x;
        this.y = y;
    }

    public Ator() {
        this("Undefined", 0, 0);
    }
}