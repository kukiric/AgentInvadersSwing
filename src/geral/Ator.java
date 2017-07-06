package geral;

import java.io.Serializable;

/**
 * Representa um objeto no canvas
 */
public class Ator implements Serializable {
    public String nomeSprite;
    public double angulo;
    public double escala;
    public int x;
    public int y;

    public Ator(String nomeSprite, int x, int y, double angulo, double escala) {
        this.nomeSprite = nomeSprite;
        this.angulo = angulo;
        this.escala = escala;
        this.x = x;
        this.y = y;
    }

    public Ator() {
        this("Undefined", 0, 0, 0, 1);
    }
}
