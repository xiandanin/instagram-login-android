# instagram-login-android
基于OAuth API对Instagram的授权登录封装成SDK

###  Gradle
```
compile 'com.dyhdyh:instagram-login:1.0.3'
```


###  示例
```
new InstagramAuthDialog(this)
        .setup(clientId, clientSecret, redirectUri)
        .setupToolbar((parentView, backClickListener, clearAuthClickListener) -> {
            View layout = LayoutInflater.from(parentView.getContext()).inflate(R.layout.layout_instagram_toolbar, parentView, false);
            layout.findViewById(R.id.tv_toolbar_left).setOnClickListener(backClickListener);
            layout.findViewById(R.id.tv_toolbar_right).setOnClickListener(clearAuthClickListener);
            return layout;
        })
        .
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
