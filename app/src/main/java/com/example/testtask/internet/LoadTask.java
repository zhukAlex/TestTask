package com.example.testtask.internet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.testtask.MainFragment;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Алексей on 04.02.2018.
 */

public class LoadTask extends AsyncTask {
    private MainFragment fragment;
    private ProgressDialog statusDialog;

    public LoadTask(MainFragment fragment) {
        this.fragment = fragment;
    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(fragment.getActivity());
        statusDialog.setMessage("Подготовка к запуску...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        String post = null;
        try {
            InputStream is = (InputStream) new URL(args[0].toString()).getContent();
            Drawable drawable = Drawable.createFromStream(is, "Avatar");
            fragment.setImage(drawable);
            publishProgress("Отправка запроса...");
            publishProgress("Получение изображений...");
        } catch (Exception e) {
            publishProgress(e.getMessage());
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());
    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }
}
