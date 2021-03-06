package geral;

import jade.core.AID;
import java.io.Serializable;

/**
 * Representa um objeto no canvas
 */
public class Ator implements Serializable {
    public final AID agenteDono;
    public final String nomeSprite;
    public final String tipo;
    public final Time time;
    public final double tamanho;
    public final double vida;
    public final double angulo;
    public final double escalaSprite;
    public final double x;
    public final double y;

    public Ator(AID agenteDono, String nomeSprite, String tipo, Time time, double vida, double x, double y, double angulo, double escalaSprite, double tamanho) {
        this.agenteDono = agenteDono;
        this.escalaSprite = escalaSprite;
        this.nomeSprite = nomeSprite;
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.angulo = angulo;
        this.vida = vida;
        this.time = time;
        this.x = x;
        this.y = y;
    }
}
