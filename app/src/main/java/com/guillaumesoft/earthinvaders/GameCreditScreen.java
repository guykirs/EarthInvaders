package com.guillaumesoft.earthinvaders;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import javax.microedition.khronos.opengles.GL10;
import tv.ouya.console.api.OuyaController;

///  OCTOBER 23, 2014
///  GUILLAUME SWOLFS
///  GUILLAUMESOFT
///  THIS CLASS DRAWS THE GAME SPLASH SCREEN
public class GameCreditScreen extends GLScreen
{
    ////////////////////////////////////////
    // CLASS VARAIBLES
    ////////////////////////////////////////

    private SpriteBatcher batcher;
    private Camera2D guiCam;

    public GameCreditScreen(Game game)
    {
        super(game);

        batcher  = new SpriteBatcher(glGraphics, 500);
        guiCam   = new Camera2D(glGraphics, 1920, 1080);
    }

    @Override
    public void update(float deltaTime)
    {
        OuyaController CurrentInput = OuyaController.getControllerByDeviceId(ScreenManager.GetController().getDeviceId());

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_A))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_A))
            {
                // CALL ON THE MAIN MENU
                game.setScreen(new MainMenuScreen(game));
            }
        }
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
           batcher .drawSprite(guiCam.position.x,  guiCam.position.y, guiCam.frustumWidth, guiCam.frustumHeight, Assets.backgroundRegion);

        batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);

           batcher.drawSprite( 300,  guiCam.frustumHeight /2 + 300, 100, 100, Assets.buttonA);

        batcher.endBatch();

        batcher.beginBatch(Assets.BlackFont);

           // SHOW THE MESSAGE TEXT
           Assets.blackfont.drawText(batcher, "This is just another Space Invaders game ",   150, 700, 30.0f, 30.0f);
           Assets.blackfont.drawText(batcher, "from the book Beginning Android Games.",      150, 650, 30.0f, 30.0f);
           Assets.blackfont.drawText(batcher, "This is an exercise from this book to learn", 150, 600, 30.0f, 30.0f);
           Assets.blackfont.drawText(batcher, "android and how to publish a game",           150, 550, 30.0f, 30.0f);
           Assets.blackfont.drawText(batcher, "This is a free game.",                        150, 500, 30.0f, 30.0f);

        batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);

    }

    @Override
    public void pause() {   }

    @Override
    public void resume(){  }

    @Override
    public void dispose() {  }
}