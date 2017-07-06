package componentes;

import geral.Ator;
import geral.PausaGlobal;
import geral.Sprites;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Desenha o estado atual do jogo quando informado
 */
public class CanvasJogo extends JPanel {

    private List<Ator> atores;
    private Font fontePausa;

    public CanvasJogo() {
        super(true);
        initComponents();
        this.setBackground(Color.black);
        this.atores = new ArrayList<>();
        this.fontePausa = new Font(Font.SANS_SERIF, Font.BOLD, 64);
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
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.clearRect(0, 0, getWidth(), getHeight());

        // Desenha o fundo
        {
            Image bg = Sprites.get("Fundo");
            int largura = bg.getWidth(this);
            int altura = bg.getHeight(this);
            for (int x = 0; x < getWidth(); x += largura) {
                for (int y = 0; y < getHeight(); y += altura) {
                    g.drawImage(bg, x, y, this);
                }
            }
        }

        // Desenha todas as sprites
        for (Ator ator : atores) {
            Image img = Sprites.get(ator.nomeSprite);
            int largura = img.getWidth(this);
            int altura = img.getHeight(this);
            AffineTransform at = new AffineTransform();
            at.translate(ator.x, ator.y);
            at.rotate(ator.angulo);
            at.scale(ator.escala, ator.escala);
            g.setTransform(at);
            g.drawImage(img, -largura / 2, -altura / 2, this);
        }

        // Desenha PAUSA no meio da tela
        if (PausaGlobal.pause) {
            g.setTransform(new AffineTransform());
            g.setColor(new Color(0, 0, 0, 0.33f));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(fontePausa);
            g.setColor(Color.white);
            FontMetrics fm = g.getFontMetrics();
            g.drawString("PAUSA", getWidth() / 2 - fm.stringWidth("PAUSA") / 2, getHeight() / 2);
        }
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
