package com.example.pc.filemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<File> fileNameList;
    private FileAdapter mAdapter;
    private Toolbar mToolbar;

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
        mAdapter = new FileAdapter(this, R.layout.list_item, fileNameList);
        checkEmpty(fileNameList);

        listView = findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File clickedDirectory = fileNameList.get(position);
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra(getString(R.string.extraName), clickedDirectory);

                startActivity(i);
            }
        });
    }

    public void checkEmpty(List<File> fileList) {
        if (fileList.size() == 0) {
            Toast.makeText(MainActivity.this, "Folder is empty!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateToolbarTitle(File file) {
        mToolbar.setTitle(file.getPath().toString());
    }

    static class FileHolder {
        public TextView fileNameView;
    }

    public class FileAdapter extends ArrayAdapter<File> {
        private List<File> fileList;
        private Context context;

        public FileAdapter(Context context, int textViewResourceId, List<File> fileList) {
            super(context, textViewResourceId, fileList);
            this.fileList = fileList;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            FileHolder fHolder = null;

            if (convertView == null) {
                view = View.inflate(context, R.layout.list_item, null);

                fHolder = new FileHolder();
                fHolder.fileNameView = (TextView) view.findViewById(R.id.file_name);

                view.setTag(fHolder);
            } else {
                fHolder = (FileHolder) view.getTag();
            }
            String fileName = fileList.get(position).getName();
            fHolder.fileNameView.setText(fileName);

            return view;
        }
    }
}
