package de.family_networking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.onesignal.OneSignal;

import de.family_networking.commons.CustomClickListener;
import de.family_networking.commons.CustomOnTouchListener;
import de.family_networking.commons.CustomWebChromeClient;
import de.family_networking.commons.CustomWebViewClient;
import de.family_networking.push_notification.CustomIdsAvailableHandler;
import de.family_networking.push_notification.JavaScriptInterface;

public class MainActivity extends AppCompatActivity
{
    private WebView contentWebView;
    private CustomWebChromeClient webChromeClient;
    private CustomOnTouchListener onTouchListerener;

    public static final String URL = "https://family-networking.de";

    public MainActivity()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Registrierung für Push-Notifications
        OneSignal.startInit(this).init();
        // Auslesen der RegistrierungsInformationen
        CustomIdsAvailableHandler customIdsAvailableHandler = new CustomIdsAvailableHandler();
        OneSignal.idsAvailable(customIdsAvailableHandler);

        this.setContentView(R.layout.activity_main);

        this.contentWebView = (WebView) findViewById(R.id.webView);
        LinearLayout mlLayoutRequestError  = (LinearLayout) findViewById(R.id.lLayoutRequestError);

        WebSettings settings = contentWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        CustomWebViewClient customWebViewClient = new CustomWebViewClient( mlLayoutRequestError );
        this.contentWebView.setWebViewClient(customWebViewClient);
        this.contentWebView.addJavascriptInterface(new JavaScriptInterface(customIdsAvailableHandler), "AndroidInterface");

        CustomClickListener reloadListener = new CustomClickListener( this.contentWebView, customWebViewClient );
        ((Button ) findViewById(R.id.btnRetry)).setOnClickListener( reloadListener );

        this.webChromeClient = new CustomWebChromeClient( this );
        this.contentWebView.setWebChromeClient(webChromeClient);

        this.onTouchListerener = new CustomOnTouchListener();
        this.contentWebView.setOnTouchListener(onTouchListerener);

        this.contentWebView.loadUrl( URL );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        this.webChromeClient.onActivityResult(requestCode,resultCode,intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        this.webChromeClient.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed()
    {
        if (this.contentWebView.canGoBack())
        {
            String current_url =  this.contentWebView.getOriginalUrl();
            if(current_url.endsWith("/"))
            {
                current_url = current_url.substring(0, current_url.length() - 1);
            }
            System.out.println(current_url + "  -  " + URL);
            if(current_url.equals(URL))
            {
                this.finish();
            }
            else if(current_url.equals(URL + "/" + CustomOnTouchListener.PHOTO) || current_url.equals(URL + "/" + CustomOnTouchListener.NEWSFEED))
            {
                onTouchListerener.resetCurrentMenuSite();
                this.contentWebView.loadUrl( URL );
            }
            else
            {
                this.contentWebView.loadUrl( URL + "/" + onTouchListerener.getCurrentMenuSite());
            }
        }
        else
        {
            super.onBackPressed();
        }
    }
}
