package com.guillaumesoft.earthinvaders;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import javax.microedition.khronos.opengles.GL10;
import tv.ouya.console.api.OuyaController;

//////////////////////////////////////////////////////////////////
// june 25 2014
// Guillaume Swolfs
// guillaumesoft
// RatingSplashScreen class
//////////////////////////////////////////////////////////////////
public class GameOptionsScreen extends GLScreen
{
    /////////////////////////////////////////
    // CLASS VARAIBLES
    /////////////////////////////////////////

    private Camera2D guiCam;
    private SpriteBatcher batcher;
    private float scale;
    private int menu;

    /////////////////////////////////////////
    // CLASS CONSTRUCTOR
    /////////////////////////////////////////
    public GameOptionsScreen(Game game)
    {
        super(game);

        guiCam   = new Camera2D(glGraphics, 1920, 1080);
        batcher  = new SpriteBatcher(glGraphics, 100);
    }

    @Override
    public void update(float deltaTime)
    {
        double time;
        float pulsate;

        OuyaController CurrentInput = OuyaController.getControllerByDeviceId(ScreenManager.GetController().getDeviceId());

        // GET THE MOVEMENTS FROM THE DIRECTIONAL STICK
        float axisX  = CurrentInput.getAxisValue(OuyaController.AXIS_LS_X);
        float axisY  = CurrentInput.getAxisValue(OuyaController.AXIS_LS_Y);

        // GET THE DEAD ZONE OF THE DIRECTIONAL STICKS
        if (axisX * axisX + axisY * axisY < OuyaController.STICK_DEADZONE * OuyaController.STICK_DEADZONE)
        {
            axisX = axisY = 0.0f;
        }

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_A))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_A))
            {
                // CALL ON THE MAIN MENU
                game.setScreen(new MainMenuScreen(game));
            }
        }

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_DPAD_DOWN))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_DPAD_DOWN))
            {
                if( menu < 1)
                    menu++;
                else
                    menu = 0;
            }
        }

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_DPAD_UP))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_DPAD_UP))
            {
                if( menu > 0)
                    menu--;
                else
                    menu = 1;
            }
        }

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_DPAD_LEFT))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_DPAD_LEFT))
            {
                switch(menu)
                {
                    case 0:
                        if(Settings.soundEnabled == true)
                            Settings.soundEnabled = false;
                        else
                            Settings.soundEnabled = true;
                        break;
                    case 1:
                        if(Settings.musicEnabled == true)
                            Settings.musicEnabled = false;
                        else
                            Settings.musicEnabled = true;
                        break;
                }

                Settings.save(game.getFileIO());
                Settings.load(game.getFileIO());
            }
        }

        // Pulsate the size of the selected menu entry.
        time = System.currentTimeMillis() / 60;

        pulsate = (float) Math.sin(time * 6) + 1;

        scale = 1 + pulsate * 0.05f;// * selectionFade;

        OuyaController.startOfFrame();

    }


    @Override
    public void present(float deltaTime)
    {

        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        guiCam.setViewportAndMatrices();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.background);

           // SHOW THE RATING SCREEN
           batcher.drawSprite( guiCam.position.x,  guiCam.position.y, 1920, 1080, Assets.backgroundRegion);

        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //1920, 1080

        batcher.beginBatch(Assets.RedFont);

           Assets.redfont.drawText(batcher, "Options", 1920 /2 - 100, 600, 25.0f, 25.0f);

        switch(menu)
        {
            case 0:
                if(Settings.soundEnabled)
                    Assets.redfont.drawText(batcher, "Sound On", 1920 /2 - 80, 1080 /2 - 50,  25.0f * scale, 25.0f * scale );
                else
                    Assets.redfont.drawText(batcher, "Sound Off", 1920 /2 - 80, 1080 /2 - 50,  25.0f * scale, 25.0f * scale );

                //SHOW THE MESSAGE TEXT
                if(Settings.musicEnabled)
                    Assets.redfont.drawText(batcher, "Music On",  1920 /2 - 80, 1080 /2, 25.0f, 25.0f);
                else
                    Assets.redfont.drawText(batcher, "Music Off", 1920 /2 - 80, 1080 /2, 25.0f, 25.0f);

                break;
            case 1:
                if(Settings.soundEnabled)
                    Assets.redfont.drawText(batcher, "Sound On",  1920 /2 - 80, 1080 /2 - 50,  25.0f, 25.0f);
                else
                    Assets.redfont.drawText(batcher, "Sound Off", 1920 /2 - 80, 1080 /2 - 50,  25.0f, 25.0f);

                if(Settings.musicEnabled)
                    Assets.redfont.drawText(batcher, "Music On",  1920 /2 - 80, 1080 /2, 25.0f * scale, 25.0f * scale);
                else
                    Assets.redfont.drawText(batcher, "Music Off", 1920 /2 - 80, 1080 /2, 25.0f * scale, 25.0f * scale);
                break;
        }

        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);

           batcher.drawSprite( 300,  guiCam.frustumHeight /2 + 300, 100, 100, Assets.buttonA);

        batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);

    }

    @Override
    public void pause(){  }

    @Override
    public void resume() { }

    @Override
    public void dispose() {  }
}
