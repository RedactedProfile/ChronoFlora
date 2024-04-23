package com.ninjaghost.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ArrayMap;

public class AssetRegistry {

    // Sprite storage so sprites only need to be loaded once, and instantiated however many times as needed
    public static ArrayMap<String, Sprite> sprites = new ArrayMap<>();

    public static Sprite GetSprite(String path) {
        if(!sprites.containsKey(path)) {
            FileHandle file = Gdx.files.internal(path);
            if(file.exists()) {
                Texture texture = new Texture(path);
                sprites.put(path, new Sprite(texture));
            }
        }

        return sprites.containsKey(path) ? sprites.get(path) : null;
    }
}

