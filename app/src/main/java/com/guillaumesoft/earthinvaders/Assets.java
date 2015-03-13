package com.guillaumesoft.earthinvaders;

import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.Sound;
import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.Font;
import com.badlogic.androidgames.framework.gl.ObjLoader;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.gl.Vertices3;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets
{
	public static Texture background;
	public static TextureRegion backgroundRegion;
    public static Texture layer0;
    public static TextureRegion layerRegion0;

	public static Texture items;
    public static TextureRegion buttonO;
    public static TextureRegion buttonU;
    public static TextureRegion buttonY;
    public static TextureRegion buttonA;
	public static TextureRegion logoRegion;
	public static TextureRegion menuRegion;
	public static TextureRegion gameOverRegion;
	public static TextureRegion pauseRegion;
	public static TextureRegion settingsRegion;
	public static TextureRegion touchRegion;
	public static TextureRegion accelRegion;
	public static TextureRegion touchEnabledRegion;
	public static TextureRegion accelEnabledRegion;
	public static TextureRegion soundRegion;
	public static TextureRegion soundEnabledRegion;
	public static TextureRegion leftRegion;
	public static TextureRegion rightRegion;
	public static TextureRegion fireRegion;
	public static TextureRegion pauseButtonRegion;
	public static Font font;

    public static Texture companyscreen;
    public static TextureRegion companyscreenRegion;

    public static Texture esrbm;
    public static TextureRegion esrbmRegion;

    public static Texture explosionTexture;
	public static Animation explosionAnim;
	public static Vertices3 shipModel;
	public static Texture shipTexture;
	public static Vertices3 invaderModel;
	public static Texture invaderTexture;
	public static Vertices3 shotModel;
	public static Vertices3 shieldModel;

    public static Texture BlackFont;
    public static Font blackfont;

    public static Texture RedFont;
    public static Font redfont;

    public static Texture BlueFont;
    public static Font bluefont;

	public static Music music;
    public static Music drum;
	public static Sound clickSound;
	public static Sound explosionSound;
	public static Sound shotSound;

	public static void load(GLGame game)
    {
        BlackFont = new Texture(game, "blackFont.png");
        blackfont = new Font(BlackFont, 224, 0, 16, 16, 20);

        RedFont = new Texture(game, "redFont.png");
        redfont = new Font(RedFont, 224, 0, 16, 16, 20);

        BlueFont  = new Texture(game, "blueFont.png");
        bluefont  = new Font(BlueFont, 224, 0, 16, 16, 20);

        companyscreen        = new Texture(game, "company.png");
        companyscreenRegion  = new TextureRegion(companyscreen , 0, 0, 512, 512);

        esrbm                = new Texture(game, "esrbm.png");
        esrbmRegion          = new TextureRegion(esrbm , 0, 0, 512, 512);

        layer0       = new Texture(game, "layer0.png", true);
        layerRegion0 = new TextureRegion(layer0, 0, 0, 512, 512);

        background         = new Texture(game, "background.jpg", true);
		backgroundRegion   = new TextureRegion(background, 0, 0, 512, 512);
		items              = new Texture(game, "items.png", true);
		logoRegion         = new TextureRegion(items, 0, 256, 384, 128);
		menuRegion         = new TextureRegion(items, 0, 128, 224, 64);
		gameOverRegion     = new TextureRegion(items, 224, 128, 128, 64);
		pauseRegion        = new TextureRegion(items, 0, 192, 160, 64);
		settingsRegion     = new TextureRegion(items, 0, 160, 224, 32);
		touchRegion        = new TextureRegion(items, 0, 384, 64, 64);
		accelRegion        = new TextureRegion(items, 64, 384, 64, 64);
		touchEnabledRegion = new TextureRegion(items, 0, 448, 64, 64);
		accelEnabledRegion = new TextureRegion(items, 64, 448, 64, 64);
		soundRegion        = new TextureRegion(items, 128, 384, 64, 64);
		soundEnabledRegion = new TextureRegion(items, 190, 384, 64, 64);
		leftRegion         = new TextureRegion(items, 0, 0, 64, 64);
		rightRegion        = new TextureRegion(items, 64, 0, 64, 64);
		fireRegion         = new TextureRegion(items, 128, 0, 64, 64);
		pauseButtonRegion  = new TextureRegion(items, 0, 64, 64, 64);
		font = new Font(items, 224, 0, 16, 16, 20);

        RedFont = new Texture(game, "redFont.png");
        redfont = new Font(RedFont, 224, 0, 16, 16, 20);

		explosionTexture = new Texture(game, "explode.png", true);
		TextureRegion[] keyFrames = new TextureRegion[16];

		int frame = 0;

		for (int y = 0; y < 256; y += 64)
        {
			for (int x = 0; x < 256; x += 64)
            {
				keyFrames[frame++] = new TextureRegion(explosionTexture, x, y,
						64, 64);
			}
		}

		explosionAnim = new Animation(0.1f, keyFrames);

		shipTexture = new Texture(game, "ship.png", true);
		shipModel = ObjLoader.load(game, "ship.obj");
		invaderTexture = new Texture(game, "invader.png", true);
		invaderModel = ObjLoader.load(game, "invader.obj");
		shieldModel = ObjLoader.load(game, "shield.obj");
		shotModel = ObjLoader.load(game, "shot.obj");

        buttonO   = new TextureRegion(items,   0, 0, 64, 64);
        buttonU   = new TextureRegion(items,  64, 0, 64, 64);
        buttonY   = new TextureRegion(items, 128, 0, 64, 64);
        buttonA   = new TextureRegion(items, 448, 448, 64, 64);

		music = game.getAudio().newMusic("Music.mp3");
		music.setLooping(true);
		music.setVolume(0.8f);

        drum = game.getAudio().newMusic("drum.wav");
        drum.setLooping(true);
        drum.setVolume(0.5f);

		clickSound = game.getAudio().newSound("click.ogg");
		explosionSound = game.getAudio().newSound("explosion.ogg");
		shotSound = game.getAudio().newSound("shot.ogg");
	}

	public static void reload()
    {
		background.reload();
		items.reload();
		explosionTexture.reload();
		shipTexture.reload();
		invaderTexture.reload();
        RedFont.reload();
        layer0.reload();
	}

    public static void playMusic()
    {
        if(Settings.musicEnabled)
            music.play();
    }

	public static void playSound(Sound sound)
    {
		if (Settings.soundEnabled)
			sound.play(1);
	}
}
