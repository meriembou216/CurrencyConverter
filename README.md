# CurrencyConverter

Application Android (Kotlin, View Binding) qui convertit un montant d'une devise vers une autre à partir de taux fixes, avec un clavier numérique personnalisé.

## Fonctionnalités
- Deux Spinner (USD, EUR, TND, GBP, JPY, CAD) alimentés depuis `strings.xml`
- Clavier numérique (TableLayout) : un seul point décimal, 10 caractères max, touche `C`
- Conversion via l'euro comme devise pivot, résultat arrondi à 2 décimales
- Bouton ⇅ pour inverser les devises
- Toast si le montant vaut 0, orientation portrait verrouillée

## Taux fixes (pour 1 EUR)
EUR 1.00 · USD 1.17 · TND 3.40 · GBP 0.87 · JPY 172.50 · CAD 1.62

## Lancer le projet
Ouvrir le dossier dans Android Studio, synchroniser Gradle, lancer sur un émulateur (API 24+).

## Captures d'écran
Voir le dossier `screenshots/`.
