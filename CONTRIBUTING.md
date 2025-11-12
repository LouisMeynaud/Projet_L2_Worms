# Contribuer à Worms MI4

1) Cloner la branche dev.
2) Créer sa branche de travail avec un nom explicite.
3) push sa branche pour définir la distante comme amont avec :
```bash
git push --set-upstream origin <nom de la branche>
```
4) Faire ses changements.
5) Commit ses changements avec : 
```bash
git add <fichier>...
```
puis
```bash
git commit -m "description concise mais explicite des changements"
```
6) push sa branche avec :
```bash
git push
```
7) Si la pipeline passe, créer une merge request sur dev.