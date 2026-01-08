"""
script python , permettant de passer d'un dessins (dessiner sur piskelapp.com) a un ficher txt map utiliser sur https://colab.research.google.com
"""

from google.colab import files
from PIL import Image
import numpy as np
import io

input_filename = "image.png"
width = 250
height = 140

# Définition des couleurs utilisées dans l'image
COLOR_AIR = np.array([135, 206, 235])       # Bleu clair
COLOR_DIRT = np.array([139, 69, 19])        # Marron
COLOR_WATER = np.array([0, 0, 150])         # Bleu foncé
COLOR_CAVERN = np.array([78, 52, 25])       # Marron foncé
COLOR_GRASS = np.array([0, 241, 44])        # Vert kaki
COLOR_UNDERDIRT = np.array([255, 0, 253])   # Rose bonbon
COLOR_ICE = np.array([192, 135, 235])       # Violet
COLOR_SNOW = np.array([255, 255, 255])      # Blanc
COLOR_LAVA = np.array([255, 118, 0])        # Orange
COLOR_MOONGRASS = np.array([148, 146, 145]) # Gris clair
COLOR_MOONDIRT = np.array([77, 77, 75])     # Gris foncé
COLOR_MOONUNDERDIRT = np.array([0, 0, 0])   # Noir


img = Image.open(io.BytesIO(uploaded[input_filename])).convert('RGB')
pixels = np.array(img) #transforme l'image en tableau numpy

output_lines = [] #crée le tableau qui contiendra les lignes du fichier txt


for y in range(height):
    line = ""
    for x in range(width):
        pixel = pixels[y][x]        
        pixel_list = pixel.tolist() #convertit la liste numpy en liste python pour comparaison

        if pixel_list == COLOR_WATER.tolist():
            line += 'W'
        elif pixel_list == COLOR_DIRT.tolist():
            line += 'D'
        elif pixel_list == COLOR_CAVERN.tolist():
            line += 'C'
        elif pixel_list == COLOR_GRASS.tolist():
            line += 'G'
        elif pixel_list == COLOR_UNDERDIRT.tolist():
            line += 'U'
        elif pixel_list == COLOR_ICE.tolist():
            line += 'I'
        elif pixel_list == COLOR_SNOW.tolist():
            line += 'S'
        elif pixel_list == COLOR_LAVA.tolist():
            line += 'L'
        elif pixel_list == COLOR_MOONGRASS.tolist():
            line += 'M'
        elif pixel_list == COLOR_MOONDIRT.tolist():
            line += 'N'
        elif pixel_list == COLOR_MOONUNDERDIRT.tolist():
            line += 'X'
        elif pixel_list == COLOR_AIR.tolist():
            line += 'A'
        output_lines.append(line)

    with open('map.txt', 'w') as f:
        f.write("\n".join(output_lines))
    files.download('map.txt')

