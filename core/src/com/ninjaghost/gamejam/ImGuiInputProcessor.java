package com.ninjaghost.gamejam;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import imgui.ImGui;
import imgui.ImGuiIO;

public class ImGuiInputProcessor implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {
        ImGuiIO io = ImGui.getIO();
        io.setKeysDown(keycode, true);
        return false; // Return true if you want to indicate the event was handled
    }

    @Override
    public boolean keyUp(int keycode) {
        ImGuiIO io = ImGui.getIO();
        io.setKeysDown(keycode, false);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        ImGuiIO io = ImGui.getIO();
        if (character != 0x7F && character >= 0x20) { // Filter out non-printable characters
            io.addInputCharacter(character);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        ImGuiIO io = ImGui.getIO();
        if (button == Input.Buttons.LEFT) {
            io.setMouseDown(0, true);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        ImGuiIO io = ImGui.getIO();
        if (button == Input.Buttons.LEFT) {
            io.setMouseDown(0, false);
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        ImGuiIO io = ImGui.getIO();
        io.setMousePos(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        ImGuiIO io = ImGui.getIO();
        io.setMouseWheel(-amountY); // Invert scroll direction
        return false;
    }
}
