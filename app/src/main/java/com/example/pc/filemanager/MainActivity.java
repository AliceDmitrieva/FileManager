package com.example.pc.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private List<File> fileNameList;
    public RecyclerViewAdapter mAdapter;
    private Toolbar mToolbar;

    private TextView textView;

    @NonNull
    public static List<File> getFileListfromSDCard(@NonNull File file) {
        String state = Environment.getExternalStorageState();
        List<File> fileList = new ArrayList<>();
        if (Environment.MEDIA_MOUNTED.equals(state) && file.isDirectory()) {
            File[] fileArr = file.listFiles();
            int length = fileArr.length;
            for (int i = 0; i < length; i++) {
                File f = fileArr[i];
                fileList.add(f);
            }
        }
        return fileList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        File file = extras == null ? null : (File) extras.getSerializable("directory");

        if (file == null) {
            file = Environment.getExternalStorageDirectory();
        }

        updateToolbarTitle(file);
        fileNameList = getFileListfromSDCard(file);


     /*   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File clickedDirectory = fileNameList.get(position);
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra(getString(R.string.extraName), clickedDirectory);

                startActivity(i);
            }
        });*/

        textView = findViewById(R.id.textView);
        if (fileNameList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }


        recyclerView = findViewById(R.id.rvFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter(this, R.layout.list_item, fileNameList);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);

    }

    public void updateToolbarTitle(File file) {
        mToolbar.setTitle(file.getPath().toString());
    }


    @Override
    public void onItemClick(View view, int position) {
        File clickedDirectory = fileNameList.get(position);
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        i.putExtra(getString(R.string.extraName), clickedDirectory);

        startActivity(i);
    }

    static class FileHolder {
        public TextView fileNameView;
    }
}
