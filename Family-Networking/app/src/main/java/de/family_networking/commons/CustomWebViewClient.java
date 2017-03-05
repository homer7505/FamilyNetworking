package de.family_networking.commons;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * Created by Lars Nitzsche on 22.08.2016.
 */
public class CustomWebViewClient extends WebViewClient
{
    private boolean mbErrorOccured = false;
    private LinearLayout mlLayoutRequestError;
    private CustomClickListener listener;
    private Handler mhErrorLayoutHide;

    public CustomWebViewClient( LinearLayout mlLayoutRequestError )
    {
        super();
        this.mlLayoutRequestError = mlLayoutRequestError;
        this.mhErrorLayoutHide = getErrorLayoutHideHandler();
    }

    @Override
    public void onReceivedError( WebView view, WebResourceRequest request, WebResourceError error )
    {
        mbErrorOccured = true;
        view.loadUrl("file:///android_asset/connection_error.html");
        this.showErrorLayout();
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        if ( mbErrorOccured == false && listener.isMbReloadPressed() )
        {
            hideErrorLayout();
            listener.resetMbReloadPressed();
        }
        /* This call inject JavaScript into the page which just finished loading. */
        view.loadUrl("javascript:window.AndroidInterface.processHTML(document.getElementsByTagName('html')[0].innerHTML);");
    }

    public boolean isMbErrorOccured()
    {
        return mbErrorOccured;
    }

    public void resetMbErrorOccured( )
    {
        this.mbErrorOccured = false;
    }

    public void setListener( CustomClickListener listener )
    {
        this.listener = listener;
    }

    private void showErrorLayout()
    {
        this.mlLayoutRequestError.setVisibility(View.VISIBLE);
    }

    private void hideErrorLayout()
    {
        mlLayoutRequestError.setVisibility(View.INVISIBLE);
    }

    private Handler getErrorLayoutHideHandler()
    {
        return new Handler()
        {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
            }
        };
    }
}
