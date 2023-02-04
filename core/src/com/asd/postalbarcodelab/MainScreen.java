package com.asd.postalbarcodelab;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends ScreenAdapter {

    private PostalBarCodeLab parent;

    //screen

    private Camera camera;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private Table mainTable;

    //graphics

    private SpriteBatch batch;

    private Texture background;

    private TextField textField;

    // timing

    private float backgroundOffset = 0;
    private float backgroundMaxScrollingSpeed;

    // world parameters

    private final int WORLD_WIDTH = 1280;
    private final int WORLD_HEIGHT = 720;

    // HUD

    BitmapFont font1;

    // font Generators

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    // audio

    public MainScreen(PostalBarCodeLab parentClass) {

        // GameClass Setup

        parent = parentClass;

        // initialize backgrounds

        background = Assets.manager.get(Assets.background);

        backgroundMaxScrollingSpeed =  (float)(WORLD_HEIGHT) / 2;

        // FONTS & HUD

        initializeFonts();

        skin = Assets.manager.get(Assets.SKIN);

        // audio

        parent.lobbyMusic = Assets.manager.get(Assets.lobbyMusic, Music.class);

        parent.lobbyMusic.setVolume(1f);
        parent.lobbyMusic.play();

        parent.clickSound = Assets.manager.get(Assets.clickSound, Sound.class);
        parent.errorSound = Assets.manager.get(Assets.errorSound, Sound.class);

        batch = new SpriteBatch();

    }

    private void initializeFonts() {

        // create bitmap fonts from file

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/EdgeFont.otf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 20;
        fontParameter.borderWidth = 2;
        fontParameter.color = new Color(0, 20, 155, 100);
        fontParameter.borderColor = new Color(0, 0, 0, 3);

        font1 = fontGenerator.generateFont(fontParameter);

        font1.getData().setScale(1.5f);

    }

    @Override
    public void render(float deltaTime) {

        batch.begin();

        detectInput(deltaTime);

        // restrict textField input

        if (textField != null) {

            String text = textField.getText();

            text = text.replaceAll("([a-z])", "");

            textField.setText(text);
            textField.setCursorPosition(textField.getText().length());

        }

        renderBackgrounds(deltaTime);

        font1.draw(batch, "Welcome to the unofficial United States Postal Office barcode", WORLD_WIDTH * 1/4, WORLD_HEIGHT * 5/6 - font1.getXHeight() * 0, (float)WORLD_WIDTH * 1/2, Align.center, false);
        font1.draw(batch, "converter. Type in your zipcode below and it will be converted", WORLD_WIDTH * 1/4, WORLD_HEIGHT * 5/6 - (font1.getLineHeight() * 1 + font1.getCapHeight()), (float)WORLD_WIDTH * 1/2, Align.center, false);
        font1.draw(batch, "to a binary barcode (| represents 0 and : represents 1)", WORLD_WIDTH * 1/4, WORLD_HEIGHT * 5/6 - (font1.getLineHeight() * 2 + font1.getCapHeight() * 2), (float)WORLD_WIDTH * 1/2, Align.center, false);

        stage.act();
        stage.draw();

        batch.end();

    }

    private void renderBackgrounds(float deltaTime) {

        backgroundOffset += (deltaTime * backgroundMaxScrollingSpeed * 1/2);

        batch.draw(background, 0, -backgroundOffset, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(background, 0, -backgroundOffset + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);

        if (backgroundOffset > WORLD_HEIGHT) {
            backgroundOffset = 0;
        }

    }

    private void detectInput(float deltaTime) {

        // escape

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {

            System.exit(0);

        }

    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport);

        createTable();

        textField = addTextField("");
        textField.setMaxLength(5);


        addTextButton("Submit").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

                if (textField.getText().length() == 5) {

                    parent.clickSound.play(0.7f);
                    parent.code = textField.getText();
                    dispose();
                    parent.changeScreen(parent.BARCODE);

                } else {

                    parent.errorSound.play(0.7f);

                }
            }
        });

        addTextButton("Exit").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                parent.clickSound.play(0.7f);
                System.exit(0);
            }
        });

        Gdx.input.setInputProcessor(stage);

    }

    private void createTable() {

        mainTable = new Table();
        mainTable.setFillParent(true);

        stage.addActor(mainTable);

        mainTable.setPosition(0, -100);

    }

    private TextButton addTextButton(String name) {

        TextButton button = new TextButton(name, skin);

        mainTable.add(button).width(1000).height(100).padBottom(25);
        mainTable.row();
        return button;

    }

    private TextField addTextField(String name) {

        TextField textField = new TextField(name, skin);

        mainTable.add(textField).width(700).height(50).padBottom(25);
        mainTable.row();
        return textField;

    }


    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);

    }

    @Override
    public void dispose() {

        // fonts

        font1.dispose();


        stage.dispose();

        //batch.dispose();


    }

}