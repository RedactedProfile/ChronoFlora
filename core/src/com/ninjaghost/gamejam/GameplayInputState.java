package com.ninjaghost.gamejam;

public class GameplayInputState {
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    public boolean action = false;

    public void reset() {
        up = down = left = right = action = false;
    }
}
