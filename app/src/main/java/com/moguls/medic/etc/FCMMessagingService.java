package com.moguls.medic.etc;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.moguls.medic.R;


import java.util.Map;


public class FCMMessagingService extends FirebaseMessagingService  {

    private static final String TAG = "FCMMessagingService";
    private static Notification notification;
    private static final int NotificationID = 1005;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(BaseKeys.ACTION_NEW_MESSAGE_RECEIVED);
        //intent.putExtra(SharedPreference.EXTRA_NOTIFICATION_DATE, date);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder customNotification = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        customNotification.setDefaults(Notification.DEFAULT_ALL);
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        String channelId = "channel_id";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
            customNotification.setChannelId(channelId);
        }
        notificationLayout.setTextViewText(R.id.notification_title, remoteMessage.getNotification().getTitle());
        notificationLayout.setTextViewText(R.id.notification_desc,  remoteMessage.getNotification().getBody());
        customNotification.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher));
        customNotification.setSmallIcon(R.mipmap.ic_launcher);
        customNotification.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        customNotification.setContent(notificationLayout);
        customNotification.setContentTitle(remoteMessage.getNotification().getTitle());
        customNotification.setContentInfo(remoteMessage.getNotification().getBody());
        notification = customNotification.build();
        notificationManager.notify(NotificationID, notification);

    }

    @Override
    public void onNewToken(String token) {
      //  sendRegistrationToServer(token);
    }

/*
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        try {
            SharedPreference.setString(this, SharedPreference.KEY_TOKEN_ID, token);
            Intent intent = new Intent(SharedPreference.ACTION_FCM_TOKEN_ID);
            intent.putExtra(SharedPreference.EXTRA_TOKEN_ID, token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
*/
}

