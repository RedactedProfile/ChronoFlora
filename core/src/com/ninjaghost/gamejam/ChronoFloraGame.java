package com.ninjaghost.gamejam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;

import java.util.ArrayList;
import java.util.List;

public class ChronoFloraGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	BitmapFont bitmapFont;

	ImGuiImplGl3 imGuiImplGl3;


	Stage uiStage;
	Skin uiSkin;

	OrthographicCamera camera;
	float[] cameraZoomFactor = new float[] { 1f };
	float[] cameraFocus = new float[] { 0f, 0f };

	ArrayList<Tile> tiles = new ArrayList<>();
	int tilemapBreak = 32;
	List<Number> tilemap = List.of(   8,9,8,9,8,9,8,9,8,9,8,9,8,9,8,9, 8,9,8,9,8,9,8,9,8,9,8,9,8,9,8,9,
												8,0,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,0,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,0,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,

												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0
		);

	// UI Widgets
	// Draw the UI
	TextField _field;


	@Override
	public void create () {
		batch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		uiStage = new Stage(new ScreenViewport());

		ImGui.createContext();
		ImGui.getIO().setDisplaySize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NavEnableSetMousePos | ImGuiConfigFlags.NavEnableKeyboard);
		imGuiImplGl3 = new ImGuiImplGl3();
		imGuiImplGl3.init("#version 150");


		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new ImGuiInputProcessor());
		inputMultiplexer.addProcessor(new GameInputProcessor());
		Gdx.input.setInputProcessor(inputMultiplexer);


		// THis is to let the stage object handle input
		// Gdx.input.setInputProcessor(uiStage);
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		camera = new OrthographicCamera();
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		img = new Texture("badlogic.jpg");

		for(FileHandle _file : Gdx.files.internal("tiles/").list()) {
			tiles.add(new Tile(_file.path()));
		}

		_field = new TextField("Hi", uiSkin);
		_field.setPosition(100, 100);
		_field.setSize(200, 50);
		uiStage.addActor(_field);
	}

	@Override
	public void render () {
		camera.update();

		// Control Camera Zoom
		float zoomDelta = 0f;
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			zoomDelta = 0.2f;
		} else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zoomDelta = -0.2f;
		}
		cameraZoomFactor[0] -= zoomDelta * Gdx.graphics.getDeltaTime();
		camera.zoom = cameraZoomFactor[0];

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
		batch.end();



		// Render the UI
		uiStage.act(Gdx.graphics.getDeltaTime());
		uiStage.draw();


		// Render ImGUI
		// Update mouse input
		ImGuiIO io = ImGui.getIO();
		io.setMousePos(Gdx.input.getX(), Gdx.input.getY());
		io.setMouseDown(0, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
		io.setMouseDown(1, Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
		io.setMouseDown(2, Gdx.input.isButtonPressed(Input.Buttons.MIDDLE));
		// Update keyboard inputs
		// You can use an InputProcessor to handle and forward keyboard events to ImGui
		for (int i = 0; i < 256; i++) {
			if (Gdx.input.isKeyJustPressed(i)) {
				io.setKeysDown(i, true);
			}
			if (Gdx.input.isKeyJustPressed(i)) {
				io.setKeysDown(i, false);
			}
		}
		io.setKeyCtrl(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT));
		io.setKeyShift(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT));
		io.setKeyAlt(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT));
		io.setKeySuper(Gdx.input.isKeyPressed(Input.Keys.SYM));

		ImGui.newFrame();

		// Example window
		ImGui.begin("Hello, ImGui!");
		ImGui.text(String.format("This is some useful text. %f", camera.zoom));
		ImGui.text("Camera Settings");
		ImGui.inputFloat2("Focus", cameraFocus);
		ImGui.sliderFloat("Zoom", cameraZoomFactor, 0.01f, 1f);
//		ImGui.button("Click Me!");


		ImGui.end();

		ImGui.render();
		imGuiImplGl3.renderDrawData(ImGui.getDrawData());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		if (ImGui.getIO() != null) {
			ImGui.getIO().setDisplaySize(width, height);
		}
	}
}
