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
        imagens.put("NaveCuradora", arquivo("Assets/Sprites/ufoGreen.png"));
        imagens.put("ProjetilBasico_Neutro", arquivo("Assets/Sprites/Lasers/laserGreen13.png"));
        imagens.put("ProjetilBasico_Jogador", arquivo("Assets/Sprites/Lasers/laserBlue07.png"));
        imagens.put("ProjetilBasico_Inimigo", arquivo("Assets/Sprites/Lasers/laserRed07.png"));
        imagens.put("BarraFundo", arquivo("Assets/UI/grey_button02.png"));
        imagens.put("BarraCor_Neutro", arquivo("Assets/UI/green_button01.png"));
        imagens.put("BarraCor_Jogador", arquivo("Assets/UI/blue_button01.png"));
        imagens.put("BarraCor_Inimigo", arquivo("Assets/UI/red_button12.png"));
        imagens.put("Fundo", arquivo("Assets/Sprites/Backgrounds/blue.png"));
    }

    public static void carregarTudo() {
        if (instancia == null) {
            instancia = new Sprites();
        }
    }

    public static Image get(String nome) {
        Image img = instancia.imagens.get(nome);
        if (img == null) {
            System.err.println("Sprite não encontrada: " + nome);
        }
        return img;
    }
}
