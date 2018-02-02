package com.dyhdyh.instagram.login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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

    private ViewGroup mAuthMainLayout;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private View mToolBarView;

    public InstagramAuthDialog(@NonNull Context context) {
        this(context, R.style.Theme_Dialog_Instagram);
    }

    public InstagramAuthDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_instagram_auth);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        mAuthMainLayout = findViewById(R.id.layout_auth_main);
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


    public InstagramAuthDialog setupToolbar(AuthToolbarCallback callback) {
        if (mToolBarView != null) {
            mAuthMainLayout.removeView(mToolBarView);
        }
        if (callback != null) {
            View toolbarView = callback.onSetupToolbar(mAuthMainLayout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAuthCookie();
                    mWebView.loadUrl(mHelper.getRedirectOAuthUrl());
                }
            });
            if (toolbarView != null) {
                mToolBarView = toolbarView;
                mAuthMainLayout.addView(mToolBarView, 0);
            }
        }
        return this;
    }

    public InstagramAuthDialog setProgressDrawable(Drawable drawable) {
        mProgressBar.setProgressDrawable(drawable);
        return this;
    }


    public void clearAuthCookie() {
        CookieSyncManager.createInstance(mWebView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
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

    public interface AuthToolbarCallback {
        View onSetupToolbar(ViewGroup parentView, View.OnClickListener backClickListener, View.OnClickListener clearAuthClickListener);
    }
}
