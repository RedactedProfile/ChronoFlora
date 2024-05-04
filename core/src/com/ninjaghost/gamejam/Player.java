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

    private Map<String, Map<String, Animation<TextureRegion>>> animationBank;
    private String playerDirection = "right";
    private String animationState = "idle";

    public Player() {
        targetPosition.set(150, 150);
        currentPosition.set(150, 150);

        load();

        m_sprite = new Sprite(new Texture("player/idle/idle_d_00.png"));
        m_sprite.flip(false, true);
    }


    public void update(float delta, GameplayInputState gameplayInputState) {
        animationTimer += delta;

        Vector2 newPosition = new Vector2(targetPosition.x, targetPosition.y);
        float normalizedMoveSpeed = moveSpeed * delta;
        if(gameplayInputState.shift) normalizedMoveSpeed *= 1.75f;

        if(gameplayInputState.up) { newPosition.y -= normalizedMoveSpeed; playerDirection = "up"; }
        else if(gameplayInputState.down) { newPosition.y += normalizedMoveSpeed; playerDirection = "down"; }
        if(gameplayInputState.left) { newPosition.x -= normalizedMoveSpeed; playerDirection = "left"; }
        else if(gameplayInputState.right) { newPosition.x += normalizedMoveSpeed; playerDirection = "right"; }

        targetPosition.set(newPosition.x, newPosition.y);

        currentPosition.lerp(targetPosition, normalizedMoveSpeed / 2);
        movementVelocity = currentPosition.dst(targetPosition);
        m_sprite.setPosition(currentPosition.x, currentPosition.y);

        lookAt.x = targetPosition.x + m_sprite.getWidth() / 2;
        lookAt.y = targetPosition.y + m_sprite.getHeight() / 2;

        if(movementVelocity <= 0.05) {
            animationState = "idle";
        } else if(movementVelocity > 0.05) {
            animationState = "walk";
            if(gameplayInputState.shift) {
                animationState = "run";
            }
        }

        m_sprite.setRegion(animationBank.get(animationState).get(playerDirection).getKeyFrame(animationTimer, true));
        m_sprite.flip(false, true);
    }

    public void draw(SpriteBatch batch) {
        m_sprite.draw(batch);
    }

    private void load() {

        // Load Animations
        animationBank = Map.of(
            "idle", Map.of(
                "down", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/idle/idle_d_00.png")),
                    new TextureRegion(new Texture("player/idle/idle_d_01.png")),
                    new TextureRegion(new Texture("player/idle/idle_d_02.png")),
                    new TextureRegion(new Texture("player/idle/idle_d_03.png")),
                }),
                "up", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/idle/idle_u_00.png")),
                    new TextureRegion(new Texture("player/idle/idle_u_01.png")),
                    new TextureRegion(new Texture("player/idle/idle_u_02.png")),
                    new TextureRegion(new Texture("player/idle/idle_u_03.png")),
                }),
                "left", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/idle/idle_l_00.png")),
                    new TextureRegion(new Texture("player/idle/idle_l_01.png")),
                    new TextureRegion(new Texture("player/idle/idle_l_02.png")),
                    new TextureRegion(new Texture("player/idle/idle_l_03.png")),
                }),
                "right", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/idle/idle_r_00.png")),
                    new TextureRegion(new Texture("player/idle/idle_r_01.png")),
                    new TextureRegion(new Texture("player/idle/idle_r_02.png")),
                    new TextureRegion(new Texture("player/idle/idle_r_03.png")),
                })
            ),
            "walk", Map.of(
                "down", new Animation<>(0.2f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_d_00.png")),
                    new TextureRegion(new Texture("player/run/run_d_01.png")),
                    new TextureRegion(new Texture("player/run/run_d_02.png")),
                }),
                "up", new Animation<>(0.2f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_u_00.png")),
                    new TextureRegion(new Texture("player/run/run_u_01.png")),
                    new TextureRegion(new Texture("player/run/run_u_02.png")),
                }),
                "left", new Animation<>(0.2f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_l_00.png")),
                    new TextureRegion(new Texture("player/run/run_l_01.png")),
                    new TextureRegion(new Texture("player/run/run_l_02.png")),
                }),
                "right", new Animation<>(0.2f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_r_00.png")),
                    new TextureRegion(new Texture("player/run/run_r_01.png")),
                    new TextureRegion(new Texture("player/run/run_r_02.png")),
                })
            ),
            "run", Map.of(
                "down", new Animation<>(0.1f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_d_00.png")),
                    new TextureRegion(new Texture("player/run/run_d_01.png")),
                    new TextureRegion(new Texture("player/run/run_d_02.png")),
                }),
                "up", new Animation<>(0.1f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_u_00.png")),
                    new TextureRegion(new Texture("player/run/run_u_01.png")),
                    new TextureRegion(new Texture("player/run/run_u_02.png")),
                }),
                "left", new Animation<>(0.1f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_l_00.png")),
                    new TextureRegion(new Texture("player/run/run_l_01.png")),
                    new TextureRegion(new Texture("player/run/run_l_02.png")),
                }),
                "right", new Animation<>(0.1f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/run/run_r_00.png")),
                    new TextureRegion(new Texture("player/run/run_r_01.png")),
                    new TextureRegion(new Texture("player/run/run_r_02.png")),
                })
            ),
            "attack", Map.of(
                "down", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/attack/attack_d_00.png")),
                    new TextureRegion(new Texture("player/attack/attack_d_01.png")),
                    new TextureRegion(new Texture("player/attack/attack_d_02.png")),
                    new TextureRegion(new Texture("player/attack/attack_d_03.png")),
                    new TextureRegion(new Texture("player/attack/attack_d_03.png")),
                }),
                "up", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/attack/attack_u_00.png")),
                    new TextureRegion(new Texture("player/attack/attack_u_01.png")),
                    new TextureRegion(new Texture("player/attack/attack_u_02.png")),
                    new TextureRegion(new Texture("player/attack/attack_u_03.png")),
                    new TextureRegion(new Texture("player/attack/attack_u_04.png")),
                }),
                "left", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/attack/attack_l_00.png")),
                    new TextureRegion(new Texture("player/attack/attack_l_01.png")),
                    new TextureRegion(new Texture("player/attack/attack_l_02.png")),
                    new TextureRegion(new Texture("player/attack/attack_l_03.png")),
                    new TextureRegion(new Texture("player/attack/attack_l_04.png")),
                }),
                "right", new Animation<>(0.5f, new TextureRegion[]{
                    new TextureRegion(new Texture("player/attack/attack_r_00.png")),
                    new TextureRegion(new Texture("player/attack/attack_r_01.png")),
                    new TextureRegion(new Texture("player/attack/attack_r_02.png")),
                    new TextureRegion(new Texture("player/attack/attack_r_03.png")),
                    new TextureRegion(new Texture("player/attack/attack_r_04.png")),
                })
            )
        );
    }
}
