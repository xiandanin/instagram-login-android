package com.dyhdyh.instagram.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.MalformedURLException;

/**
 * @author dengyuhan
 *         created 2018/1/30 20:31
 */
public class InstagramAuthDialog extends Dialog {
    protected InstagramAuthHelper mHelper;

    private WebView mWebView;
    private ProgressBar mProgressBar;

    public InstagramAuthDialog(@NonNull Activity context) {
        this(context, R.style.Theme_Dialog_Instagram);
    }

    public InstagramAuthDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_instagram_auth);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        mWebView = findViewById(R.id.wb_dialog_instagram);
        mProgressBar = findViewById(R.id.pb_dialog_instagram);
    }

    public InstagramAuthDialog setup(String clientId, String clientSecret, String redirectUri) {
        mHelper = new InstagramAuthHelper(clientId, clientSecret, redirectUri);
        applyWebViewBasicSetting();
        return this;
    }


    public InstagramAuthDialog setInstagramRequest(InstagramRequest request) {
        mHelper.setInstagramRequest(request);
        return this;
    }


    protected void applyWebViewBasicSetting() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                requestAccessTokenByCode(url);
                view.loadUrl(url);
                return true;
            }
        });
    }

    protected void requestAccessTokenByCode(String url) {
        try {
            String code = mHelper.parserCodeByUrl(url);
            if (!TextUtils.isEmpty(code)) {
                mHelper.requestAccessToken(code);
                super.cancel();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        super.show();
        mWebView.loadUrl(mHelper.getRedirectOAuthUrl());
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
