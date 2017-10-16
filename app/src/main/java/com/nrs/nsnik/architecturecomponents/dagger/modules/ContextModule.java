package com.nrs.nsnik.architecturecomponents.dagger.modules;

import android.content.Context;

import com.nrs.nsnik.architecturecomponents.dagger.qualifier.ApplicationQualifier;

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
    @ApplicationQualifier
    Context provideContext() {
        return this.mContext;
    }
}
