package componentes;

import geral.Sprites;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Responsabilidade:
 * - Desenhar o estado atual do jogo
 */
public class CanvasJogo extends JPanel {

    public CanvasJogo() {
        initComponents();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        System.out.println("Tela do jogo inicializada em " + getWidth() + "x" + getHeight());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
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
