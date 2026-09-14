package gestionenseignants.model;

public class Enseignant {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String specialite;
    private String grade;
    private double salaire;

    // Constructeur vide
    public Enseignant() {}

    // Constructeur sans id (pour l'ajout)
    public Enseignant(String nom, String prenom, String email,
                      String telephone, String specialite, String grade, double salaire) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.specialite = specialite;
        this.grade = grade;
        this.salaire = salaire;
    }

    // Constructeur complet (pour la lecture)
    public Enseignant(int id, String nom, String prenom, String email,
                      String telephone, String specialite, String grade, double salaire) {
        this(nom, prenom, email, telephone, specialite, grade, salaire);
        this.id = id;
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + specialite + ")";
    }
}