package geral;

import java.io.Serializable;

/**
 * Representa um objeto no canvas
 */
public class Ator implements Serializable {
    public final String nomeSprite;
    public final Time time;
    public final double tamanho;
    public final double vida;
    public final double angulo;
    public final double escalaSprite;
    public final double x;
    public final double y;

    public Ator(String nomeSprite, Time time, double vida, double x, double y, double angulo, double escalaSprite, double tamanho) {
        this.escalaSprite = escalaSprite;
        this.nomeSprite = nomeSprite;
        this.tamanho = tamanho;
        this.angulo = angulo;
        this.vida = vida;
        this.time = time;
        this.x = x;
        this.y = y;
    }
}
