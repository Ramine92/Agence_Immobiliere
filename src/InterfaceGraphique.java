import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class InterfaceGraphique {
    private AgenceImmobiliere agence;
    private JFrame frame;
    private JPanel panel;
    private JButton ajouterBienButton;
    private JButton ajouterClientButton;
    private JButton rechercherBiensButton;
    private JButton acheterBienButton;
    private JButton afficherBiensDisponiblesButton;
    private JTextArea outputArea;

    public InterfaceGraphique(AgenceImmobiliere agence) {
        this.agence = agence;
        this.frame = new JFrame("Interface Graphique Agence Immobilière");
        this.panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Initialisation des composants
        ajouterBienButton = new JButton("Ajouter Bien");
        ajouterClientButton = new JButton("Ajouter Client");
        rechercherBiensButton = new JButton("Rechercher Biens");
        acheterBienButton = new JButton("Acheter Bien");
        afficherBiensDisponiblesButton = new JButton("Afficher Biens Disponibles");
        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);

        // Ajouter les boutons au panneau
        panel.add(ajouterBienButton);
        panel.add(ajouterClientButton);
        panel.add(rechercherBiensButton);
        panel.add(acheterBienButton);
        panel.add(afficherBiensDisponiblesButton);
        panel.add(new JScrollPane(outputArea));

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);

        // Actions des boutons
        ajouterBienButton.addActionListener(e -> ajouterBien());
        ajouterClientButton.addActionListener(e -> ajouterClient());
        acheterBienButton.addActionListener(e -> acheterBien());
        afficherBiensDisponiblesButton.addActionListener(e -> afficherBiensDisponibles());
        rechercherBiensButton.addActionListener(e -> rechercherBiens());
    }
    private double promptForDouble(String message) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(frame, message);
                if (input == null) return 0; // Annuler si l'utilisateur ferme la boîte de dialogue
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide.");
            }
        }
    }

    private void ajouterBien() {
        try {
            String idBien = JOptionPane.showInputDialog(frame, "Entrez l'ID du Bien :");
            if (idBien == null || idBien.isEmpty()) {
                outputArea.append("ID du bien est requis.\n");
                return;
            }

            String typeBien = JOptionPane.showInputDialog(frame, "Entrez le type de Bien :");
            if (typeBien == null || typeBien.isEmpty()) {
                outputArea.append("Le type du bien est requis.\n");
                return;
            }

            double surface = promptForDouble("Entrez la surface (en m²) :");
            if (surface <= 0) {
                outputArea.append("Surface invalide.\n");
                return;
            }

            double prix = promptForDouble("Entrez le prix :");
            if (prix <= 0) {
                outputArea.append("Prix invalide.\n");
                return;
            }

            String localisation = JOptionPane.showInputDialog(frame, "Entrez la localisation :");
            if (localisation == null || localisation.isEmpty()) {
                outputArea.append("La localisation est requise.\n");
                return;
            }

            String description = JOptionPane.showInputDialog(frame, "Entrez la description du bien :");

            BienImmobilier bien = new BienImmobilier(idBien, typeBien, surface, prix, localisation, description);

            // Demander à l'utilisateur de choisir un agent via un choix prédéfini
            AgentImmobilier agent = choisirAgent();
            if (agent == null) {
                outputArea.append("Agent non sélectionné.\n");
                return;
            }

            bien.setAgent(agent); // Associer l'agent au bien

            agence.ajouterBien(bien, agent);
            outputArea.append("Bien ajouté : " + bien.getDescription() + "\n");
        } catch (Exception ex) {
            outputArea.append("Erreur lors de l'ajout du bien : " + ex.getMessage() + "\n");
        }
    }


    private void ajouterClient() {
        String nom = JOptionPane.showInputDialog(frame, "Entrez le nom du client :");
        String prenom = JOptionPane.showInputDialog(frame, "Entrez le prénom du client :");
        String email = JOptionPane.showInputDialog(frame, "Entrez l'email du client :");
        String telephone = JOptionPane.showInputDialog(frame, "Entrez le numéro de téléphone du client :");

        if (isValidClientInput(nom, prenom, email, telephone)) {
            Client client = new Client(nom, prenom, email, telephone);
            agence.ajouterClient(client);
            outputArea.append("Client ajouté : " + client.getNom() + " " + client.getPrenom() + "\n");
        } else {
            outputArea.append("Informations client manquantes ou ajout annulé.\n");
        }
    }

    private boolean isValidClientInput(String nom, String prenom, String email, String telephone) {
        return nom != null && !nom.isEmpty() &&
                prenom != null && !prenom.isEmpty() &&
                email != null && !email.isEmpty() &&
                telephone != null && !telephone.isEmpty();
    }

    private void acheterBien() {
        String idBien = JOptionPane.showInputDialog(frame, "Entrez l'ID du Bien à acheter :");
        BienImmobilier bien = agence.rechercherBienParId(idBien);

        if (bien == null) {
            outputArea.append("Aucun bien trouvé avec l'ID : " + idBien + "\n");
            return;
        }

        if (bien.isVendu()) {
            outputArea.append("Le bien avec l'ID " + idBien + " est déjà vendu.\n");
            return;
        }

        String nomClient = JOptionPane.showInputDialog(frame, "Entrez le nom du client :");
        Client client = agence.rechercherClientParNom(nomClient);

        if (client == null) {
            outputArea.append("Aucun client trouvé avec le nom : " + nomClient + "\n");
            return;
        }

        agence.acheterBien(bien, client);

        // Afficher le nom de l'agent responsable
        String nomAgent = bien.getAgent() != null ? bien.getAgent().getNom() : "Inconnu";
        outputArea.append("Le bien avec l'ID " + idBien + " a été vendu à " + nomClient + " par l'agent " + nomAgent + ".\n");
    }


    private void afficherBiensDisponibles() {
        List<BienImmobilier> biensDisponibles = agence.rechercherBiensDisponibles();
        outputArea.setText("");
        if (biensDisponibles.isEmpty()) {
            outputArea.append("Aucun bien disponible.\n");
        } else {
            for (BienImmobilier bien : biensDisponibles) {
                outputArea.append("ID : " + bien.getId() + "\n");
                outputArea.append("Type : " + bien.getType() + "\n");
                outputArea.append("Superficie : " + bien.getSurface() + " m²\n");
                outputArea.append("Prix : " + bien.getPrix() + " DA\n");
                outputArea.append("Localisation : " + bien.getLocalisation() + "\n");
                outputArea.append("Description : " + bien.getDescription() + "\n");
                outputArea.append("------\n");
            }
        }
    }

    private void rechercherBiens() {
        // Demander à l'utilisateur les critères de recherche
        String type = JOptionPane.showInputDialog(frame, "Entrez le type du bien (ex: Maison, Appartement) :");
        String surfaceMinStr = JOptionPane.showInputDialog(frame, "Surface minimale (m²) :");
        Double surfaceMin = (surfaceMinStr != null && !surfaceMinStr.isEmpty()) ? Double.parseDouble(surfaceMinStr) : null;

        String localisation = JOptionPane.showInputDialog(frame, "Entrez la localisation du bien (ex: Algiers) :");

        String prixMaxStr = JOptionPane.showInputDialog(frame, "Prix maximal :");
        Double prixMax = (prixMaxStr != null && !prixMaxStr.isEmpty()) ? Double.parseDouble(prixMaxStr) : null;

        // Appeler la méthode de recherche
        List<BienImmobilier> resultats = agence.rechercherBiens(type, surfaceMin, null, localisation, prixMax);

        // Afficher les résultats
        if (resultats.isEmpty()) {
            outputArea.append("Aucun bien ne correspond aux critères.\n");
        } else {
            outputArea.append("Résultats de la recherche :\n");
            for (BienImmobilier bien : resultats) {
                outputArea.append(bien.getDescriptionComplete() + "\n\n");
            }
        }
    }




    private AgentImmobilier choisirAgent() {
        String[] options = {"Alice Dupont", "Bob Martin", "Vincent Van Gogh"};
        int choix = JOptionPane.showOptionDialog(frame, "Choisissez un agent :", "Sélection d'Agent",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choix) {
            case 0: return new AgentImmobilier("Alice", "Dupont", new ArrayList<>());
            case 1: return new AgentImmobilier("Bob", "Martin", new ArrayList<>());
            case 2: return new AgentImmobilier("Vincent", "Van Gogh", new ArrayList<>());
            default: return null;
        }
    }

    public static void main(String[] args) {
        AgenceImmobiliere agence = new AgenceImmobiliere();
        new InterfaceGraphique(agence);
    }
}
