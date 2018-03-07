package com.example.pc.filemanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by PC on 3/6/2018.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<File> fileList;
    private ItemClickListener mClickListener;

    RecyclerViewAdapter(Context context, int textViewResourceId, List<File> fileList) {
        this.fileList = fileList;


    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String fileName = fileList.get(position).getName();
        holder.myTextView.setText(fileName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return fileList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.file_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}