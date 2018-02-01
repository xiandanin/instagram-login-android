package com.dyhdyh.instagram.login;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    private InstagramRequest mInstagramRequest;

    public InstagramAuthHelper(String clientId, String clientSecret, String redirectUri) {
        this.mClientId = clientId;
        this.mClientSecret = clientSecret;
        this.mRedirectUri = redirectUri;
    }

    /**
     * 请求获取token
     *
     * @param code
     */
    public void requestAccessToken(String code) {
        if (mInstagramRequest != null) {
            mInstagramRequest.requestAccessToken(URL_ACCESS_TOKEN, buildAccessTokenParams(code));
        }
    }

    public void setInstagramRequest(InstagramRequest request) {
        this.mInstagramRequest = request;
    }

    /**
     * 授权页面的url
     *
     * @return
     */
    public String getRedirectOAuthUrl() {
        return String.format("%s?client_id=%s&redirect_uri=%s&response_type=code", URL_OAUTH, mClientId, mRedirectUri);
    }


    /**
     * 从url里解析出code
     *
     * @param url
     * @return
     * @throws MalformedURLException
     */
    public String parserCodeByUrl(String url) throws MalformedURLException {
        if (!TextUtils.isEmpty(url) && url.startsWith(mRedirectUri)) {
            URL newUrl = new URL(url);
            String queryString = newUrl.getQuery();
            Map<String, String> params = queryString2Params(queryString);
            return params.get("code");
        }
        return "";
    }


    /**
     * 组装获取AccessToken要传的参数
     *
     * @param code
     * @return
     */
    public Map<String, String> buildAccessTokenParams(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", mClientId);
        params.put("client_secret", mClientSecret);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", mRedirectUri);
        params.put("code", code);
        return params;
    }


    /**
     * 从path里解析参数
     *
     * @param queryString
     * @return
     */
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
