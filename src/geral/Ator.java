package geral;

import java.io.Serializable;

/**
 * Representa um objeto no canvas
 */
public class Ator implements Serializable {
    public String nomeSprite;
    public Time time;
    public double vida;
    public double angulo;
    public double escala;
    public double x;
    public double y;

    public Ator(String nomeSprite, Time time, double vida, double x, double y, double angulo, double escala) {
        this.nomeSprite = nomeSprite;
        this.angulo = angulo;
        this.escala = escala;
        this.vida = vida;
        this.time = time;
        this.x = x;
        this.y = y;
    }

    public Ator() {
        this("Undefined", Time.Neutro, 1, 0, 0, 0, 1);
    }
}
