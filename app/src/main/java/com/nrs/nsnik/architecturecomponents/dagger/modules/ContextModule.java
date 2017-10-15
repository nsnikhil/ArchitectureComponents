package com.nrs.nsnik.architecturecomponents.dagger.modules;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context mContext;

    public ContextModule(Context mContext) {
        this.mContext = mContext;
    }

    @NotNull
    @Provides
    Context provideContext() {
        return this.mContext;
    }
}
