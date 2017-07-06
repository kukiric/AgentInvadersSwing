package geral;

import java.io.Serializable;

/**
 * Representa um objeto no canvas
 */
public class Ator implements Serializable {
    public String nomeSprite;
    public Time time;
    public double angulo;
    public double escala;
    public double x;
    public double y;

    public Ator(String nomeSprite, Time time, double x, double y, double angulo, double escala) {
        this.nomeSprite = nomeSprite;
        this.angulo = angulo;
        this.escala = escala;
        this.x = x;
        this.y = y;
    }

    public Ator() {
        this("Undefined", Time.Neutro, 0, 0, 0, 1);
    }
}
