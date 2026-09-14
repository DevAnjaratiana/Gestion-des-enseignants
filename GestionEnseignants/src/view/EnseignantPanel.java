package gestionenseignants.view;

import gestionenseignants.dao.EnseignantDAO;
import gestionenseignants.model.Enseignant;
import gestionenseignants.util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EnseignantPanel extends JPanel {

    private final EnseignantDAO dao;

    // Formulaire
    private JTextField tfNom, tfPrenom, tfEmail, tfTelephone, tfSpecialite, tfSalaire;
    private JComboBox<String> cbGrade;

    // Tableau
    private JTable table;
    private DefaultTableModel tableModel;

    public EnseignantPanel(EnseignantDAO dao) {
        this.dao = dao;

        setLayout(new BorderLayout(10, 10));
        setBackground(AppFrame.COULEUR_FOND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(creerEntete(), BorderLayout.NORTH);
        add(creerTableau(), BorderLayout.CENTER);

        chargerTous();
    }

    // ================= ENTÊTE =================
    private JPanel creerEntete() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppFrame.COULEUR_FOND);

        JLabel titre = new JLabel("Gestion des enseignants");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(52, 73, 94));
        titre.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(titre, BorderLayout.NORTH);

        panel.add(creerFormulaire(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel creerFormulaire() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNom        = new JTextField(15);
        tfPrenom     = new JTextField(15);
        tfEmail      = new JTextField(15);
        tfTelephone  = new JTextField(15);
        tfSpecialite = new JTextField(15);
        cbGrade      = new JComboBox<>(new String[]{"Assistant", "Maître-assistant", "Professeur"});
        tfSalaire    = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nom * :"), gbc);
        gbc.gridx = 1;                 panel.add(tfNom, gbc);
        gbc.gridx = 2;                 panel.add(new JLabel("Spécialité :"), gbc);
        gbc.gridx = 3;                 panel.add(tfSpecialite, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Prénom * :"), gbc);
        gbc.gridx = 1;                 panel.add(tfPrenom, gbc);
        gbc.gridx = 2;                 panel.add(new JLabel("Grade :"), gbc);
        gbc.gridx = 3;                 panel.add(cbGrade, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email * :"), gbc);
        gbc.gridx = 1;                 panel.add(tfEmail, gbc);
        gbc.gridx = 2;                 panel.add(new JLabel("Salaire :"), gbc);
        gbc.gridx = 3;                 panel.add(tfSalaire, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1;                 panel.add(tfTelephone, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBoutons.setBackground(Color.WHITE);

        JButton btnAjouter   = creerBouton("Ajouter",   new Color(46, 204, 113));
        JButton btnModifier  = creerBouton("Modifier",  new Color(52, 152, 219));
        JButton btnSupprimer = creerBouton("Supprimer", new Color(231, 76, 60));
        JButton btnVider     = creerBouton("Vider",     new Color(149, 165, 166));

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        panel.add(panelBoutons, gbc);

        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnVider.addActionListener(e -> viderFormulaire());

        return panel;
    }

    // ================= TABLEAU =================
    private JPanel creerTableau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppFrame.COULEUR_FOND);

        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Téléphone",
                             "Spécialité", "Grade", "Salaire"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    remplirFormulaire(row);
                    if (e.getClickCount() == 2) modifier();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ================= CRUD =================
    private void ajouter() {
        Enseignant e = getEnseignantFromForm();
        String erreur = Validation.validerEnseignant(e);
        if (erreur != null) {
            JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dao.ajouter(e)) {
            JOptionPane.showMessageDialog(this, "Enseignant ajouté !");
            viderFormulaire();
            chargerTous();
        }
    }

    private void modifier() {
        int id = getIdSelectionne();
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un enseignant.",
                    "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Enseignant e = getEnseignantFromForm();
        e.setId(id);
        String erreur = Validation.validerEnseignant(e);
        if (erreur != null) {
            JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dao.modifier(e)) {
            JOptionPane.showMessageDialog(this, "Enseignant modifié !");
            viderFormulaire();
            chargerTous();
        }
    }

    private void supprimer() {
        int id = getIdSelectionne();
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un enseignant.",
                    "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirmer la suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && dao.supprimer(id)) {
            JOptionPane.showMessageDialog(this, "Enseignant supprimé !");
            viderFormulaire();
            chargerTous();
        }
    }

    // ================= UTILITAIRES =================
    public void chargerTous() {
        tableModel.setRowCount(0);
        List<Enseignant> liste = dao.listerTous();
        for (Enseignant e : liste) {
            tableModel.addRow(new Object[]{
                    e.getId(), e.getNom(), e.getPrenom(), e.getEmail(),
                    e.getTelephone(), e.getSpecialite(), e.getGrade(), e.getSalaire()
            });
        }
    }

    public void viderFormulaire() {
        tfNom.setText("");
        tfPrenom.setText("");
        tfEmail.setText("");
        tfTelephone.setText("");
        tfSpecialite.setText("");
        cbGrade.setSelectedIndex(0);
        tfSalaire.setText("");
        table.clearSelection();
    }

    private void remplirFormulaire(int row) {
        tfNom.setText((String) tableModel.getValueAt(row, 1));
        tfPrenom.setText((String) tableModel.getValueAt(row, 2));
        tfEmail.setText((String) tableModel.getValueAt(row, 3));
        tfTelephone.setText((String) tableModel.getValueAt(row, 4));
        tfSpecialite.setText((String) tableModel.getValueAt(row, 5));
        cbGrade.setSelectedItem(tableModel.getValueAt(row, 6));
        tfSalaire.setText(String.valueOf(tableModel.getValueAt(row, 7)));
    }

    private Enseignant getEnseignantFromForm() {
        Enseignant e = new Enseignant();
        e.setNom(tfNom.getText().trim());
        e.setPrenom(tfPrenom.getText().trim());
        e.setEmail(tfEmail.getText().trim());
        e.setTelephone(tfTelephone.getText().trim());
        e.setSpecialite(tfSpecialite.getText().trim());
        e.setGrade((String) cbGrade.getSelectedItem());
        try {
            e.setSalaire(Double.parseDouble(tfSalaire.getText().trim().isEmpty()
                    ? "0" : tfSalaire.getText().trim()));
        } catch (NumberFormatException ex) {
            e.setSalaire(-1);
        }
        return e;
    }

    private int getIdSelectionne() {
        int row = table.getSelectedRow();
        if (row == -1) return -1;
        return Integer.parseInt(tableModel.getValueAt(row, 0).toString());
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}