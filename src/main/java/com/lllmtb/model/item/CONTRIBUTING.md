# ITEMS


## Implémentation d'une nouvelle arme

Pour ajouter une arme fonctionnelle dans le jeu, suivez ces étapes :

1.  **Création de la classe :** Créez une nouvelle classe pour votre arme qui doit extends de la classe abstraite `Weapon`.
2.  **Logique de tir :** Implémentez la méthode `fire`. Celle-ci doit retourner une liste de `GameCommand` définissant l'action de tir (généralement la création d'un projectile via `RegisterEntityCommand`). Vous pouvez utiliser un projectile existant ou en créer un nouveau.
3.  **Textures :** Ajoutez les fichiers images correspondants dans les dossiers de textures d'armes.
4.  **Définition (WeaponId) :** Ajoutez l'arme dans l'énumération `WeaponId`. Vous devrez y configurer :
    * Le mode de tir (`ShootMode`).
    * La description de l'arme.
    * Le temps de charge maximum (si nécessaire).
5.  **Enregistrement (Registry) :** Ajoutez l'arme dans le `WeaponRegistry` pour lier l'identifiant (`WeaponId`) à votre nouvelle classe.
6.  **Boutique (Shop) :** Enfin, ajoutez l'arme dans `ShopElement` pour la rendre disponible à l'achat, en définissant ses prix et quantités de munitions à acheter.
7.  *(Optionnel)* **Son de tir :** Pour ajouter un effet sonore lors du tir, placez un fichier `.wav` dans le dossier _**resources/sound/**_. Le fichier doit être nommé selon le format : `<WeaponId>Shot.wav` (ex : `BAZOOKAShot.wav`).

---

## Implémentation d'un nouvel outil

Pour ajouter un outil fonctionnel (utilitaire, attaque spéciale, soin), suivez ces étapes :

1.  **Création de la classe :** Créez une nouvelle classe pour votre outil qui doit extends de la classe abstraite `Tool`.
2.  **Logique d'utilisation :** Implémentez la méthode `use`. Celle-ci doit retourner une liste de `GameCommand` définissant l'action de l'outil (soin, invocation, déplacement, etc.).
3.  **Textures :** Ajoutez les fichiers images correspondants dans les dossiers de textures d'outils.
4.  **Définition (ToolId) :** Ajoutez l'outil dans l'énumération `ToolId`. Vous devrez y configurer :
    * Le mode de visée (`ShootMode`) : `NONE` (aucun viseur), `CROSSHAIR` (cible), ou `AIM` (lancé/visé).
    * La description de l'outil.
    * Le temps de charge maximum (optionnel).
5.  **Enregistrement (Registry) :** Ajoutez l'outil dans le `ToolRegistry` pour lier l'identifiant (`ToolId`) à votre nouvelle classe.
6.  **Boutique (Shop) :** Ajoutez l'outil dans `ShopElement` pour le rendre disponible à l'achat, en définissant ses prix et quantités.

------

## Implémentation des textures

> **Important :** Le nom du fichier image doit correspondre **exactement** au nom de l'arme ou de l'outil défini dans le `WeaponId` / `ToolId`.

## Ajouter une texture d'arme :

- Les textures d'armes sont à ajouter dans un sous dossier de _**ressources/textures/weapon**_
  - Pour les munitions des armes : **_/ammo_**
  - Pour la texture des armes non débloquées dans l'inventaire : **_/lockedWeapon_**
  - Pour la texture des armes débloquées dans l'inventaire : **_/weapon_**
- Pour le sprite en main de l'arme : **_ressources/sprites/weapon_**

## Ajouter une texture d'outil :

- Les textures d'outils sont à ajouter dans un sous dossier de _**ressources/textures/tool**_
  - Pour les munitions d'outils : **_/ammo_**
  - Pour la texture des outils dans l'inventaire : **_/tool_**
- Pour le sprite en main de l'outil : **_ressources/sprites/tool_**