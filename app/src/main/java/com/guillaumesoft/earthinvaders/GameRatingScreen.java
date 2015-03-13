package com.guillaumesoft.earthinvaders;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;

import javax.microedition.khronos.opengles.GL10;

//////////////////////////////////////////////////////////////////
// October / 21/ 2014
// Guillaume Swolfs
// guillaumesoft
// RatingSplashScreen class
//////////////////////////////////////////////////////////////////
public class GameRatingScreen extends GLScreen
{
    /////////////////////////////////////////
    // CLASS VARAIBLES
    /////////////////////////////////////////

    private Camera2D guiCam;
    private SpriteBatcher batcher;

    /////////////////////////////////////////
    // CLASS CONSTRUCTOR
    /////////////////////////////////////////
    public GameRatingScreen(Game game)
    {
        super(game);

        guiCam   = new Camera2D(glGraphics, 1280, 720);
        batcher  = new SpriteBatcher(glGraphics, 100);
    }

    @Override
    public void update(float deltaTime)
    {
        Thread  mSplashThread;

        // thread for displaying the SplashScreen
        mSplashThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized(this)
                    {
                        //wait 5 sec
                        wait(5000);
                    }
                }
                catch(InterruptedException e)
                {
                    System.out.println(e.getMessage());
                }

                finally
                {
                    CompanySplashScreen company = new CompanySplashScreen(game);
                    game.setScreen(company);
                }
            }
        };

        mSplashThread.start();
    }

    @Override
    public void present(float deltaTime)
    {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        guiCam.setViewportAndMatrices();

        gl.glEnable(GL10.GL_TEXTURE_2D);

          batcher.beginBatch(Assets.esrbm);

              // SHOW THE RATING SCREEN
              batcher.drawSprite(guiCam.position.x, guiCam.position.y, guiCam.frustumWidth, guiCam.frustumHeight, Assets.esrbmRegion);

          batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

           batcher.beginBatch(Assets.BlackFont);

              // SHOW THE MESSAGE TEXT
              Assets.blackfont.drawText(batcher, "Everybody", guiCam.frustumWidth /2 - 120, guiCam.frustumHeight /2, 30.0f, 30.0f);

           batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {   }

    @Override
    public void resume() {  }

    @Override
    public void dispose() {  }
}
