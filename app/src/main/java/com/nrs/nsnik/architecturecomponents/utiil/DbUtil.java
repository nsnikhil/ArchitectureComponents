package com.nrs.nsnik.architecturecomponents.utiil;


import android.arch.lifecycle.LiveData;

import com.nrs.nsnik.architecturecomponents.dagger.scope.ApplicationScope;
import com.nrs.nsnik.architecturecomponents.data.NoteDatabase;
import com.nrs.nsnik.architecturecomponents.data.NoteEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@ApplicationScope
public class DbUtil {

    private final NoteDatabase mNoteDatabase;

    @Inject
    public DbUtil(NoteDatabase noteDatabase) {
        this.mNoteDatabase = noteDatabase;
    }

    public void insert(NoteEntity... noteEntities) {
        Single<long[]> single = Single.fromCallable(() -> mNoteDatabase.getNoteDao().insertNote(noteEntities)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        single.subscribe(new SingleObserver<long[]>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(long[] longs) {
                for (long aLong : longs) {
                    Timber.d(String.valueOf(aLong));
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e.getMessage());
            }
        });
    }

    public void delete(NoteEntity... noteEntities) {
        Completable completable = Completable.fromCallable(() -> {
            mNoteDatabase.getNoteDao().deleteNote(noteEntities);
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        completable.subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Timber.d("Delete successful");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e.getMessage());
            }
        });
    }

    public void upadate(NoteEntity... noteEntities) {
        Single<Integer> single = Single.fromCallable(() -> mNoteDatabase.getNoteDao().updateNote(noteEntities)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        single.subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                Timber.d(String.valueOf(integer));
            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e.getMessage());
            }
        });
    }

    public LiveData<List<NoteEntity>> getNoteList() {
        return mNoteDatabase.getNoteDao().getNoteList();
    }

    public LiveData<NoteEntity> getNote(int id) {
        return mNoteDatabase.getNoteDao().getNote(id);
    }
}
