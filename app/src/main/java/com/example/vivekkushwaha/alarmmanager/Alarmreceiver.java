package com.example.vivekkushwaha.alarmmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import javax.xml.transform.Result;

import static android.content.Context.NOTIFICATION_SERVICE;


public class Alarmreceiver extends BroadcastReceiver{
    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    static Ringtone r;
    public static int NOTIFICATION_ID=1234;
    public static NotificationManager notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        final String NOTIFICATION_CHANNEL_ID = "4565";
        CharSequence channelName = "NOTIFICATION_CHANNEL_NAME";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        Intent inten=new Intent(context,Main2Activity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,inten,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle("This is your Alarm")
                .setContentText("Remember you have set an Alarm for this time")
                .setContentIntent(pendingIntent);
        notificationManager.notify(NOTIFICATION_ID,builder.build());
        r = RingtoneManager.getRingtone(context, notification);
        r.play();





    }





        }
