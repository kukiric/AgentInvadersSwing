package geral;

import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import java.util.stream.Stream;

/**
 * Responsabilidade:
 * - Encapsular os métodos da biblioteca JADE e tratar as excessões diretamente de forma que faça sentido no escopo da aplicação
 * @author Ricardo Maes
 */
public class JadeHelper {

    private static JadeHelper instancia;
    private AgentContainer ac;

    private JadeHelper() {
        // Cria a instância do JADE
        ProfileImpl p = new ProfileImpl("localhost", 0, null);
        ac = jade.core.Runtime.instance().createMainContainer(p);
    }

    public static JadeHelper instancia() {
        if (instancia == null) {
            instancia = new JadeHelper();
        }
        return instancia;
    }

    public AgentContainer getContainer() {
        return ac;
    }

    public AgentController criaAgente(String nome, String classe) {
        AgentController agente = null;
        try {
            agente = ac.createNewAgent(nome, classe, new Object[] {});
            agente.start();
        }
        catch (StaleProxyException e) {
            System.err.println("Erro na criação do agente " + nome + ": " + e.getMessage());
            System.exit(1);
        }
        return agente;
    }

    public AgentController[] criaAgentes(String templateNome, String classe, int num) {
        AgentController[] agentes = new AgentController[num];
        for (int i = 0; i < num; i++) {
            agentes[i] = criaAgente(templateNome + "_" + i, classe);
        }
        return agentes;
    }

    public AgentController getAgenteLocal(String nome) {
        try {
            return ac.getAgent(nome);
        }
        catch (ControllerException e) {
            return null;
        }
    }
}
