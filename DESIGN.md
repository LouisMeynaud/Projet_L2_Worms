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