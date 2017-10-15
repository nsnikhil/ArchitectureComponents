package com.nrs.nsnik.architecturecomponents.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.nrs.nsnik.architecturecomponents.MyApplication;
import com.nrs.nsnik.architecturecomponents.data.NoteEntity;
import com.nrs.nsnik.architecturecomponents.utiil.DbUtil;

import java.util.List;

public class ListViewModel extends AndroidViewModel {

    private LiveData<List<NoteEntity>> mNoteList;
    private final DbUtil mDbUtil;

    ListViewModel(Application application) {
        super(application);
        mDbUtil = ((MyApplication) application).getDbUtil();
        mNoteList = mDbUtil.getNoteList();
    }

    public LiveData<List<NoteEntity>> getNoteList() {
        return mNoteList;
    }

    public void insert(NoteEntity... entities) {
        mDbUtil.insert(entities);
    }

    public void delete(NoteEntity... entities) {
        mDbUtil.delete(entities);
    }

    public void update(NoteEntity... entities) {
        mDbUtil.upadate(entities);
    }

    public LiveData<NoteEntity> getNote(int id) {
        return mDbUtil.getNote(id);
    }
}
