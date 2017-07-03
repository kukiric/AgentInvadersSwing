package agentes;

import geral.JadeHelper;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

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
                JadeHelper jade = JadeHelper.instancia();
                jade.criaAgentes("InimigoBasico", "agentes.InimigoBasico", 30);
                jade.criaAgente("NaveJogador", "agentes.NaveJogador");
                jade.criaAgente("NaveCuradoura", "agentes.NaveCuradoura");
                System.out.println("AgenteGerenciador: Terminada a criação de agentes");
                doDelete();
            }
        });
    }
}
