package com.ninjaghost.gamejam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

final public class Player {

    public boolean visible = true;

    private Sprite m_sprite;
    public final Vector2 targetPosition = new Vector2();
    public final Vector2 currentPosition = new Vector2();

    public final Vector2 lookAt = new Vector2();

    public float moveSpeed = 25f;
    public float movementVelocity = 0f;


    private TextureRegion[] frames = new TextureRegion[4];
    private Animation<TextureRegion> frameAnimation;
    private float animationTimer = 0f;

    private Map<String, Map<String, String>> animationBank;

    public Player() {
        targetPosition.set(150, 150);
        currentPosition.set(150, 150);

        load();

        m_sprite = new Sprite(frames[0]);
        m_sprite.flip(false, true);
    }


    public void update(float delta, GameplayInputState gameplayInputState) {
        animationTimer += delta;

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

        m_sprite.setRegion(frameAnimation.getKeyFrame(animationTimer, true));
        m_sprite.flip(false, true);
    }

    public void draw(SpriteBatch batch) {
        m_sprite.draw(batch);
    }

    private void load() {

        frames = new TextureRegion[] {
                new TextureRegion(new Texture("player/idle/idle_d_00.png")),
                new TextureRegion(new Texture("player/idle/idle_d_01.png")),
                new TextureRegion(new Texture("player/idle/idle_d_02.png")),
                new TextureRegion(new Texture("player/idle/idle_d_03.png")),
        };

        frameAnimation = new Animation<>(0.5f, frames);


        animationBank = Map.of(
                "idle", Map.of(
                        "down", "idling down"
                ),
                "walk", Map.of(
                        "down", "walking down"
                )
        );

        System.out.printf("Anim state: %s:%s = %s%n", "idle", "down", animationBank.get("idle").get("down"));

//        animationBank["idle"]["down"] = new TextureRegion[] {};

//        animationBank.put("idle", Map.of(
//                { "down", new TextureRegion[] {} }
//        ));

    }
}
