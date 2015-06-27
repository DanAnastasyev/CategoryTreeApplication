package com.manager.qbic.categorytreeapplication.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONDownloader {
    private static final String TAG = "JSONDownloader";
    // С таким адресом загружать не получилось
    //private static final String ENDPOINT = "http://oorraa.com/api";
    //private static final String METHOD_GET_CATEGORY_TREE = "categories.getCategoryTree";
    private static final String ENDPOINT = "https://raw.githubusercontent.com/DanAnastasyev/" +
            "CategoryTreeApplication/master/app/src/androidTest/CategoryTree.json";

    public String getBytesFromUrl(String urlSpec) throws IOException, JSONException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            return out.toString();
        } finally {
            connection.disconnect();
        }
    }

    public JSONArray getJSONArray() {
        String url = Uri.parse(ENDPOINT).buildUpon()
                //.appendQueryParameter("method", METHOD_GET_CATEGORY_TREE)
                .build().toString();

        try {
            return new JSONArray(getBytesFromUrl(url));
        } catch (IOException e) {
            Log.e(TAG, "Error in downloading JSON", e);
        } catch (JSONException e) {
            Log.e(TAG, "Error in encoding JSON", e);
        }
        return null;
    }
}