package com.example.pc.filemanager;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class FileListFragment extends Fragment implements FilesAdapter.OnEntryClickListener {

    private static final String EXTRA_DIRECTORY_NAME = "directory_name";
    private OnFolderClickListener listener;

    public interface OnFolderClickListener {
        void onFolderClick(@NonNull File file);
    }

    @NonNull
    public static FileListFragment newInstance(@Nullable File file) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putSerializable(FileListFragment.EXTRA_DIRECTORY_NAME, file);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnFolderClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_list, parent, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        Bundle extras = getArguments();
        File file = extras == null ? null : (File) extras.getSerializable(EXTRA_DIRECTORY_NAME);
        if (file == null) {
            file = Environment.getExternalStorageDirectory();
        }
        toolbar.setTitle(file.getPath());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<File> fileNameList = FileUtils.getFileListFromSDCard(file);
        TextView textMessage = view.findViewById(R.id.textMessage);
        if (fileNameList.isEmpty()) {
            textMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        FilesAdapter adapter = new FilesAdapter(getActivity(), fileNameList);
        recyclerView.setAdapter(adapter);
        adapter.setOnEntryClickListener(this);

        return view;
    }

    @Override
    public void onEntryClick(File file) {
        if (file.isFile()) {
            FileUtils.openFile(getActivity(), file);
        } else {
            listener.onFolderClick(file);
        }
    }
}