package com.dyhdyh.instagram.login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;

/**
 * @author dengyuhan
 *         created 2018/1/30 20:31
 */
public class InstagramAuthDialog extends Dialog {
    private InstagramAuthHelper mHelper;

    private WebView mWebView;

    public InstagramAuthDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_instagram_auth);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mWebView = findViewById(R.id.wb_dialog_instagram);
    }


    public InstagramAuthDialog setup(String clientId, String clientSecret, String redirectUri) {
        mHelper = new InstagramAuthHelper(clientId, clientSecret, redirectUri);
        applyWebViewBasicSetting();
        return this;
    }


    protected void applyWebViewBasicSetting() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try {
                    String code = mHelper.parserCodeByUrl(url);
                    if (!TextUtils.isEmpty(code)) {
                        mHelper.requestAccessToken(code);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    @Override
    public void show() {
        super.show();
        mWebView.loadUrl(mHelper.getOAuthUrl());
    }
}
