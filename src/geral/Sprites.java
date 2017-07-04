package geral;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Responsabilidade:
 * - Carregar e gerenciar todas as sprites em escopo global
 */
public class Sprites {

    private static Sprites instancia;

    private Map<String, Image> imagens;

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
        imagens = new HashMap<>();
        imagens.put("NaveJogador", arquivo("Assets/Sprites/playerShip3_blue.png"));
        imagens.put("InimigoBasico", arquivo("Assets/Sprites/Ships/spaceShips_001.png"));
        imagens.put("NaveCuradoura", arquivo("Assets/Sprites/ufoGreen.png"));
        imagens.put("ProjetilBasico_Jogador", arquivo("Assets/Sprites/Lasers/laserBlue07.png"));
        imagens.put("ProjetilBasico_Inimigo", arquivo("Assets/Sprites/Lasers/laserRed07.png"));
        imagens.put("Fundo", arquivo("Assets/Sprites/Backgrounds/blue.png"));
    }

    public static void carregarTudo() {
        if (instancia == null) {
            instancia = new Sprites();
        }
    }

    public static Image get(String nome) {
        return instancia.imagens.get(nome);
    }
}
