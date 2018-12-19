package com.example.root.androidcebimdebylkerdurmaz;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class sensorServis extends Service implements SensorEventListener {

    Timer zamanlayici;
    int dakika=0;
    SensorManager sensorManager;
    Sensor sensor;
    Handler handler;
    Date tarih;
    SimpleDateFormat bugun;
    Veritabani vt;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        handler=new Handler(Looper.getMainLooper());
        bugun=new SimpleDateFormat("dd/MM/yyyy");
        vt=new Veritabani(this);

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "AndroidCebimde::uyandirmaKilidi");
        wakeLock.acquire();

    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //servis wakelock tarafından kapatılmasın diye deneme

        return START_STICKY;
    }*/

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if(event.values[0]<=2)
            {

                zamanlayici=new Timer();
                zamanlayici.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                              //  Toast.makeText(getApplicationContext(),"DK ++++",Toast.LENGTH_SHORT).show(); //deneme amaçlı kullanılmıştır.
                                dakika++;
                            }
                        });
                    }
                },60000,60000);
            }
            else
            {
                if(zamanlayici!=null)
                {
                    Toast.makeText(getApplicationContext(),"DK EKLENDİ",Toast.LENGTH_SHORT).show();
                    dkEkle();
                    zamanlayici.cancel();
                    zamanlayici.purge();
                    zamanlayici=null;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void dkEkle()
    {
        tarih=new Date();
        String bugunTarih=bugun.format(tarih);
        vt.dakikaEkle(bugunTarih,dakika);
        dakika=0;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        wakeLock.release();
    }
}
//By İlker Durmaz