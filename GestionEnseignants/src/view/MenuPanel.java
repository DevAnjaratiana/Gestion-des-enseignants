package gestionenseignants.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MenuPanel extends JPanel {

    private final AppFrame app;
    private final Map<String, JButton> boutons = new HashMap<>();
    private String actif = "dashboard";

    public MenuPanel(AppFrame app) {
        this.app = app;

        setPreferredSize(new Dimension(220, 0));
        setBackground(AppFrame.COULEUR_MENU);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 0, 20, 0));

        // Titre de l'app
        JLabel titre = new JLabel("🎓 GESTION");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(new EmptyBorder(0, 0, 5, 0));
        add(titre);

        JLabel sousTitre = new JLabel("Enseignants");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sousTitre.setForeground(new Color(189, 195, 199));
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        sousTitre.setBorder(new EmptyBorder(0, 0, 30, 0));
        add(sousTitre);

        // Boutons du menu
        ajouterBouton("dashboard",   "🏠  Dashboard",   e -> app.afficherDashboard());
        ajouterBouton("enseignants", "👥  Enseignants", e -> app.afficherEnseignants());
        ajouterBouton("recherche",   "🔍  Recherche",   e -> app.afficherRecherche());
        ajouterBouton("export",      "📤  Export PDF",  e -> app.afficherExport());

        // Espace flexible
        add(Box.createVerticalGlue());

        // Bouton Quitter en bas
        JButton btnQuitter = new JButton("🚪  Quitter");
        btnQuitter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuitter.setMaximumSize(new Dimension(220, 45));
        btnQuitter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnQuitter.setForeground(AppFrame.COULEUR_TEXTE_MENU);
        btnQuitter.setBackground(AppFrame.COULEUR_MENU);
        btnQuitter.setBorderPainted(false);
        btnQuitter.setFocusPainted(false);
        btnQuitter.setHorizontalAlignment(SwingConstants.LEFT);
        btnQuitter.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnQuitter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuitter.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(app,
                    "Voulez-vous vraiment quitter ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) System.exit(0);
        });
        add(btnQuitter);

        // Appliquer l'état actif initial
        mettreAJourStyle();
    }

    private void ajouterBouton(String cle, String texte, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texte);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(AppFrame.COULEUR_TEXTE_MENU);
        btn.setBackground(AppFrame.COULEUR_MENU);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        btn.addActionListener(e -> {
            actif = cle;
            mettreAJourStyle();
        });
        boutons.put(cle, btn);
        add(btn);
    }

    public void setActif(String cle) {
        actif = cle;
        mettreAJourStyle();
    }

    private void mettreAJourStyle() {
        for (Map.Entry<String, JButton> entry : boutons.entrySet()) {
            JButton btn = entry.getValue();
            if (entry.getKey().equals(actif)) {
                btn.setBackground(AppFrame.COULEUR_MENU_ACTIF);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            } else {
                btn.setBackground(AppFrame.COULEUR_MENU);
                btn.setForeground(AppFrame.COULEUR_TEXTE_MENU);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        }
    }
}