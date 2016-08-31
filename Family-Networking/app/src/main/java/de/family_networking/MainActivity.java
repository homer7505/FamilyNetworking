package de.family_networking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import de.family_networking.de.family_networking.commons.CustomClickListener;
import de.family_networking.de.family_networking.commons.CustomWebChromeClient;
import de.family_networking.de.family_networking.commons.CustomWebViewClient;

public class MainActivity extends AppCompatActivity
{
    private WebView contentWebView;
    private CustomWebChromeClient webChromeClient;

    public static final String URL = "";

    public MainActivity()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        this.contentWebView = (WebView) findViewById(R.id.webView);
        LinearLayout mlLayoutRequestError  = (LinearLayout) findViewById(R.id.lLayoutRequestError);

        WebSettings settings = contentWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);


        CustomWebViewClient customWebViewClient = new CustomWebViewClient( mlLayoutRequestError );
        this.contentWebView.setWebViewClient(customWebViewClient);

        CustomClickListener reloadListener = new CustomClickListener( this.contentWebView, customWebViewClient );
        ((Button ) findViewById(R.id.btnRetry)).setOnClickListener( reloadListener );

        this.webChromeClient = new CustomWebChromeClient( this );
        this.contentWebView.setWebChromeClient(webChromeClient);

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
            this.contentWebView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
