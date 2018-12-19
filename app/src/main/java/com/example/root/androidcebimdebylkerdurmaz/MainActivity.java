package com.example.root.androidcebimdebylkerdurmaz;

import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button bkayitlar;
    TextView tvsure,tvdaksaat,tvay,tvhafta;
    ProgressBar pb;
    Intent i,s;
    Veritabani vt;
    Date tarih;
    SimpleDateFormat bugun;
    boolean b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bkayitlar=findViewById(R.id.bkayitlar);
        tvsure=findViewById(R.id.tv_sure);
        tvdaksaat=findViewById(R.id.tv_daksaat);
        tvay=findViewById(R.id.tv_ay);
        tvhafta=findViewById(R.id.tv_hafta);
        pb=findViewById(R.id.progressBar);
        i=new Intent(this,kayitlar.class);
        s=new Intent(this,sensorServis.class);
        vt=new Veritabani(this);
        tarih=new Date();
        bugun=new SimpleDateFormat("dd/MM/yyyy");

        if(servisDurumu())
        {
            Toast.makeText(this,"Servis çalışmaya devam ediyor.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            startService(s);
            Toast.makeText(this,"Servis başlatıldı.",Toast.LENGTH_SHORT).show();
        }
        pbGuncelle();
        ayHaftaGuncelle();

        bkayitlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    public void pbGuncelle()
    {
        int dakika=vt.bugunDakika(bugun.format(tarih));
        int yuzde=(dakika*100)/1440;
        pb.setProgress(yuzde);
        tvsure.setText(dakika+"");
    }

    public void ayHaftaGuncelle()
    {
        Cursor cursor=vt.listele();
        cursor.moveToFirst();

        float haftaDakika=0;
        float ayDakika=0;


        if(cursor.getCount()>0)
        {
            for(int i=0;i<7;i++)
            {
                haftaDakika+=(float)cursor.getInt(2);
                b=cursor.moveToNext();
                if(!b)
                    break;
            }

            cursor.moveToFirst();

            for(int i=0;i<30;i++)
            {
                ayDakika+=(float)cursor.getInt(2);
                b=cursor.moveToNext();
                if(!b)
                    break;
            }
        }

        float haftaGun=(int)((haftaDakika/1440)*100);
        float ayGun=(int)((ayDakika/1440)*100);
        haftaGun/=100;
        ayGun/=100;

        tvhafta.setText("Bu hafta: yaklaşık "+haftaGun+" gün.");
        tvay.setText("Bu ay: yaklaşık "+ayGun+" gün.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        pbGuncelle();
        ayHaftaGuncelle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pbGuncelle();
        ayHaftaGuncelle();
    }

    public boolean servisDurumu()
    {
        ActivityManager servisKontrol= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo servisInfo: servisKontrol.getRunningServices(Integer.MAX_VALUE))
        {
            if(getPackageName().equals(servisInfo.service.getPackageName()))
            {
                return true;
            }
        }
        return false;
    }
}
