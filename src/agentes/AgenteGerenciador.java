package agentes;

import geral.JadeHelper;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Responsabilidade:
 * - Criar e gerenciar todos os agentes do sistema
 */
public class AgenteGerenciador extends Agent {
    @Override
    protected void setup() {
       addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // Cria os agentes do jogo
                JadeHelper jade = JadeHelper.instancia();
                jade.criaAgentes("inimigo", "agentes.InimigoBasico", 15, (i) -> new Integer[] {i});
                jade.criaAgente("jogador", "agentes.NaveJogador");
                //jade.criaAgente("curadora", "agentes.NaveCuradora");
                System.out.println("AgenteGerenciador: Terminada a criação de agentes");
            }
        });
    }
}
