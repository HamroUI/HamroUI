package com.mantraideas.samplproject.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mantraideas.samplproject.MainActivity;
import com.mantraideas.samplproject.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String message = remoteMessage.getData().get("alert_message");
        String imageUri = remoteMessage.getData().get("alert_image");
        String title = remoteMessage.getData().get("alert_title");
        String url = remoteMessage.getData().get("alert_url");
        sendNotification(title, message, imageUri, url);
    }

    private void sendNotification(String title, String message, String image_url, String url) {
        android.support.v4.app.NotificationCompat.Builder mBuilder;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        if (TextUtils.isEmpty(url)) {
            notificationIntent = new Intent(this, MainActivity.class);
        } else {
            notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse(url));
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new android.support.v4.app.NotificationCompat.Builder(this, "100");
        mBuilder.setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(message));
        if (!TextUtils.isEmpty(image_url)) {
            try {
                Bitmap bitmap = Glide.with(getApplicationContext())
                        .load(image_url)
                        .asBitmap()
                        .into(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                        .get();
                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(title)
                        .bigPicture(bitmap)
                        .setSummaryText(message);
                mBuilder.setStyle(style);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notificationManager.notify(new Random().nextInt(80 - 65) + 65, mBuilder.build());
    }

}