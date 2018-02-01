package com.dyhdyh.instagram.login;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author dengyuhan
 *         created 2018/1/31 16:13
 */
public class InstagramAuthHelper {

    public static final String URL_OAUTH = "https://api.instagram.com/oauth/authorize";
    public static final String URL_ACCESS_TOKEN = "https://api.instagram.com/oauth/access_token";

    private String mClientId;
    private String mClientSecret;
    private String mRedirectUri;

    public InstagramAuthHelper(String clientId, String clientSecret, String redirectUri) {
        this.mClientId = clientId;
        this.mClientSecret = clientSecret;
        this.mRedirectUri = redirectUri;
    }

    public String getOAuthUrl() {
        return String.format("%s?client_id=%s&redirect_uri=%s&response_type=code", URL_OAUTH, mClientId, mRedirectUri);
    }


    public String parserCodeByUrl(String url) throws MalformedURLException {
        URL newUrl = new URL(url);
        if (url.startsWith(mRedirectUri)) {
            String queryString = newUrl.getQuery();
            Map<String, String> params = queryString2Params(queryString);
            return params.get("code");
        }
        return "";
    }


    public void requestAccessToken(String code) {
        Map<String, String> params = getAccessTokenParams(code);
        Set<Map.Entry<String, String>> entries = params.entrySet();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        Request post = new Request.Builder().url(URL_ACCESS_TOKEN).post(builder.build()).build();
        new OkHttpClient.Builder().build().newCall(post).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("--------------->", "登录失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d("--------------->", json);
            }
        });
    }


    public Map<String, String> getAccessTokenParams(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", mClientId);
        params.put("client_secret", mClientSecret);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", mRedirectUri);
        params.put("code", code);
        return params;
    }


    public static Map<String, String> queryString2Params(String queryString) {
        Map<String, String> params = new HashMap<>();
        String[] paramsArray = queryString.split("&");
        for (String paramsChild : paramsArray) {
            String[] paramsDetail = paramsChild.split("=");
            if (paramsDetail.length > 0 && !paramsDetail[0].trim().isEmpty()) {
                String key = paramsDetail[0];
                String value = paramsDetail.length > 1 ? paramsDetail[1] : "";
                params.put(key, value);
            }
        }
        return params;
    }
}
