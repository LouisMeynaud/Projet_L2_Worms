# Worms MI4

## Statut du projet
![Pipeline status](https://moule.informatique.univ-paris-diderot.fr/3LMTB/Worms-mi4/badges/main/pipeline.svg)

Version actuelle du jeu :
[jar](http://moule.informatique.univ-paris-diderot.fr/3lmtb/worms-mi4/-/jobs/artifacts/main/browse/target?job=package)

Ligne de code pour lancer le jeu :
```bash
java -jar worms-x.x-fat.jar
```

## Description :

(à modifier en fonction des différentes implémentations/feature)
Worms MI4, inpiré de Worms WMD de Team17, est un jeu de stratégie au tour par tour consistant en une simulation de guerre entre plusieurs équipes composées de vers. Les vers sont disposés aléatoirement ou selon les choix des joueurs sur la carte. L'objectif est d'éliminer tous les vers ennemis en gardant au moins un ver de son équipe en vie. Pour cela, chaque joueur aura un inventaire d'armes et d'outils afin de se déplacer, se protéger et surtout attaquer. Au sein de l'inventaire, toutes les armes ne sont pas débloquées dès le début de la partie, certaine sont déblocables à l'aide d'un système aléatoire de colis. Lors d'un tour, le joueur peut de déplacer, puis utiliser un objet (arme ou outil) puis se redéplacer afin de se cacher.

## Gameplay / Fonctionnalités

- Le jeu se joue avec la souris et les flèches du clavier :
  - Les flèches gauche et droite permettent de déplacer le ver actif horizontalement.
  - La flèche haut permet de sauter.
  - Le clic gauche permet de sélectionner des éléments à l'écran (boutons, armes, outils...) et d'utiliser un item.
  - Le pointeur de la souris permet de viser avec les armes et outils.
  - 
- Terrain destructible
- Physique réaliste
- Armes disponibles dans le jeu :
  - Gun : Le gun tire 1 balle avec une grande portée, disponible dans l'inventaire dès le début du jeu.
  - BaseballBat : La batte de baseball est une arme de corps à corps disponible dans l'inventaire dès le début du jeu. La batte de baseball fonctionne avec un système de charge, plus on maintient le clic gauche, plus le coup sera fort.
  - Bazooka : Le bazzoka tire une rocket explosant au premier contact avec un autre joueur ou de la terre,  déformant la map. Le bazooka fonctionne avec un système de charge, plus on maintient le clic gauche, plus la roquette ira loin. Le bazooka est disponible dans le magasin pour 400 coins et ses munitions pour 80 coins l'unité, ou gratuitement dans les colis.
  - HealingBazooka : Le healingBazooka tire une roquette qui soigne les vers alliés à l'impact, elle annule égallement les altérations de statut. Le healingbazzoka fonctionne avec un système de charge, plus on maintient le clic gauche, plus la roquette ira loin. Le HealingBazooka est disponible dans le magasin pour 230 coins et ses munitions pour 100 coins l'unité, ou gratuitement dans les colis.
  - Shotgun : Le shotgun tire 5 balles en éventail, idéal pour les courtes distances. Le Shotgun est disponible dans le magasin pour 250 coins et ses munitions pour 50 coins les 3 munitions, ou gratuitement dans les colis.
  - Electrocanon : L'électrocanon tire un électron qui rebondit 5 fois d'un ennemi à l'ennemi le plus proche, infligeant de moins en moins de dégâts. L'électron est le seul projectile pouvant traverser l'eau et la terre avec le nuage. Le Electrocanon est disponible dans le magasin pour 600 coins et ses munitions pour 120 coins l'unité, ou gratuitement dans les colis.
  - Gasgun : Le gasgun propage un nuage de gaz toxique qui inflige des dégâts et empoisonne aux vers se trouvant dans le nuage. Le nuage est le seul projectile pouvant traverser l'eau et la terre avec l'électron. Le Gasgun est disponible dans le magasin pour 450 coins et ses munitions pour 90 coins l'unité, ou gratuitement dans les colis.
  - Bow : L'arc tire une flèche. L'arc fonctionne avec un système de charge, plus on maintient le clic gauche, plus la flèche ira loin et fera de dégâts. L'arc est disponible dans le magasin pour 140 coins et ses munitions pour 30 coins les 3 flèches, ou gratuitement dans les colis.
- Outils disponibles dans le jeu (*les outils qui se lancent bénéficient d'une jauge, plus on maintient le clic gauche, plus il est lancé loin*) :
  - Parachute : Le parachute permet d'éviter les dégâts de chute, il est disponible en 2 exemplaires dans l'inventaire dès le début du jeu et disponible dans le magasin pour 40 coins l'unité ou gratuitement dans les colis.
  - Jetpack : Le jetpack permet de voler librement dans les airs pendant 5 secondes ou pour 20 sauts. Le jetpack est disponible dans le magasin pour 150 coins l'unité ou gratuitement dans les colis.
  - CallStrike : Le CallStrike permet d'appeler une frappe aérienne infligeant des dégâts dans une zone ciblée. Le CallStrike est disponible dans le magasin pour 400 coins l'unité ou gratuitement dans les colis.
  - CatConcrete : Le CatConcrete est une tête de chat en béton qui tombe du ciel, rebondissant et creusant la terre avant de disparaître. Le CatConcrete est disponible dans le magasin pour 200 coins l'unité ou gratuitement dans les colis.
  - Tower : La tower est une tourelle envoyée par le joueur qui tire automatiquement sur les ennemis à portée pendant un tour. La tower est disponible dans le magasin pour 150 coins l'unité ou gratuitement dans les colis.
  - Mine : La mine est un explosif lancé par le joueur qui explose au moindre contact avec un ver. La mine est disponible dans le magasin pour 80 coins l'unité ou gratuitement dans les colis.
  - Grenade : La grenade est un explosif lancé par le joueur qui explose au bout de 5 secondes. La grenade est disponible dans le magasin pour 150 coins l'unité ou gratuitement dans les colis.
  - Medkit : Le medkit soigne le ver actif de 50 points de vie et annule les altérations de statut. Le medkit est disponible dans le magasin pour 125 coins l'unité ou gratuitement dans les colis.
  - Trap : le trap est un piège lancé par le joueur qui blesse et empoisonne le ver qui marche dessus tant qu'il reste dessus, le trap ne disparait jamais. Le trap est disponible dans le magasin pour 150 coins l'unité ou gratuitement dans les colis.
- Mode multijoueur sur une machine uniquement et un mode solo contre des bots. Les bots se déplacent vers le ver ennemi le plus proche jusqu'à une certaine distance maximale et tirent uniquemant avec le gun et avec une faible probabilité de toucher.

## Technologies utilisées :
- java 17 ([pour télécharger](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- Swing ([page wikipedia](https://en.wikipedia.org/wiki/Swing_(Java)))
- AWT ([page wikipedia](https://en.wikipedia.org/wiki/Abstract_Window_Toolkit))
- ...

## Comment contribuer ?

Pour contribuer à ce projet, se référer à [ce fichier](/CONTRIBUTING.md).

## Licence

Pour visualiser la Licence, cliquer [ici](/LICENSE).

## Crédits

#### Développeurs :
- LORIOT Baptiste
- MONA Matthys
- BRUNO Thomas
- DENQUIN Leo
- MOSCARDI Lorenzo
- MEYNAUD Louis