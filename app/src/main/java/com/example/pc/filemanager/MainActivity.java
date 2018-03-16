package com.example.pc.filemanager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilesAdapter.OnEntryClickListener {

    private static final String EXTRA_DIRECTORY_NAME = "directory_name";

    public FilesAdapter adapter;

    public static void start(Context context, File file) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(EXTRA_DIRECTORY_NAME, file);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        TextView textMessage = findViewById(R.id.textMessage);

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

    private String getFileExtension(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String extension = url.substring(url.lastIndexOf(".") + 1);
            if (extension.contains("%")) {
                extension = extension.substring(0, extension.indexOf("%"));
            }
            if (extension.contains("/")) {
                extension = extension.substring(0, extension.indexOf("/"));
            }
            return extension.toLowerCase();
        }
    }

    public void openFile(File file) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = mime.getMimeTypeFromExtension(getFileExtension(file.toString()));
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.missing_file_handler, Toast.LENGTH_LONG).show();
        }
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