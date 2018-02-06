package com.example.testtask.internet;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Base64;

import com.example.testtask.MainFragment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Scanner;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;

/**
 * Created by Алексей on 04.02.2018.
 */

public class PostTask extends AsyncTask {
    private MainFragment fragment;
    private ProgressDialog statusDialog;
    private Bitmap bitmap;

    public PostTask(MainFragment fragment, Bitmap bitmap) {
        this.fragment = fragment;
        this.bitmap = bitmap;
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
            fragment.saveOwnerPhoto(getResponse(args[0].toString(), args[1].toString()));
            publishProgress("Отправка запроса...");
            publishProgress("Получение изображений...");
        } catch (Exception e) {
            publishProgress(e.getMessage());
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        if (values != null && statusDialog != null)
            statusDialog.setMessage(values[0].toString());
    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }

    private String getResponse(String url, String s) throws IOException, GeneralSecurityException{

        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        builder.addTextBody("photo", s);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        CloseableHttpResponse response = client.execute(httpPost);

        InputStream in = response.getEntity().getContent();
        String str = convertStreamToString(in);
        System.out.println(str);
        client.close();
        return str;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}