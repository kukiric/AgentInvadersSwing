package componentes;

import geral.Ator;
import geral.Sprites;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import javax.swing.JPanel;

/**
 * Responsabilidade:
 * - Desenhar o estado atual do jogo
 */
public class CanvasJogo extends JPanel {

    private List<Ator> atores;

    public CanvasJogo() {
        initComponents();
        setBackground(Color.black);
    }

    private void repeteFundo(Image img, Graphics g) {
        int largura = img.getWidth(this);
        int altura = img.getHeight(this);
        for (int x = 0; x < getWidth(); x += largura) {
            for (int y = 0; y < getHeight(); y += altura) {
                g.drawImage(img, x, y, this);
            }
        }
    }

    public void atualizar(List<Ator> atores) {
        this.atores = atores;
        repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        System.out.println("Tela do jogo inicializada em " + getWidth() + "x" + getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        // Desenha o fundo
        Image bg = Sprites.get("Fundo");
        repeteFundo(bg, g);
        // Desenha as naves inimigas
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
