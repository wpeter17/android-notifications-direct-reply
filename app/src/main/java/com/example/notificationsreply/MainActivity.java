package com.example.notificationsreply;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    private Button createNotificationButton;
    public final String CHANNEL_ID = "notificationsreply";
    public static final int NOTIFICATION_ID = 001;
    public static final String TXT_REPLY = "text_reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationButton = (Button) findViewById(R.id.createNotificationButton);
        createNotification();
    }

    public void createNotification() {
        createNotificationChannel();

        createNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity", "New notification");
                Intent intent = new Intent(view.getContext(), NotificationsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext(), CHANNEL_ID);
                builder.setSmallIcon(R.drawable.ic_sms_notification);
                builder.setContentTitle("Simple Notification");
                builder.setContentText("This is a simple notification");
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setAutoCancel(true);
                builder.setContentIntent(pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    androidx.core.app.RemoteInput remoteInput = new androidx.core.app.RemoteInput.Builder(TXT_REPLY).setLabel("Reply").build();

                    Intent replyIntent = new Intent(view.getContext(), RemoteReceiver.class);
                    replyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent replyPendingIntent = PendingIntent.getActivity(view.getContext(), 0, replyIntent, PendingIntent.FLAG_ONE_SHOT);

                    NotificationCompat.Action action =
                            new NotificationCompat.Action.Builder(R.drawable.ic_sms_notification,
                                    getString(R.string.reply_text), replyPendingIntent)
                                    .addRemoteInput(remoteInput)
                                    .build();
                    builder.addAction(action);
                }

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(view.getContext());
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications Reply";
            String description = "Include all notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
