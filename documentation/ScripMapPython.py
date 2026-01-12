"""
script python , permettant de passer d'un dessins (dessiner sur piskelapp.com) a un ficher txt map utiliser sur https://colab.research.google.com
"""

from google.colab import files
from PIL import Image
import numpy as np
import io

uploaded = files.upload()
input_filename = next(iter(uploaded))

map_width = 250
map_height = 140

# Color → character mapping
COLOR_MAP = {
    'A': np.array([135, 206, 235]),   # AIR #87CEEB
    'D': np.array([139, 69, 19]),     # DIRT #8B4513
    'W': np.array([0, 0, 150]),       # WATER #000096
    'C': np.array([78, 52, 25]),      # CAVERN #4E3419
    'G': np.array([0, 241, 44]),      # GRASS #00F12C
    'U': np.array([255, 0, 253]),     # UNDERDIRT #FF00FD
    'I': np.array([192, 135, 235]),   # ICE
    'S': np.array([255, 255, 255]),   # SNOW
    'Y': np.array([0, 0, 255]),       # ICEUNDERDIRT
    'L': np.array([255, 118, 0]),     # LAVA #FF7600
    'M': np.array([148, 146, 145]),   # MOONGRASS
    'N': np.array([77, 77, 75]),      # MOONDIRT
    'X': np.array([0, 0, 0]),         # MOONUNDERDIRT
    'H': np.array([0, 255, 255]),     # HOLYWATER #00FFFF
    'V': np.array([255, 255, 0]),     # VINE #FFFF00
    'Q': np.array([100, 100, 50]),    # DIRTVINE #646432
    'E': np.array([0, 150, 0]),       # GRASSVINE #009600
    'K': np.array([0, 50, 0]),        # CAVERNVINE #003200
    'T': np.array([255, 0, 150]),     # TRAP #FF0000
    'Z': np.array([111, 62, 212]),    # TRAPCAVERN #6f3ed4
}

def closest_char(pixel):
    return min(
        COLOR_MAP,
        key=lambda c: np.linalg.norm(pixel - COLOR_MAP[c])
    )

try:
    img = Image.open(io.BytesIO(uploaded[input_filename])).convert("RGB")
    pixels = np.array(img)

    output_lines = [
        "".join(
            closest_char(pixels[y, x])
            for x in range(map_width)
        )
        for y in range(map_height)
    ]

    with open("map.txt", "w") as f:
        f.write("\n".join(output_lines))

    files.download("map.txt")

except Exception as e:
    print(f"Erreur : {e}")
