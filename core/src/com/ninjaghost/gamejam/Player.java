package com.ninjaghost.gamejam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

final public class Player {

    public boolean visible = true;

    private Sprite m_sprite;
    public final Vector2 targetPosition = new Vector2();
    public final Vector2 currentPosition = new Vector2();

    public final Vector2 lookAt = new Vector2();

    public float moveSpeed = 25f;
    public float movementVelocity = 0f;

    public Player() {
        targetPosition.set(150, 150);
        currentPosition.set(150, 150);
        m_sprite = new Sprite(new Texture("player/idle/idle_d_00.png"));
        m_sprite.flip(false, true);
    }

    public void update(float delta, GameplayInputState gameplayInputState) {
        Vector2 newPosition = new Vector2(targetPosition.x, targetPosition.y);
        float normalizedMoveSpeed = moveSpeed * delta;

        if(gameplayInputState.up) { newPosition.y -= normalizedMoveSpeed; }
        else if(gameplayInputState.down) { newPosition.y += normalizedMoveSpeed; }
        if(gameplayInputState.left) { newPosition.x -= normalizedMoveSpeed; }
        else if(gameplayInputState.right) { newPosition.x += normalizedMoveSpeed; }

        targetPosition.set(newPosition.x, newPosition.y);

        currentPosition.lerp(targetPosition, normalizedMoveSpeed / 2);
        movementVelocity = currentPosition.dst(targetPosition);
        m_sprite.setPosition(currentPosition.x, currentPosition.y);

        lookAt.x = targetPosition.x + m_sprite.getWidth() / 2;
        lookAt.y = targetPosition.y + m_sprite.getHeight() / 2;
    }

    public void draw(SpriteBatch batch) {
        m_sprite.draw(batch);
    }

}
