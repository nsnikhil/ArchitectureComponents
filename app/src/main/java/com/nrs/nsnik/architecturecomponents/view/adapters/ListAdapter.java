package com.nrs.nsnik.architecturecomponents.view.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.support.v7.widget.RxPopupMenu;
import com.jakewharton.rxbinding2.view.RxView;
import com.nrs.nsnik.architecturecomponents.R;
import com.nrs.nsnik.architecturecomponents.data.NoteEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<NoteEntity> mNoteEntityList;
    private final Context mContext;

    public ListAdapter(Context context, List<NoteEntity> noteEntityList) {
        mContext = context;
        mNoteEntityList = noteEntityList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoteEntity noteEntity = mNoteEntityList.get(position);
        holder.mHeader.setText(String.valueOf(position));
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

    private void inflatePopUoMenu(RelativeLayout view) {
        PopupMenu popupMenu = new PopupMenu(mContext, view, Gravity.START);
        popupMenu.inflate(R.menu.pop_up_menu);
        RxPopupMenu.itemClicks(popupMenu).subscribe(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popUpEdit:
                    Toast.makeText(mContext, "Edit", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.popUpDelete:
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemHeader)
        TextView mHeader;
        @BindView(R.id.itemContent)
        TextView mContent;
        @BindView(R.id.itemDate)
        TextView mDate;
        @BindView(R.id.itemBackground)
        RelativeLayout mBackground;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.longClicks(mBackground).subscribe(o -> inflatePopUoMenu(mBackground));
        }
    }
}
