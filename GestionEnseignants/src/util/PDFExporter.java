package gestionenseignants.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import gestionenseignants.model.Enseignant;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFExporter {

    private static final BaseColor COULEUR_PRIMAIRE  = new BaseColor(41, 128, 185);
    private static final BaseColor COULEUR_ENTETE    = new BaseColor(52, 73, 94);
    private static final BaseColor COULEUR_LIGNE_ALT = new BaseColor(236, 240, 241);

    public static boolean exporter(List<Enseignant> enseignants, String cheminFichier) {
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));
            document.open();

            // Titre
            Font fontTitre = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, COULEUR_PRIMAIRE);
            Paragraph titre = new Paragraph("LISTE DES ENSEIGNANTS", fontTitre);
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setSpacingAfter(5);
            document.add(titre);

            // Date
            Font fontDate = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, BaseColor.GRAY);
            String dateStr = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm").format(new Date());
            Paragraph date = new Paragraph("Généré le " + dateStr, fontDate);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            document.add(date);

            // Statistiques
            document.add(creerBlocStatistiques(enseignants));
            document.add(Chunk.NEWLINE);

            // Tableau
            document.add(creerTableau(enseignants));

            // Pied
            document.add(Chunk.NEWLINE);
            Font fontPied = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY);
            Paragraph pied = new Paragraph(
                    "Document généré automatiquement — Gestion des Enseignants", fontPied);
            pied.setAlignment(Element.ALIGN_CENTER);
            document.add(pied);

            document.close();
            return true;

        } catch (Exception e) {
            System.err.println("Erreur export PDF : " + e.getMessage());
            e.printStackTrace();
            if (document.isOpen()) document.close();
            return false;
        }
    }

    private static Paragraph creerBlocStatistiques(List<Enseignant> liste) {
        int total = liste.size();
        double moyenne = liste.stream().mapToDouble(Enseignant::getSalaire).average().orElse(0);
        double min = liste.stream().mapToDouble(Enseignant::getSalaire).min().orElse(0);
        double max = liste.stream().mapToDouble(Enseignant::getSalaire).max().orElse(0);

        Font fontStat = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.DARK_GRAY);
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

        Paragraph p = new Paragraph();
        p.add(new Chunk("Nombre total : ", fontStat));
        p.add(new Chunk(String.valueOf(total), fontBold));
        p.add(new Chunk("     Salaire moyen : ", fontStat));
        p.add(new Chunk(formatMontant(moyenne), fontBold));
        p.add(new Chunk("     Min : ", fontStat));
        p.add(new Chunk(formatMontant(min), fontBold));
        p.add(new Chunk("     Max : ", fontStat));
        p.add(new Chunk(formatMontant(max), fontBold));
        p.setSpacingAfter(10);
        return p;
    }

    private static PdfPTable creerTableau(List<Enseignant> liste) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{0.5f, 1.2f, 1.2f, 2f, 1.2f, 1.5f, 1.5f, 1f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        String[] entetes = {"ID", "Nom", "Prénom", "Email", "Téléphone",
                            "Spécialité", "Grade", "Salaire"};
        Font fontEntete = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

        for (String entete : entetes) {
            PdfPCell cell = new PdfPCell(new Phrase(entete, fontEntete));
            cell.setBackgroundColor(COULEUR_ENTETE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font fontCell = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
        boolean alt = false;

        for (Enseignant e : liste) {
            BaseColor fond = alt ? COULEUR_LIGNE_ALT : BaseColor.WHITE;
            alt = !alt;

            ajouterCellule(table, String.valueOf(e.getId()), fontCell, fond, Element.ALIGN_CENTER);
            ajouterCellule(table, nullSafe(e.getNom()), fontCell, fond, Element.ALIGN_LEFT);
            ajouterCellule(table, nullSafe(e.getPrenom()), fontCell, fond, Element.ALIGN_LEFT);
            ajouterCellule(table, nullSafe(e.getEmail()), fontCell, fond, Element.ALIGN_LEFT);
            ajouterCellule(table, nullSafe(e.getTelephone()), fontCell, fond, Element.ALIGN_LEFT);
            ajouterCellule(table, nullSafe(e.getSpecialite()), fontCell, fond, Element.ALIGN_LEFT);
            ajouterCellule(table, nullSafe(e.getGrade()), fontCell, fond, Element.ALIGN_LEFT);
            ajouterCellule(table, formatMontant(e.getSalaire()), fontCell, fond, Element.ALIGN_RIGHT);
        }
        return table;
    }

    private static void ajouterCellule(PdfPTable table, String texte, Font font,
                                       BaseColor fond, int alignement) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, font));
        cell.setBackgroundColor(fond);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignement);
        table.addCell(cell);
    }

    private static String nullSafe(String s) { return s == null ? "" : s; }

    private static String formatMontant(double montant) {
        return String.format("%,.0f Ar", montant);
    }
}