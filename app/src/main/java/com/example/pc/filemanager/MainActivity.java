package com.example.pc.filemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilesAdapter.OnEntryClickListener {

    private static final String EXTRA_DIRECTORY_NAME = "directory_name";

    public FilesAdapter adapter;
    private Toolbar toolbar;
    private TextView textMessage;

    public static void start(Context context, File file) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(EXTRA_DIRECTORY_NAME, file);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        File file = extras == null ? null : (File) extras.getSerializable(EXTRA_DIRECTORY_NAME);

        if (file == null) {
            file = Environment.getExternalStorageDirectory();
        }

        toolbar.setTitle(file.getPath());
        List<File> fileNameList = FileUtils.getFileListFromSDCard(file);

        textMessage = findViewById(R.id.textMessage);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (fileNameList.isEmpty()) {
            textMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        adapter = new FilesAdapter(this, fileNameList);
        recyclerView.setAdapter(adapter);

        adapter.setOnEntryClickListener(this);
    }

    public void openFile(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        String mime = "*/*";
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if (mimeTypeMap.hasExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
            mime = mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()));
            intent.setDataAndType(uri, mime);
        }
        startActivity(intent);
    }

    @Override
    public void onEntryClick(File file) {
        if (file.isFile()) {
            openFile(file);
        } else {
            start(MainActivity.this, file);
        }
    }
}