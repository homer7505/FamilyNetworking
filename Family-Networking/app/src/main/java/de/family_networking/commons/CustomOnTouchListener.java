package de.family_networking.commons;

import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by Lars Nitzsche on 05.03.2017.
 */

public class CustomOnTouchListener implements View.OnTouchListener
{
    private String currentMenuSite = null;
    private boolean onTouchDetected = false;


    public static final String PHOTO = "photo";
    public static final String NEWSFEED = "newsfeed";

    @Override
    public boolean onTouch( View view, MotionEvent motionEvent )
    {
        WebView.HitTestResult hr = ((WebView)view).getHitTestResult();
        if( hr.getType() == 7 && hr.getExtra().endsWith(PHOTO))
        {
            this.currentMenuSite = PHOTO;
        }
        else if( hr.getType() == 7 && hr.getExtra().endsWith(NEWSFEED))
        {
            this.currentMenuSite = NEWSFEED;
        }
        else
        {
            onTouchDetected = true;
        }

        return false;
    }

    public String getCurrentMenuSite()
    {
        return currentMenuSite;
    }

    public void resetCurrentMenuSite()
    {
        currentMenuSite = null;
    }

    public boolean isOnTouchDetected()
    {
        return onTouchDetected;
    }

    public void resetIsOnTouchDetected()
    {
        onTouchDetected = false;
    }
}
