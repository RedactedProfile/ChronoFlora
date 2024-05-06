package com.ninjaghost.gamejam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Plant {

    private Sprite m_sprite;
    private Vector2 m_position;

    public String tag = "plant";

    public Plant(int x, int y) {
        m_sprite = new Sprite(new Texture("plants/001.png"));
        m_sprite.setSize(16, 16);
        m_position = new Vector2(x, y);
        m_sprite.setPosition(m_position.x, m_position.y);
        m_sprite.setFlip(false, true);
    }

    public void update(float delta) {
        if(m_sprite == null) return;
        
    }

    public void draw(SpriteBatch batch) {
        if(m_sprite == null) return;

        m_sprite.draw(batch);
    }

    public void checkCollision(Rectangle collider) {
        if(m_sprite == null) return;

        if(collider.overlaps(m_sprite.getBoundingRectangle())) {
            m_sprite = null;
        }
    }

}
