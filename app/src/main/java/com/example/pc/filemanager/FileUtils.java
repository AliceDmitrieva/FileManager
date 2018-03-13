package com.example.pc.filemanager;

import android.support.annotation.NonNull;

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


}