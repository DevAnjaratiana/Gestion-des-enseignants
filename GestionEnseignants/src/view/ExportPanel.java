package gestionenseignants.view;

import gestionenseignants.dao.EnseignantDAO;
import gestionenseignants.model.Enseignant;
import gestionenseignants.util.PDFExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ExportPanel extends JPanel {

    private final EnseignantDAO dao;
    private JLabel lblStatut;

    public ExportPanel(EnseignantDAO dao) {
        this.dao = dao;

        setLayout(new BorderLayout());
        setBackground(AppFrame.COULEUR_FOND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ===== Titre =====
        JLabel titre = new JLabel("Export PDF");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(52, 73, 94));
        add(titre, BorderLayout.NORTH);

        // ===== Carte centrale =====
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel description = new JLabel(
                "<html><div style='text-align:center;'>"
                + "Exportez la liste complète des enseignants<br>"
                + "dans un fichier <b>PDF</b> professionnel."
                + "</div></html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(new Color(127, 140, 141));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(description);
        carte.add(Box.createVerticalStrut(25));

        JButton btnExporter = new JButton("📄  Exporter en PDF");
        btnExporter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExporter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExporter.setBackground(AppFrame.COULEUR_PRIMAIRE);
        btnExporter.setForeground(Color.WHITE);
        btnExporter.setFocusPainted(false);
        btnExporter.setPreferredSize(new Dimension(220, 45));
        btnExporter.setMaximumSize(new Dimension(220, 45));
        btnExporter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        carte.add(btnExporter);
        carte.add(Box.createVerticalStrut(15));

        lblStatut = new JLabel(" ");
        lblStatut.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStatut.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatut.setForeground(new Color(127, 140, 141));
        carte.add(lblStatut);

        JPanel conteneur = new JPanel(new GridBagLayout());
        conteneur.setBackground(AppFrame.COULEUR_FOND);
        conteneur.add(carte);
        add(conteneur, BorderLayout.CENTER);

        btnExporter.addActionListener(e -> exporterPDF());
    }

    private void exporterPDF() {
        List<Enseignant> liste = dao.listerTous();

        if (liste.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Aucun enseignant à exporter.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Enregistrer le PDF");
        chooser.setSelectedFile(new File("enseignants.pdf"));

        int choix = chooser.showSaveDialog(this);
        if (choix != JFileChooser.APPROVE_OPTION) {
            lblStatut.setText("Export annulé.");
            return;
        }

        File fichier = chooser.getSelectedFile();
        if (!fichier.getName().toLowerCase().endsWith(".pdf")) {
            fichier = new File(fichier.getAbsolutePath() + ".pdf");
        }

        lblStatut.setText("Génération en cours...");
        lblStatut.setForeground(new Color(52, 152, 219));

        boolean ok = PDFExporter.exporter(liste, fichier.getAbsolutePath());

        if (ok) {
            lblStatut.setText("✅ Export réussi : " + fichier.getName());
            lblStatut.setForeground(new Color(46, 204, 113));

            int rep = JOptionPane.showConfirmDialog(this,
                    "PDF généré avec succès !\n\nVoulez-vous l'ouvrir ?",
                    "Succès", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (rep == JOptionPane.YES_OPTION) {
                ouvrirFichier(fichier);
            }
        } else {
            lblStatut.setText("❌ Échec de l'export.");
            lblStatut.setForeground(new Color(231, 76, 60));
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la génération du PDF.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ouvrirFichier(File fichier) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fichier);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Impossible d'ouvrir le fichier : " + e.getMessage(),
                    "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }
}