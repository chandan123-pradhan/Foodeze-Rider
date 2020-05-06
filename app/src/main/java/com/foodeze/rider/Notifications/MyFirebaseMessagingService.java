package com.foodeze.rider.Notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.foodeze.rider.ActivitiesAndFragments.Activities.RiderMainActivity;
import com.foodeze.rider.Constants.Config;
import com.foodeze.rider.Constants.PreferenceClass;
import com.foodeze.rider.Utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    String channelId = "channel-01";

    String imageUrl,timestamp;

    SharedPreferences sPref;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        if(s==null){

        }else if(s.length()>8){
            storeRegIdInPref(s);
        }
    }



    private void storeRegIdInPref(String token) {
        sPref = getSharedPreferences(PreferenceClass.user,MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(PreferenceClass.device_token, token);
        editor.commit();
    }



    private void handleDataMessage(JSONObject json) {


        Log.e(TAG, "push json: " + json.toString());
        SharedPreferences.Editor editor = sPref.edit();

        try {

            if(!json.getString("type").isEmpty()) {

                String title = json.getString("title");

                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", title);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext(), channelId);
                    notificationUtils.playNotificationSound();
                } else {
                    // app is in background, show the notification in notification tray
                    Intent resultIntent = new Intent(getApplicationContext(), RiderMainActivity.class);
                    resultIntent.putExtra("message", title);

                    // check for image attachment
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, title, timestamp, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, title, timestamp, resultIntent, imageUrl);
                    }
                }
            }
            } catch (JSONException e) {
                Log.e(TAG, "Json Exception: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }





    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context,channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context,channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
