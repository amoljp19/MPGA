package com.softaai.mpga;

import android.app.Application;

import com.softaai.mpga.common.BackgroundThreadPoster;
import com.softaai.mpga.common.MainThreadPoster;

/**
 * Our custom {@link Application}
 */
public class MpgaApplication extends Application {

    private final MainThreadPoster mMainThreadPoster = new MainThreadPoster();
    private final BackgroundThreadPoster mBackgroundThreadPoster = new BackgroundThreadPoster();

    public MainThreadPoster getMainThreadPoster() {
        return mMainThreadPoster;
    }

    public BackgroundThreadPoster getBackgroundThreadPoster() {
        return mBackgroundThreadPoster;
    }
}
