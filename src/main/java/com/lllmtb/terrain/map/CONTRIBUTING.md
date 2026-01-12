# Map

## Ajouter un bloc spécifique :

> **Important :** Le nom du fichier image doit correspondre **exactement** au nom de la Tile dans l'enum `Tile`, suivi par le numéro de la texture en commençant à 0. Il faudra aussi indiquer le nombre de textures pour ce bloc dans l'enum.

- Les textures de bloc sont à ajouter dans un sous dossier de _**ressources/textures/block**_

## Ajouter une map spécifique :

- Les dossiers de map sont à ajouter dans _**ressources/map**_
  - Un dossier ayant comme nom le nom de la map, contenant :
    - `map.txt` : fichier texte contenant pour chaque ligne et chaque colonne la lettre correspondant à la tile souhaitée à cette position.
        - **Conseil :** Idéalement, la map doit avoir une hauteur de 0.56 fois sa largeur (exemple : une dimension de 250x140 tiles).
    - `info.txt` : fichier contenant une courte description de la map.
    - `image.png` : une image de la map.