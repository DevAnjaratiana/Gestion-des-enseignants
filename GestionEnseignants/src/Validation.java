package gestionenseignants.util;

import gestionenseignants.model.Enseignant;

public class Validation {

    // Regex
    private static final String EMAIL_REGEX = "^[\\w.+-]+@[\\w-]+\\.[\\w.]{2,}$";
    private static final String TEL_REGEX   = "^[0-9]{8,15}$";

    // Constantes métier
    public static final double SALAIRE_MINIMUM = 250000;

    public static boolean estVide(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean emailValide(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean telephoneValide(String tel) {
        return tel != null && tel.matches(TEL_REGEX);
    }

    /**
     * Valide un enseignant.
     * @return null si tout est OK, sinon le message d'erreur.
     */
    public static String validerEnseignant(Enseignant e) {
        if (estVide(e.getNom()))         return "Le nom est obligatoire.";
        if (estVide(e.getPrenom()))      return "Le prénom est obligatoire.";
        if (estVide(e.getEmail()))       return "L'email est obligatoire.";
        if (!emailValide(e.getEmail()))  return "Format d'email invalide.";

        if (!estVide(e.getTelephone()) && !telephoneValide(e.getTelephone()))
            return "Le téléphone doit contenir 8 à 15 chiffres.";

        if (e.getSalaire() < SALAIRE_MINIMUM)
            return "Le salaire doit être au minimum de "
                    + String.format("%,.0f", SALAIRE_MINIMUM) + " Ar.";

        return null;
    }
}