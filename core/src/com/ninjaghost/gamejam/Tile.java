package com.ninjaghost.gamejam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Tile {
    public enum LAYER { NONE, BOTTOM, FLOOR, PLAYER, CEILING, SKY };

    private LAYER m_layer = LAYER.NONE;
    private Sprite m_sprite;
    int x = 0;
    int y = 0;

    public Tile(String spritePath) {
        m_sprite = AssetRegistry.GetSprite(spritePath);
        if(m_sprite == null) {
            throw new IllegalArgumentException("Sprite path '" + spritePath + "' does not exist");
        }
    }
}
