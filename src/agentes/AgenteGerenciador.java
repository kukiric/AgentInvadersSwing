package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.StaleProxyException;

/**
 * Responsabilidade:
 * - Criar e gerenciar todos os agentes do sistema
 * @author ricardo
 */
public class AgenteGerenciador extends Agent {
    @Override
    protected void setup() {
       addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // Cria os agentes do jogo
                try {
                    for (int i = 0; i < 30; i++) {
                        getContainerController().createNewAgent("Inimigo " + i, "agents.BasicEnemy", new Object[] {}).start();
                    }
                    getContainerController().createNewAgent("Jogador", "agents.PlayerShip", new Object[] {}).start();
                    getContainerController().createNewAgent("Jogador", "agents.Healer", new Object[] {}).start();
                }
                catch (StaleProxyException e) {
                    System.err.println("Erro na criação dos agentes: " + e.getMessage());
                    System.exit(1);
                }
            }
        });
    }
}
