package com.example.testtask;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by Алексей on 04.02.2018.
 */

public class Requester {
    private MainFragment mainFragment;

    public Requester(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    public void execute(final VKRequest request, final String text) {
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                mainFragment.sendRequest(response.responseString, text);
            }

            @Override
            public void onError(VKError error) {
                System.out.println(error.errorMessage);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                System.out.println(request.attempts);
            }
        });
    }
}
