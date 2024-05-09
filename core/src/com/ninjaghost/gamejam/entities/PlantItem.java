package com.ninjaghost.gamejam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ninjaghost.gamejam.GameSettingsState;

import java.util.concurrent.ThreadLocalRandom;

public class PlantItem {

    public enum MODE { COLLECTABLE, INVENTORY }
    MODE m_mode = MODE.INVENTORY;

    boolean visible = true;
    boolean isSpawnAnimationDone = false;

    private Sprite m_sprite;
    private Vector2 m_position;

    public String tag = "item";

    // Inventory Mode stuff
    public int stackCount = 0;
    public int stackCountMax = 20;

    // Collectable Item Mode Stuff
    boolean isCollectable = false;
    float gravityVelocity = 0f;
    float isCollectableTimer = 0f;
    float animationTimer = 0f;
    float getIsCollectableTimerMax = 0.8f;
    float animationTimerMax = 0.8f;
    float popHorizontalDirection = 4f;

    public PlantItem(int x, int y) {
        m_sprite = new Sprite(new Texture("plants/001_item.png"));
        m_sprite.setSize(8, 8);
        m_position = new Vector2(x, y);
        m_sprite.setPosition(m_position.x, m_position.y);
        m_sprite.setFlip(false, true);

        visible = false;
    }

    public boolean canStack() {
        return stackCount < stackCountMax;
    }

    public void addStack() {
        stackCount++;
    }

    public int getStackCount() {
        return stackCount;
    }

    public Sprite getSprite() {
        return m_sprite;
    }

    public void spawnItem() {
        m_mode = MODE.INVENTORY;
        m_sprite.setSize(32, 32);
        m_sprite.setFlip(false, false);
    }

    public void spawnCollectable() {
        if(m_sprite == null) return;
        m_mode = MODE.COLLECTABLE;
        visible = true;

//        collectableTargetPosition = new Vector2(m_sprite.getX() - 5f, m_sprite.getY() - 5f);
        m_sprite.setSize(8, 8);
        m_sprite.setPosition(m_position.x, m_position.y);
    }

    public void update(float delta) {
        if(m_sprite == null) return;

        if(!isCollectable && m_mode == MODE.COLLECTABLE && isCollectableTimer < getIsCollectableTimerMax) {
            // item is a collectable item but we aren't quite collectable yet
            isCollectableTimer += delta;
        }

        if(!isCollectable && m_mode == MODE.COLLECTABLE && !isSpawnAnimationDone) {
            boolean start = false;
            if(animationTimer <= 0f) {
                start = true;

                // starting out we want a solid set for velocity
                gravityVelocity = GameSettingsState.itemJumpFactor[0];
                popHorizontalDirection = ThreadLocalRandom.current().nextFloat(-8f, 8f);
            }

            // do the next frame of the bounce animation here
            animationTimer += delta;

            // we're going to do a kind of bounce effect from the location registered
            if(!start) {
                gravityVelocity += -GameSettingsState.itemGravityFactor[0] * delta;
            }

            m_position.x += popHorizontalDirection * delta;
            m_position.y += gravityVelocity * delta;
            m_sprite.setPosition(m_position.x, m_position.y);
        }

        if(!isSpawnAnimationDone && m_mode == MODE.COLLECTABLE && animationTimer >= animationTimerMax) {
            isSpawnAnimationDone = true;
        }

        if(!isCollectable && m_mode == MODE.COLLECTABLE && isCollectableTimer >= getIsCollectableTimerMax) {
            isCollectable = true;
        }


        if(isCollectable) {
            // animate with a bouncing effect
            isCollectableTimer += delta; // this is for the purposes of the bounce effect
            float newY = MathUtils.sin(MathUtils.PI2 * 0.9f * isCollectableTimer) * 0.1f;
            m_position.y = m_position.y + newY;
            m_sprite.setPosition(m_position.x, m_position.y);

            if(GameSettingsState.gameInstance.playerCanCollect(this)) {
                // this is the area where we magnetize toward the player if they're close enough
                if(GameSettingsState.gameInstance.getPlayer().currentPosition.dst(m_position) <= 25f) {
                    m_position.lerp(GameSettingsState.gameInstance.getPlayer().currentPosition, 5f * delta);
                    m_sprite.setPosition(m_position.x, m_position.y);
                }

                if(GameSettingsState.gameInstance.getPlayer().getSprite().getBoundingRectangle().overlaps(m_sprite.getBoundingRectangle())) {
                    // do collection, wipe this instance clean
                    GameSettingsState.gameInstance.doCollectItem(this);
                }
            }
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
