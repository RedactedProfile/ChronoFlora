package com.ninjaghost.gamejam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PlantItem {

    public enum MODE { COLLECTABLE, INVENTORY }
    MODE m_mode = MODE.INVENTORY;

    boolean visible = true;
    boolean isCollectable = false;
    boolean isSpawnAnimationDone = false;

    private Sprite m_sprite;
    private Vector2 m_position;

    public String tag = "item";

    float isCollectableTimer = 0f;
    float getIsCollectableTimerMax = 1f;

    public PlantItem(int x, int y) {
        m_sprite = new Sprite(new Texture("plants/001_item.png"));
        m_sprite.setSize(8, 8);
        m_position = new Vector2(x, y);
        m_sprite.setPosition(m_position.x, m_position.y);
        m_sprite.setFlip(false, true);

        visible = false;
    }

    public void spawnItem() {
        if(m_sprite == null) return;
        m_mode = MODE.COLLECTABLE;
        visible = true;


        m_position = new Vector2(m_sprite.getX() - 5f, m_sprite.getY() - 5f);
        m_sprite.setPosition(m_position.x, m_position.y);
    }

    public void update(float delta) {
        if(m_sprite == null) return;

        if(!isCollectable && m_mode == MODE.COLLECTABLE && isCollectableTimer < getIsCollectableTimerMax) {
            // item is a collectable item but we aren't quite collectable yet
            isCollectableTimer += delta;
        }

        if(!isCollectable && m_mode == MODE.COLLECTABLE && !isSpawnAnimationDone) {
            // do the next frame of the bounce animation here

            // we're going to do a kind of bounce effect from the location registered
            // but for now let's just put it in the "spot" it's going to wind up at

        }

        if(!isCollectable && m_mode == MODE.COLLECTABLE && isCollectableTimer >= getIsCollectableTimerMax) {
            isCollectable = true;
        }

        if(isCollectable) {
            // this is the area where we magnetize toward the player if they're close enough

            // animate with a bouncing effect
            isCollectableTimer += delta; // this is for the purposes of the bounce effect
            float newY = MathUtils.sin(MathUtils.PI2 * 0.9f * isCollectableTimer) * 0.1f;
            m_position.y = m_position.y + newY;
            m_sprite.setPosition(m_position.x, m_position.y);
        }
    }

    public void draw(SpriteBatch batch) {
        if(m_sprite == null && visible) return;

        m_sprite.draw(batch);
    }

    public void checkCollision(Rectangle collider) {
        if(m_sprite == null && visible) return;
    }

}
