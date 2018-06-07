package com.yuvi.hamroui.simplelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.hamroui.Pref;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

/**
 * Created by yubaraj on 2/4/18.
 */

public class SimpleWebViewActivity extends AppCompatActivity {
    ProgressBar pBar;
    WebView wv;
    boolean fromDeepLink = false;
    String link;
    String title = "", source = "", desc = "";
    Pref pref;
    boolean hideTitle = false;
    TextView tv_source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simpledesc);
        Utils.log(this.getClass(), "data = " + getIntent().getDataString());
        String dataFromDeepLink = getIntent().getDataString();
        fromDeepLink = !TextUtils.isEmpty(dataFromDeepLink);
        pref = new Pref(this);
        tv_source = findViewById(R.id.news_source);
        if (fromDeepLink) {
            try {
                Uri uri = Uri.parse(dataFromDeepLink);
                title = "News";
                link = uri.getQueryParameter("url");
                hideTitle = uri.getBooleanQueryParameter("hide_url", false);
                if(hideTitle){
                    tv_source.setVisibility(View.GONE);
                }
                source = "(Loading) " + link;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (getIntent().hasExtra("title")) {
                title = getIntent().getStringExtra("title");
            }
            if (getIntent().hasExtra("source")) {
                source = getIntent().getStringExtra("source");
            }
            if (getIntent().hasExtra("link")) {
                link = getIntent().getStringExtra("link");
            }
            if (getIntent().hasExtra("desc")) {
                desc = getIntent().getStringExtra("desc");
            }

        }
        final WebView wv = findViewById(R.id.wv);
        final TextView tv_title = findViewById(R.id.news_title);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(source)) {
            tv_source.setText(source);
        }

        pBar = findViewById(R.id.pBar);
        pBar.setMax(100);
        pBar.setProgress(0);

        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                (SimpleWebViewActivity.this).setProgress(newProgress);
                pBar.incrementProgressBy(newProgress);
                if (newProgress == 100 && pBar.isShown()) pBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tv_title.setText(title);
//                Utilities.sendScreen("web/"+ title);
                super.onReceivedTitle(view, title);
            }
        });

        wv.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Uri i = Uri.parse(url);
                tv_source.setText(i.getHost());
            }
        });


        wv.getSettings().setJavaScriptEnabled(true);
        if (!TextUtils.isEmpty(link)) {
            wv.loadUrl(link);
        } else {
            wv.loadDataWithBaseURL("", desc, "text/html", "UTF-8", null);
        }

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence colors[] = new CharSequence[]{getString(R.string.share), getString(R.string.copyline), getString(R.string.mesg_open_in_browser), getString(R.string.refresh)};
                AlertDialog.Builder builder = new AlertDialog.Builder(SimpleWebViewActivity.this);
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
//                                Utilities.sendEvent("News", "share", title);
                                startActivity(Utils.getDefaultShareIntent(getString(R.string.mesg_share_link_to), link));
                                break;
                            case 1:
//                                Utilities.sendEvent("News", "CopyClipboard", title);
                                Utils.copyClipboard(SimpleWebViewActivity.this, link);
                                toast(getString(R.string.mesg_copied_clipboard));
                                break;
                            case 2:
//                                Utilities.sendEvent("News", "Link Opened", title);
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                                break;
                            case 3:
                                wv.reload();
                                toast(getString(R.string.mesg_refreshing) + " ...");
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

//        initializeGoogleAddOnlyIfNetworkAvailable(true);
        String admobId = pref.getPreferences(Pref.KEY_ADMOB_ID);
        String bannerAddId = pref.getPreferences(Pref.KEY_BANNER_ID);
        if (!TextUtils.isEmpty(admobId) && !TextUtils.isEmpty(admobId) && DmUtilities.isNetworkConnected(this)) {
            Bundle extras = new Bundle();
            extras.putString("max_ad_content_rating", "G");
            MobileAds.initialize(this, admobId);
            AdView mAdView = new AdView(this);
            mAdView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(bannerAddId);
            AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
            mAdView.loadAd(adRequest);
            LinearLayout ll_ads = findViewById(R.id.ll_ads);
            ll_ads.removeAllViews();
            ll_ads.addView(mAdView);
        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        Utils.log(this.getClass(), "deeplink = " + fromDeepLink);
        if (fromDeepLink) {
            PackageManager pm = getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(pref.getPreferences("pkgname")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.finish();
        } else {
            super.finish();
        }
    }
}
