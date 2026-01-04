package com.lllmtb.ui;

import org.junit.jupiter.api.Test;

import com.lllmtb.ui.graphical.Utilitaire.TextureManager;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class TextureManagerTest {

    @Test
    void testLoadTexture() throws IOException {

        URL defaultUrl = TextureManager.class.getResource("/default.png");
        assertNotNull(defaultUrl);

        BufferedImage resultImage = TextureManager.loadImage("/textures/testtttttttttttttt.png");
        assertNotNull(resultImage, "le textureManager ne devrais pas renvoyer nul meme si la texture n'existe pas");
    }
}