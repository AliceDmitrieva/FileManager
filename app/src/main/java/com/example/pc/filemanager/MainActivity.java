package com.example.pc.filemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private File file;
    private List<String> fileNameList;
    private FileAdapter mAdapter;

    private boolean isClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        fileNameList = getFileListfromSDCard();
        mAdapter = new FileAdapter(this, R.layout.list_item, fileNameList);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedDirectoryString = fileNameList.get(position);
                File clickedDirectory = new File(selectedDirectoryString);
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra(getString(R.string.extraName), clickedDirectory);

                startActivity(i);
            }
        });
    }

    public List<String> getFileListfromSDCard() {
            Bundle extras = getIntent().getExtras();
            File file = extras == null ? null : (File) extras.getSerializable("directory");
            if (file == null) {
                file = Environment.getExternalStorageDirectory();
            }
                String state = Environment.getExternalStorageState();
                List<String> fileList = new ArrayList<String>();
                if (Environment.MEDIA_MOUNTED.equals(state) && file.isDirectory()) {
                    File[] fileArr = file.listFiles();
                    int length = fileArr.length;
                    for (int i = 0; i < length; i++) {
                        File f = fileArr[i];
                        fileList.add(f.getName());
                    }
                }

        return fileList;
    }

    public class FileAdapter extends ArrayAdapter<String> {
        private List<String> fileList;
        private Context context;

        public FileAdapter(Context context, int textViewResourceId, List<String> fileList) {
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
            String fileName = fileList.get(position);
            fHolder.fileNameView.setText(fileName);

            return view;
        }
    }

    static class FileHolder {
        public TextView fileNameView;
    }
}
