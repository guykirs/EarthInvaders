package com.guillaumesoft.earthinvaders;

import android.content.Context;

import tv.ouya.console.api.OuyaController;

/**
 * Created by Gustave on 30/01/2015.
 */
public class ScreenManager
{
    /////////////////////////////////////////////////////////////////////////
    // GET AND SET GAME CONTROLLER ID
    public static void SetController(OuyaController value)
    {
        ouyacontroller = value;
    }

    public static OuyaController GetController()
    {
        return ouyacontroller;
    }
    public static  OuyaController ouyacontroller;

    ////////////////////////////////////////////////////////////////////////
    // GET AND SET GAME CONTENT
    public static Context GetContext()
    {
        return context;
    }

    public static void SetContext(Context value)
    {
        context = value;
    }
    public static Context context;

    // GET AND SET USER NAME
    public static void setUserName(String value)
    {
        mUserName = value;
    }
    public static String getUserName()
    {
        return mUserName;
    }
    public static String mUserName;
}
