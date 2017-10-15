package com.nrs.nsnik.architecturecomponents;


import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.nrs.nsnik.architecturecomponents.dagger.components.DaggerDatabaseComponent;
import com.nrs.nsnik.architecturecomponents.dagger.components.DatabaseComponent;
import com.nrs.nsnik.architecturecomponents.dagger.modules.ContextModule;
import com.nrs.nsnik.architecturecomponents.utiil.DbUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public class MyApplication extends Application {

    private DbUtil mDbUtil;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(@NonNull Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @NonNull
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
            refWatcher = LeakCanary.install(this);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        moduleSetter();
    }

    private void moduleSetter() {
        setDatabaseComponent();
    }

    private void setDatabaseComponent() {
        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().contextModule(new ContextModule(this)).build();
        mDbUtil = databaseComponent.getDbUtil();
    }

    public DbUtil getDbUtil() {
        return mDbUtil;
    }
}
