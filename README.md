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

- Les déplacements des vers et de la caméra se font à l'aide des flèches du clavier. Pour choisir un item et l'utiliser, cela se fait à l'aide de la souris :
  - (explications des touches)
- Terrain destructible
- Physique réaliste
- Armes disponibles dans le jeu :
  - Shotgun : tire x balle(s) infligeant n dégats avec une grande portée, disponible dans l'inventaire dès le début du jeu
  - Bazooka : tire une rocket explosant au premier contact avec un autre joueur ou de la terre, infligeant n dégats au centre de l'explosion et déformant la map. Le bazooka n'est pas disponible dès le début du jeu et donc obtention à l'aide des colis
  - ...
- Objets disponibles dans le jeu
  - ...
- Mode multijoueur sur une machine uniquement

## Technologies utilisées :
- java 17 ([pour télécharger](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- Swing ([page wikipedia](https://en.wikipedia.org/wiki/Swing_(Java)))
- AWT ([page wikipedia](https://en.wikipedia.org/wiki/Abstract_Window_Toolkit))
- ...

## Structure du projet

(dessin de l'arborescence)

## Comment contribuer ?

Pour contribuer à ce projet, se référer à [ce fichier](/CONTRIBUTING.md).

## Licence

(licence pas encore créée)

## Crédits

#### Développeurs :
- LORIOT Baptiste
- MONA Matthys
- BRUNO Thomas
- DENQUIN Leo
- MOSCARDI Lorenzo
- MEYNAUD Louis

#### Musiques / sons :
- ...