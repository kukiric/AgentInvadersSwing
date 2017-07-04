package geral;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Responsabilidade:
 * - Carregar e gerenciar todas as sprites em escopo global
 */
public class Sprites {

    private static Sprites instancia;

    private Map<String, Image> indice;

    private Image arquivo(String caminho) {
        File arquivo = new File(caminho);
        try {
            return ImageIO.read(arquivo);
        }
        catch (IOException ex) {
            System.err.println("Não foi possível carregar o arquivo: " + arquivo.getAbsolutePath());
            System.exit(1);
            return null;
        }
    }
    
    public Sprites() {
        indice = new HashMap<>();
        indice.put("NaveJogador", arquivo("Assets/Sprites/playerShip3_blue.png"));
        indice.put("InimigoBasico", arquivo("Assets/Sprites/Ships/spaceShips_001.png"));
        indice.put("NaveCuradoura", arquivo("Assets/Sprites/ufoGreen.png"));
        indice.put("ProjetilBasico_Jogador", arquivo("Assets/Sprites/Lasers/laserBlue07.png"));
        indice.put("ProjetilBasico_Inimigo", arquivo("Assets/Sprites/Lasers/laserRed07.png"));
    }

    public static void carregarTudo() {
        if (instancia == null) {
            instancia = new Sprites();
        }
    }

    public static Image get(String nome) {
        return instancia.indice.get(nome);
    }
}
