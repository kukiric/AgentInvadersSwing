package agentes;

import geral.Ambiente;
import geral.JadeHelper;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Responsabilidade:
 * - Criar e gerenciar todos os agentes do sistema
 */
public class AgenteGerenciador extends Agent {

    private Ambiente ambiente;

    @Override
    protected void setup() {
        ambiente = (Ambiente) getArguments()[0];
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // Cria os agentes do jogo
                JadeHelper jade = JadeHelper.instancia();
                jade.criarAgentes("inimigo", "agentes.InimigoBasico", 15, (i) -> new Object[] {ambiente, i});
                jade.criarAgente("jogador", "agentes.NaveJogador", new Object[] {ambiente});
                jade.criarAgente("curadora", "agentes.NaveCuradora", new Object[] {ambiente});
                System.out.println("AgenteGerenciador: Terminada a criação de agentes");
            }
        });
    }
}
