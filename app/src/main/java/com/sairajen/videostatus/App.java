package com.sairajen.videostatus;

import android.app.Application;

import com.onesignal.OneSignal;

/**
 * Created by Gaurav Bordoloi on 5/2/2018.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OneSignal.startInit(this)
                .init();

    }

}
