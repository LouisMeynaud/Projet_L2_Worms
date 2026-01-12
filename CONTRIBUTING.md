# Contribuer à Worms MI4

1) Cloner le dépôt Git.
```bash
git clone https://moule.informatique.univ-paris-diderot.fr/3lmtb/worms-mi4.git
```
1) Depuis dev, créer sa branche de travail avec un nom explicite selon les conventions GitFlow (`feature/` ou `bugfix/`).
2) Faire ses changements et commits avec des messages explicites.
3) Pousser sa branche sur le dépôt distant.
4) Créer une merge request (MR) de sa branche vers dev.
5) La MR déclenche une pipeline CI/CD GitLab qui exécute les tests automatisés.
6) Si la pipeline passe, demander une revue de code à un mainteneur du projet.
7) Après approbation, la MR peut être fusionnée dans dev par le mainteneur.

### Guides de contribution :
* [Ajouter un Item (Weapon/Tool)](src/main/java/com/lllmtb/model/item/CONTRIBUTING.md)
* [Ajouter une Map](src/main/java/com/lllmtb/terrain/map/CONTRIBUTING.md)
