package com.ninjaghost.gamejam;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;

public class GameInputProcessor implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                System.out.println("Move forward");
                break;
            case Keys.S:
                System.out.println("Move backward");
                break;
            case Keys.A:
                System.out.println("Move left");
                break;
            case Keys.D:
                System.out.println("Move right");
                break;
            case Keys.SPACE:
                System.out.println("Jump");
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Handle key release if necessary
        return false;
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
