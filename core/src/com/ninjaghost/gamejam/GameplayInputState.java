package com.ninjaghost.gamejam;

public class GameplayInputState {
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    public boolean action = false;
    public boolean shift = false;
    public boolean escape = false;

    public boolean invOne = false;
    public boolean invTwo = false;
    public boolean invThree = false;
    public boolean invFour = false;
    public boolean invFive = false;
    public boolean invSix = false;
    public boolean invRight = false;
    public boolean invLeft = false;

    public void reset() {
        up = down = left = right = action = shift = escape = false;
        invOne = invTwo = invThree = invFour = invFive = invSix = invRight = invLeft = false;
    }
}
