package gestionenseignants;

import gestionenseignants.dao.EnseignantDAO;
import gestionenseignants.util.Database;
import gestionenseignants.view.AppFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database.initDatabase();

        // Thème Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new AppFrame(new EnseignantDAO()));
    }
}