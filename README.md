# instagram-login-android
基于OAuth API对Instagram第三方登录的封装

###  Gradle
```
compile 'com.dyhdyh:instagram-login:1.0.0'
```


###  示例
```
new InstagramAuthDialog(this)
        .setup(clientId, clientSecret, redirectUri)
        //.setProgressDrawable(drawable)
        .setInstagramRequest(new InstagramRequest() {
            @Override
            public void requestAccessToken(String tokenUrl, Map<String, String> params) {
                //用自己的http框架 带上params里的参数,请求tokenUrl
            }
        })
        .show();
```


###  完全自定义
参考`InstagramAuthDialog`