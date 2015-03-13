package com.guillaumesoft.earthinvaders;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;

import javax.microedition.khronos.opengles.GL10;

//////////////////////////////////////////////////////////////////
// October /21 /2014
// Guillaume Swolfs
// guillaumesoft
// CompanySplashScreen class
//////////////////////////////////////////////////////////////////
public class CompanySplashScreen extends GLScreen
{
    /////////////////////////////////////////
    // CLASS VARAIBLES
    /////////////////////////////////////////

    private SpriteBatcher batcher;
    private Camera2D guiCam;

    /////////////////////////////////////////
    // CLASS CONSTRUCTOR
    /////////////////////////////////////////
    public CompanySplashScreen(Game game)
    {
        super(game);

        batcher  = new SpriteBatcher(glGraphics, 100);
        guiCam   = new Camera2D(glGraphics, 1920, 1080);

        Assets.drum.play();
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
                    Assets.drum.stop();

                    GameSplashScreen gameSplash = new GameSplashScreen(game);
                    game.setScreen(gameSplash);
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

           batcher.beginBatch(Assets.companyscreen);

              // SHOW THE RATING SCREEN
              batcher.drawSprite(guiCam.position.x, guiCam.position.y, guiCam.frustumWidth, guiCam.frustumHeight, Assets.companyscreenRegion);

           batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {  }

    @Override
    public void resume() {  }

    @Override
    public void dispose() {  }
}

