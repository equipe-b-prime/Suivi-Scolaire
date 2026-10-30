# 🎓 Plateforme API — Suivi Scolaire

API REST complète de gestion et de suivi de scolarité développée avec **Spring Boot (Java 21)**, **Spring Data JPA**, **PostgreSQL**, **Flyway** et **SpringDoc OpenAPI (Swagger UI)**.

L'application permet de piloter l'ensemble de la scolarité d'un établissement : gestion des promotions, inscriptions, suivi pédagogique, calcul automatique des moyennes, génération des bulletins trimestriels, gestion des absences et clôture/réouverture sécurisée des périodes d'évaluation.

---

## 📌 Accès Rapide & Liens Swagger

L'API est entièrement déployée en continu dans le cloud sur **Render**, connectée à une base de données cloud **Neon PostgreSQL**, tout en étant immédiatement reproductible et testable en environnement local.

| Environnement | URL de Base | Documentation Interactive Swagger UI |
| :--- | :--- | :--- |
| **🌐 En Ligne (Render + Neon)** | `https://suivi-scolaire-t62q.onrender.com` | [Swagger UI En Ligne](https://suivi-scolaire-t62q.onrender.com/swagger-ui/index.html) |
| **💻 Local (PostgreSQL)** | `http://localhost:8080` | [Swagger UI Local](http://localhost:8080/swagger-ui/index.html) |

---

## 🗄️ Architecture des Bases de Données & Migrations (Flyway)

Le projet sépare et standardise la persistance grâce aux migrations de schéma et de données gérées par **Flyway** :

- **Base de données en ligne** : Hébergée sur **Neon PostgreSQL** (serverless, haute résilience et connectivité SSL).
- **Base de données locale** : PostgreSQL local (`localhost:5432/suivi_scolaire`).
- **Migrations Flyway intégrées** :
  - [`V1.0.1__DDL.sql`](file:///home/yokan/TAF/FORMATION/TESTO/suiviScolaire/src/main/resources/db/migration/V1.0.1__DDL.sql) : Création intégrale du schéma relationnel (19 tables : `annee`, `niveau`, `classe`, `matiere`, `professeur`, `eleve`, `periode`, `type_evaluation`, `coefficient`, `inscription`, `enseignement`, `note`, `salle`, `creneaux`, `cours`, `absence`, `bulettin`, `moyenne_matieres`, `amenagement`), contraintes d'intégrité, clés étrangères et index d'optimisation.
  - [`V1.0.2__DML.sql`](file:///home/yokan/TAF/FORMATION/TESTO/suiviScolaire/src/main/resources/db/migration/V1.0.2__DML.sql) : Initialisation du jeu de données de test et de référence (JDD dynamique avec les élèves `ELV-1` à `ELV-78`, classes, coefficients, inscriptions et notes), ainsi que réalignement des séquences `IDENTITY` (`setval`).

---

## 📬 Pièce Jointe : Collection Postman Complète

Pour simplifier les tests et la recette fonctionnelle, une collection Postman exhaustive est fournie en pièce jointe directement à la racine du projet :

📄 **[`ZKANTAPI.postman_collection.json`](file:///home/yokan/TAF/FORMATION/TESTO/suiviScolaire/ZKANTAPI.postman_collection.json)**

### Comment l'importer et basculer en un clic :
1. Dans **Postman**, cliquez sur le bouton **Import** (en haut à gauche).
2. Glissez-déposez le fichier `ZKANTAPI.postman_collection.json`.
3. Une variable de collection nommée `baseUrl` est pré-configurée. Pour basculer entre vos environnements :
   - **Test En Ligne (Render)** : Réglez `baseUrl` à `https://suivi-scolaire-t62q.onrender.com`
   - **Test En Local** : Réglez `baseUrl` à `http://localhost:8080`
4. La collection regroupe l'ensemble des scénarios découpés par niveaux fonctionnels :
   - **Niveau 1 — CRUD** : Gestion des classes, élèves, matières, professeurs, types d'évaluation.
   - **Niveau 2 — Consultation & Recherche** : Planning des cours, historique des absences, notes par élève, liste des classes triées.
   - **Niveau 3 — Règles Métier** : Clôture et réouverture exceptionnelle des trimestres avec motif, gestion des reports et sanctions d'absences, application des coefficients.
   - **Niveau 4 — Restitution & Décision** : Génération des bulletins de notes, calcul des moyennes générales, classements et statistiques de réussite.

---

## 🧪 Tests & Exemples de Requêtes cURL

Vous pouvez exécuter les commandes cURL ci-dessous indifféremment sur l'environnement en ligne ou en local.

### 1. Consulter la liste des classes
* **En ligne :**
```bash
curl -X GET "https://suivi-scolaire-t62q.onrender.com/classes" \
     -H "Accept: application/json"
```
* **En local :**
```bash
curl -X GET "http://localhost:8080/classes" \
     -H "Accept: application/json"
```

### 2. Consulter la liste des élèves (paginée)
```bash
curl -X GET "https://suivi-scolaire-t62q.onrender.com/eleves?page=0&taille=5" \
     -H "Accept: application/json"
```

### 3. Consulter les matières et leurs coefficients
```bash
curl -X GET "https://suivi-scolaire-t62q.onrender.com/matieres" \
     -H "Accept: application/json"
```

### 4. Consulter l'état des trimestres d'évaluation
```bash
curl -X GET "https://suivi-scolaire-t62q.onrender.com/trimestres" \
     -H "Accept: application/json"
```

### 5. Consulter le bulletin de notes d'un élève (Niveau 4)
```bash
curl -X GET "https://suivi-scolaire-t62q.onrender.com/eleves/1/bulletin" \
     -H "Accept: application/json"
```

---

## 📁 Organisation des Fichiers de Tests (`test-docs/`)

Afin de préserver un dépôt Git épuré et respectant les meilleures pratiques de maintenance :
- Tous les scripts d'automatisation de tests (`.sh`) ainsi que les fiches détaillées de commandes cURL (`.md`) ont été isolés dans le dossier dédié :
  📁 **`test-docs/`**
- Ce dossier est explicitement exclu du versionnement dans [`.gitignore`](file:///home/yokan/TAF/FORMATION/TESTO/suiviScolaire/.gitignore) via la règle `test-docs/`.

---

## 💻 Démarrage Rapide en Local

### Prérequis
- Java 21 (JDK)
- PostgreSQL (ou Docker PostgreSQL)
- Maven (`./mvnw` inclus)

### 1. Lancement
```bash
git clone git@github.com:kanteyouss/Suivi-Scolaire.git
cd Suivi-Scolaire
./mvnw clean spring-boot:run
```

Au démarrage, Flyway applique automatiquement les migrations `V1.0.1__DDL.sql` et `V1.0.2__DML.sql`.

### 2. Accès
- API : `http://localhost:8080`
- Swagger UI : `http://localhost:8080/swagger-ui/index.html`
