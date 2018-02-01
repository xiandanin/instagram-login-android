package com.dyhdyh.instagram.login;

import java.util.Map;

/**
 * @author dengyuhan
 *         created 2018/2/1 11:01
 */
public interface InstagramRequest {


    void requestAccessToken(String tokenUrl, Map<String, String> params);
}
