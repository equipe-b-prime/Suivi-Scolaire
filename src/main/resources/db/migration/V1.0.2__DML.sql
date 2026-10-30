-- =========================================================================
-- V1.0.2__DML.sql : JEU DE DONNÉES DE RÉFÉRENCE ET DE TEST POUR SUIVI SCOLAIRE
-- =========================================================================

-- =========================================================================
-- 1. NETTOYAGE PRÉALABLE DES TABLES
-- =========================================================================

TRUNCATE TABLE
    note,
    moyenne_matieres,
    bulettin,
    absence,
    cours,
    amenagement,
    enseignement,
    inscription,
    coefficient,
    professeur,
    eleve,
    classe,
    matiere,
    type_evaluation,
    periode,
    niveau,
    annee,
    salle,
    creneaux
RESTART IDENTITY CASCADE;

-- =========================================================================
-- 2. DONNÉES RÉFÉRENTIELLES (Années, Niveaux, Périodes, Types d'évaluation)
-- =========================================================================

INSERT INTO annee (id, libelle) VALUES
    (1, '2025-2026');

INSERT INTO niveau (id, nom) VALUES
    (1, '6ème'),
    (2, '5ème'),
    (3, '4ème'),
    (4, '3ème');

INSERT INTO periode (id, nom, numero, cloture, date_rouverte, motif_rouverte, utilisateur_rouverte) VALUES
    (1, '1er Trimestre', 1, false, NULL, NULL, NULL),
    (2, '2ème Trimestre', 2, false, NULL, NULL, NULL),
    (3, '3ème Trimestre', 3, false, '2026-10-30', 'Correction erreur de saisie bulletin', 'responsable_scolarite');

INSERT INTO type_evaluation (id, nom, code, poid) VALUES
    (1, 'Interrogation', 'interrogation', 1.00),
    (2, 'Devoir', 'devoir', 2.00),
    (3, 'Examen', 'examen', 3.00);

-- =========================================================================
-- 3. MATIÈRES ET COEFFICIENTS
-- =========================================================================

INSERT INTO matiere (id, nom, coefficient) VALUES
    (1, 'Mathématiques', 4.00),
    (2, 'Français', 4.00),
    (3, 'Anglais', 3.00),
    (4, 'SVT', 2.00),
    (5, 'Physique-Chimie', 2.00),
    (6, 'Histoire-Géographie', 2.00),
    (7, 'EPS', 1.00),
    (8, 'Informatique', 2.00),
    (9, 'Physique', 2.00),
    (27, 'Demo Mathématiques', 4.00),
    (28, 'Demo Anglais', 3.00),
    (29, 'Demo Histoire-Géo', 2.00),
    (30, 'Demo Français', 4.00);

INSERT INTO coefficient (id, valeur, matiere_id, niveau_id) VALUES
    (1, 4.00, 1, 4),
    (2, 4.00, 1, 3),
    (3, 3.00, 2, 4),
    (4, 3.00, 2, 3),
    (5, 2.00, 3, 4),
    (6, 2.00, 4, 4),
    (7, 2.00, 5, 4),
    (8, 2.00, 6, 4),
    (9, 1.00, 7, 4),
    (10, 2.00, 8, 4),
    (11, 4.00, 1, 1),
    (12, 4.00, 2, 1);

-- =========================================================================
-- 4. CLASSES
-- =========================================================================

INSERT INTO classe (id, nom, niveau, effectif_max) VALUES
    (1, '6ème A modifiée', '6EME', 35),
    (2, '6ème B', '6ème', 45),
    (3, '5ème A', '5ème', 40),
    (4, '4ème A', '4ème', 40),
    (5, '3ème A', '3ème', 35),
    (6, '3ème B', '3ème', 35),
    (39, 'Demo 6ème A', '6ème', 40),
    (40, 'Demo 5ème B', '5ème', 38),
    (41, 'Demo 4ème B', '4ème', 36),
    (42, 'Demo 3ème C', '3ème', 34),
    (50, 'F1_TEST', '6ème', 30),
    (52, 'F2_CLASSE', '6ème', 30),
    (54, '6ème A', '6EME', 40);

-- =========================================================================
-- 5. PROFESSEURS
-- =========================================================================

INSERT INTO professeur (id, nom, prenom, email, matiere_id, classe_id) VALUES
    (1, 'Kouassi', 'Alain', 'alain.kouassi@ecole.ci', 1, NULL),
    (2, 'Sangaré', 'Awa', 'awa.sangare@ecole.ci', 2, NULL),
    (3, 'Koffi', 'Paul', 'paul.koffi@ecole.ci', 3, NULL),
    (4, 'Zadi', 'Marie', 'marie.zadi@ecole.ci', 4, NULL),
    (5, 'Bakayoko', 'Salif', 'salif.bakayoko@ecole.ci', 6, NULL),
    (6, 'Touré', 'Nadia', 'nadia.toure@ecole.ci', 7, NULL),
    (10, 'Koffi', 'Yao', 'yao.koffi@example.com', NULL, NULL),
    (31, 'Demo Martin', 'Sophie', 'demo.martin@test.fr', 28, NULL),
    (32, 'Demo Bernard', 'Luc', 'demo.bernard@test.fr', 30, NULL),
    (33, 'Demo Claire', 'Nadia', 'demo.claire@test.fr', 29, NULL),
    (34, 'Demo David', 'Marc', 'demo.david@test.fr', 27, NULL);

-- =========================================================================
-- 6. ÉLÈVES
-- =========================================================================

INSERT INTO eleve (id, nom, prenom, date_naissance, matricule, moyenne_trimestre_courant, ecart_trimestre_precedent, est_sigle, trimestre_sigle) VALUES
    (1, 'Kouassi', 'Jean', '2013-01-01', 'ELV-1', 14.50, 0.50, false, 1),
    (2, 'Koné', 'Ibrahim', '2013-07-25', 'ELV-2', 12.00, -0.50, false, 1),
    (3, 'Traoré', 'Fatou', '2013-03-12', 'ELV-3', 15.00, 1.00, false, 1),
    (4, 'Yao', 'Koffi', '2013-11-30', 'ELV-4', 11.50, 0.00, false, 1),
    (5, 'Ouattara', 'Mariam', '2013-05-19', 'ELV-5', 13.00, 0.25, false, 1),
    (6, 'Diabaté', 'Seydou', '2012-09-14', 'ELV-6', 10.50, -1.00, false, 1),
    (7, 'Bamba', 'Aminata', '2012-12-02', 'ELV-7', 16.00, 0.75, false, 1),
    (8, 'N''Guessan', 'Yann', '2012-04-21', 'ELV-8', 12.50, -0.25, false, 1),
    (9, 'Doumbia', 'Fatoumata', '2012-08-17', 'ELV-9', 14.00, 0.50, false, 1),
    (10, 'Gnahoré', 'Éric', '2012-02-05', 'ELV-10', 9.50, -2.00, true, 1),
    (11, 'Kouadio', 'Grâce', '2011-10-11', 'ELV-11', 15.50, 1.25, false, 1),
    (12, 'Assi', 'Serge', '2011-06-28', 'ELV-12', 13.50, 0.00, false, 1),
    (70, 'Demo Dupont', 'Thomas', '2012-05-15', 'ELV-DEMO-01', 14.00, 0.50, false, 1),
    (71, 'Demo Durand', 'Léa', '2012-09-20', 'ELV-DEMO-02', 12.50, -0.50, false, 1),
    (72, 'Demo Petit', 'Lucas', '2012-03-10', 'ELV-DEMO-03', 16.00, 1.00, false, 1),
    (73, 'Demo Roux', 'Chloé', '2011-11-25', 'ELV-DEMO-04', 13.00, 0.00, false, 1),
    (74, 'Demo Moreau', 'Hugo', '2011-07-18', 'ELV-DEMO-05', 11.00, -1.50, false, 1),
    (75, 'Demo Simon', 'Camille', '2011-01-30', 'ELV-DEMO-06', 15.00, 0.50, false, 1),
    (76, 'Demo Michel', 'Arthur', '2010-12-05', 'ELV-DEMO-07', 10.00, -2.50, true, 1),
    (77, 'Demo Lefebvre', 'Emma', '2010-08-22', 'ELV-DEMO-08', 17.00, 1.50, false, 1),
    (78, 'Demo Garcia', 'Jules', '2010-04-14', 'ELV-DEMO-09', 14.50, 0.50, false, 1);

-- =========================================================================
-- 7. INSCRIPTIONS
-- =========================================================================

INSERT INTO inscription (id, annee_id, classe_id, eleve_id, date_debut, date_fin) VALUES
    (1, 1, 1, 1, '2025-09-01', NULL),
    (2, 1, 1, 2, '2025-09-01', NULL),
    (3, 1, 1, 3, '2025-09-01', NULL),
    (4, 1, 2, 4, '2025-09-01', NULL),
    (5, 1, 3, 5, '2025-09-01', NULL),
    (6, 1, 3, 6, '2025-09-01', NULL),
    (7, 1, 4, 7, '2025-09-01', NULL),
    (8, 1, 4, 8, '2025-09-01', NULL),
    (9, 1, 5, 9, '2025-09-01', NULL),
    (10, 1, 5, 10, '2025-09-01', NULL),
    (11, 1, 6, 11, '2025-09-01', NULL),
    (12, 1, 6, 12, '2025-09-01', NULL),
    (70, 1, 40, 70, '2025-09-01', NULL),
    (71, 1, 40, 71, '2025-09-01', NULL),
    (72, 1, 40, 72, '2025-09-01', NULL),
    (73, 1, 41, 73, '2025-09-01', NULL),
    (74, 1, 41, 74, '2025-09-01', NULL),
    (75, 1, 41, 75, '2025-09-01', NULL),
    (76, 1, 42, 76, '2025-09-01', NULL),
    (77, 1, 42, 77, '2025-09-01', NULL),
    (78, 1, 42, 78, '2025-09-01', NULL);

-- =========================================================================
-- 8. ENSEIGNEMENTS
-- =========================================================================

INSERT INTO enseignement (id, matiere_id, professeur_id, classe_id) VALUES
    (1, 1, 1, 1),
    (2, 2, 2, 1),
    (3, 3, 3, 1),
    (4, 4, 4, 1),
    (5, 5, 1, 2),
    (6, 6, 5, 2),
    (7, 7, 6, 3),
    (8, 8, 3, 3),
    (9, 1, 2, 4),
    (10, 2, 4, 5),
    (40, 27, 34, 39),
    (41, 28, 31, 39),
    (42, 30, 32, 39),
    (43, 27, 34, 40),
    (44, 28, 31, 40),
    (45, 29, 33, 41),
    (46, 30, 32, 41),
    (47, 27, 34, 42);

-- =========================================================================
-- 9. NOTES DE TEST
-- =========================================================================

INSERT INTO note (id, valeur, enseignement_id, inscription_id, periode_id, type_evaluation_id, date_saisie) VALUES
    (1, 15.00, 1, 1, 1, 2, '2026-10-22'),
    (2, 12.50, 1, 1, 2, 1, '2026-10-22'),
    (3, 16.00, 2, 1, 1, 2, '2026-10-22'),
    (4, 14.00, 1, 2, 1, 2, '2026-10-22'),
    (5, 11.00, 2, 2, 1, 3, '2026-10-22'),
    (6, 13.50, 1, 3, 1, 2, '2026-10-22'),
    (7, 9.50, 3, 3, 1, 1, '2026-10-22'),
    (8, 15.50, 1, 4, 1, 2, '2026-10-22'),
    (9, 12.00, 4, 4, 1, 2, '2026-10-22'),
    (10, 14.75, 2, 5, 1, 3, '2026-10-22'),
    (80, 14.00, 40, 70, 1, 1, '2026-10-15'),
    (81, 16.00, 40, 70, 1, 2, '2026-10-16'),
    (82, 12.00, 41, 70, 1, 1, '2026-10-17'),
    (83, 15.50, 40, 71, 1, 1, '2026-10-15'),
    (84, 13.00, 40, 71, 1, 2, '2026-10-16'),
    (85, 18.00, 42, 72, 1, 3, '2026-10-18'),
    (86, 11.00, 43, 73, 1, 1, '2026-10-15'),
    (87, 10.50, 44, 74, 1, 2, '2026-10-16'),
    (88, 17.00, 45, 75, 1, 3, '2026-10-18'),
    (89, 8.50, 46, 76, 1, 1, '2026-10-15'),
    (90, 19.00, 47, 77, 1, 3, '2026-10-18');

-- =========================================================================
-- 10. RECALCUL ET RÉALIGNEMENT DES SÉQUENCES POSTGRESQL
-- =========================================================================

SELECT setval(pg_get_serial_sequence('annee', 'id'), COALESCE((SELECT MAX(id) FROM annee), 1));
SELECT setval(pg_get_serial_sequence('niveau', 'id'), COALESCE((SELECT MAX(id) FROM niveau), 1));
SELECT setval(pg_get_serial_sequence('classe', 'id'), COALESCE((SELECT MAX(id) FROM classe), 1));
SELECT setval(pg_get_serial_sequence('matiere', 'id'), COALESCE((SELECT MAX(id) FROM matiere), 1));
SELECT setval(pg_get_serial_sequence('type_evaluation', 'id'), COALESCE((SELECT MAX(id) FROM type_evaluation), 1));
SELECT setval(pg_get_serial_sequence('periode', 'id'), COALESCE((SELECT MAX(id) FROM periode), 1));
SELECT setval(pg_get_serial_sequence('professeur', 'id'), COALESCE((SELECT MAX(id) FROM professeur), 1));
SELECT setval(pg_get_serial_sequence('eleve', 'id'), COALESCE((SELECT MAX(id) FROM eleve), 1));
SELECT setval(pg_get_serial_sequence('coefficient', 'id'), COALESCE((SELECT MAX(id) FROM coefficient), 1));
SELECT setval(pg_get_serial_sequence('inscription', 'id'), COALESCE((SELECT MAX(id) FROM inscription), 1));
SELECT setval(pg_get_serial_sequence('enseignement', 'id'), COALESCE((SELECT MAX(id) FROM enseignement), 1));
SELECT setval(pg_get_serial_sequence('note', 'id'), COALESCE((SELECT MAX(id) FROM note), 1));

ANALYZE annee, niveau, classe, matiere, type_evaluation, periode, professeur, eleve, coefficient, inscription, enseignement, note;
