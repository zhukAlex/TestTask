package com.example.testtask;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vk.sdk.api.VKApi;
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
import java.util.Iterator;

import static android.app.Activity.RESULT_OK;

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
                VKParameters parameters = new VKParameters();
                //   parameters.put("text", "new status");
                VKRequest request = new VKRequest("status.get", parameters);

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
        });

        return view;
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
                        is = getActivity().getContentResolver().openInputStream(data.getData());
                        imageViewAvatar.setImageBitmap(addBitmap(is));
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
                    String str = response.json.toString();
                    String buf;
                    JSONObject obj;

                    obj = new JSONObject(str);
                    str = obj.getString("response");
                    JSONArray arr = new JSONArray(str);
                    buf = arr.get(0).toString();
                    obj = new JSONObject(buf);
                    String url = obj.getString("photo_400_orig");
                    new LoadTask(mainFragment).execute(url);
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
}
