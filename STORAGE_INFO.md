# Informations sur le Stockage des Données (PayFam)

Ce document explique comment l'application **PayFam** gère le stockage des données financières et budgétaires de votre famille, conformément à vos exigences de confidentialité et de performance.

---

## 💾 Stockage Local Exclusif (Mémoire du Téléphone)

L'application **PayFam** est conçue pour être **100 % hors ligne et confidentielle**. Toutes vos transactions, vos budgets et la configuration des membres de votre famille sont stockés dans la **mémoire de stockage interne sécurisée de votre téléphone**.

Aucun serveur externe, base de données cloud ou tiers ne reçoit vos informations financières privées.

---

## 🛠️ Architecture Technique : Base de données Room (SQLite)

Pour garantir une persistance rapide, fiable et sécurisée, nous utilisons la bibliothèque officielle de persistance Android de Google : **Room Database** (qui repose sur un moteur **SQLite** local encodé directement au cœur du système d'exploitation Android).

### 1. Fichiers de Base de Données (.db)
Sur le système d'exploitation de votre téléphone mobile, la base de données est persistée de manière chiffrée ou gérée par les bacs à sable (sandbox) de sécurité d'Android sous :
```
/data/data/com.aistudio.payfam.kgpuzr/databases/family_expense_v1_db
```
*(Remarque : Ce dossier n'est accessible que par l'application PayFam elle-même afin d'empêcher d'autres applications de lire vos relevés financiers).*

### 2. Gestion Réactive (Flows Coroutines)
Les accès à la base de données tirent parti de **Kotlin Coroutines / StateFlow** pour garantir qu'aucune opération d'écriture ou de lecture ne ralentisse l'interface utilisateur. Vous obtenez un taux de rafraîchissement fluide à 60/120 images par seconde (Edge-to-Edge).

---

## 📋 Informations Audit & Lancement de l'Application

### Diagnostic d'Audit de Lancement
Si l'application rencontrait des difficultés à se lancer initialement dans certaines versions, voici les correctifs audités et appliqués avec succès :

1. **Intégration d'Icônes Étendues** : L'extension d'icônes Compose (`androidx.compose.material.icons.extended`) a été correctement activée dans le fichier `build.gradle.kts` pour éviter tout crash de rendu visuel dû à des icônes de catégories manquantes (telles que `DirectionsCar`, `MedicalServices`, `LocalPizza` etc.).
2. **Gestion d'Imports Matériels** : Tous les imports de défilement Compose et les ombrages ont été résolus de manière propre.
3. **Optimisation Room Database** : Les modèles d'entités locaux (`FamilyMember`, `FamilyTransaction`, `CategoryBudget`) ont été associés à des ID auto-incrémentés de clés primaires pour éviter des conflits et assurer une intégrité parfaite.

---

*PayFam reste à vos côtés au quotidien pour une gestion saine, confidentielle et sécurisée du foyer.*
