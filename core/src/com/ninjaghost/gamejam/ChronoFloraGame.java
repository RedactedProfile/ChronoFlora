package com.ninjaghost.gamejam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ninjaghost.gamejam.entities.Plant;
import com.ninjaghost.gamejam.entities.PlantItem;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImFloat;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ChronoFloraGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    BitmapFont bitmapFont;

    boolean showImGui = false;
    ImGuiImplGl3 imGuiImplGl3;


    Stage uiStage;
    Skin uiSkin;

    OrthographicCamera camera;
    float maxZoom = 0.2f;
    float minZoom = 0.3f;
    float[] cameraTargetZoomFactor = new float[] { maxZoom };
    float[] calculatedZoomFactor = new float[] { minZoom };
    float zoomSpeed = 1f;
    float[] cameraFocus = new float[] { 0f, 0f };
    float cameraFocusSpeed = 1f;

    ArrayList<Float> fpsHistory = new ArrayList<>();

    ArrayList<Tile> tiles = new ArrayList<>();
    int tilemapBreak = 64;
    int puzzlemapBreak = 12;
    int[] tilemap = new int[ tilemapBreak * tilemapBreak ];
    int[] puzzlemap = new int[ puzzlemapBreak * puzzlemapBreak ];
    int puzzlemapBridgeLength = 6;
    int puzzlemapOffset = -((puzzlemapBreak * 16) + (puzzlemapBridgeLength * 16));

    // UI Widgets
    ArrayList<TextField> _fields;
    TextArea _field2;
    Table _panel;
    TextArea _timer;

    // Playable NPC Object
    Player player;

    // Inventory
    ArrayList<PlantItem> playerInventory = new ArrayList<>();
    int playerInventoryStackMax = 6;
    int activeInventorySlot = 1;
    int activeTileX = 0,
        activeTileY = 0;

    // Controller
    GameplayInputState gameplayInputState;

    // Flora related stuff
    ArrayList<Plant> plants = new ArrayList<>();
    ArrayList<PlantItem> plantItems = new ArrayList<>();
    ArrayList<PlantItem> plantItemsForDeletion = new ArrayList<>();

    // Shape Rendering (Usually debug stuff)
    ShapeRenderer shapeRenderer;
    ArrayList<Rectangle> rectanglesToRender = new ArrayList<>();

    // Audio Related Stuff
    HashMap<String, Sound> sounds = new HashMap<>();

    float countdownTimer = 5 * 60 * 1000; // Five minutes

    private String convertTimerToString() {
        long totalSeconds = (long) (countdownTimer / 1000),
                minutes = totalSeconds / 60,
                seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void create () {
        GameSettingsState.gameInstance = this;
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        uiStage = new Stage(new ScreenViewport());

        ImGui.createContext();
        ImGui.getIO().setDisplaySize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NavEnableSetMousePos | ImGuiConfigFlags.NavEnableKeyboard);

        imGuiImplGl3 = new ImGuiImplGl3();
        imGuiImplGl3.init("#version 150");


        gameplayInputState = new GameplayInputState();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new ImGuiInputProcessor());
        inputMultiplexer.addProcessor(new GameInputProcessor(gameplayInputState));
        Gdx.input.setInputProcessor(inputMultiplexer);


        // THis is to let the stage object handle input
        // Gdx.input.setInputProcessor(uiStage);
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        img = new Texture("badlogic.jpg");

        System.out.println("Loading Tiles");
        for(int tid = 0; tid <= 271; tid++) {
            String formatTid =  "tiles/tile_" + StringUtils.leftPad(Integer.toString(tid), 3, '0') + ".png";
            System.out.println("Loading " + formatTid);
            tiles.add(new Tile(formatTid));
        }
        Arrays.fill(tilemap, 47);
        Arrays.fill(puzzlemap, 56);
        System.out.println("Loaded " + tiles.size() + " tiles");

        _field2 = new TextArea("Controls:\nW/A/S/D = Move around\nShift (hold) = Run\nSpace = Attack", uiSkin);
        _field2.setSize(200, 100);
        _field2.setPosition(  0, Gdx.graphics.getHeight() - _field2.getHeight());
        uiStage.addActor(_field2);

        FreeTypeFontGenerator timerFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CCOverbyteOn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;

        BitmapFont timerFont = timerFontGenerator.generateFont(parameter);
        timerFontGenerator.dispose();

        TextField.TextFieldStyle timertextFieldStyle = new TextField.TextFieldStyle();
        timertextFieldStyle.font = timerFont;
        timertextFieldStyle.fontColor = Color.WHITE;
//        timertextFieldStyle.background = uiSkin.getDrawable("textfield");

        _timer = new TextArea(convertTimerToString(), uiSkin);
        _timer.setStyle(timertextFieldStyle);
        _timer.setSize(90, 45);
        _timer.setAlignment(Align.center);
        _timer.setPosition(((float) Gdx.graphics.getWidth() / 2) - (_timer.getWidth() / 2), Gdx.graphics.getHeight() - _timer.getHeight() - 10);
        uiStage.addActor(_timer);

        _panel = new Table(uiSkin);
//		_panel.debugAll();
        _panel.defaults().align(Align.left);
        _panel.setSize(450, 50);
        _panel.setPosition(((float) Gdx.graphics.getWidth() / 2) - _panel.getWidth() / 2, 0);
        _panel.setBackground(new TextureRegionDrawable(new TextureRegion( new Texture("ui/inv_bg.png") )));
        uiStage.addActor(_panel);

        shapeRenderer = new ShapeRenderer();
        player = new Player();

        // Load common sounds
        sounds.put("collect", Gdx.audio.newSound(Gdx.files.internal("sfx/pop.mp3")));
        sounds.put("swish", Gdx.audio.newSound(Gdx.files.internal("sfx/swish.mp3")));
        sounds.put("throw", Gdx.audio.newSound(Gdx.files.internal("sfx/throw.mp3")));
        sounds.put("rustle", Gdx.audio.newSound(Gdx.files.internal("sfx/rustle.mp3")));


        // Generate Islands

        // Generate Winstate Pattern

        // Plant Flowers
        int row = -1;
        int col = -1;
        for(int x = 0; x < tilemap.length; x++) {
            col = x % tilemapBreak;
            if(col == 0) { row++; }
            plants.add(new Plant(col * 16, row * 16));
        }
    }

    @Override
    public void render () {
        // remove things out of circulation
        cleanupThingsForDeletion();
        rectanglesToRender.clear();

        // update timer
        countdownTimer -= Gdx.graphics.getDeltaTime() * 1000;
        if(countdownTimer <= 0) countdownTimer = 0;
        _timer.setText(convertTimerToString());

        if(Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) {
            showImGui = !showImGui;
        }

        updateInventoryInput();

        // Initialize ImGUI Frame
        ImGui.newFrame();
        ImGui.begin("Hello, ImGui!");
        ImGui.text(String.format("%d FPS", Gdx.graphics.getFramesPerSecond()));
        // Update mouse input
        ImGuiIO io = ImGui.getIO();
        io.setMousePos(Gdx.input.getX(), Gdx.input.getY());
        io.setMouseDown(0, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
        io.setMouseDown(1, Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
        io.setMouseDown(2, Gdx.input.isButtonPressed(Input.Buttons.MIDDLE));
        fpsHistory.add(((float) Gdx.graphics.getFramesPerSecond()));
        if(fpsHistory.size() > 100) { fpsHistory.remove(0); }
        ImFloat refScale = new ImFloat(1.0f);
        float[] data = new float[fpsHistory.size()];
        for(int i = 0; i < fpsHistory.size(); i++) { data[i] = fpsHistory.get(i).floatValue(); }
        ImGui.plotLines("Framerate", data, data.length, 0, "", 0, 60, 400, 100, 4);


        // This chunk is all about setting a new target camera zoom based on player movement
        float newTargetCameraZoom = getNewTargetCameraZoom();
        cameraTargetZoomFactor[0] = newTargetCameraZoom;

        // Control Camera Zoom
        calculatedZoomFactor[0] = Interpolation.linear.apply(calculatedZoomFactor[0], cameraTargetZoomFactor[0], zoomSpeed * Gdx.graphics.getDeltaTime());;
        camera.zoom = calculatedZoomFactor[0];

        // Control camera focus
        camera.position.set(new Vector3(cameraFocus[0], cameraFocus[1], 0));
        camera.update();


        // Update non-player entities
        for(Plant p : plants) {
            p.update(Gdx.graphics.getDeltaTime());
        }

        // Clear and Setup camera projection
        ScreenUtils.clear(1, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);

        // Draw the map tiles
        batch.begin();
        int row = 0;
        int column = 0;
        for (Number number : tilemap) {
            if (column >= tilemapBreak) {
                row++;
                column = 0;
            }
            int tileId = (int) number;

            tiles.get(tileId).draw(batch, column * 16, row * 16);

            column++;
        }

        // Draw the Puzzle Input map island
        row = 0; column = 0; // reset these values to start again
        for (Number number : puzzlemap) {
            if (column >= puzzlemapBreak) {
                row++;
                column = 0;
            }
            int tileId = (int) number;

            tiles.get(tileId).draw(batch,  column * 16 + puzzlemapOffset, row * 16);

            column++;
        }
        batch.end();

        // Update/think routines
        player.update(Gdx.graphics.getDeltaTime(), gameplayInputState);
        cameraFocus[0] = player.lookAt.x;
        cameraFocus[1] = player.lookAt.y;
        for(Plant p : plants) {
            p.update(Gdx.graphics.getDeltaTime());
        }
        for(PlantItem pl : plantItems) {
            pl.update(Gdx.graphics.getDeltaTime());
        }
        if(player.attackCollisionBounds != null) {
            // Run through all attackable entities to check the bounding box

            for(Plant p : plants) {
                p.checkAttackCollision(player.attackCollisionBounds);
            }
        }


        calculateActiveTile();
        // Render the "selected tile"
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.rect(activeTileX * 16, activeTileY * 16, 16, 16);
        shapeRenderer.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            playerUseHandler();
        }

        // draw entities
        batch.begin();
        for(Plant p : plants) {
            p.draw(batch);
        }
        for(PlantItem pl : plantItems) {
            pl.draw(batch);
        }

        player.draw(batch);

        batch.end();

        // Let's show the bounding boxes of invisible bounding boxes
        if(GameSettingsState.showCollisionBoxes && !rectanglesToRender.isEmpty()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.RED);

            for(Rectangle r : rectanglesToRender) {
                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }

            shapeRenderer.end();
        }


        // Render the UI
        _panel.clearChildren();
        _panel.align(Align.left);
        _panel.padLeft(45f);
        int slots = 1;
        for(PlantItem pl : playerInventory) {
            InventorySpriteActor _actor = new InventorySpriteActor(pl.getSprite());
            _actor.data = pl;
            _actor.slot = slots;
            _actor.activeSlot = activeInventorySlot;
            _actor.update(Gdx.graphics.getDeltaTime());
            _panel.add(_actor).padRight(34f);
            slots++;
        }
        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();


        // Render ImGUI

//		// Update keyboard inputs
//		// You can use an InputProcessor to handle and forward keyboard events to ImGui
//		for (int i = 0; i < 256; i++) {
//			if (Gdx.input.isKeyJustPressed(i)) {
//				io.setKeysDown(i, true);
//			}
//			if (Gdx.input.isKeyJustPressed(i)) {
//				io.setKeysDown(i, false);
//			}
//		}
//		io.setKeyCtrl(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT));
//		io.setKeyShift(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT));
//		io.setKeyAlt(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT));
//		io.setKeySuper(Gdx.input.isKeyPressed(Input.Keys.SYM));

        ImGui.text("Player");
        ImGui.text("- Inventory");
        for (PlantItem plant : playerInventory) {
            ImGui.text(plant.tag + ": " + plant.getStackCount());
        }

        ImGui.text("- Tile");
        ImGui.text(String.format("%d,%d", activeTileX, activeTileY));

//		ImGui.text(String.format("player vel: %f", player.movementVelocity));
        ImGui.text("Camera Settings");
        ImGui.inputFloat2("Focus", cameraFocus);
        ImGui.sliderFloat("Target Zoom", cameraTargetZoomFactor, 0.01f, 1f);
        ImGui.sliderFloat("Current Zoom", calculatedZoomFactor, 0.01f, 1f);

        ImGui.text("Item Settings");
        ImGui.sliderFloat("gravity", GameSettingsState.itemGravityFactor, -100f, 100f);
        ImGui.sliderFloat("jump", GameSettingsState.itemJumpFactor, -50f, 50f);
        ImGui.sliderFloat("speed", GameSettingsState.itemSpeedFactor, 0.01f, 50f);
        ImGui.sliderFloat("launchVelocity", GameSettingsState.launchVelocity, 70f, 150f);

        ImGui.end();

        if(showImGui) {
            ImGui.render();
            imGuiImplGl3.renderDrawData(ImGui.getDrawData());
        } else {
            ImGui.endFrame();
        }
    }

    /**
     * This function deals with handling placing inventory items in valid locations
     */
    private void playerUseHandler() {
        // check if we even have available inventory to spend
        if(playerInventory.isEmpty()) {
            return;
        }
        if(playerInventory.size() < activeInventorySlot) {
            return;
        }

        PlantItem activePlantItem = playerInventory.get(activeInventorySlot - 1);
        if(activePlantItem.stackCount <= 0) {
            return;
        }

        // check if the tile is valid
        // @todo (we'll just allow placement anywhere for the moment)
        // we can't place items outside of the map or on the bridge
        // we can't place items inside of collision barriers (@todo map collision not in yet)

        boolean validTile = false;
        // 1. check if we're on the main island
        if(activeTileX >= 0 && activeTileY >= 0 && activeTileX < tilemapBreak && activeTileY < tilemapBreak) {
            validTile = true;
        }
        if(!validTile) {
            // 2. check if we're on the 2nd island
            int left = puzzlemapOffset / 16,
                top = 0,
                right = puzzlemapOffset / 16 + puzzlemapBreak,
                bottom = puzzlemapBreak;

            boolean bleft = activeTileX >= left,
                    btop = activeTileY >= top,
                    bright = activeTileX < right,
                    bbottom = activeTileY < bottom;
//
//            System.out.printf("%d >= %d == %b\n%d >= %d == %b\n%d < %d == %b\n%d < %d == %b\n",
//                    activeTileX, left, bleft, activeTileY, top, btop, activeTileX, right, bright, activeTileY, bottom, bbottom);

            if(bleft && btop && bright && bbottom) {
                validTile = true;
            }
//            System.out.printf("%b", validTile);
        }

        if(!validTile) {
            return;
        }

        // if all is good then do an inventory usage
        plantInventoryItem(activePlantItem);
    }

    private void calculateActiveTile() {
        // Find the "affectable" tile the player would be interacting with, and hilight it
        // - get which island the player is on
        // - get player location
        // - multiply divide 16 w/ island offset
        // - this is to get the current tile the player occupies
        // - after this we get the player direction, and offset the affected tile by one ahead of player direction
        int tileX = (int) Math.floor(player.currentPosition.x / 16),
            tileY = (int) Math.floor(player.currentPosition.y / 16);

        if(player.playerDirection.equals("up")) {
            tileY -= 1;
        } else if (player.playerDirection.equals("down")) {
            tileY += 1;
        } else if (player.playerDirection.equals("left")) {
            tileX -= 1;
        } else if (player.playerDirection.equals("right")) {
            tileX += 1;
        }

        activeTileX = tileX;
        activeTileY = tileY;
    }


    private void plantInventoryItem(PlantItem from) {
        int locX = activeTileX * 16,
            locY = activeTileY * 16;
        Rectangle locRect = new Rectangle(locX, locY, 16, 16);

        // one last set of conditions regarding planting
        boolean doUse = true;

        // find if something is in this tile first
        Plant plantToRemove = null;
        for(Plant _pl : plants) {
            if(_pl.checkCollision(locRect)) {
                if(!_pl.isCut) {
                    // we only want to plant over plants that are cut, but not if there's an uncut plant here
                    doUse = false;
                    break;
                }

                plantToRemove = _pl;
                break;
            }
        }

        if(!doUse) {
            return;
        }

        if(plantToRemove != null) {
            plants.remove(plantToRemove);
        }

        Plant plant = new Plant(locX, locY);
        plants.add(plant);
        playSound("rustle", 0.6f, true);
        removeItemFromStack(from);
    }

    public void spawnPlantItem(Plant from) {
        PlantItem newPlantItem = new PlantItem((int) from.getPosition().x, (int)from.getPosition().y);
        newPlantItem.spawnCollectable();
        plantItems.add(newPlantItem);
    }

    public boolean playerCanCollect(PlantItem plantItem) {
        // check if inventory stack count is maxed
        if(playerInventory.size() < playerInventoryStackMax) {
            return true;
        }

        // if so, check if there's an available stack
        for(PlantItem pl : playerInventory) {
            if(pl.tag.equals(plantItem.tag) && pl.canStack()) {
                return true;
            }
        }

        // all else fails, don't collect
        return false;
    }

    public void doCollectItem(PlantItem plantItem) {
        // remove from rotation
        plantItemsForDeletion.add(plantItem);

        playSound("collect", 0.4f, true);

        // add to inventory
        // check if a stack of this item already exists in inventory and has available stack
        boolean stacked = false;
        for(PlantItem pl : playerInventory) {
            if(pl.tag.equals(plantItem.tag) && pl.canStack()) {
                pl.addStack();
                stacked = true;
            }
        }
        if(!stacked) {
            PlantItem _new = new PlantItem(0,0);
            _new.spawnItem();
            _new.addStack();
            playerInventory.add(_new);
        }
    }



    public Player getPlayer() {
        return player;
    }

    private void updateInventoryInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if(!playerInventory.isEmpty()) {
                activeInventorySlot = 1;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if(playerInventory.size() > 1) {
                activeInventorySlot = 2;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            if(playerInventory.size() > 2) {
                activeInventorySlot = 3;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if(playerInventory.size() > 3) {
                activeInventorySlot = 4;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            if(playerInventory.size() > 4) {
                activeInventorySlot = 5;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            if(playerInventory.size() > 5) {
                activeInventorySlot = 6;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
            activeInventorySlot--;
            if(activeInventorySlot < 1) {
                activeInventorySlot = playerInventory.size();
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
            activeInventorySlot++;
            if(activeInventorySlot > playerInventory.size() || activeInventorySlot > playerInventoryStackMax) {
                activeInventorySlot = 1;
            }
        }
    }

    public void playerDiscardActiveInventory(Player player, float discardTimer) {
        // in this function, we're going to drop a single value of the currently selected inventory item
        // the discardTimer however allows us to gauge a throwing distance velocity, if any

        if(playerInventory.size() >= activeInventorySlot) {

            PlantItem newPlantItem = new PlantItem((int) player.currentPosition.x, (int) player.currentPosition.y);
            newPlantItem.spawnCollectable(player.playerDirection, discardTimer);
            plantItems.add(newPlantItem);

            playSound("throw", 0.4f, true);

            removeItemFromStack(playerInventory.get(activeInventorySlot -1));
        }
    }

    private void removeItemFromStack(PlantItem item) {
        // remove item
        item.stackCount--;

        // if stack count is 0 (or somehow less) remove it from player inventory
        if(item.stackCount <= 0) {
            playerInventory.remove(item);
        }
    }

    private void cleanupThingsForDeletion() {
        for(PlantItem pl : plantItemsForDeletion) {
            plantItems.remove(pl);
        }
    }

    private float getNewTargetCameraZoom() {
        float newTargetCameraZoom = cameraTargetZoomFactor[0];
        if(player.movementVelocity > 0.5f) { // is moving, effectively
            newTargetCameraZoom = minZoom;
        } else {
            newTargetCameraZoom = maxZoom;
        }
        return newTargetCameraZoom;
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
//		player.dispose();
        for(PlantItem pl : plantItems) {
//			pl.dispose();
        }
        for(Plant p : plants) {
//			p.dispose();
        }
        for(Sound sound : sounds.values()) {
            sound.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (ImGui.getIO() != null) {
            ImGui.getIO().setDisplaySize(width, height);
        }
    }

    public void playSound(String sfx, float volume, boolean varyPitch) {
        float pitch = 1.0f;
        if(varyPitch) {
            pitch = ThreadLocalRandom.current().nextFloat(0.5f, 1.5f);
        }

        sounds.get(sfx).play(volume, pitch, 0f);
    }
}
