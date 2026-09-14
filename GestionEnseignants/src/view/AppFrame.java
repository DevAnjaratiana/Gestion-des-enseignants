package gestionenseignants.view;

import gestionenseignants.dao.EnseignantDAO;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    private final EnseignantDAO dao;
    private final JPanel zoneContenu;
    private final MenuPanel menuPanel;

    // Couleurs du thème
    public static final Color COULEUR_PRIMAIRE = new Color(41, 128, 185);
    public static final Color COULEUR_FOND = new Color(236, 240, 241);
    public static final Color COULEUR_MENU = new Color(52, 73, 94);
    public static final Color COULEUR_MENU_ACTIF = new Color(41, 128, 185);
    public static final Color COULEUR_TEXTE_MENU = new Color(236, 240, 241);

    public AppFrame(EnseignantDAO dao) {
        this.dao = dao;

        setTitle("Gestion des Enseignants");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu latéral gauche
        menuPanel = new MenuPanel(this);
        add(menuPanel, BorderLayout.WEST);

        // Zone de contenu dynamique
        zoneContenu = new JPanel(new BorderLayout());
        zoneContenu.setBackground(COULEUR_FOND);
        add(zoneContenu, BorderLayout.CENTER);

        // Afficher le dashboard au démarrage
        afficherDashboard();

        setVisible(true);
    }

    /** Change le contenu de la zone centrale. */
    public void setContenu(JPanel panel) {
        zoneContenu.removeAll();
        zoneContenu.add(panel, BorderLayout.CENTER);
        zoneContenu.revalidate();
        zoneContenu.repaint();
    }

    // ================= PAGES =================

    public void afficherDashboard() {
        menuPanel.setActif("dashboard");
        setContenu(new DashboardPanel(dao));
    }

    public void afficherEnseignants() {
        menuPanel.setActif("enseignants");
        setContenu(new EnseignantPanel(dao));
    }

    public void afficherRecherche() {
        menuPanel.setActif("recherche");
        setContenu(new RecherchePanel(dao));
    }

    public void afficherExport() {
        menuPanel.setActif("export");
        setContenu(new ExportPanel(dao));
    }
}