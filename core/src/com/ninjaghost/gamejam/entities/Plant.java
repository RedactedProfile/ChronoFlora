package com.ninjaghost.gamejam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Plant {

    private Sprite m_sprite;
    private Vector2 m_position;

    public Plant(int x, int y) {
        m_sprite = new Sprite(new Texture("plants/001.png"));
        m_sprite.setSize(16, 16);
        m_position = new Vector2(x, y);
        m_sprite.setPosition(m_position.x, m_position.y);
        m_sprite.setFlip(false, true);
    }

    public void update(float delta) {

    }

    public void draw(SpriteBatch batch) {
        m_sprite.draw(batch);
    }

}
