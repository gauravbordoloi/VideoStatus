package com.sairajen.videostatus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import im.delight.android.webview.AdvancedWebView;

public class HomeActivity extends AppCompatActivity implements AdvancedWebView.Listener{

    private Toolbar toolbar;
    private AdvancedWebView webView;
    private SmoothProgressBar progressBar;
    private AdView adView;

    private InterstitialAd mInterstitialAd;
    private String link = "http://www.5status.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (AdvancedWebView) findViewById(R.id.webView);
        progressBar = (SmoothProgressBar) findViewById(R.id.webview_progressBar);
        adView = (AdView) findViewById(R.id.bannerAd);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webView.setListener(this, this);
        webView.setDesktopMode(false);

        webView.loadUrl(link);

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.ad_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adView.loadAd(new AdRequest.Builder().build());
        
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    @Override
    public void onPageFinished(String url) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "some error occurred!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        webView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (!webView.onBackPressed()) {
            return;
        }
        if (!showInterstitial()) {
            super.onBackPressed();
        }
    }

    public boolean showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    onBackPressed();
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.link_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.link_copy:
                Util.copyText(HomeActivity.this,link);
                break;

            case R.id.link_share:
                Util.shareLink(HomeActivity.this,link);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
