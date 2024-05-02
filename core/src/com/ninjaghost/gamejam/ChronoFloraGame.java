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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
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
	float[] cameraTargetZoomFactor = new float[] { 0.3f };
	float[] calculatedZoomFactor = new float[] { 0.5f };
	float zoomSpeed = 1f;
	float[] cameraFocus = new float[] { 0f, 0f };
	float cameraFocusSpeed = 1f;

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


	Player player;
	GameplayInputState gameplayInputState;

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

		for(FileHandle _file : Gdx.files.internal("tiles/").list()) {
			tiles.add(new Tile(_file.path()));
		}

		_field = new TextField("Hi", uiSkin);
		_field.setPosition(100, 100);
		_field.setSize(200, 50);
		uiStage.addActor(_field);

		player = new Player();
	}

	@Override
	public void render () {


		// Control Camera Zoom
		float zoomDelta = 1.f;
		calculatedZoomFactor[0] = Interpolation.linear.apply(calculatedZoomFactor[0], cameraTargetZoomFactor[0], zoomDelta * Gdx.graphics.getDeltaTime());;
		camera.zoom = calculatedZoomFactor[0];

		// Control camera focus
		camera.position.set(new Vector3(cameraFocus[0], cameraFocus[1], 0));

		camera.update();


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

		// draw entities
		batch.begin();

//		player.render();
		player.update(Gdx.graphics.getDeltaTime(), gameplayInputState);
		cameraFocus[0] = player.targetPosition.x;
		cameraFocus[1] = player.targetPosition.y;
		player.draw(batch);

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


		ImGui.newFrame();
		ImGui.begin("Hello, ImGui!");
		ImGui.text("Camera Settings");
		ImGui.inputFloat2("Focus", cameraFocus);
		ImGui.sliderFloat("Target Zoom", cameraTargetZoomFactor, 0.01f, 1f);
		ImGui.sliderFloat("Current Zoom", calculatedZoomFactor, 0.01f, 1f);
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
