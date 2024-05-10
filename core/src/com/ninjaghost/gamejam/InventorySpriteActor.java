package com.ninjaghost.gamejam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ninjaghost.gamejam.entities.PlantItem;

public class InventorySpriteActor extends Actor {
    private Sprite sprite;
    boolean isSelected = false;

    public PlantItem data;

    TextureAtlas invMeterAtlas;
    Sprite invMeterSprite;

    Sprite invSelected;

    public int slot = 0;
    public int activeSlot = 0;

    public InventorySpriteActor(Sprite _sprite) {
        this.sprite = _sprite;
        setSize(sprite.getWidth(), sprite.getHeight());
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

        invMeterAtlas = new TextureAtlas(Gdx.files.internal("ui/inv_capacity_meter/InventoryMeter.atlas"));
        invMeterSprite = new Sprite(invMeterAtlas.findRegion("inv_capacity_meter01"));

        invSelected = new Sprite(new Texture("ui/inv_selected.png"));
//        invSelected.setScale(1.7f);

        if(activeSlot == slot) {
            isSelected = true;
        }
    }

    public void update(float delta) {
        // we choose a meter sprite based on percentage
        int frames = invMeterAtlas.getRegions().size;
        float percent = (float) data.stackCount / data.stackCountMax;
        int frame = (int) Math.ceil( frames * percent );
        if(frame == 0) {
            frame = 1; // hack because 00 doesnt exist
        }
        String s_frame = String.valueOf(frame);
        if(frame < 10) {
            s_frame = "0" + s_frame;
        }
        s_frame = "inv_capacity_meter" + s_frame;
        invMeterSprite = new Sprite(invMeterAtlas.findRegion(s_frame));

        isSelected = false;
        if(activeSlot == slot) {
            isSelected = true;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isSelected) {
            invSelected.setSize(52, 48);
            invSelected.setPosition(getX() - 11f, getY() - 8f);
            invSelected.draw(batch);
        }
        sprite.setPosition(getX() - 5f, getY());
        sprite.draw(batch);
        invMeterSprite.setPosition(getX() + 30f, getY());
        invMeterSprite.draw(batch);


    }
}
