package com.example.onesignaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new FirebaseNotificationOpenedHandler(this))
                .init();
        setContentView(R.layout.activity_main);
    }

    public class FirebaseNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        Context ctx;
        FirebaseNotificationOpenedHandler(Context context) {
            ctx = context;
        }
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            Toast.makeText(ctx, "Halo, saya klik notifikasi ya", Toast.LENGTH_SHORT).show();
            if (data != null) {
                String customKey = data.optString("customkey", null);
                String lagikey = data.optString("lagikey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
                if (lagikey != null)
                    Log.i("OneSignalExample", "lagikey set with value: " + lagikey);
            }
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            Intent intent = new Intent(getApplicationContext(), PenerimaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

}