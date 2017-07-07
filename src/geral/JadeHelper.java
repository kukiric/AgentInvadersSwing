package geral;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Responsabilidade:
 * - Encapsular os métodos da biblioteca JADE e tratar as excessões diretamente de forma que faça sentido no escopo da aplicação
 */
public class JadeHelper {

    private static ProfileImpl perfil = new ProfileImpl("localhost", 0, null);
    private static JadeHelper instancia;
    private AgentContainer ac;
    private Runtime rt;

    private JadeHelper() {
        // Cria a instância do JADE
        rt = Runtime.instance();
        rt.setCloseVM(true);
        ac = rt.createMainContainer(perfil);
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

    public AgentController criarAgente(String nome, String classe, Object[] args) {
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

    public AgentController criarAgente(String nome, String classe) {
        return criarAgente(nome, classe, new Object[] {});
    }

    public AgentController[] criarAgentes(String templateNome, String classe, int num, Function<Integer, Object[]> provedorArgs) {
        AgentController[] agentes = new AgentController[num];
        for (int i = 0; i < num; i++) {
            agentes[i] = criarAgente(templateNome + ":" + i, classe, provedorArgs.apply(i));
        }
        return agentes;
    }

    public AgentController[] criarAgentes(String templateNome, String classe, int num) {
        return criarAgentes(templateNome, classe, num, (i) -> new Object[] {});
    }

    public AgentController getAgenteLocal(String nome) {
        try {
            return ac.getAgent(nome);
        }
        catch (ControllerException e) {
            return null;
        }
    }

    public static ServiceDescription criarServico(String nome, String protocolo) {
        ServiceDescription svc = new ServiceDescription();
        if (!nome.isEmpty()) {
            svc.setName(nome);
        }
        if (!protocolo.isEmpty()) {
            svc.addProtocols(protocolo);
        }
        return svc;
    }

    public void registrarServico(Agent agente, ServiceDescription[] servicos) {
        DFAgentDescription info = new DFAgentDescription();
        info.setName(agente.getAID());
        for (ServiceDescription svc : servicos) {
            info.addServices(svc);
        }
        try {
            DFService.register(agente, info);
        } catch (FIPAException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<AID> buscarServico(Agent agente, String servico, String protocolo) {
        ArrayList<AID> agentes = new ArrayList<>();
        DFAgentDescription filtro = new DFAgentDescription();
        filtro.addServices(criarServico(servico, protocolo));
        try {
            DFAgentDescription[] provedores = DFService.search(agente, filtro);
            agentes.ensureCapacity(provedores.length);
            for (DFAgentDescription dfd : provedores) {
                agentes.add(dfd.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return agentes;
    }

    public void removerServico(Agent agente) {
        try {
            DFService.deregister(agente);
        } catch (FIPAException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static ACLMessage criaMensagemInscricaoDF(Agent agente, String servico, String protocolo) {
        DFAgentDescription dfd = new DFAgentDescription();
        SearchConstraints sc = new SearchConstraints();
        dfd.addServices(criarServico(servico, protocolo));
        sc.setMaxResults(new Long(100));
        return DFService.createSubscriptionMessage(agente, agente.getDefaultDF(), dfd, sc);
    }
}
