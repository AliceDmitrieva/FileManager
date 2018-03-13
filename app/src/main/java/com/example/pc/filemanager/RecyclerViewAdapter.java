package com.example.pc.filemanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by PC on 3/6/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private OnEntryClickListener mOnEntryClickListener;
    private List<File> fileList;

    RecyclerViewAdapter(Context context, int textViewResourceId, List<File> fileList) {
        this.fileList = fileList;
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindFile(fileList.get(position));
        holder.setIcon(fileList.get(position));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnEntryClickListener {
        void onEntryClick(File file);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView myTextView;
        private ImageView icon;
        private File file;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            myTextView = itemView.findViewById(R.id.file_name);
            icon = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(file);
            }
        }

        void bindFile(File file) {
            this.file = file;
            myTextView.setText(file.getName());
        }

        void setIcon(File file) {
            if (file.isFile()) {
                icon.setImageResource(R.drawable.ic_file_24dp);
            } else if (file.isDirectory()) {
                icon.setImageResource(R.drawable.ic_folder_24dp);
            }
        }
    }
}