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
import com.bumptech.glide.request.target.Target;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mantraideas.samplproject.MainActivity;
import com.mantraideas.samplproject.R;
import com.yuvi.mantraui.video.PlayerActivity;

import java.util.Map;
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
        Map<String, String> a = remoteMessage.getData();
            generateNotification(this, a.get("alert_title"), a.get("alert_message"), a.get("data"), a.get("type"), a.get("image"));
    }

    private void generateNotification(Context context, String title, String message, String data, String type, String image_url) {

        android.support.v4.app.NotificationCompat.Builder mBuilder;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        if (TextUtils.isEmpty(data)) {
            notificationIntent = new Intent(context, MainActivity.class);
        } else {
            notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse(data));
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new android.support.v4.app.NotificationCompat.Builder(context, "1001");
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
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
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
