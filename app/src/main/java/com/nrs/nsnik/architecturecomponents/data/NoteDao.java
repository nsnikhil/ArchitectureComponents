package com.nrs.nsnik.architecturecomponents.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM NoteEntity")
    LiveData<List<NoteEntity>> getNoteList();

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    LiveData<NoteEntity> getNote(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertNote(NoteEntity... entities);

    @Delete
    void deleteNote(NoteEntity... entities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateNote(NoteEntity... entities);

    @Query("SELECT COUNT(*) FROM NoteEntity")
    int getCount();
}
