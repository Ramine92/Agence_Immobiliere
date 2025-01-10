import java.util.ArrayList;
import java.util.List;

public class AgenceImmobiliere {
    private List<BienImmobilier> biens;
    private List<AgentImmobilier> agents;
    private List<Client> clients; // Liste des clients ajoutée

    public AgenceImmobiliere() {
        this.biens = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.clients = new ArrayList<>(); // Initialisation de la liste des clients
    }

    // Recherche des biens selon plusieurs critères
    public List<BienImmobilier> rechercherBiens(String type, Double surfaceMin, Double surfaceMax, String localisation, Double prixMax) {
        List<BienImmobilier> resultats = new ArrayList<>();
        for (BienImmobilier bien : biens) {
            boolean correspond = true;

            // Vérification du type
            if (type != null && !type.isEmpty() && !bien.getType().equalsIgnoreCase(type)) {
                correspond = false;
            }

            // Vérification de la surface minimale
            if (surfaceMin != null && bien.getSurface() < surfaceMin) {
                correspond = false;
            }

            // Vérification de la localisation
            if (localisation != null && !localisation.isEmpty() && !bien.getLocalisation().equalsIgnoreCase(localisation)) {
                correspond = false;
            }

            // Vérification du prix maximum
            if (prixMax != null && bien.getPrix() > prixMax) {
                correspond = false;
            }

            // Si le bien correspond à tous les critères, on l'ajoute aux résultats
            if (correspond) {
                resultats.add(bien);
            }
        }
        return resultats;
    }



    // Ajouter un client
    public void ajouterClient(Client client) {
        if (client != null) {
            clients.add(client);
            System.out.println("Client ajouté : " + client);
        } else {
            System.out.println("Erreur : Client non valide.");
        }
    }

    // Ajouter un agent immobilier
    public void ajouterAgent(AgentImmobilier agent) {
        agents.add(agent);
        System.out.println("Agent ajouté : " + agent);
    }

    public List<AgentImmobilier> getAgents() {
        return agents;
    }

    // Rechercher un client par son nom
    public Client rechercherClientParNom(String nom) {
        for (Client client : clients) {
            if (client.getNom().equalsIgnoreCase(nom)) {
                return client;
            }
        }
        return null; // Client non trouvé
    }

    // Rechercher un bien par son ID
    public BienImmobilier rechercherBienParId(String id) {
        for (BienImmobilier bien : biens) {
            if (bien.getId().equals(id)) {
                return bien;
            }
        }
        return null;
    }

    // Ajouter un bien immobilier à un agent
    public void ajouterBien(BienImmobilier bien, AgentImmobilier agent) {
        bien.setAgent(agent);  // Associe le bien à un agent
        biens.add(bien);
        agent.ajouterBien(bien); // Ajoute également à la liste des biens gérés par l'agent
    }

    // Vérifie si un bien existe déjà
    private boolean bienExistant(String id) {
        for (BienImmobilier bien : biens) {
            if (bien.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void acheterBien(BienImmobilier bien, Client client) {
        bien.setVendu(true); // Marque le bien comme vendu
        client.ajouterBienAchete(bien); // Ajouter le bien à la liste des biens achetés par le client
    }

    // Dans la classe AgenceImmobiliere
    public List<BienImmobilier> rechercherBiensDisponibles() {
        List<BienImmobilier> disponibles = new ArrayList<>();
        for (BienImmobilier bien : biens) {
            if (!bien.isVendu()) {
                disponibles.add(bien);
            }
        }
        return disponibles;
    }



    // Point d'entrée principal
    public static void main(String[] args) {
        // Création de l'agence
        AgenceImmobiliere agence = new AgenceImmobiliere();

        // Création des agents
        AgentImmobilier agent1 = new AgentImmobilier("Alice", "Dupont", new ArrayList<>());
        AgentImmobilier agent2 = new AgentImmobilier("Bob", "Martin", new ArrayList<>());
        AgentImmobilier agent3 = new AgentImmobilier("Angela", "White", new ArrayList<>());

        // Ajout des agents à l'agence
        agence.ajouterAgent(agent1);
        agence.ajouterAgent(agent2);
        agence.ajouterAgent(agent3);

        // Affichage des agents
        System.out.println("Liste des agents : " + agence.getAgents());
    }
}
