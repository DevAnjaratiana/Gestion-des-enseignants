package gestionenseignants.dao;

import gestionenseignants.model.Enseignant;
import gestionenseignants.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDAO {

    // ================= CREATE =================
    public boolean ajouter(Enseignant e) {
        String sql = """
            INSERT INTO enseignant (nom, prenom, email, telephone, specialite, grade, salaire)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getTelephone());
            ps.setString(5, e.getSpecialite());
            ps.setString(6, e.getGrade());
            ps.setDouble(7, e.getSalaire());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Erreur ajout : " + ex.getMessage());
            return false;
        }
    }

    // ================= READ (tous) =================
    public List<Enseignant> listerTous() {
        List<Enseignant> liste = new ArrayList<>();
        String sql = "SELECT * FROM enseignant ORDER BY nom, prenom;";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lecture : " + ex.getMessage());
        }
        return liste;
    }

    // ================= UPDATE =================
    public boolean modifier(Enseignant e) {
        String sql = """
            UPDATE enseignant
            SET nom = ?, prenom = ?, email = ?, telephone = ?,
                specialite = ?, grade = ?, salaire = ?
            WHERE id = ?;
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getTelephone());
            ps.setString(5, e.getSpecialite());
            ps.setString(6, e.getGrade());
            ps.setDouble(7, e.getSalaire());
            ps.setInt(8, e.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Erreur modification : " + ex.getMessage());
            return false;
        }
    }

    // ================= DELETE =================
    public boolean supprimer(int id) {
        String sql = "DELETE FROM enseignant WHERE id = ?;";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Erreur suppression : " + ex.getMessage());
            return false;
        }
    }

    // ================= RECHERCHE =================
    public List<Enseignant> rechercher(String motCle) {
        List<Enseignant> liste = new ArrayList<>();
        String sql = """
            SELECT * FROM enseignant
            WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? OR LOWER(specialite) LIKE ?
            ORDER BY nom, prenom;
            """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + motCle.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapResultSet(rs));
                }
            }

        } catch (SQLException ex) {
            System.err.println("Erreur recherche : " + ex.getMessage());
        }
        return liste;
    }

    // ================= UTILITAIRE =================
    private Enseignant mapResultSet(ResultSet rs) throws SQLException {
        return new Enseignant(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getString("specialite"),
                rs.getString("grade"),
                rs.getDouble("salaire")
        );
    }
}