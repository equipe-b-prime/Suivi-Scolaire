# 🎓 Plateforme API Suivi Scolaire

API REST complète de gestion et de suivi scolaire développée avec **Spring Boot (Java 21)**, **Spring Data JPA**, **PostgreSQL**, **Flyway** et **SpringDoc OpenAPI (Swagger UI)**.

---

## 📌 Présentation & Liens d'Accès

L'application est entièrement déployée et accessible en ligne, tout en restant facilement exécutable et testable en environnement local.

| Environnement | URL de Base | Documentation Swagger UI |
| :--- | :--- | :--- |
| **🌐 En Ligne (Render)** | `https://suivi-scolaire-t62q.onrender.com` | [Swagger UI En Ligne](https://suivi-scolaire-t62q.onrender.com/swagger-ui/index.html) |
| **💻 Local** | `http://localhost:8080` | `http://localhost:8080/swagger-ui/index.html` |

---

## 🗄️ Bases de Données & Migrations Automatiques (Flyway)

Le projet intègre **Flyway** pour garantir que toute personne clonant le dépôt ou déployant l'application dispose automatiquement d'une base de données prête à l'emploi, initialisée avec ses tables et ses données de test.

* **Base de données en ligne** : Hébergée sur **Neon PostgreSQL** (serverless, haute disponibilité).
* **Base de données locale** : PostgreSQL local (`localhost:5432/suivi_scolaire`).
* **Migrations Flyway** :
  * `V1.0.1__DDL.sql` : Création intégrale de la structure relationnelle (19 tables : `annee`, `niveau`, `classe`, `matiere`, `professeur`, `eleve`, `periode`, `type_evaluation`, `coefficient`, `inscription`, `enseignement`, `note`, `salle`, `creneaux`, `cours`, `absence`, `bulettin`, `moyenne_matieres`, `amenagement`), contraintes d'intégrité, clés étrangères et index.
  * `V1.0.2__DML.sql` : Jeu de données de référence pré-rempli (élèves `ELV-1` à `ELV-78`, classes, matières, coefficients, inscriptions, notes initiales) et réalignement automatique des séquences (`setval`).

---

## 📬 Collection Postman (Pièce Jointe)

Le fichier de collection Postman complet est directement fourni à la racine du dépôt :

📄 **`ZKANTAPI.postman_collection.json`**

### Comment l'importer et l'utiliser dans Postman :
1. Ouvrez **Postman**.
2. Cliquez sur le bouton **Import** (en haut à gauche).
3. Sélectionnez le fichier `ZKANTAPI.postman_collection.json`.
4. La collection contient tous les dossiers de requêtes organisés par niveaux (Niveaux 1 à 4) couvrant l'ensemble des règles métier :
   - Inscriptions & Gestion des élèves
   - Gestion des classes et des cours
   - Saisie et calcul des notes
   - Calcul des moyennes et génération des bulletins
   - Gestion des absences et aménagements
5. **Basculer entre Local et En Ligne** : Vous pouvez configurer la variable d'environnement Postman `base_url` :
   - Local : `http://localhost:8080`
   - En ligne : `https://suivi-scolaire-t62q.onrender.com`

---

## 🚀 Exécution en Local

### Prérequis
- Java 21 (JDK 21)
- PostgreSQL (instance locale)
- Maven (ou utiliser le wrapper `./mvnw` inclus)

### 1. Cloner le projet
```bash
git clone git@github.com:kanteyouss/Suivi-Scolaire.git
cd Suivi-Scolaire
git checkout KANTAPI-N4-F001
```

### 2. Configuration (`application.properties`)
Par défaut, l'application se connecte sur PostgreSQL local ou utilise les variables d'environnement si elles sont définies :
```properties
server.port=${PORT:8080}
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/suivi_scolaire}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```

### 3. Lancer l'application
```bash
./mvnw clean spring-boot:run
```
Au démarrage, Flyway appliquera automatiquement les scripts DDL et DML. Vous pouvez ensuite accéder directement à Swagger sur :
👉 `http://localhost:8080/swagger-ui/index.html`

---

## 🧪 Exemples de Requêtes cURL

### 1. Tester la liste des classes (En Ligne)
```bash
curl -X GET "https://suivi-scolaire-t62q.onrender.com/api/classes" \
     -H "Accept: application/json"
```

### 2. Tester la liste des classes (En Local)
```bash
curl -X GET "http://localhost:8080/api/classes" \
     -H "Accept: application/json"
```

### 3. Consulter les détails d'un élève
```bash
curl -X GET "https://suivi-scolaire-t62q.onrender.com/api/eleves/1" \
     -H "Accept: application/json"
```

---

## 📂 Structure du Projet

```text
├── Dockerfile                           # Configuration Docker pour déploiement Render
├── pom.xml                              # Dépendances Maven (Spring Boot, Flyway, PostgreSQL, Lombok...)
├── README.md                            # Documentation du projet
├── ZKANTAPI.postman_collection.json     # Collection Postman complète importable
└── src/
    ├── main/
    │   ├── java/com/suiviScolaire/      # Code source (Controllers, Services, Repositories, DTOs, Entities)
    │   └── resources/
    │       ├── application.properties   # Configuration applicative, DB et Flyway
    │       └── db/migration/            # Scripts Flyway
    │           ├── V1.0.1__DDL.sql      # Création des tables et index
    │           └── V1.0.2__DML.sql      # Données initiales et réalignement des séquences
```
