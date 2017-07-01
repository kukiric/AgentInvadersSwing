package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.StaleProxyException;

public class AgenteGerenciador extends Agent {

    @Override
    protected void setup() {
       /* addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // Cria os agentes do jogo
               /* try {
                    for (int i = 0; i < 30; i++) {
                        getContainerController().createNewAgent("Inimigo " + i, "agents.BasicEnemy", new Object[] {}).start();
                    }
                    getContainerController().createNewAgent("Jogador", "agents.PlayerShip", new Object[] {}).start();
                    getContainerController().createNewAgent("Jogador", "agents.Healer", new Object[] {}).start();
                }
                catch (StaleProxyException e) {
                    System.out.println("Erro na criação dos agentes: " + e.getMessage());
                }
            }
        });*/
    }
    
}
