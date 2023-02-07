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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*

This class handles the calculations for the barcode as well
as displays the result to the user and offers them to try another code

 */

public class BarCodeScreen extends ScreenAdapter {

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

    // binary values

    String code;
    String[] binaryDigits = {"11000", "00011",
            "00101", "00110", "01001", "01010",
            "01100", "10001", "10010", "10100", "11000"};
    String binary;
    String convertedCode;

    // in the constructor all the declared variables/parameters are initialized

    public BarCodeScreen(PostalBarCodeLab parentClass, String code) {

        // GameClass Setup

        parent = parentClass;

        this.code = code;

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

        batch = new SpriteBatch();

    }

    // this method initializes the variables required to generate and display font on-screen

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

    // this method renders the elements that are on screen (it is called continuously with increasing deltaTime)

    @Override
    public void render(float deltaTime) {

        batch.begin();

        detectInput(deltaTime);

        renderBackgrounds(deltaTime);

        font1.draw(batch, "Your converted barcode is shown below:", WORLD_WIDTH * 1/4, WORLD_HEIGHT * 5/6 - font1.getXHeight() * 0, (float)WORLD_WIDTH * 1/2, Align.center, false);
        font1.draw(batch, "Raw Binary: " + binary, WORLD_WIDTH * 1/4, WORLD_HEIGHT * 5/6 - (font1.getLineHeight() * 2 + font1.getCapHeight() * 2), (float)WORLD_WIDTH * 1/2, Align.center, false);
        font1.draw(batch, "Barcode: " + convertedCode, WORLD_WIDTH * 1/4, WORLD_HEIGHT * 5/6 - (font1.getLineHeight() * 3 + font1.getCapHeight() * 3), (float)WORLD_WIDTH * 1/2, Align.center, false);


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

    // this method detects input from the user (used for escape key)

    private void detectInput(float deltaTime) {

        // escape

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {

            super.dispose();
            System.exit(0);

        }

    }

    // this method initializes the screen variables needed and shows the initial elements of the screen before render starts to be called

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport);

        createTable();

        addTextButton("Try Another Code").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                parent.clickSound.play(0.7f);
                dispose();
                parent.changeScreen(parent.MAINMENU);
            }
        });

        Gdx.input.setInputProcessor(stage);

        binary = generateBinary(code);
        convertedCode = binaryToCode(binary);

    }

    // this method generates the initial binary string from the inputted zipcode

    private String generateBinary(String input) {

        String binary = "";
        int sum = 0;

        for (int i = 0; i < input.length(); i++) {

            int currentVal = Character.getNumericValue(input.charAt(i));
            sum += currentVal;

            binary += binaryDigits[currentVal] + " ";

        }

        if (sum == 0) {

            binary += binaryDigits[0];

        } else {

            binary += binaryDigits[10 - (sum % 10)];

        }

        return binary;

    }

    // this method converts the binary to a "barCode" using ":" and "|" characters

    private String binaryToCode(String input) {

        String code = " |";

        for (int i = 0; i < input.length(); i++) {

            if (input.charAt(i) == ' ') {

                code += "  ";

            }

            else if (input.charAt(i) == '0') {

                code += ":";

            } else {

                code += "|";

            }

        }

        code += "|";

        return code;

    }

    // this method creates the table that contains all the stage elements (ex. text boxes)

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

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);

    }

    // this method disposes various instance variables that are not needed when switching to a different screen

    @Override
    public void dispose() {

        // fonts

        font1.dispose();


        stage.dispose();

        batch.dispose();


    }

}