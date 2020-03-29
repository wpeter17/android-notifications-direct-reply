package com.example.notificationsreply;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class RemoteReceiver extends AppCompatActivity {
    private TextView notificationMesage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_receiver);

        notificationMesage = (TextView) findViewById(R.id.notificationResponseMessage);
        Bundle remoteReply = RemoteInput.getResultsFromIntent(getIntent());

        if (remoteReply != null) {
            String message = remoteReply.getCharSequence(MainActivity.TXT_REPLY).toString();
            notificationMesage.setText(message);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MainActivity.NOTIFICATION_ID);
    }
}
