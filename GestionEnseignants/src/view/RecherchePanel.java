package gestionenseignants.view;

import gestionenseignants.dao.EnseignantDAO;
import gestionenseignants.model.Enseignant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecherchePanel extends JPanel {

    private final EnseignantDAO dao;
    private JTextField tfRecherche;
    private JTable table;
    private DefaultTableModel tableModel;

    public RecherchePanel(EnseignantDAO dao) {
        this.dao = dao;

        setLayout(new BorderLayout(10, 10));
        setBackground(AppFrame.COULEUR_FOND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titre = new JLabel("Recherche d'enseignants");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(52, 73, 94));

        // Barre de recherche
        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelRecherche.setBackground(Color.WHITE);
        panelRecherche.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        tfRecherche = new JTextField(30);
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(AppFrame.COULEUR_PRIMAIRE);
        btnRechercher.setForeground(Color.WHITE);
        btnRechercher.setFocusPainted(false);

        panelRecherche.add(new JLabel("Mot-clé (nom, prénom, spécialité) :"));
        panelRecherche.add(tfRecherche);
        panelRecherche.add(btnRechercher);

        // En-tête
        JPanel entete = new JPanel(new BorderLayout(0, 10));
        entete.setBackground(AppFrame.COULEUR_FOND);
        entete.add(titre, BorderLayout.NORTH);
        entete.add(panelRecherche, BorderLayout.CENTER);
        add(entete, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Téléphone",
                             "Spécialité", "Grade", "Salaire"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        add(scroll, BorderLayout.CENTER);

        // Évènements
        btnRechercher.addActionListener(e -> rechercher());
        tfRecherche.addActionListener(e -> rechercher());

        // Chargement initial
        rechercher();
    }

    private void rechercher() {
        String motCle = tfRecherche.getText().trim();
        List<Enseignant> resultats = motCle.isEmpty()
                ? dao.listerTous()
                : dao.rechercher(motCle);

        tableModel.setRowCount(0);
        for (Enseignant e : resultats) {
            tableModel.addRow(new Object[]{
                    e.getId(), e.getNom(), e.getPrenom(), e.getEmail(),
                    e.getTelephone(), e.getSpecialite(), e.getGrade(), e.getSalaire()
            });
        }
    }
}