package com.example.vivekkushwaha.alarmmanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageButton image;
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder stringBuilder1 = new StringBuilder();
    Button btnSelectDate,btnSelectTime;

    private int mYear, mMonth, mDay, mHour, mMinute,mSecond;
    private int eYear , eMonth ,eDay ,eHour ,eMinute;
    private Context context;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.imageButton2);
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
                if (v.findViewById(R.id.setdate) == btnSelectDate) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    mSecond=c.get(Calendar.SECOND);
                    DatePickerDialog datePickerDialog = new DatePickerDialog( MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    eDay=dayOfMonth;
                                    eYear=year;
                                    eMonth=monthOfYear;
                                    stringBuilder= stringBuilder.append(dayOfMonth);
                                    stringBuilder= stringBuilder.append("-");
                                    stringBuilder= stringBuilder.append(monthOfYear+1);
                                    stringBuilder= stringBuilder.append("-");
                                    stringBuilder= stringBuilder.append(year);
                                    btnSelectDate.setText(stringBuilder);
                                    stringBuilder=new StringBuilder("");
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
        final Calendar c = Calendar.getInstance();
        Log.i("vivek","day"+mDay);
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnSelectTime) {
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String am_pm ;
                                    eHour =hourOfDay; // used for storing hour entered
                                    eMinute=minute;
                                    if(hourOfDay>12) {
                                        hourOfDay= hourOfDay%12;
                                        am_pm="PM" ;
                                    }
                                    if (hourOfDay==12)
                                        am_pm="PM";
                                    else
                                        am_pm="AM";
                                    if (hourOfDay<10) {
                                        stringBuilder1= stringBuilder1.append("0");
                                        stringBuilder1= stringBuilder1.append(hourOfDay);
                                    }
                                    else
                                        stringBuilder1= stringBuilder1.append(hourOfDay); // for hours
                                        stringBuilder1= stringBuilder1.append(":");
                                    if(minute<10){
                                        stringBuilder1= stringBuilder1.append("0");
                                        stringBuilder1= stringBuilder1.append(minute);
                                    }
                                    else
                                        stringBuilder1= stringBuilder1.append(minute);   // for minute
                                        stringBuilder1= stringBuilder1.append(" ");
                                        stringBuilder1= stringBuilder1.append(am_pm);
                                        btnSelectTime.setText(stringBuilder1);
                                        stringBuilder1=new StringBuilder("");
                                }

                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
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
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar enteredDate = Calendar.getInstance();
                        enteredDate.set(Calendar.DATE,eDay);
                        enteredDate.set(Calendar.MONTH, eMonth);
                        enteredDate.set(Calendar.YEAR, eYear);
                        enteredDate.set(Calendar.HOUR ,eHour);
                        enteredDate.set(Calendar.MINUTE,eMinute);
                        boolean isAfterToday = enteredDate.after(c);
                        if(!isAfterToday) {
                            Toast.makeText(getApplicationContext(),"can't set reminder on past date",Toast.LENGTH_SHORT).show() ;
                        }
                        else {
                            Date  edate= new Date(eYear,eMonth,eDay,eHour,eMinute);
                            Date sydate= new Date(mYear,mMonth,mDay,mHour,mMinute);
                            long differnce = edate.getTime()-sydate.getTime();
                            Long time = differnce-mSecond*1000;

                            SharedPreferences.Editor editor = getSharedPreferences("value", MODE_PRIVATE).edit();
                            editor.putString("name", editText.getText().toString());
                            editor.apply();

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent = new Intent(MainActivity.this,Alarmreceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10000,pendingIntent);
                            Toast.makeText(MainActivity.this,"Alarm is working",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) ;
        alert.create().show();

    }
}
