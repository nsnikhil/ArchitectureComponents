package com.nrs.nsnik.architecturecomponents.view.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v7.widget.RxPopupMenu;
import com.jakewharton.rxbinding2.view.RxView;
import com.nrs.nsnik.architecturecomponents.R;
import com.nrs.nsnik.architecturecomponents.data.NoteEntity;
import com.nrs.nsnik.architecturecomponents.view.listeners.ListClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private final ListClickListener mListClickListener;
    private final Context mContext;
    private List<NoteEntity> mNoteEntityList;

    public ListAdapter(Context context, List<NoteEntity> noteEntityList, ListClickListener listClickListener) {
        mContext = context;
        mListClickListener = listClickListener;
        mNoteEntityList = noteEntityList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoteEntity noteEntity = mNoteEntityList.get(position);
        holder.mHeader.setText(String.valueOf(noteEntity.getId()));
        holder.mContent.setText(noteEntity.getNote());
        holder.mDate.setText(String.valueOf(noteEntity.getDate()));
    }

    @Override
    public int getItemCount() {
        return mNoteEntityList == null ? 0 : mNoteEntityList.size();
    }

    public void swapList(List<NoteEntity> noteEntityList) {
        mNoteEntityList = noteEntityList;
    }

    private void inflatePopUoMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(mContext, view, Gravity.END);
        popupMenu.inflate(R.menu.pop_up_menu);
        RxPopupMenu.itemClicks(popupMenu).subscribe(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popUpEdit:
                    mListClickListener.update(position);
                    break;
                case R.id.popUpDelete:
                    mListClickListener.delete(position);
                    break;
            }
        });
        popupMenu.show();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemHeader)
        TextView mHeader;
        @BindView(R.id.itemContent)
        TextView mContent;
        @BindView(R.id.itemDate)
        TextView mDate;
        @BindView(R.id.itemBackground)
        ConstraintLayout mBackground;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.longClicks(itemView).subscribe(o -> inflatePopUoMenu(itemView, getAdapterPosition()));
        }
    }
}
