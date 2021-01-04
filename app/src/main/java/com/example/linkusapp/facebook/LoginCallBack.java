package com.example.linkusapp.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.linkusapp.view.HomeActivity;
import com.example.linkusapp.view.JoinActivity;
import com.example.linkusapp.view.MainActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

public class LoginCallBack implements FacebookCallback<LoginResult> {

    private HomeActivity home;

    public void onSuccess(LoginResult loginResult)
    {
        Log.e("Callback :: ", "onSuccess");
        requestMe(loginResult.getAccessToken());
    }

    public void onCancel()
    {
        Log.e("Callback :: ", "onCancel");
    }

    public void onError(FacebookException error)
    {
        Log.e("Callback :: ", "onError : " + error.getMessage());
    }

    // 사용자 정보 요청
    public void requestMe(AccessToken token)
    {
        GraphRequest graphRequest = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.e("result",object.toString());
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
}