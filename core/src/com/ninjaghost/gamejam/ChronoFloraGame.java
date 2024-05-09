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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import java.util.HashMap;
import java.util.List;
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
	List<Number> tilemap = List.of(   8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,

												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,

												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,

												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,0, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
												8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8
		);

	// UI Widgets
	ArrayList<TextField> _fields;
	TextArea _field2;
	Table _panel;

	// Playable NPC Object
	Player player;

	// Inventory
	ArrayList<PlantItem> playerInventory = new ArrayList<>();
	int playerInventoryStackMax = 6;

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
		for(int tid = 1; tid <= 272; tid++) {
			String formatTid =  "tiles/tile_" + StringUtils.leftPad(Integer.toString(tid), 3, '0') + ".png";
			System.out.println("Loading " + formatTid);
			tiles.add(new Tile(formatTid));
		}
		System.out.println("Loaded " + tiles.size() + " tiles");

		_field2 = new TextArea("Controls:\nW/A/S/D = Move around\nShift (hold) = Run\nSpace = Attack", uiSkin);
		_field2.setSize(200, 100);
		_field2.setPosition(  0, Gdx.graphics.getHeight() - _field2.getHeight());
		uiStage.addActor(_field2);

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


		// Generate Islands

		// Generate Winstate Pattern

		// Plant Flowers
		int row = -1;
		int col = -1;
		for(int x = 0; x < tilemap.size(); x++) {
			col = x % tilemapBreak;
			if(col == 0) { row++; }
			plants.add(new Plant(col * 16, row * 16));
		}
	}

	@Override
	public void render () {

		// this was used to test the FPS graph
//		Random rnd = new Random();
//		int rndMin = 1;
//		int rndMax = 500;
//		if(rnd.nextInt(rndMax) < 50) {
//			try {
//				Thread.sleep(rnd.nextInt(rndMax - rndMin) + rndMin);
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e);
//			}
//		}

		// remove things out of circulation
		cleanupThingsForDeletion();

        rectanglesToRender.clear();

		if(Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) {
			showImGui = !showImGui;
		}

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
				p.checkCollision(player.attackCollisionBounds);
			}
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
		for(PlantItem pl : playerInventory) {
			SpriteActor _actor = new SpriteActor(pl.getSprite());
			_panel.add(_actor).padRight(34f);
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

//		ImGui.text(String.format("player vel: %f", player.movementVelocity));
		ImGui.text("Camera Settings");
		ImGui.inputFloat2("Focus", cameraFocus);
		ImGui.sliderFloat("Target Zoom", cameraTargetZoomFactor, 0.01f, 1f);
		ImGui.sliderFloat("Current Zoom", calculatedZoomFactor, 0.01f, 1f);

		ImGui.text("Item Settings");
		ImGui.sliderFloat("gravity", GameSettingsState.itemGravityFactor, -100f, 100f);
		ImGui.sliderFloat("jump", GameSettingsState.itemJumpFactor, -50f, 50f);
		ImGui.sliderFloat("speed", GameSettingsState.itemSpeedFactor, 0.01f, 50f);
		ImGui.checkbox("reverse gravity", GameSettingsState.reverseGravity);
		ImGui.checkbox("reverse move input", GameSettingsState.reverseInput);

		ImGui.end();

		if(showImGui) {
			ImGui.render();
			imGuiImplGl3.renderDrawData(ImGui.getDrawData());
		} else {
			ImGui.endFrame();
		}
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


		// Randomize the volume and pitch
//		Random random = new Random();
		float pitch = ThreadLocalRandom.current().nextFloat(0.5f, 1.5f);
		sounds.get("collect").play(0.4f, pitch, 0f);

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
}
