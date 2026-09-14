
# 🎓 Gestion des Enseignants

Application desktop de gestion des enseignants développée en **Java Swing** avec une base de données **SQLite**.

---

## 📋 Présentation

Ce projet est une application complète de gestion des enseignants d'un établissement scolaire ou universitaire. Elle permet d'ajouter, modifier, supprimer, rechercher et exporter les informations des enseignants via une interface graphique moderne.

L'application suit l'architecture **MVC** (Model - View - Controller) et utilise une base de données **SQLite** pour la persistance des données.

---

## ✨ Fonctionnalités

### Gestion des enseignants (CRUD)
- ➕ **Ajouter** un enseignant
- ✏️ **Modifier** un enseignant existant
- 🗑️ **Supprimer** un enseignant avec confirmation
- 📋 **Afficher** tous les enseignants dans un tableau

### Recherche et affichage
- 🔍 **Recherche** par nom, prénom ou spécialité
- 🔄 **Tri** par colonne (clic sur l'en-tête)
- ⏎ **Recherche rapide** avec la touche Entrée

### Validation des données
- ✅ Champs obligatoires : nom, prénom, email
- 📧 Format d'email vérifié (regex)
- 📞 Format de téléphone vérifié (8 à 15 chiffres)
- 💰 Salaire minimum : **250 000 Ar**

### Tableau de bord
- 📊 Nombre total d'enseignants
- 💵 Salaire moyen, minimum, maximum
- 👥 Répartition par grade (barres)
- 📚 Répartition par spécialité (barres)

### Export
- 📄 **Export PDF** de la liste complète des enseignants
- 📊 Statistiques incluses dans le PDF
- 📁 Choix de l'emplacement du fichier

### Interface
- 🎨 Menu latéral de navigation (comme une application web)
- 🖥️ Design moderne avec thème Nimbus
- 🌈 Couleurs distinctes pour chaque action

---

## 🛠️ Technologies utilisées

| Technologie | Version | Rôle |
|---|---|---|
| **Java** | 17+ | Langage principal |
| **Java Swing** | — | Interface graphique |
| **SQLite** | 3.x | Base de données |
| **JDBC** (sqlite-jdbc) | 3.53.4.0 | Connexion Java ↔ SQLite |
| **iText** | 5.5.13 | Génération PDF |
| **NetBeans** | 17+ | IDE de développement |

### Architecture
- **MVC** (Model - View - Controller)
- **DAO** (Data Access Object) pour l'accès aux données

---

## 📦 Prérequis

Avant de lancer l'application, assure-toi d'avoir :

- ✅ **JDK 17** ou supérieur ([télécharger](https://adoptium.net/))
- ✅ **NetBeans** 17+ (ou tout autre IDE Java)
- ✅ Le fichier **`sqlite-jdbc-3.53.4.0.jar`** (fourni dans `libs/`)
- ✅ Le fichier **`itextpdf-5.5.13.jar`** (fourni dans `libs/`)

---

## ⚙️ Installation

### 1. Extraire le projet

```bash
unzip GestionEnseignants.zip
cd GestionEnseignants