# Décisions importantes de conception du projet

Ce document décrit les décisions de conception clés prises lors du développement de ce 
projet. Il vise à fournir un contexte et une justification pour les choix techniques et 
architecturaux effectués.

## 1. Choix de l'implémentation des collisions

### A) Technologie choisie :

Pour la gestion des collisions, nous nous sommes inpirés de la bibliothèque 
[JBump](https://github.com/implicit-invocation/jbump), qui est une bibliothèque de 
détection et de résolution de collisions en 2D. Pour cela, nous avons implémenté un 
système de **Swept Circle** en créant une hitbox circulaire à l'intérieur de chaque 
projectile. Pour les vers, nous les avons divisés en deux cercles l'un au-dessus de 
l'autre, inspiré du principe de "capsule".

### B) Justification du choix :

1. **Robustesse** : Le principe de Swept Circle calcule la trajectoire complète d'un 
objet en mouvement entre deux ticks, ce qui permet d'éviter les riques de tunneling.
2. **Précision** : Move calcule le temps d'impact exact (ti), ce qui permet de placer 
l'objet exactement au point de collision.
3. **Performance** : On itère uniquement les tuiles contenues dans l'AABB englobant le
mouvement, ce qui réduit le nombre de tests de collision nécessaires.
4. **Flexibilité des réponses** : En séparant la détection de la réponse, cela permet 
d'implémenter facilement différents types de réponses aux collisions (rebond, arrêt, 
glissement, etc.) selon l'objet.

## 2. Implémentation de la Sauvegarde des données d'une partie.

### A) Architecture choisie :
Pour la persistance des données, nous avons opté pour une architecture basée sur le **Data Transfer Object (DTO)** couplée à une sérialisation au format **JSON**.

L'implémentation permet de transformer une classe complexe en un conteneur passif facilement sérialisable :
* **`GameWorld` (classe complexe)** : C'est l'objet complexe qui contient tout ce dont le jeu a besoin pour correctement fonctionner.

* **`GameSave` (Conteneur Passif)** : C'est une classe conteneur simple, sans logique, qui ne contient que les données primitives nécessaires à la reconstruction de la partie (durée d'un tour, tour actuel, liste des entités, des équipes...). Les données trop complexes sont aussi aussi **mappées** dans des classes conteneurs (l'inventaire, le registre des armes, les équipes...).

Le processus de sauvegarde consiste à créer une instance de `GameSave` à partir d'une instance de `GameWorld`, le `GameSave` est ensuite transformée en chaîne de caractères par la bibliothèque [GSON](https://github.com/google/gson) pour l'écriture dans un fichier json.

### B) Justification du choix :
1.  **Découplage Données complexes/Données primaires** Le DTO permet de ne garder que les données essentielles, réduisant ainsi la taille des fichiers de sauvegarde.
2.  **Lisibilité et Résolution des Bugs** : Le format JSON a l'avantage d'être facilement lisible pour un humain. Cela permet de vérifier l'état des variables ou de modifier manuellement une sauvegarde pour tester des scénarios spécifiques.
3.  **Flexibilité** : Le JSON est un standard universel, facilitant l'ajout de fonctionnalités futures et de nouvelles données à sauvegarder.

## 3. Implémentation de notre propre moteur physique 

Nous avons décidé de coder notre propre moteur physique, plutôt que d'utiliser une bibliothèque. 

### A) Justification du choix : 
1. **Pas de surcharge** du code avec des méthodes de la bibliothèque inutiles et une bibliothèque finalement très peu utilisé en général
2. **Gestion des entités** : Une gestion des entités qui nous est propre, et qui est adaptable selon nos besoins grâce à une classe spécifique `PhysicsBody`.
