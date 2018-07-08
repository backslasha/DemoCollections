package yhb.dc.demo.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import yhb.dc.R;
import yhb.dc.common.Demo;

import static android.app.Notification.CATEGORY_PROGRESS;
import static android.app.Notification.CATEGORY_STATUS;

public class NotificationActivity extends AppCompatActivity implements Demo {
    private static final int REQUEST_CODE_WHEN_NOTIFICATION_IS_CLICKED = 1;
    private static final int REQUEST_CODE_WHEN_NOTIFICATION_IS_CLEARED = 2;

    // 8.0 通知栏需要用到 Channel
    private static final String CHANNEL_ID_PROGRESS = "channel_id_progress";
    private static final String CHANNEL_NAME_PROGRESS = "进度通知";
    private int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    public void send2(View view) {

        Intent intent = new Intent(this, EchoBroadcastReceiver.class);
        intent.putExtra(EchoBroadcastReceiver.MESSAGE, "点击了通知");

        Intent intent2 = new Intent(this, EchoBroadcastReceiver.class);
        intent2.putExtra(EchoBroadcastReceiver.MESSAGE, "划掉了通知");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE_WHEN_NOTIFICATION_IS_CLICKED, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, REQUEST_CODE_WHEN_NOTIFICATION_IS_CLEARED, intent2, PendingIntent.FLAG_ONE_SHOT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.content_launch_mode);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker("ticker")
                .setAutoCancel(true) // 点击后消失
                .setContentText("ContentText")
                .setContentTitle("ContentTitle")
                .setContentIntent(pendingIntent) // 通知被点击时的 Intent
                .setDeleteIntent(pendingIntent2) // 通知被划掉时的 Intent
                .setSubText("SubText")
//                .setProgress(100, 38, true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setNumber(2018)
                .setShowWhen(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomBigContentView(remoteViews);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "channel1",
                    "Channel1Name",
                    NotificationManager.IMPORTANCE_LOW
            );

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannelGroup(new NotificationChannelGroup("groupId", "groupMame"));
                notificationChannel.setGroup("groupId");
                notificationManager.createNotificationChannel(notificationChannel);
                builder.setBadgeIconType(Notification.BADGE_ICON_LARGE)
                        .setCategory(CATEGORY_STATUS)
                        .setGroup("Group")
                        .setColor(Color.GREEN)
                        .setColorized(true)
                        .setSettingsText("SettingsText")
//                    .setGroupSummary(true)
                        .setChannelId(notificationChannel.getId());
            }


        }

        Notification notification = builder.build();
        NotificationManagerCompat.from(this)
                .notify(notificationId++, notification);
    }

    public void send1(View view) {

        Intent intent = new Intent(this, EchoBroadcastReceiver.class);
        intent.putExtra(EchoBroadcastReceiver.MESSAGE, "点击了通知");

        Intent intent2 = new Intent(this, EchoBroadcastReceiver.class);
        intent2.putExtra(EchoBroadcastReceiver.MESSAGE, "划掉了通知");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE_WHEN_NOTIFICATION_IS_CLICKED, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, REQUEST_CODE_WHEN_NOTIFICATION_IS_CLEARED, intent2, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker("ticker")
                .setAutoCancel(true)
                .setContentText("ContentText")
                .setContentTitle("ContentTitle")
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent2)
                .setSubText("SubText")
//                .setProgress(100, 38, true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setNumber(2018)
                .setOngoing(true)
                .setShowWhen(true);

        // 适配 Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID_PROGRESS,
                        CHANNEL_NAME_PROGRESS,
                        NotificationManager.IMPORTANCE_LOW/* 不弹出，无声音*/
                );
                notificationManager.createNotificationChannel(notificationChannel);
                builder.setCategory(CATEGORY_PROGRESS)
                        .setChannelId(notificationChannel.getId());
            }
        }
        Notification notification = builder.build();
        NotificationManagerCompat.from(this)
                .notify(notificationId++, notification);
    }
}
