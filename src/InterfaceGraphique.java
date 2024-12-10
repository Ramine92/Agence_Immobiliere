import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InterfaceGraphique {
    private AgenceImmobiliere agence;
    private JFrame frame;
    private JPanel panel;
    private JButton ajouterBienButton;
    private JButton ajouterAgentButton;
    private JButton ajouterClientButton;
    private JButton rechercherBiensButton;
    private JButton acheterBienButton;
    private JTextArea outputArea;

    public InterfaceGraphique(AgenceImmobiliere agence) {
        this.agence = agence;
        this.frame = new JFrame("Interface Graphique Agence Immobilière");
        this.panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Initialisation des composants
        this.ajouterBienButton = new JButton("Ajouter Bien");
        this.ajouterAgentButton = new JButton("Ajouter Agent");
        this.ajouterClientButton = new JButton("Ajouter Client");
        this.rechercherBiensButton = new JButton("Rechercher Biens");
        this.acheterBienButton = new JButton("Acheter Bien");
        this.outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);

        // Ajout des boutons à l'interface
        panel.add(ajouterBienButton);
        panel.add(ajouterAgentButton);
        panel.add(ajouterClientButton);
        panel.add(rechercherBiensButton);
        panel.add(acheterBienButton);
        panel.add(new JScrollPane(outputArea));

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);

        // Action pour ajouter un bien
        ajouterBienButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Demander à l'utilisateur les informations pour le bien
                String idBien = JOptionPane.showInputDialog(frame, "Entrez l'ID du Bien :");
                String typeBien = JOptionPane.showInputDialog(frame, "Entrez le type de Bien :");
                double surface = Double.parseDouble(JOptionPane.showInputDialog(frame, "Entrez la surface (en m²) :"));
                double prix = Double.parseDouble(JOptionPane.showInputDialog(frame, "Entrez le prix :"));
                String localisation = JOptionPane.showInputDialog(frame, "Entrez la localisation :");
                String description = JOptionPane.showInputDialog(frame, "Entrez la description du bien :");

                // Ajouter le bien
                BienImmobilier bien = new BienImmobilier(idBien, typeBien, surface, prix, localisation, description);
                // Par défaut, ajouter un agent existant ou un agent pré-créé pour l'exemple
                AgentImmobilier agent = agence.getAgents().isEmpty()
                        ? new AgentImmobilier("Nom", "Prénom", new ArrayList<>())
                        : agence.getAgents().get(0);
                agence.ajouterBien(bien, agent);
                outputArea.append("Bien ajouté : " + bien.getDescription() + "\n");
            }
        });

        // Action pour ajouter un agent
        ajouterAgentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Demander à l'utilisateur les informations pour l'agent
                String nomAgent = JOptionPane.showInputDialog(frame, "Entrez le nom de l'agent :");
                String prenomAgent = JOptionPane.showInputDialog(frame, "Entrez le prénom de l'agent :");

                // Ajouter l'agent
                AgentImmobilier agent = new AgentImmobilier(nomAgent, prenomAgent, new ArrayList<>());
                agence.ajouterAgent(agent);
                outputArea.append("Agent ajouté : " + agent + "\n");
            }
        });

        // Action pour ajouter un client
        ajouterClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Demander à l'utilisateur les informations pour le client
                String nomClient = JOptionPane.showInputDialog(frame, "Entrez le nom du client :");
                String prenomClient = JOptionPane.showInputDialog(frame, "Entrez le prénom du client :");
                String emailClient = JOptionPane.showInputDialog(frame, "Entrez l'email du client :");
                String telephoneClient = JOptionPane.showInputDialog(frame, "Entrez le telephone :");
                // Ajouter le client
                Client client = new Client(nomClient, prenomClient, emailClient,telephoneClient);
                agence.ajouterClient(client);
                outputArea.append("Client ajouté : " + client + "\n");
            }
        });

        // Action pour rechercher un bien
        // Action pour rechercher un bien
        rechercherBiensButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Demander les critères de recherche
                String typeBien = JOptionPane.showInputDialog(frame, "Entrez le type de bien à rechercher :");
                String localisation = JOptionPane.showInputDialog(frame, "Entrez la localisation à rechercher :");

                // Demander le prix maximum
                String prixMaxStr = JOptionPane.showInputDialog(frame, "Entrez le prix maximum :");
                Double prixMax = null;
                if (prixMaxStr != null && !prixMaxStr.isEmpty()) {
                    try {
                        prixMax = Double.parseDouble(prixMaxStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Veuillez entrer un prix valide.");
                        return;
                    }
                }

                // Recherche des biens
                List<BienImmobilier> biens = agence.rechercherBiens(typeBien, null, null, localisation, prixMax);
                outputArea.setText("");  // Vider la zone de texte avant d'afficher les résultats

                if (biens.isEmpty()) {
                    outputArea.append("Aucun bien trouvé.\n");
                } else {
                    for (BienImmobilier bien : biens) {
                        // Afficher les informations détaillées du bien
                        outputArea.append("ID : " + bien.getId() + "\n");
                        outputArea.append("Type : " + bien.getType() + "\n");
                        outputArea.append("Superficie : " + bien.getSurface() + " m²\n");
                        outputArea.append("Prix : " + bien.getPrix() + " EUR\n");
                        outputArea.append("Localisation : " + bien.getLocalisation() + "\n");
                        outputArea.append("Description : " + bien.getDescription() + "\n");
                        outputArea.append("Vendu : " + (bien.isVendu() ? "Oui" : "Non") + "\n");
                        outputArea.append("------\n"); // Séparateur entre les biens
                    }
                }
            }
        });




        // Action pour acheter un bien
        acheterBienButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Demander à l'utilisateur le client et le bien à acheter
                String clientEmail = JOptionPane.showInputDialog(frame, "Entrez l'email du client :");
                Client client = agence.rechercherClientParEmail(clientEmail);

                if (client != null) {
                    String idBien = JOptionPane.showInputDialog(frame, "Entrez l'ID du bien à acheter :");
                    BienImmobilier bien = agence.rechercherBienParId(idBien);

                    if (bien != null && !bien.isVendu()) {
                        // Acheter le bien
                        bien.setVendu(true);
                        client.acheterBien(bien);
                        outputArea.append("Client " + client.getNom() + " " + client.getPrenom() + " a acheté le bien : " + bien.getDescription() + "\n");
                    } else {
                        outputArea.append("Le bien n'est pas disponible ou déjà vendu.\n");
                    }
                } else {
                    outputArea.append("Client non trouvé.\n");
                }
            }
        });
    }

    public static void main(String[] args) {
        AgenceImmobiliere agence = new AgenceImmobiliere();
        new InterfaceGraphique(agence);
    }
}