package recursos;

import java.awt.Image;
import java.util.Map;

/**
 * Responsabilidade:
 * - Carregar e gerenciar todas as sprites em escopo global
 * @author ricardo
 */
public class Sprites {

    private static Sprites instancia;

    private Map<String, Image> indice;

    public static void carregar() {
        instancia = new Sprites();
    }

    public static Image get(String nome) {
        return instancia.indice.get(nome);
    }
}
