package com.wangxingxing.ndkgifdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class GifPlayer {

    private static final String TAG = "GifPlayer";

    static {
        System.loadLibrary("gifplayer");
    }

    public enum PlayState {
        IDLE, PREPARE, PLAYING, STOP
    }

    public interface OnGifListener {
        void start();

        void draw(Bitmap bitmap);

        void end();
    }

    private Bitmap mBitmap;
    private OnGifListener mOnGifListener;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private PlayState mPlayState;
    private long mGifPlayerPtr = 0;

    public GifPlayer() {
        mGifPlayerPtr = native_create();
        mPlayState = PlayState.IDLE;
        mHandlerThread = new HandlerThread("GifPlayer");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void setOnGifListener(OnGifListener onGifListener) {
        mOnGifListener = onGifListener;
    }

    public boolean assetPlay(Context context, String gifPath) {
        return play(false, context, gifPath);
    }

    public boolean storagePlay(String gifPath) {
        return play(false, null, gifPath);
    }

    public boolean assetPlay(boolean once, Context context, String gifPath) {
        return play(once, context, gifPath);
    }

    public boolean storagePlay(boolean once, String gifPath) {
        return play(once, null, gifPath);
    }

    private boolean play(final boolean once, final Context context, final String gifPath) {
        if (mPlayState == PlayState.IDLE) {
            mPlayState = PlayState.PREPARE;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mOnGifListener != null) {
                        mOnGifListener.start();
                    }
                    if (native_load(mGifPlayerPtr, context != null ? context.getResources().getAssets() : null, gifPath)) {
                        Log.i(TAG, "run: native_load success");
                        mPlayState = PlayState.PLAYING;
                        mBitmap = Bitmap.createBitmap(native_get_width(mGifPlayerPtr),
                                native_get_height(mGifPlayerPtr),
                                Bitmap.Config.ARGB_8888);
                        native_start(mGifPlayerPtr, once, mBitmap, new Runnable() {
                            @Override
                            public void run() {
                                if (mOnGifListener != null) {
                                    mOnGifListener.draw(mBitmap);
                                }
                            }
                        });
                    } else {
                        Log.e(TAG, "gif: native_load error");
                    }
                    mPlayState = PlayState.IDLE;
                    if (mOnGifListener != null) {
                        mOnGifListener.end();
                    }
                }
            });
        } else {
            return false;
        }
        return true;
    }

    public boolean pause() {
        if (mPlayState == PlayState.PLAYING) {
            native_pause(mGifPlayerPtr);
            return true;
        }
        return false;
    }

    public boolean resume() {
        if (mPlayState == PlayState.PLAYING) {
            native_resume(mGifPlayerPtr);
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (mPlayState != PlayState.IDLE) {
            mPlayState = PlayState.STOP;
            native_stop(mGifPlayerPtr);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPlayState = PlayState.IDLE;
                }
            });
            return true;
        }
        return false;
    }

    public void destroy() {
        native_stop(mGifPlayerPtr);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                native_release(mGifPlayerPtr);
                mGifPlayerPtr = 0;
                mBitmap = null;
                mHandler = null;
                mHandlerThread.quit();
                mHandlerThread = null;
            }
        });
    }

    private native long native_create();

    private native boolean native_load(long ptr, AssetManager assetManager, String gifPath);

    private native void native_start(long ptr, boolean once, Bitmap bitmap, Runnable updateBitmap);

    private native int native_get_width(long ptr);

    private native int native_get_height(long ptr);

    private native void native_pause(long ptr);

    private native void native_resume(long ptr);

    private native void native_stop(long ptr);

    private native void native_release(long ptr);
}
