# CHANGELOG - Sprint N°4


---

**Ajouts :**

- Ajout des drops ( un item aléatoire + munitions )
- Ajout des altérations de statuts ( poison / brûlure )
- Ajout du changement de worm au sein d’une team
- Ajout du tir allié
- Ajout des dégâts de chute
- Ajout d’une map de base en mode lune et une en mode glace
- Ajout d’un écran spécial à la fin de partie 
- Ajout de musiques
- Ajout d’un load de partie
- Ajout d’une save automatique
- Ajout de 2 modes de jeux : lune et glace
- Ajout d’un système de notifications
- Ajout d’une monnaie dans le jeu
- Ajout d’un magasin pour acheter des nouvelles armes et des munitions
- Ajout d’un système de montée des eaux
- Ajout des explosions
- Ajout d’une méthode déterminant le worm le plus proche d’une position donnée
- Ajout d’animations notamment pour les explosions
- Ajout d’un visuel pour le vent
- Ajout d’un système de bot
- Ajout du suivi des worms par la caméra
- Ajout d’une description sur les armes dans l’inventaire
- Ajout d’un menu audio
- Ajout de nouvelles textures
- ajout de la tiles liane permettant de grimper
- ajout de la tiles eau bénite permettant d'annuler les effets de brûlure et poison
- Ajout de deux maps implémentant les mécaniques de piège, liane et eau bénite
- Ajout d'un visuel de munitions lors de l'équipement d'une arme

## Armes / Outils ajoutés :

- HealingBazooka
- ShotGun
- GasGun
- Electrocanon
- Bow
- Grenade
- CatConcrete
- Tower
- Parachute
- Jetpack
- CallStrike
- Mine
- BaseballBatt
- Medkit
- Trap

**Améliorations :**

- Limitation du nombre de shoot par tour à 3
- Attente en fin de tour pour que les projectiles disparaissent et que les worms atterrissent
- À la fin d’un tour le worm en jetpack obtient un parachute pour retomber
- Utilisation des games Commandes pour les projectiles
- Adaptation de la save aux nouveautés 
- Suppression des altérations de statut avec les items de soin
- Ajout d’effets actifs pour certains projectiles
- Ajout des dégâts par les blocs
- Mise à jour de la consoleUI
- Nouveau mode de visée pour certains items ( cible )
- Suppression du bouton “Shoot mode”
- Fluidité des déplacements de la caméra
- Modification de l’inventaire pour qu’il contienne le Gun débloqué et avec des munitions infinies et deux parachutes

**Fix :**

- Fix Vent - direction, intensité
- Fix Collision - eau, tunneling
- Fix Worm - mort
- Fix SpawnPoint