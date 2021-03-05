package com.example.linkusapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends BaseViewModel{

    public MutableLiveData<String> sendMsgResLD = new MutableLiveData<>();
    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public void sendMessage(String gName,String myNickName,String yourNickName, String msg, String msgTime){
        service.sendMessage(gName,myNickName,yourNickName,msg,msgTime).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.i("msgResponse",result);
                if(result.equals("200")){
                    sendMsgResLD.setValue(result);
                }else{

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
