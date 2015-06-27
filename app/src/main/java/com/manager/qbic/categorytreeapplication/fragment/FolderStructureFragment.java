package com.manager.qbic.categorytreeapplication.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.manager.qbic.categorytreeapplication.holder.IconTreeItemHolder;
import com.manager.qbic.categorytreeapplication.utils.CategoryTree;
import com.manager.qbic.categorytreeapplication.utils.JSONDownloader;
import com.unnamed.b.atv.com.qbic.R;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;

public class FolderStructureFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private AndroidTreeView tView;
    private CategoryTree mCategoryTree;
    private TreeNode root;

    private class DownloadItemsTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONDownloader downloader = new JSONDownloader();
            return downloader.getJSONArray();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            try {
                mCategoryTree = new CategoryTree(jsonArray);
            } catch (JSONException e) {
                Log.e(TAG, "Error in creating CategoryTree", e);
            }

            tView.addNode(root, mCategoryTree.getRoot());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        DownloadItemsTask task = new DownloadItemsTask();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        root = TreeNode.root();

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(IconTreeItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                tView.expandAll();
                break;

            case R.id.collapseAll:
                tView.collapseAll();
                break;
        }
        return true;
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}
