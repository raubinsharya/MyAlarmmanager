package com.example.vivekkushwaha.alarmmanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnSelectDate,btnSelectTime;

    private int systemYear, systemMonth, systemDay, systemHour, systemMinute,systemSecond;
    private int userYear , userMonth ,userDay ,userHour ,userMinute,userSecond;
    private Context context;
    private EditText editText;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText2);
    }

    public void buttonClicked(View view) {
        final LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alertdialog, null);

        btnSelectDate = alertLayout.findViewById(R.id.setdate);
        btnSelectTime = alertLayout.findViewById(R.id.settime);

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    systemYear = calendar.get(Calendar.YEAR);
                    systemMonth = calendar.get(Calendar.MONTH);
                    systemDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog( MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            userYear=year;
                            userMonth=monthOfYear;
                            userDay=dayOfMonth;
                            btnSelectDate.setText(dayOfMonth+"/"+monthOfYear+1+"/"+year);
                        }
                    }, systemYear, systemMonth, systemDay);
                    datePickerDialog.show();

            }
        });
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemHour=calendar.get(Calendar.HOUR_OF_DAY);
                systemMinute=calendar.get(Calendar.MINUTE);
                systemSecond=calendar.get(Calendar.SECOND);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                       userHour=hourOfDay;
                                       userMinute=minute;
                                       btnSelectTime.setText(hourOfDay+"/"+minute);
                                }

                            }, systemHour, systemMinute, false);
                    timePickerDialog.show();

            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Alarm");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar enteredDate = Calendar.getInstance();
                        enteredDate.set(Calendar.DATE,userDay);
                        enteredDate.set(Calendar.MONTH, userMonth);
                        enteredDate.set(Calendar.YEAR, userYear);
                        enteredDate.set(Calendar.HOUR ,userHour);
                        enteredDate.set(Calendar.MINUTE,userMinute);
                        boolean isAfterToday = enteredDate.after(calendar);
                        if(!isAfterToday) {
                            Toast.makeText(getApplicationContext(),"Can't set reminder on past date",Toast.LENGTH_SHORT).show() ;
                        }
                        else {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), userHour, userMinute, 0);
                            setAlarm(calendar.getTimeInMillis());
                        }
                    }
                });
                alert.create().show();
    }
    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, Alarmreceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
  }
