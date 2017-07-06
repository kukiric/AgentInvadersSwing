package geral;

import java.io.Serializable;

/**
 * Representa um objeto no canvas
 */
public class Ator implements Serializable {
    public String nomeSprite;
    public double angulo;
    public double escala;
    public double x;
    public double y;

    public Ator(String nomeSprite, double x, double y, double angulo, double escala) {
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
