package gestionenseignants.controller;

import gestionenseignants.dao.EnseignantDAO;
import gestionenseignants.model.Enseignant;
import gestionenseignants.util.Validation;
import gestionenseignants.view.EnseignantPanel;

import javax.swing.*;
import java.util.List;

public class EnseignantController {

    private final EnseignantPanel vue;
    private final EnseignantDAO dao;

    public EnseignantController(EnseignantPanel vue, EnseignantDAO dao) {
        this.vue = vue;
        this.dao = dao;
    }

    public void ajouter(Enseignant e) {
        String erreur = Validation.validerEnseignant(e);
        if (erreur != null) {
            JOptionPane.showMessageDialog(vue, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dao.ajouter(e)) {
            JOptionPane.showMessageDialog(vue, "Enseignant ajouté !");
            vue.chargerTous();
            vue.viderFormulaire();
        }
    }

    public void modifier(Enseignant e) {
        // ...
    }

    public void supprimer(int id) {
        // ...
    }

    public List<Enseignant> rechercher(String motCle) {
        return motCle.isEmpty() ? dao.listerTous() : dao.rechercher(motCle);
    }

    public List<Enseignant> listerTous() {
        return dao.listerTous();
    }
}