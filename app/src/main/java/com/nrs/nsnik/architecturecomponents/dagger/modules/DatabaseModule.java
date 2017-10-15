package com.nrs.nsnik.architecturecomponents.dagger.modules;


import android.arch.persistence.room.Room;
import android.content.Context;

import com.nrs.nsnik.architecturecomponents.dagger.qualifier.ApplicationQualifier;
import com.nrs.nsnik.architecturecomponents.dagger.scope.ApplicationScope;
import com.nrs.nsnik.architecturecomponents.data.NoteDatabase;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class})
public class DatabaseModule {

    private static final String DATABASE_NAME = "notesDb";

    @NotNull
    @Provides
    String getDatabaseName() {
        return DATABASE_NAME;
    }

    @NotNull
    @Provides
    NoteDatabase getNoteDatabase(@NotNull Context context, @NotNull String databaseName) {
        return Room.databaseBuilder(context, NoteDatabase.class, databaseName).build();
    }

}
