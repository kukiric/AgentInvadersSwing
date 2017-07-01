package componentes;

import geral.JadeHelper;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import geral.Sprites;

/**
 * Responsabilidades:
 * - Criar a janela onde o jogo será desenhado
 * - Passar todos os eventos para o jogo
 * - Desenhar elementos adicionais sobre o canvas
 * @author ricardo
 */
public class TelaPrincipal extends JFrame {

    JadeHelper jade;

    public TelaPrincipal() {
        // Pré-carrega todas as sprites
        Sprites.carregarTudo();

        // Inicializa a interface
        initComponents();

        // Conecta os eventos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                confirmarSair();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                teclaPressionada(ke.getKeyCode(), ke.getModifiers());
            }
            @Override
            public void keyReleased(KeyEvent ke) {
                teclaSoltada(ke.getKeyCode(), ke.getModifiers());
            }
        });

        // Inicializa o JADE
        jade = new JadeHelper();
    }

    private void teclaPressionada(int codigo, int mod) {
        switch (codigo) {
            case KeyEvent.VK_ESCAPE:
                confirmarSair();
                break;
            case KeyEvent.VK_R:
                // Cria um novo RMA se ele não existe
                if (jade.getAgenteLocal("rma") == null) {
                    jade.criaAgente("rma", "jade.tools.rma.rma");
                }
        }
    }

    private void teclaSoltada(int codigo, int mod) {
        switch (codigo) {
        }
    }

    private void confirmarSair() {
        int code = JOptionPane.showConfirmDialog(rootPane, "Deseja mesmo encerrar a simulação?", "Sair", JOptionPane.YES_NO_OPTION);
        if (code == JOptionPane.YES_OPTION) {
            setVisible(false);
            System.exit(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painel = new javax.swing.JPanel();
        labelCanto = new javax.swing.JLabel();
        canvas = new componentes.CanvasJogo();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("AgentInvaders");
        setResizable(false);
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        painel.setOpaque(false);

        labelCanto.setText("<html>\n<b>R: </b>Abrir RMA\n<br>\n<b>Esc: </b>Sair\n</html>");

        javax.swing.GroupLayout painelLayout = new javax.swing.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelLayout.createSequentialGroup()
                .addContainerGap(543, Short.MAX_VALUE)
                .addComponent(labelCanto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        painelLayout.setVerticalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelLayout.createSequentialGroup()
                .addContainerGap(491, Short.MAX_VALUE)
                .addComponent(labelCanto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(painel);

        canvas.setPreferredSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 615, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );

        getContentPane().add(canvas);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Configura o estilo nativo do sistema */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setLocationRelativeTo(null);
            tela.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.CanvasJogo canvas;
    private javax.swing.JLabel labelCanto;
    private javax.swing.JPanel painel;
    // End of variables declaration//GEN-END:variables
}
