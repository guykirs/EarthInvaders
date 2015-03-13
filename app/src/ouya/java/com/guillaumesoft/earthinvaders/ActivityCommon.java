package com.guillaumesoft.earthinvaders;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import tv.ouya.console.api.OuyaActivity;
import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.OuyaResponseListener;
import tv.ouya.console.api.Receipt;

// Common code used by both activities
// Isolate the purchase IAP code here
public class ActivityCommon extends OuyaActivity
{
    // The tag for log messages
    private static final String TAG = ActivityCommon.class.getSimpleName();

    // Your game talks to the OuyaFacade, which hides all the mechanics of doing an in-app purchase.
    private OuyaFacade mOuyaFacade = null;

    // listener for getting receipts
    private OuyaResponseListener<Collection<Receipt>> m_requestReceiptsListener = null;

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i(TAG, "onPause class="+getClass().getSimpleName());
        if (null != mOuyaFacade)
        {
            mOuyaFacade.shutdown();
            mOuyaFacade = null;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(TAG, "onResume class="+getClass().getSimpleName());
        init();
    }

    private void init()
    {

        if (mOuyaFacade != null)
        {
            return;
        }

        Context context = getApplicationContext();

        byte[] applicationKey = null;

        // load the application key from assets
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("key.der", AssetManager.ACCESS_BUFFER);
            applicationKey = new byte[inputStream.available()];
            inputStream.read(applicationKey);
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (null == applicationKey)
        {
            Log.e(TAG, "Failed to load signing key");
            return;
        }

        Bundle developerInfo = new Bundle();
        developerInfo.putString(OuyaFacade.OUYA_DEVELOPER_ID, "ab58c9eb-0774-4cfc-8309-0c24895fc58f");
        developerInfo.putByteArray(OuyaFacade.OUYA_DEVELOPER_PUBLIC_KEY, applicationKey);

        mOuyaFacade = OuyaFacade.getInstance();
        mOuyaFacade.init(context, developerInfo);

        m_requestReceiptsListener = new OuyaResponseListener<Collection<Receipt>>() {

            // Handle the successful fetching of the data for the receipts from the server.
            @Override
            public void onSuccess(Collection<Receipt> receipts) {
                Log.i(TAG, "requestReceipts onSuccess: received "+receipts.size() + " receipts");
                onSuccessRequestReceipts(receipts);
            }

            // Handle a failure. Because displaying the receipts is not critical to the application we just show an error
            // message rather than asking the user to authenticate themselves just to start the application up.
            @Override
            public void onFailure(int errorCode, String errorMessage, Bundle optionalData) {
                Log.e(TAG, "requestReceipts onFailure: errorCode="+errorCode+" errorMessage="+errorMessage);
                onFailureRequestReceipts(errorCode, errorMessage, optionalData);
            }

            /**
             * Handle the cancel event.
             *
             */
            @Override
            public void onCancel() {
                Log.i(TAG, "requestReceipts onCancel");
            }
        };
    }

    // Process onActivityResult events that only happen for Activity
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.i(TAG, "Processing activity result");

        if (null == mOuyaFacade)
        {
            return;
        }

        // Forward this result to the facade, in case it is waiting for any activity results
        if (mOuyaFacade.processActivityResult(requestCode, resultCode, data))
        {
            return;
        }
    }

    protected void requestReceipts()
    {
        if (null == m_requestReceiptsListener)
        {
            Log.e(TAG, "requestReceipts: m_requestReceiptsListener is null");
            return;
        }
        mOuyaFacade.requestReceipts(this, m_requestReceiptsListener);
    }

    // override this method in the activity to show in the text status field
    protected void onSuccessRequestReceipts(Collection<Receipt> receipts)
    {
    }

    // override this method in the activity to show in the text status field
    protected void onFailureRequestReceipts(int errorCode, String errorMessage, Bundle optionalData)
    {
    }
}