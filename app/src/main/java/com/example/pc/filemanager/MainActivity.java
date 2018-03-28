package com.example.pc.filemanager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity implements FileListFragment.OnElementClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(null);
    }

    @Override
    public void onFolderClick(@NonNull File file) {
        changeFragment(file);
    }

    @Override
    public void onBackArrowClick() {
        onBackPressed();
    }

    private void changeFragment(@Nullable File file) {
        FileListFragment fragment = FileListFragment.newInstance(file);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        int countOfEntries = getFragmentManager().getBackStackEntryCount();
        if (countOfEntries == 1) {
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}