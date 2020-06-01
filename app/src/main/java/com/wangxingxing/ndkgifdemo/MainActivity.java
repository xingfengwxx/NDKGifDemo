package com.wangxingxing.ndkgifdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ImageView mImageView;
    private Button mBtnPlay;
    private Button mBtnPause;
    private Button mBtnResume;
    private Button mBtnRelease;
    private Button mBtnGifDrawable;
    private Button mBtnGlideLoad;
    private GifPlayer mGifPlayer;

    public static final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       checkPermission();
       initView();
    }

    private void initView() {
        mImageView = findViewById(R.id.iv_show);
        mBtnPlay = findViewById(R.id.btn_play);
        mBtnPause = findViewById(R.id.btn_pause);
        mBtnResume = findViewById(R.id.btn_resume);
        mBtnRelease = findViewById(R.id.btn_release);
        mBtnGifDrawable = findViewById(R.id.btn_gif_drawable);
        mBtnGlideLoad = findViewById(R.id.btn_glide_load);

        mBtnPlay.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnResume.setOnClickListener(this);
        mBtnRelease.setOnClickListener(this);
        mBtnGifDrawable.setOnClickListener(this);
        mBtnGlideLoad.setOnClickListener(this);

        mGifPlayer = new GifPlayer();
        mGifPlayer.setOnGifListener(new GifPlayer.OnGifListener() {
            @Override
            public void start() {
                Log.i(TAG, "start: ");
            }

            @Override
            public void draw(final Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void end() {
                Log.i(TAG, "end: ");
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 666);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                mGifPlayer.assetPlay(false, MainActivity.this, "test.gif");
                break;
            case R.id.btn_pause:
                mGifPlayer.pause();
                break;
            case R.id.btn_resume:
                mGifPlayer.resume();
                break;
            case R.id.btn_release:
                mGifPlayer.stop();
                break;
            case R.id.btn_gif_drawable:
                try {
                    GifDrawable gifFromAssets = new GifDrawable(getAssets(), "test.gif" );
                    mImageView.setImageDrawable(gifFromAssets);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_glide_load:
                String pathUri="file:///android_asset/test.gif";
                Glide.with(this).load(Uri.parse(pathUri)).into(mImageView);
                break;
        }
    }
}
