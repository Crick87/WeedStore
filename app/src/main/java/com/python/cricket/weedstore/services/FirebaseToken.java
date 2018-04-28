package com.python.cricket.weedstore.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseToken extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh(); //?
        String token = FirebaseInstanceId.getInstance().getToken();
        reg_token(token);
    }
    private void reg_token(String token)
    {
        // TODO: Using WebService for registering the token in DB.
    }
}