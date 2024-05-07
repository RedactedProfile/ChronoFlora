package com.ninjaghost.gamejam;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;

public class GameInputProcessor implements InputProcessor {

    GameplayInputState gameplayInputState;

    public GameInputProcessor(GameplayInputState gameplayInputState) {
        this.gameplayInputState = gameplayInputState;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.UP:
                System.out.println("Move forward");
                gameplayInputState.up = true;
                break;
            case Keys.S:
            case Keys.DOWN:
                System.out.println("Move backward");
                gameplayInputState.down = true;
                break;
            case Keys.A:
            case Keys.LEFT:
                System.out.println("Move left");
                gameplayInputState.left = true;
                break;
            case Keys.D:
            case Keys.RIGHT:
                System.out.println("Move right");
                gameplayInputState.right = true;
                break;
            case Keys.SHIFT_LEFT:
                System.out.println("Shift left");
                gameplayInputState.shift = true;
                break;
            case Keys.SPACE:
                System.out.println("Jump");
                gameplayInputState.action = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.UP:
                gameplayInputState.up = false;
                break;
            case Keys.S:
            case Keys.DOWN:
                gameplayInputState.down = false;
                break;
            case Keys.A:
            case Keys.LEFT:
                gameplayInputState.left = false;
                break;
            case Keys.D:
            case Keys.RIGHT:
                gameplayInputState.right = false;
                break;
            case Keys.SHIFT_LEFT:
                gameplayInputState.shift = false;
                break;
            case Keys.SPACE:
                gameplayInputState.action = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // Handle character input if necessary
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
            System.out.println("Left mouse button pressed at (" + screenX + ", " + screenY + ")");
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Handle touch release if necessary
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Handle drag if necessary
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Handle mouse move if necessary
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        System.out.println("Mouse scrolled");
        return true;
    }
}
