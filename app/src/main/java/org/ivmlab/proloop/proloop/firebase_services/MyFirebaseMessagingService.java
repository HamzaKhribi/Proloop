package org.ivmlab.proloop.proloop.firebase_services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.ivmlab.proloop.proloop.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServce";
    SharedPreferences sharedPreferences;
    private String username;
    private String pub_id;
    private String chatting_with;
    private String chatting_with_idS;
    private String chatting_with_id;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationTitle = null, notificationBody = null;
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("NAME","user");
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();

            if((remoteMessage.getData().size() > 0)){
                if(remoteMessage.getData().get("pub_id")!=null){
                    pub_id= remoteMessage.getData().get("pub_id");

                    sendNotificationPub(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),pub_id,remoteMessage.getNotification().getClickAction());
                }else{
                    chatting_with=remoteMessage.getData().get("chatting_with");
                    Log.e("chatting with",chatting_with);

                    chatting_with_id=remoteMessage.getData().get("chatting_with_id");
                    Log.e("chatting with id",chatting_with_id);


                    sendNotification(notificationTitle, notificationBody,chatting_with,chatting_with_id,remoteMessage.getNotification().getClickAction());

                }

            }

            }





        }






    private void sendNotification(String notificationTitle, String notificationBody,String chatting_with,String chatting_with_id,String clickAction) {
        Intent intent = new Intent(clickAction);
        intent.putExtra("chatting_with",chatting_with);
        intent.putExtra("chatting_with_id",chatting_with_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);



        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSound(defaultSoundUri);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    private void sendNotificationPub(String notificationTitle, String notificationBody,String pub_id,String clickAction) {
        Intent intent = new Intent(clickAction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pub_id",pub_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);



        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSound(defaultSoundUri);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}