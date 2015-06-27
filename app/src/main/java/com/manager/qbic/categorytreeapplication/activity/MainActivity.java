package com.manager.qbic.categorytreeapplication.activity;

import android.app.Fragment;

import com.manager.qbic.categorytreeapplication.fragment.FolderStructureFragment;


public class MainActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new FolderStructureFragment();
    }
}
