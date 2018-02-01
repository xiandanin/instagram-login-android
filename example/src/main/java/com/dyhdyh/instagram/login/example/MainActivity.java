package com.dyhdyh.instagram.login.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dyhdyh.instagram.login.InstagramAuthDialog;
import com.dyhdyh.instagram.login.InstagramRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickAccessToken(View v) {
        new InstagramAuthDialog(this)
                .setup(getString(R.string.client_id), getString(R.string.client_secret), getString(R.string.redirect_uri))
                .setInstagramRequest(new InstagramRequest() {
                    @Override
                    public void requestAccessToken(String tokenUrl, Map<String, String> params) {
                        requestInstagramAccessToken(tokenUrl, params);
                    }
                })
                .show();
    }

    private void requestInstagramAccessToken(String tokenUrl, Map<String, String> params) {
        Set<Map.Entry<String, String>> entries = params.entrySet();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        Request post = new Request.Builder().url(tokenUrl).post(builder.build()).build();
        new OkHttpClient.Builder().build().newCall(post).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Instagram-Login", "登录失败", e);
                        Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String json = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Instagram-Login", "登录成功" + json);
                        Toast.makeText(MainActivity.this, "登录成功" + json, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}
