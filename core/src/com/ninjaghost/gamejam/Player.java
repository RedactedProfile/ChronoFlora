package com.ninjaghost.gamejam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

final public class Player {

    public boolean visible = true;

    private Sprite m_sprite;
    public final Vector2 position = new Vector2();

    public float moveSpeed = 25f;

    public Player() {
        position.set(0, 0);
        m_sprite = new Sprite(new Texture("player/idle/idle_d_00.png"));
        m_sprite.flip(false, true);
    }

    public void update(float delta, GameplayInputState gameplayInputState) {
        Vector2 newPosition = new Vector2(position.x, position.y);
        float normalizedMoveSpeed = moveSpeed * delta;

        if(gameplayInputState.up) { newPosition.y -= normalizedMoveSpeed; }
        else if(gameplayInputState.down) { newPosition.y += normalizedMoveSpeed; }
        if(gameplayInputState.left) { newPosition.x -= normalizedMoveSpeed; }
        else if(gameplayInputState.right) { newPosition.x += normalizedMoveSpeed; }

        position.set(newPosition.x, newPosition.y);

        m_sprite.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch) {
        m_sprite.draw(batch);
    }

}
