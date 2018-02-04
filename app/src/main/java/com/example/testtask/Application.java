package com.example.testtask;

import android.content.Intent;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Алексей on 03.02.2018.
 */

public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
      //  Intent intent = new Intent(Application.this, MainActivity.class);
     //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //    startActivity(intent);
    }
}
