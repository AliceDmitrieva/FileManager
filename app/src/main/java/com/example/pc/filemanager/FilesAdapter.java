package com.example.pc.filemanager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private OnEntryClickListener onEntryClickListener;
    private List<File> fileList;

    FilesAdapter(Context context, List<File> fileList) {
        this.fileList = fileList;
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
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

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final @Nullable
        TextView fileName;
        private final ImageView icon;
        private File file;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            fileName = itemView.findViewById(R.id.file_name);
            icon = itemView.findViewById(R.id.file_icon);
        }

        @Override
        public void onClick(View v) {
            if (onEntryClickListener != null) {
                onEntryClickListener.onEntryClick(file);
            }
        }

        void bindFile(File file) {
            this.file = file;
            assert fileName != null;
            fileName.setText(file.getName());
            int imageID = file.isFile() ? R.drawable.ic_file_24dp : R.drawable.ic_folder_24dp;
            icon.setImageResource(imageID);
        }
    }
}