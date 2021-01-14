package com.example.linkusapp.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.linkusapp.R;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.view.activity.HomeActivity;
import com.example.linkusapp.view.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseInstanceIDService";



    @Override
    public void onNewToken(@NonNull String token) {
        Log.e("firebase", "FirebaseInstanceIDService : " + token);
//        sendRegistrationToServer(token);

    }

    //fcm메시지를 받았을 떄 실행되는 메소드
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = "";
        String body = "";
        String user = "";
        if (remoteMessage.getData().size() > 0) {
            Log.d("message", "getNotification() ");
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            user = remoteMessage.getData().get("user");
//            Log.d("message", "getNotification() "+title+", "+body);
            Map<String,String> data = remoteMessage.getData();

        }

        //앱이 포어그라운드 상태에서 Notification을 받는 경우
//        if (remoteMessage.getNotification() != null) {
//            Log.d("message", "getData() ");
//            title = remoteMessage.getNotification().getTitle();
//            body = remoteMessage.getNotification().getBody();
////            Log.d("message", "getData() "+title+", "+body);
//        }
        Log.d("test", "onMessageReceived: "+title+body+user);
        sendNotification(title,body,user);
    }

    //fcm 메시지를 받았을 때 실행할 메소드
    private void sendNotification(String title, String body,String user) {
        Intent intent;
        PendingIntent pendingIntent;

        intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user",user); //push정보 중 body값을 HomeActivity로 넘긴다.

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        /**
         * 오레오 버전부터는 Notification Channel이 없으면 푸시가 생성되지 않기 때문에 분기
         * 처리를 해준다.
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = "test push";
            String channelName = "test Push Message";
            String channelDescription = "New test Information";

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            //각종 채널에 대한 설정
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationManager.createNotificationChannel(channel);

            //channel이 등록된 builder
            notificationBuilder = new NotificationCompat.Builder(this, channelId);
        }else{
            notificationBuilder = new NotificationCompat.Builder(this,"");
        }
        notificationBuilder.setSmallIcon(R.drawable.icon_pencil)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentText(body);

        notificationManager.notify(9999 /* ID of notification */, notificationBuilder.build());
    }

}
