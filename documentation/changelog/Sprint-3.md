# CHANGELOG - Sprint n°3
---

**Ajout:**

- Ajout du logger
- Ajout des collisions avec l'eau (cas spécial)
- Ajout des collisions avec les bords de map (cas spécial)
- Ajout des events prévenant des collisions
- Ajout du multi-saut
- Ajout de boutons et de textures pour ces derniers
- Ajout d'animations pour le Worm
- Ajout de textures pour les armes (gun / bazooka) et pour les munitions
- Ajout de texture pour le parachute
- Ajout des textures dans l'inventaire
- Ajout d'une image de fond dans le menu
- Ajout de la gestion à double-niveau des évènements (système/jeu)
- Ajout de la save
- Ajout de la barre de vie du Worm
- Ajout d'un curseur pour indiquer le Worm qui joue
- Ajout des paramètres max jump, île volante et max HP
- Ajout d'un timer in game
- Ajout graphique de l'inventaire
- Ajout de la possibilité de passer son tour (SkipTurn)
- Ajout du viseur - Shootmode
- Ajout du panel LoadGame
- Ajout de la possibilité de choisir une arme, de tirer avec et de générer des projectiles dans l'ui
- Ajout des textures aléatoires pour la map
- Ajout des dégâts sur Worm avec une collision en bordure de map ou avec un projectile
- Ajout d'une nouvelle map (Gorgonzola)


**Amélioration:**

- Amélioration des listeners dans le TurnManager
- Amélioration des events (en terme d'héritage)
- Amélioration de l'enum Tile
- Suppresion de la HeightLine

**Fix:**

- Fix CI - Checkstyle
- Fix Collisions - Mouvements plus fluides, fin de la glisse (à l'infini), fin de l'accrochage au mur
