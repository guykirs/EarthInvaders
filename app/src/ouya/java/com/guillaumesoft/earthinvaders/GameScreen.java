package com.guillaumesoft.earthinvaders;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.FPSCounter;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;
import com.guillaumesoft.earthinvaders.World.WorldListener;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import tv.ouya.console.api.OuyaController;

public class GameScreen extends GLScreen
{
    /////////////////////////////////////////////
    // CLASS STATIC VARAIBLES
	static final int GAME_RUNNING = 0;
	static final int GAME_PAUSED = 1;
	static final int GAME_OVER = 2;

    /////////////////////////////////////////////
    // CLASS VARAIBLES
	int           state;
	Camera2D      guiCam;
	Vector2       touchPoint;
	SpriteBatcher batcher;
	World         world;
	WorldListener worldListener;
	WorldRenderer renderer;
	int           lastScore;
	int           lastLives;
	int           lastWaves;
	String        scoreString;
	FPSCounter    fpsCounter;

    OuyaController CurrentInput;
    private int    menu = 0;
    private float  scale = 0.0f;

    /// <summary>
    /// Current user movement input.
    /// </summary>
    private Vector2 movement;

	public GameScreen(Game game)
    {
		super(game);

		state      = GAME_RUNNING;
		guiCam     = new Camera2D(glGraphics, 1280, 720);
		touchPoint = new Vector2();
		batcher    = new SpriteBatcher(glGraphics, 100);
		world      = new World();

		worldListener = new WorldListener()
        {
			@Override
			public void shot() {
				Assets.playSound(Assets.shotSound);
			}

			@Override
			public void explosion() {
				Assets.playSound(Assets.explosionSound);
			}
		};

		world.setWorldListener(worldListener);
		renderer = new WorldRenderer(glGraphics);

		lastScore     = 0;
		lastLives     = world.ship.lives;
		lastWaves     = world.waves;
		scoreString   = "lives:" + lastLives + " waves:" + lastWaves + " score:"	+ lastScore;
		fpsCounter    = new FPSCounter();

        CurrentInput  = OuyaController.getControllerByDeviceId(ScreenManager.GetController().getDeviceId());
        this.movement = new Vector2();

       Assets.playMusic();
	}

	@Override
	public void update(float deltaTime)
    {
		switch (state)
        {
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}

	private void updatePaused()
    {
        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_DPAD_DOWN))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_DPAD_DOWN))
            {
                if( menu < 1)
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
                    menu = 1;
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
                        // RESUME
                        state = GAME_RUNNING;
                        break;
                    case 1:
                        // MAINMENU
                        game.setScreen(new MainMenuScreen(game));
                        break;
                }
            }
        }

        OuyaController.startOfFrame();

        // Pulsate the size of the selected menu entry.
        double time = System.currentTimeMillis() / 60;

        float pulsate = (float) Math.sin(time * 6) + 1;

        scale = 1 + pulsate * 0.05f;// * selectionFade;
	}

	private void updateRunning(float deltaTime)
    {
        // GET THE MOVEMENTS FROM THE DIRECTIONAL STICK
        movement.x = CurrentInput.getAxisValue(OuyaController.AXIS_LS_X);
        movement.y = CurrentInput.getAxisValue(OuyaController.AXIS_LS_Y);

        // GET THE DEAD ZONE OF THE DIRECTIONAL STICKS
        if (movement.x * movement.x + movement.y * movement.y < OuyaController.STICK_DEADZONE * OuyaController.STICK_DEADZONE)
        {
            movement.x = movement.y = 0.0f;
        }

        world.update(deltaTime, calculateInputAcceleration());

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_O))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_O))
            {

                world.shot();

            }
        }

        if (CurrentInput.buttonChangedThisFrame(OuyaController.BUTTON_MENU))
        {
            if (CurrentInput.buttonPressedThisFrame(OuyaController.BUTTON_MENU))
            {
                state = GAME_PAUSED;
            }
        }

        if (world.ship.lives != lastLives || world.score != lastScore || world.waves != lastWaves)
        {
            lastLives = world.ship.lives;
            lastScore = world.score;
            lastWaves = world.waves;
            scoreString = "lives:" + lastLives + " waves:" + lastWaves	+ " score:" + lastScore;
        }

        if (world.isGameOver())
        {
            state = GAME_OVER;
        }
	}

	private float calculateInputAcceleration()
    {
        float accelX;

        accelX = movement.x * Ship.SHIP_VELOCITY / 5;

        return accelX;
	}

	private void updateGameOver()
    {
		List<TouchEvent> events = game.getInput().getTouchEvents();
		int len = events.size();
		for (int i = 0; i < len; i++)
        {
			TouchEvent event = events.get(i);
			if (event.type == TouchEvent.TOUCH_UP)
            {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
			}
		}
	}

	@Override
	public void present(float deltaTime)
    {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		guiCam.setViewportAndMatrices();

		gl.glEnable(GL10.GL_TEXTURE_2D);

		batcher.beginBatch(Assets.layer0);

		   batcher.drawSprite(guiCam.position.x, guiCam.position.y, 1280, 720, Assets.layerRegion0);

		batcher.endBatch();

		gl.glDisable(GL10.GL_TEXTURE_2D);

		renderer.render(world, deltaTime);

		switch (state)
        {
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		case GAME_OVER:
			presentGameOver();
		}

		fpsCounter.logFrame();
	}

	private void presentPaused()
    {
		GL10 gl = glGraphics.getGL();
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.RedFont);

        switch(menu)
        {
            case 0:
                //PLAY SCREEN SELECTED
                Assets.redfont.drawText(batcher, "Resume"     ,  1280 /2 - 80, 720 /2, 25.0f * scale, 25.0f * scale);
                Assets.redfont.drawText(batcher, "Quit",  1280 /2 - 80, 720 /2 - 50,  25.0f, 25.0f  );


                break;
            case 1:
                //PLAY SCREEN SELECTED
                Assets.redfont.drawText(batcher, "Resume"     ,  1280 /2 - 80, 720 /2, 25.0f, 25.0f);
                Assets.redfont.drawText(batcher, "Quit",  1280 /2 - 80, 720 /2 - 50,  25.0f * scale, 25.0f * scale  );

                break;
        }

        batcher.endBatch();

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
	}

	private void presentRunning()
    {
		GL10 gl = glGraphics.getGL();
		guiCam.setViewportAndMatrices();

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);

           batcher.beginBatch(Assets.RedFont);

              Assets.redfont.drawText(batcher, scoreString,  1280 /2 - 220, 670,  25.0f, 25.0f  );

           batcher.endBatch();

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
	}

	private void presentGameOver()
    {
		GL10 gl = glGraphics.getGL();
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		batcher.beginBatch(Assets.items);

		   batcher.drawSprite(240, 160, 128, 64, Assets.gameOverRegion);

           Assets.redfont.drawText(batcher, scoreString,  1280 /2 - 220, 670,  25.0f, 25.0f  );

		batcher.endBatch();

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause()
    {
		state = GAME_PAUSED;
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
