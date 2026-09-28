# VisionAI Studio — Studio Créatif d'Images & Vidéos IA

![Android CI & Automatic Build](https://github.com/USER/REPO/actions/workflows/android.yml/badge.svg)
![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF.svg?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4.svg?logo=jetpackcompose)

**VisionAI Studio** est une application Android moderne développée avec **Jetpack Compose (Material Design 3)** pour la génération d'images et de vidéos par Intelligence Artificielle.

L'application intègre des modèles de pointe gratuits et open-source tels que **LTX Video (Lightricks)** et **FLUX.1 Schnell**, aux côtés de **Stable Diffusion** et **Runway ML**.

---

## ✨ Fonctionnalités Principales

- **🎬 Génération Vidéo IA** :
  - **LTX Video 2.5 Turbo & 2.3 HD (100% Gratuit & Open-Source)** : Rendu vidéo temps réel DiT à 60 FPS avec physique fluide et contrôle de trajectoire de caméra (*Travelling, Orbite, Zoom avant/arrière, Contre-plongée*).
  - **Runway ML (Gen-3 Alpha & Gen-2)** : Rendu cinématographique haut de gamme.
  - **CogVideoX** : Modèle vidéo open-source avec cohérence 3D spatiale.

- **🎨 Génération d'Images IA** :
  - **FLUX.1 Schnell (100% Gratuit & Open-Source)** : Architecture Flow Matching 4 étapes ultra-détaillée.
  - **SDXL Turbo Instant** : Diffusion 1 étape sans latence.
  - **Stable Diffusion XL & 3.5 Large** : Rendu ultra-haute résolution.

- **✍️ Constructeur de Prompt Avancé** :
  - Suggestions catégorisées de tags (*Styles, Éclairage & Lumière, Qualité 8K, Angles Caméra, Mouvement Vidéo 60 FPS*).
  - Ajout et retrait dynamique en un clic sans détruire votre phrase.
  - Générateur de combinaisons aléatoires créatives (🎲 Combo).

- **📥 Téléchargement Local dans la Galerie** :
  - Sauvegarde haute fidélité des images (`Pictures/VisionAI Studio`) et vidéos (`Movies/VisionAI Studio`) via l'API Android **MediaStore** (conforme aux exigences Google Play Scoped Storage).
  - Accessible directement depuis l'écran de détail, les cartes de l'historique et le panneau de partage social.

- **💾 Persistance & Base de Données Locale** :
  - Historique complet, gestion des favoris et remixage de prompts sauvegardés localement avec **Room Database**.

---

## 🛠️ Stack Technique

- **Langage** : Kotlin (100%)
- **Interface Utilisateur** : Jetpack Compose & Material 3 (Theming Cyber Dark Studio)
- **Architecture** : MVVM (Model-View-ViewModel) + StateFlow & Lifecycle
- **Base de Données Locale** : Room Database + KSP
- **Traitement d'Images & Médias** : Coil, Android MediaStore
- **Intégration Continue (CI)** : GitHub Actions (`.github/workflows/android.yml`)

---

## 🚀 Compilation Automatique sur GitHub (CI/CD)

Le workflow GitHub Actions est configuré dans `.github/workflows/android.yml` et se déclenche automatiquement à chaque `push` ou `pull_request` sur les branches `main` et `master`, ainsi que manuellement via `workflow_dispatch`.

### Étapes exécutées par la CI :
1. Configuration de l'environnement Ubuntu avec **JDK 17 (Temurin)**.
2. Mise en cache automatique de Gradle et des dépendances.
3. Préparation automatique des fichiers `.env` et décodage de `debug.keystore`.
4. Exécution des tests unitaires et Robolectric (`./gradlew testDebugUnitTest`).
5. Compilation de l'APK Debug (`./gradlew assembleDebug`).
6. Téléversement de l'artefact APK (`VisionAI-Studio-debug-apk`) téléchargeable directement depuis l'onglet **Actions** de GitHub.

--

## 💻 Compilation en Local

### Prérequis :
- **Android Studio** (Koala / Ladybug ou version supérieure)
- **JDK 17** installé
- **Android SDK** (API 35 ou 36)

### Lancer la compilation :

```bash
# Cloner le dépôt
git clone https://github.com/VOTRE_NOM/VOTRE_DEPOT.git
cd VOTRE_DEPOT

# Donner les droits d'exécution au wrapper Gradle
chmod +x gradlew

# Exécuter les tests unitaires
./gradlew testDebugUnitTest

# Compiler l'APK Debug
./gradlew assembleDebug
```

L'APK compilé sera généré dans :
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📄 Licence

Ce projet est distribué sous licence open-source.
