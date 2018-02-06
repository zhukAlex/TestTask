package com.example.testtask;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testtask.helpers.Change;
import com.example.testtask.internet.LoadTask;
import com.example.testtask.internet.PostTask;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.WIFI_SERVICE;

public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Button buttonSend;
    private EditText editTextStatus;
    private ImageView imageViewAvatar;
    private OnFragmentInteractionListener mListener;
    private Requester requester = new Requester(this);
    private String path;
    private String status;
    private boolean isChangeStatus, isChangeAvatar;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        buttonSend = (Button)view.findViewById(R.id.buttonSend);
        editTextStatus = (EditText)view.findViewById(R.id.editTextStatus);
        imageViewAvatar = (ImageView)view.findViewById(R.id.imageViewAvatar);

        loadImage();
        loadStatus();
        status = editTextStatus.getText().toString();

        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChanges();
              //  VKRequest request = new VKRequest("photos.getProfileUploadServer");
              //  requester.execute(request, "upload_url");

                VKParameters parameters = new VKParameters();
                parameters.put("text", editTextStatus.getText().toString());
                VKRequest request = new VKRequest("status.set", parameters);
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        Toast.makeText(getContext(), "Status update", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

    private void setChanges() {
        isChangeStatus = !editTextStatus.getText().toString().equals(status);

        if (isChangeStatus || isChangeAvatar) {
            Change change;
            String text;
            WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy : hh:mm");
            String date = formatForDateNow.format(new Date());

            if (isChangeAvatar && isChangeStatus)
                text = "Upload picture and status";
            else if (isChangeStatus && !isChangeAvatar)
                text = "Upload status";
            else
                text = "Upload picture";

            change = new Change(date, ip, text);
            change.writeToDB();
            
            isChangeAvatar = false;
            isChangeStatus = false;
        }
    }

    private void loadStatus() {
        VKRequest request = new VKRequest("status.get");

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    JSONObject js = new JSONObject(response.json.getString("response"));
                    editTextStatus.setText(js.getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {

            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1:
            {
                if (resultCode == RESULT_OK)
                {
                    InputStream is = null;
                    try {
                        path = getRealPathFromURI(getContext(), data.getData());
                        is = getActivity().getContentResolver().openInputStream(data.getData());
                        final Bitmap bitmap = addBitmap(is);
                        imageViewAvatar.setImageBitmap(bitmap);
                        isChangeAvatar = true;

                     //   VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(bitmap,
                        //        VKImageParameters.jpgImage(0.9f)), 0, 60479154);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    private Bitmap addBitmap(InputStream is) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void loadImage() {
     //   new LoadComisTask(terminal, activity).execute();
        VKParameters parameters = new VKParameters();
        parameters.put(VKApiConst.FIELDS, "photo_400_orig");
        VKRequest request = new VKRequest("users.get", parameters);
        final MainFragment mainFragment = this;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    new LoadTask(mainFragment).execute(parse(response.responseString, "photo_400_orig"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {

            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        });
    }

    public String parse(String response, String text) throws JSONException {
        String str = response;
        String buf;
        JSONObject obj;

        obj = new JSONObject(str);
        str = obj.getString("response");
        JSONArray arr = new JSONArray(str);
        buf = arr.get(0).toString();
        obj = new JSONObject(buf);
        String url = obj.getString(text);
        return url;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setImage(Drawable image) {
        final Drawable drawable = image;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageViewAvatar.setImageDrawable(drawable);
            }
        });
    }

    public void sendRequest(String responseString, String text) {
        System.out.println(responseString);
        try {
            if (text.equals("upload_url")) {
                String url = new JSONObject(new JSONObject(responseString).getString("response")).getString(text);//parse(responseString, text);
                new PostTask(this, ((BitmapDrawable) imageViewAvatar.getDrawable()).getBitmap()).execute(url, path);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveOwnerPhoto(final String resopnse) {
        try {
            JSONObject js = new JSONObject(resopnse);
            String server = js.getString("server");
            String hash = js.getString("hash");
            String photo = js.getString("photo");
            VKParameters parameters = new VKParameters();
            parameters.put("server", server);
            parameters.put("photo", photo);
            parameters.put("hash", hash);

            VKRequest request = new VKRequest("photos.saveProfilePhoto", parameters);
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    System.out.println(resopnse);
                }

                @Override
                public void onError(VKError error) {
             //       System.out.println(error.errorMessage);
                }

                @Override
                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
