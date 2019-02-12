package com.example.parklayoutsimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

class SpriteGenerator {

    public static Sprite generateParkingSpace() {
        Pixmap pixmap = new Pixmap(256, 128, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        pixmap.setColor(Color.YELLOW);
        pixmap.drawRectangle(0, 0, 256, 128);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();   // Now, the pixmap is the textures responsibility.
        return new Sprite(texture);
    }
}
