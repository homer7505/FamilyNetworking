package de.family_networking.de.family_networking.commons;

import android.view.View;
import android.webkit.WebView;

import de.family_networking.R;

/**
 * Created by Lars Nitzsche on 30.08.2016.
 */
public class CustomClickListener implements View.OnClickListener
{
    private WebView contentWebView;
    private CustomWebViewClient customWebViewClient;

    private boolean mbReloadPressed = false;

    public  CustomClickListener(WebView contentWebView, CustomWebViewClient customWebViewClient)
    {
        this.contentWebView = contentWebView;
        this.customWebViewClient = customWebViewClient;
        this.customWebViewClient.setListener(this);
    }

    @Override
    public void onClick( View view )
    {
        int id = view.getId();

        if (id == R.id.btnRetry) {
            if (!this.customWebViewClient.isMbErrorOccured()) {
                return;
            }

            mbReloadPressed = true;
            this.contentWebView.loadUrl("https://family-networking.de");
            this.customWebViewClient.resetMbErrorOccured();
        }
    }

    public boolean isMbReloadPressed()
    {
        return mbReloadPressed;
    }

    public void resetMbReloadPressed()
    {
        this.mbReloadPressed = false;
    }
}
