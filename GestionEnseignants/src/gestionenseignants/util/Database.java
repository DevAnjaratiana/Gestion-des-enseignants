package gestionenseignants.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestion de la connexion à la base SQLite.
 */
public class Database {

    // Chemin du fichier de base (créé automatiquement dans le dossier du projet)
    private static final String URL = "jdbc:sqlite:enseignants.db";

    /**
     * Retourne une connexion à la base SQLite.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Crée la table "enseignant" si elle n'existe pas encore.
     */
    public static void initDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS enseignant (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                prenom TEXT NOT NULL,
                email TEXT,
                telephone TEXT,
                specialite TEXT,
                grade TEXT,
                salaire REAL
            );
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Base de données initialisée avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur d'initialisation de la base : " + e.getMessage());
        }
    }

    /**
     * Test rapide de connexion (à utiliser une seule fois pour vérifier).
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connexion à SQLite réussie !");
                System.out.println("   Fichier : " + conn.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.err.println("❌ Connexion échouée : " + e.getMessage());
        }
    }
}