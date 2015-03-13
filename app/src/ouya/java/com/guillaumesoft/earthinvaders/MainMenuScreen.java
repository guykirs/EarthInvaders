package com.guillaumesoft.earthinvaders;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import javax.microedition.khronos.opengles.GL10;
import tv.ouya.console.api.OuyaController;

public class MainMenuScreen extends GLScreen
{
    //////////////////////////////////////////
    // CLASS VARAIBLES
	Camera2D      guiCam;
	SpriteBatcher batcher;
    private float scale = 0.0f;
    private int   menu = 0;

	public MainMenuScreen(Game game)
    {
		super(game);

		guiCam = new Camera2D(glGraphics, 1280, 720);
		batcher = new SpriteBatcher(glGraphics, 100);
	}

	@Override
	public void update(float deltaTime)
    {
        OuyaController CurrentInput = OuyaController.getControllerByDeviceId(ScreenManager.GetController().getDeviceId());

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_DPAD_DOWN))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_DPAD_DOWN))
            {
                if( menu < 2)
                {
                    menu++;
                }
                else
                {
                    menu = 0;
                }
            }
        }

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_DPAD_UP))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_DPAD_UP))
            {
                if( menu > 0)
                {
                    menu--;
                }
                else
                {
                    menu = 2;
                }
            }
        }

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_O))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_O))
            {
                switch(menu)
                {
                    case 0:
                        // PLAY SCREEN
                        game.setScreen(new GameScreen(game));
                        break;
                    case 1:
                        // HIGHSCORE
                        game.setScreen(new GameOptionsScreen(game));
                        break;
                    case 2:
                        game.setScreen(new GameCreditScreen(game));
                        break;
                }
            }
        }

        OuyaController.startOfFrame();

        // Pulsate the size of the selected menu entry.
        double time = System.currentTimeMillis() / 60;

        float pulsate = (float) Math.sin(time * 6) + 1;

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

		   batcher.drawSprite(guiCam.position.x, guiCam.position.y, 1280, 720, Assets.backgroundRegion);

		batcher.endBatch();


		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		batcher.beginBatch(Assets.items);

		   batcher.drawSprite(1280 /2, 720 /2 + 200, 384, 128, Assets.logoRegion);

		batcher.endBatch();

        batcher.beginBatch(Assets.RedFont);

           Assets.redfont.drawText(batcher, "Main Menu", 1280 /2 - 100, 450, 25.0f, 25.0f);

           switch(menu)
           {
               case 0:
                   //PLAY SCREEN SELECTED
                   Assets.redfont.drawText(batcher, "Play"   ,  1280 /2 - 80, 720 /2, 25.0f * scale, 25.0f * scale);
                   Assets.redfont.drawText(batcher, "Options",  1280 /2 - 80, 720 /2 - 50,  25.0f, 25.0f  );

                   Assets.redfont.drawText(batcher, "Credit",  1280 /2 - 80, 720 /2 - 100,  25.0f, 25.0f  );

                   break;
               case 1:
                   //PLAY SCREEN SELECTED
                   Assets.redfont.drawText(batcher, "Play"     ,  1280 /2 - 80, 720 /2, 25.0f, 25.0f);
                   Assets.redfont.drawText(batcher, "Options",  1280 /2 - 80, 720 /2 - 50,  25.0f * scale, 25.0f * scale  );

                   Assets.redfont.drawText(batcher, "Credit",  1280 /2 - 80, 720 /2 - 100,  25.0f, 25.0f  );

                   break;
               case 2:
                   //PLAY SCREEN SELECTED
                   Assets.redfont.drawText(batcher, "Play"     ,  1280 /2 - 80, 720 /2, 25.0f, 25.0f);
                   Assets.redfont.drawText(batcher, "Options",  1280 /2 - 80, 720 /2 - 50,  25.0f, 25.0f);

                   Assets.redfont.drawText(batcher, "Credit",  1280 /2 - 80, 720 /2 - 100,  25.0f * scale, 25.0f * scale  );

                   break;
           }

        batcher.endBatch();

        batcher.beginBatch(Assets.BlackFont);

           Assets.blackfont.drawText(batcher, "Double tap U to exit", 1920 /2 - 600, 1080 / 2 - 400, 35.0f, 35.0f);

        batcher.endBatch();


        gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause()
    {
	}

	@Override
	public void resume()
    {
	}

	@Override
	public void dispose()
    {
	}
}
