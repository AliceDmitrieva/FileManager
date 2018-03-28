package com.example.pc.filemanager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    @NonNull
    public static List<File> getFileListFromSDCard(@NonNull File file) {
        List<File> fileList = new ArrayList<>();
        if (file.isDirectory()) {
            File[] fileArr = file.listFiles();
            int length = fileArr.length;
            for (int i = 0; i < length; i++) {
                File f = fileArr[i];
                fileList.add(f);
            }
        }
        Collections.sort(fileList);
        return fileList;
    }

    @Nullable
    private static String getFileExtension(String url) {
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

    @NonNull
    public static void openFile(Context context, File file) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = mime.getMimeTypeFromExtension(getFileExtension(file.toString()));
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String title = context.getResources().getString(R.string.chooser_title);
        Intent chooser = Intent.createChooser(intent, title);

        try {
            context.startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.missing_file_handler, Toast.LENGTH_LONG).show();
        }
    }
}