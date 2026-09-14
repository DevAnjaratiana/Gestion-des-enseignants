package gestionenseignants.view;

import gestionenseignants.dao.EnseignantDAO;
import gestionenseignants.model.Enseignant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {

    private final EnseignantDAO dao;

    public DashboardPanel(EnseignantDAO dao) {
        this.dao = dao;

        setLayout(new BorderLayout());
        setBackground(AppFrame.COULEUR_FOND);

        // Conteneur scrollable vertical
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBorder(new EmptyBorder(20, 20, 20, 20));
        contenu.setBackground(AppFrame.COULEUR_FOND);

        // Titre de la page
        JLabel titre = new JLabel("Tableau de bord");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(52, 73, 94));
        titre.setBorder(new EmptyBorder(0, 0, 20, 0));
        contenu.add(titre);

        // Sections
        contenu.add(creerSectionStatistiques());
        contenu.add(Box.createVerticalStrut(15));
        contenu.add(creerSectionParGrade());
        contenu.add(Box.createVerticalStrut(15));
        contenu.add(creerSectionParSpecialite());

        JScrollPane scroll = new JScrollPane(contenu);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    // ================= SECTION 1 : STATS GÉNÉRALES =================
    private JPanel creerSectionStatistiques() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titre = new JLabel("📊 Statistiques générales");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titre.setForeground(AppFrame.COULEUR_PRIMAIRE);
        titre.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(titre);

        List<Enseignant> liste = dao.listerTous();

        int total = liste.size();
        double salaireMoyen = liste.stream()
                .mapToDouble(Enseignant::getSalaire).average().orElse(0);
        double salaireMin = liste.stream()
                .mapToDouble(Enseignant::getSalaire).min().orElse(0);
        double salaireMax = liste.stream()
                .mapToDouble(Enseignant::getSalaire).max().orElse(0);

        panel.add(creerLigneStat("Nombre d'enseignants", String.valueOf(total)));
        panel.add(creerLigneStat("Salaire moyen", formatMontant(salaireMoyen)));
        panel.add(creerLigneStat("Salaire minimum", formatMontant(salaireMin)));
        panel.add(creerLigneStat("Salaire maximum", formatMontant(salaireMax)));

        return panel;
    }

    // ================= SECTION 2 : PAR GRADE =================
    private JPanel creerSectionParGrade() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titre = new JLabel("👥 Répartition par grade");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titre.setForeground(new Color(155, 89, 182));
        titre.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(titre);

        List<Enseignant> liste = dao.listerTous();
        Map<String, Long> parGrade = liste.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getGrade() == null ? "Non défini" : e.getGrade(),
                        Collectors.counting()
                ));

        int max = parGrade.values().stream().mapToInt(Long::intValue).max().orElse(1);

        for (Map.Entry<String, Long> entry : parGrade.entrySet()) {
            panel.add(creerLigneBarre(entry.getKey(), entry.getValue().intValue(), max,
                    new Color(155, 89, 182)));
        }

        return panel;
    }

    // ================= SECTION 3 : PAR SPÉCIALITÉ =================
    private JPanel creerSectionParSpecialite() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titre = new JLabel("📚 Répartition par spécialité");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titre.setForeground(new Color(46, 204, 113));
        titre.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(titre);

        List<Enseignant> liste = dao.listerTous();
        Map<String, Long> parSpec = liste.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getSpecialite() == null || e.getSpecialite().isEmpty()
                                ? "Non définie" : e.getSpecialite(),
                        Collectors.counting()
                ));

        int max = parSpec.values().stream().mapToInt(Long::intValue).max().orElse(1);

        for (Map.Entry<String, Long> entry : parSpec.entrySet()) {
            panel.add(creerLigneBarre(entry.getKey(), entry.getValue().intValue(), max,
                    new Color(46, 204, 113)));
        }

        return panel;
    }

    // ================= UTILITAIRES =================
    private JPanel creerLigneStat(String label, String valeur) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblGauche = new JLabel(label);
        lblGauche.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblDroite = new JLabel(valeur);
        lblDroite.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDroite.setForeground(new Color(52, 73, 94));

        panel.add(lblGauche, BorderLayout.WEST);
        panel.add(lblDroite, BorderLayout.EAST);
        return panel;
    }

    private JPanel creerLigneBarre(String label, int valeur, int max, Color couleur) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setPreferredSize(new Dimension(150, 20));

        JProgressBar barre = new JProgressBar(0, max);
        barre.setValue(valeur);
        barre.setStringPainted(true);
        barre.setString(String.valueOf(valeur));
        barre.setForeground(couleur);
        barre.setBackground(new Color(230, 230, 230));
        barre.setPreferredSize(new Dimension(200, 20));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(barre, BorderLayout.CENTER);
        return panel;
    }

    private String formatMontant(double montant) {
        return String.format("%,.0f Ar", montant);
    }
}