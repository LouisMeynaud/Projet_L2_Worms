# Worms MI4

## Statut du projet
![Pipeline status](https://moule.informatique.univ-paris-diderot.fr/3LMTB/Worms-mi4/badges/main/pipeline.svg)

## Lancer le jeu :

Téléchargez l'exécutable `.jar` publié dans la dernière [release](https://moule.informatique.univ-paris-diderot.fr/3lmtb/worms-mi4/-/releases)
et utilisez la ligne de commande suivante avec Java 17 ou une version ultérieure :

- en mode graphique :
```bash
java -jar worms-2.0-fat.jar
```

- en mode console :
```bash
java -jar worms-2.0-fat.jar console
```

#### Compiler le jeu soi-même depuis le code source :
Cloner le dépôt git :
```bash
git clone https://moule.informatique.univ-paris-diderot.fr/3lmtb/worms-mi4.git
```
Se placer dans le répertoire du projet :
```bash
cd worms-mi4
```
Compiler le projet avec Maven :
```bash
mvn clean package
```
Le jar exécutable se trouve dans le répertoire `target` sous le nom `worms-2.0-fat.jar`.

---

### Pour charger la partie de démo :
- Télécharger les fichiers de sauvegarde [ici](https://moule.informatique.univ-paris-diderot.fr/3lmtb/worms-mi4/-/blob/main/documentation/Demo.zip).
- Placer le dossier `Demo` dans le répertoire des sauvegardes :

| Unix                 | Windows                       |
|----------------------|-------------------------------|
| `$HOME/Worms/saves/` | `%USERPROFILE%/Worms/saves/`  |

- Lancer le jeu et cliquer sur "Jouer" -> "Charger une partie", puis sélectionner la sauvegarde.

---

## Description :

### Presentation du jeu et objectif :

Worms MI4, inpiré de Worms WMD de Team17, est un jeu de stratégie au tour par tour 
consistant en une simulation de guerre entre plusieurs équipes composées de vers. 
Les vers sont disposés aléatoirement sur la carte. L'objectif est d'être le dernier
survivant.

### Inventaire :

Chaque joueur dispose d'un inventaire d'armes et d'outils afin de se protéger et 
surtout d'attaquer.
L'inventaire est partagé entre tous les vers d'une même équipe.
Au sein de l'inventaire, toutes les armes ne sont pas débloquées dès le début de la 
partie.
Deux armes sont disponibles dès le début : le gun et la batte de baseball (décrits 
ci-dessous), ainsi que deux parachutes.
Les autres armes et outils sont disponibles dans le magasin ou dans les colis.

### Magasin :

Le magasin permet d'acheter des armes et outils avec des pièces. Chaque arme doit être 
achetée une seule fois avant d'en acheter les munitions et de pouvoir l'utiliser. Les
outils eux sont utilisables directement après achat. L'obtention des pièces se fait de 
deux manières différentes : à chaque début de tour, le joueur actif reçoit 150 pièces, 
et des pièces apparaissent aléatoirement sur la carte, le joueur peut les ramasser en
déplaçant son ver actif dessus, elles rapportent 50 pièces.

### Colis :

À chaque tour, il y a une probabilité qu'un colis apparaisse aléatoirement sur 
la carte. Ceux-ci contiennent une arme ou un outil aléatoire parmi ceux disponibles dans 
le magasin, ainsi que des munitions pour cette arme. Le joueur peut ramasser le colis 
en déplaçant son ver actif dessus.

### Tours :

Chaque joueur joue à tour de rôle. Lorsqu'un joueur commence son tour, il peut
déplacer son ver actif et utiliser des armes ou outils de son inventaire. Le joueur
dispose d'un temps limité pour jouer son tour, une fois le temps écoulé, le
tour passe au joueur suivant. Il dispose de trois tirs maximum par tour. Une fois
ceux-ci réalisés, le temps est réduit à cinq secondes pour finir le tour. Si le ver 
actif du joueur meurt avant la fin de son tour, le tour se termine immédiatement.
Un tour ne peut se terminer que sous la condition que tous les worms soient au sol et 
que tous les projectiles aient disparu (à l'exception des pièges, mines, tourelles). 
À la fin du timer, les inputs du joueur courant sont désactivés, ses outils équipés sont 
retirés, et on accélère le temps pour finir le tour.

### Fin de partie :

La partie se termine lorsqu'une seule équipe possède encore des vers en vie. Le joueur
dont l'équipe est la dernière en vie remporte la partie. Attention, au bout d'un certain
temps, la mer monte progressivement, mettant la pression aux joueurs.

## Gameplay / Fonctionnalités

- Le jeu se joue avec la souris et les flèches du clavier :
  - Les flèches gauche et droite permettent de déplacer le ver actif horizontalement.
  - La flèche haut permet de sauter.
  - Le clic gauche permet de sélectionner des éléments à l'écran (boutons, armes, 
outils...) et d'utiliser un item.
  - Le pointeur de la souris permet de viser avec les armes et outils.
  - Le clic droit permet de bouger la caméra : maintenir le clic droit et déplacer la
souris pour bouger la caméra ou utiliser la molette pour zoomer/dézoomer.


- Possibilité de passer son tour avant la fin du temps imparti.


- Possibilité de déplacer la caméra librement pendant son tour. Si le joueur ne la
touche pas, elle suit automatiquement le ver actif.


- Possibilité de changer de ver pour le suivant dans son équipe une fois par tour.


- Terrain destructible.


- Physique réaliste.


- Présence sur certaines maps de la lave qui fait des dégâts aux vers dedans et leur applique un effet de
brûlure.


- Présence sur certaines maps d'eau bénite qui soigne les vers qui nagent dedans de leurs altérations de
statut.


- Présence sur certaines maps de lianes permettant de monter en sautant et redescendre en toute sécurité.


- Sauvegarde automatique à chaque début de tour.


- Mode multijoueur sur une machine uniquement et un mode solo contre des bots. Les
bots se déplacent vers le ver ennemi le plus proche jusqu'à une certaine distance 
minimale et tirent uniquement avec le gun et avec une faible probabilité de toucher.


- Armes disponibles dans le jeu :
  - Gun : Le Gun tire une balle avec une grande portée, disponible dans l'inventaire 
dès le début du jeu.
  - BaseballBat : La batte de baseball est une arme de corps à corps disponible dans 
l'inventaire dès le début du jeu. La batte de baseball fonctionne avec un système de 
charge, plus on maintient le clic gauche, plus le coup sera fort.
  - Bazooka : Le Bazooka tire une roquette explosant au premier contact avec un autre 
joueur ou de la terre,  déformant la map. Le Bazooka fonctionne avec un système de 
charge, plus on maintient le clic gauche, plus la roquette ira loin. Le Bazooka est 
disponible dans le magasin pour 400 coins et ses munitions pour 80 coins l'unité, ou 
gratuitement dans les colis.
  - HealingBazooka : Le HealingBazooka tire une roquette qui soigne les vers alliés à 
l'impact, elle annule également les altérations de statut. Le healingbazooka 
fonctionne avec un système de charge, plus on maintient le clic gauche, plus la 
roquette ira loin. Le HealingBazooka est disponible dans le magasin pour 230 coins et 
ses munitions pour 100 coins l'unité, ou gratuitement dans les colis.
  - Shotgun : Le Shotgun tire cinq balles en éventail, idéal pour les courtes distances. 
Le Shotgun est disponible dans le magasin pour 250 coins et ses munitions pour 50 coins 
les trois munitions, ou gratuitement dans les colis.
  - Electroncanon : L'Electroncanon tire un électron qui rebondit cinq fois d'un ennemi 
à l'ennemi le plus proche, infligeant de moins en moins de dégâts. L'électron peut 
traverser l'eau et la terre. L'Electroncanon est disponible dans le magasin pour 600 coins 
et ses munitions pour 120 coins l'unité, ou gratuitement dans les colis.
  - Gasgun : Le Gasgun propage un nuage de gaz toxique qui inflige des dégâts aux vers 
se trouvant dans le nuage, leur infligeant un effet de poison. Le nuage peut traverser l'eau 
et la terre. Le Gasgun est disponible dans le magasin pour 450 coins et ses munitions pour 90 
coins l'unité, ou gratuitement dans les colis.
  - Bow : L'arc tire une flèche. L'arc fonctionne avec un système de charge, plus on 
maintient le clic gauche, plus la flèche ira loin et fera de dégâts. L'arc est 
disponible dans le magasin pour 140 coins et ses munitions pour 30 coins les 3 
flèches, ou gratuitement dans les colis.


- Outils disponibles dans le jeu (*les outils qui se lancent bénéficient d'une jauge, 
plus on maintient le clic gauche, plus il est lancé loin*) :
  - Parachute : Le parachute permet d'éviter les dégâts de chute, il est disponible en 
2 exemplaires dans l'inventaire dès le début du jeu et disponible dans le magasin pour 
40 coins l'unité ou gratuitement dans les colis.
  - Jetpack : Le jetpack permet de voler librement dans les airs pendant 5 secondes ou 
pour 20 sauts. Le jetpack est disponible dans le magasin pour 150 coins l'unité ou 
gratuitement dans les colis.
  - CallStrike : Le CallStrike permet d'appeler une frappe aérienne infligeant des 
dégâts dans une zone ciblée. Le CallStrike est disponible dans le magasin pour 400 
coins l'unité ou gratuitement dans les colis.
  - CatConcrete : Le CatConcrete est une tête de chat en béton qui tombe du ciel, 
rebondissant et creusant la terre avant de disparaître. Le CatConcrete est disponible 
dans le magasin pour 200 coins l'unité ou gratuitement dans les colis.
  - Tower : La tower est une tourelle envoyée par le joueur qui tire automatiquement 
sur les ennemis à portée pendant un tour. La tower est disponible dans le magasin pour 
150 coins l'unité ou gratuitement dans les colis.
  - Mine : La mine est un explosif lancé par le joueur qui explose au moindre contact 
avec un ver. La mine est disponible dans le magasin pour 80 coins l'unité ou 
gratuitement dans les colis.
  - Grenade : La grenade est un explosif lancé par le joueur qui explose au bout de 
4 secondes. La grenade est disponible dans le magasin pour 150 coins l'unité ou 
gratuitement dans les colis.
  - Medkit : Le medkit soigne le ver actif de 50 points de vie et annule les 
altérations de statut. Le medkit est disponible dans le magasin pour 125 coins l'unité 
ou gratuitement dans les colis.
  - Trap : le trap est un piège lancé par le joueur qui blesse et empoisonne le ver qui 
marche dessus tant qu'il reste dessus, le trap ne disparait jamais. Le trap est 
disponible dans le magasin pour 150 coins l'unité ou gratuitement dans les colis.

## Comment lancer une partie depuis l'interface graphique ?

Pour lancer une partie, cliquez sur "Jouer" puis sur "Nouvelle partie". Ensuite, choisissez la map ou le
type de génération souhaité. Enfin, choisissez les paramètres de votre partie avant de cliquer à nouveau
"Jouer".  
Pour charger une partie, cliquez sur "Jouer", puis sur "Charger une partie" et choisissez la save de votre
choix et cliquez sur "Commencer".

## Comment contribuer ?

Pour contribuer à ce projet, se référer au fichier [CONTRIBUTING.md](/CONTRIBUTING.md).

## Licence

Pour visualiser la Licence, cliquer [ici](/LICENSE).

## Crédits

- LORIOT Baptiste
- MONA Matthys
- BRUNO Thomas
- DENQUIN Leo
- MOSCARDI Lorenzo
- MEYNAUD Louis