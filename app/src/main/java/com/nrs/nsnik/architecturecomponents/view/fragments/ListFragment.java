package com.nrs.nsnik.architecturecomponents.view.fragments;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.nrs.nsnik.architecturecomponents.BuildConfig;
import com.nrs.nsnik.architecturecomponents.MyApplication;
import com.nrs.nsnik.architecturecomponents.R;
import com.nrs.nsnik.architecturecomponents.data.NoteEntity;
import com.nrs.nsnik.architecturecomponents.view.adapters.ListAdapter;
import com.nrs.nsnik.architecturecomponents.view.listeners.ListClickListener;
import com.nrs.nsnik.architecturecomponents.viewmodel.ListViewModel;
import com.squareup.leakcanary.RefWatcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class ListFragment extends Fragment implements ListClickListener {

    @BindView(R.id.listFragmentRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.listFragmentAddItem)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.listContainer)
    CoordinatorLayout mContainer;
    private Unbinder mUnbinder;
    private CompositeDisposable mCompositeDisposable;
    private ListViewModel mListViewModel;
    private List<NoteEntity> mNoteEntityList;
    private ListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        mListViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        initialize();
        listeners();
        return v;
    }

    private void initialize() {
        mCompositeDisposable = new CompositeDisposable();
        mNoteEntityList = new ArrayList<>();
        mListAdapter = new ListAdapter(getActivity(), mNoteEntityList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mListAdapter);
        mListViewModel.getNoteList().observe(this, noteEntityList -> {
            mNoteEntityList = noteEntityList;
            mListAdapter.swapList(mNoteEntityList);
            mListAdapter.notifyDataSetChanged();
        });
    }

    private void cleanUp() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    private void listeners() {
        RxView.clicks(mFloatingActionButton).subscribe(o -> {
            AlertDialog.Builder newNoteDialog = getNoteDialog();
            View v = getNoteDialogView();
            newNoteDialog.setView(v);
            EditText editText = v.findViewById(R.id.addNoteEditText);
            newNoteDialog.setPositiveButton(getActivity().getResources().getString(R.string.add), (dialogInterface, i) -> {
                if (isValid(editText)) {
                    NoteEntity entity = new NoteEntity();
                    entity.setNote(editText.getText().toString());
                    entity.setDate(getCurrentDate());
                    mListViewModel.insert(entity);
                }
            });
            newNoteDialog.create().show();
        });
    }

    private Date getCurrentDate() {
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        simpleDateFormat.format(date);
        return date;
    }

    private boolean isValid(EditText editText) {
        return !(editText.getText().toString().length() <= 0 || editText.getText().toString().isEmpty());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanUp();
        if (BuildConfig.DEBUG) {
            RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
            refWatcher.watch(this);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }
        if (animation != null) {
            if (getView() != null) {
                getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (getView() != null) {
                        getView().setLayerType(View.LAYER_TYPE_NONE, null);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        return animation;
    }


    private void updateNote(NoteEntity entity) {
        AlertDialog.Builder newNoteDialog = getNoteDialog();
        View v = getNoteDialogView();
        newNoteDialog.setView(v);
        EditText editText = v.findViewById(R.id.addNoteEditText);
        editText.setText(entity.getNote());
        newNoteDialog.setPositiveButton(getActivity().getResources().getString(R.string.add), (dialogInterface, i) -> {
            if (isValid(editText)) {
                entity.setNote(editText.getText().toString());
                entity.setDate(getCurrentDate());
                mListViewModel.update(entity);
            }
        });
        newNoteDialog.create().show();
    }

    private AlertDialog.Builder getNoteDialog() {
        return new AlertDialog.Builder(getActivity())
                .setNegativeButton(getActivity().getResources().getString(R.string.cancel), (dialogInterface, i) -> {
                });
    }

    private View getNoteDialogView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_note_dialog, null);
    }

    private void deleteItem(int position) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getResources().getString(R.string.warning))
                .setMessage(getActivity().getResources().getString(R.string.deleteConfirmation))
                .setNegativeButton(getActivity().getResources().getString(R.string.cancel), (dialog, which) -> {
                }).setPositiveButton(getActivity().getResources().getString(R.string.delete), (dialog, which) -> {
                    mListViewModel.delete(mNoteEntityList.get(position));
                });
        deleteDialog.create().show();
    }

    @Override
    public void delete(int position) {
        deleteItem(position);
    }

    @Override
    public void update(int position) {
        updateNote(mNoteEntityList.get(position));
    }
}