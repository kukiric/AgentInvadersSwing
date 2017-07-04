package geral;

import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 * Responsabilidade:
 * - Encapsular os métodos da biblioteca JADE e tratar as excessões diretamente de forma que faça sentido no escopo da aplicação
 */
public class JadeHelper {

    private static JadeHelper instancia;
    private AgentContainer ac;

    private JadeHelper() {
        // Cria a instância do JADE
        Runtime rt = Runtime.instance();
        rt.setCloseVM(true);
        ProfileImpl p = new ProfileImpl("localhost", 0, null);
        ac = rt.createMainContainer(p);
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

    public AgentController criaAgente(String nome, String classe, Object[] args) {
        AgentController agente = null;
        try {
            agente = ac.createNewAgent(nome, classe, args);
            agente.start();
        }
        catch (StaleProxyException e) {
            System.err.println("Erro na criação do agente " + nome + ": " + e.getMessage());
            System.exit(1);
        }
        return agente;
    }

    public AgentController criaAgente(String nome, String classe) {
        return criaAgente(nome, classe, new Object[] {});
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

    public void registrarServico(Agent agente, String protocolo) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agente.getAID());
        dfd.addProtocols(protocolo);
        try {
            DFService.register(agente, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
