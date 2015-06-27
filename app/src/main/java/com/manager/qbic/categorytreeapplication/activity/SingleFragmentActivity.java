package com.manager.qbic.categorytreeapplication.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.unnamed.b.atv.com.qbic.R;

public abstract class SingleFragmentActivity extends ActionBarActivity {
    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragment, fragment).commit();
        }
    }
}
