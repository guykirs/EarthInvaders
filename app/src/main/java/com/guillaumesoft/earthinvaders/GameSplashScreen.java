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
public class GameSplashScreen extends GLScreen
{
    /////////////////////////////////////////
    // CLASS VARAIBLES
    /////////////////////////////////////////

    private SpriteBatcher batcher;
    private Camera2D guiCam;

    /////////////////////////////////////////
    // CLASS CONSTRUCTOR
    /////////////////////////////////////////
    public GameSplashScreen(Game game)
    {
        super(game);

        batcher  = new SpriteBatcher(glGraphics, 100);
        guiCam   = new Camera2D(glGraphics, 1280, 720);
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
                    MainMenuScreen mainmenu = new MainMenuScreen(game);
                    game.setScreen(mainmenu);
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

           batcher.beginBatch(Assets.background);

              // SHOW THE RATING SCREEN
              batcher.drawSprite(guiCam.position.x,  guiCam.position.y, 1280, 720, Assets.backgroundRegion);

           batcher.endBatch();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

           batcher.beginBatch(Assets.RedFont);

              // SHOW THE MESSAGE TEXT
              Assets.redfont.drawText(batcher, "Welcome to Android Earth Invaders", 300 , guiCam.frustumHeight /2, 25.0f, 25.0f);

           batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {   }

    @Override
    public void resume() {  }

    @Override
    public void dispose() {   }
}


