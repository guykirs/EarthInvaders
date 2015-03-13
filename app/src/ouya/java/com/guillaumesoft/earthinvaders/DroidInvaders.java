package com.guillaumesoft.earthinvaders;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.GLGame;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.Collection;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import tv.ouya.console.api.CancelIgnoringOuyaResponseListener;
import tv.ouya.console.api.GamerInfo;
import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.OuyaController;
import tv.ouya.console.api.OuyaResponseListener;
import tv.ouya.console.api.Receipt;
import tv.ouya.console.api.content.OuyaContent;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;

public class DroidInvaders extends GLGame
{
	boolean firstTimeCreate = true;
    boolean ControllerNull  = true;
    private OuyaFacade mOuyaFacade;
    private static final String LOG_TAG = "Invaders";
    public static final String DEVELOPER_ID = "ab58c9eb-0774-4cfc-8309-0c24895fc58f";
    private PublicKey mPublicKey = null;
    private OuyaContent mContent;

    byte[] loadApplicationKey()
    {
        // Create a PublicKey object from the key data downloaded from the developer portal.
        try
        {
            // Read in the key.der file (downloaded from the developer portal)
            InputStream inputStream = getResources().openRawResource(R.raw.key);
            byte[] applicationKey = new byte[inputStream.available()];
            inputStream.read(applicationKey);
            inputStream.close();
            return applicationKey;
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "Unable to load application key", e);
        }

        return null;
    }

	@Override
	public Screen getStartScreen()
    {
		return new PressStartScreen(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
		super.onSurfaceCreated(gl, config);

		if (firstTimeCreate)
        {
			Settings.load(getFileIO());
			Assets.load(this);

			firstTimeCreate = false;
            OuyaController.init(this);

            // CREATE A STATIC CONTEXT FOR THE GAME
            ScreenManager.SetContext(this);
            //requestGamerInfo();

            OuyaController.startOfFrame();
		}
        else
        {
			Assets.reload();
		}
	}
    private synchronized void requestGamerInfo()
    {
        Log.d(LOG_TAG, "Requesting gamerinfo");
        mOuyaFacade.requestGamerInfo(this, new CancelIgnoringOuyaResponseListener<GamerInfo>() {
            @Override
            public void onSuccess(GamerInfo result)
            {
                ScreenManager.setUserName(result.getUsername());
            }

            @Override
            public void onFailure(int errorCode, String errorMessage, Bundle optionalData)
            {
                ScreenManager.setUserName("");
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode,  KeyEvent event)
    {
        if(ControllerNull == true)
        {
            OuyaController controller = OuyaController.getControllerByDeviceId(event.getDeviceId());
            ScreenManager.SetController(controller);

            // Check the keycode itself
            if (keyCode == OuyaController.BUTTON_O)
            {
                ControllerNull = false;

                GameRatingScreen rating = new GameRatingScreen(this);
                setScreen(rating);
            }
        }

        return OuyaController.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return OuyaController.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        return OuyaController.onGenericMotionEvent(event) || super.onGenericMotionEvent(event);
    }


    @Override
	public void onPause()
    {
		super.onPause();
		if (Settings.soundEnabled)
			Assets.music.pause();
	}

    /**
     * The callback for when the list of user receipts has been requested.
     */
    private class ReceiptListener implements OuyaResponseListener<Collection<Receipt>>
    {
        /**
         * Handle the successful fetching of the data for the receipts from the server.
         *
         //* @param receiptResponse The response from the server.
         */
        @Override
        public void onSuccess(Collection<Receipt> receipts)
        {
            for (Receipt receipt : receipts)
            {
                if(receipt.getIdentifier().equals("earthinvaders"))
                {
                    // THE PLAYERIS PLAYING THE FULL GAME

                }
            }

        }

        /**
         * Handle a failure. Because displaying the receipts is not critical to the application we just show an error
         * message rather than asking the user to authenticate themselves just to start the application up.
         *
         * @param errorCode An HTTP error code between 0 and 999, if there was one. Otherwise, an internal error code from the
         *                  //Ouya server, documented in the  class.
         *
         * @param errorMessage Empty for HTTP error codes. Otherwise, a brief, non-localized, explanation of the error.
         *
         * @param optionalData A Map of optional key/value pairs which provide additional information.
         */

        @Override
        public void onFailure(int errorCode, String errorMessage, Bundle optionalData)
        {
            Log.w(LOG_TAG, "Request Receipts error (code " + errorCode + ": " + errorMessage + ")");
            //showError("Could not fetch receipts (error " + errorCode + ": " + errorMessage + ")");
        }

        /*
         * Handle user canceling
         */
        @Override
        public void onCancel()
        {

        }
    }

  /*  @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        OuyaController.init(this);

        Bundle developerInfo = new Bundle();
        developerInfo.putString(OuyaFacade.OUYA_DEVELOPER_ID, DEVELOPER_ID);
        developerInfo.putByteArray(OuyaFacade.OUYA_DEVELOPER_PUBLIC_KEY, loadApplicationKey());
        mOuyaFacade = OuyaFacade.getInstance();
        mOuyaFacade.init(this, developerInfo);

        mOuyaFacade.requestReceipts(this, new ReceiptListener());

        requestGamerInfo();
        mContent = OuyaContent.getInstance();

        try
        {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(loadApplicationKey());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            mPublicKey = keyFactory.generatePublic(keySpec);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        mContent.init(getApplicationContext(), mPublicKey);

    }*/
}