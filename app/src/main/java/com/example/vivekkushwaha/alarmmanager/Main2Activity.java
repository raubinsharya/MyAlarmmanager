package com.example.vivekkushwaha.alarmmanager;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView=findViewById(R.id.textview);
        if (Alarmreceiver.r!=null && Alarmreceiver.r.isPlaying()){
            Alarmreceiver.r.stop();
            Alarmreceiver.notificationManager.cancel(Alarmreceiver.NOTIFICATION_ID);
        }
        SharedPreferences sharedPreferences=getSharedPreferences("value",MODE_PRIVATE);
        textView.setText(sharedPreferences.getString("name","value"));
    }
}
