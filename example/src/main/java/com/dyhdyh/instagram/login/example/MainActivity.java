package com.dyhdyh.instagram.login.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dyhdyh.instagram.login.InstagramAuthDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickAccessToken(View v) {
        new InstagramAuthDialog(this)
                .setup(getString(R.string.client_id), getString(R.string.client_secret),getString(R.string.redirect_uri))
                .show();
    }
}
