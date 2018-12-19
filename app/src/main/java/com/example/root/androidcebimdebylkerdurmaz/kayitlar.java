package com.example.root.androidcebimdebylkerdurmaz;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class kayitlar extends AppCompatActivity {

    ListView lv;
    Veritabani vt;
    Button btemizle,bsecili;
    int pozisyon=-1;
    int[] id;
    String[] veri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayitlar);

        lv=findViewById(R.id.listView);
        vt=new Veritabani(this);
        btemizle=findViewById(R.id.btemizle);
        bsecili=findViewById(R.id.bsecili);
        doldur();

        btemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vt.sil();
                doldur();
            }
        });

        bsecili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.length>0&&pozisyon>-1)
                {
                    vt.sil(id[pozisyon]+"");
                    doldur();
                    Toast.makeText(getApplicationContext(),"Başarıyla silindi!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Silinecek kayıt seçmediniz!",Toast.LENGTH_SHORT).show();
                }
                doldur();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pozisyon=position;
                for (int i = 0; i < lv.getChildCount(); i++) {
                    if(position == i ){
                        lv.getChildAt(i).setBackgroundColor(Color.parseColor("#FF4500"));
                    }else{
                        lv.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        doldur();
    }

    @Override
    protected void onPause() {
        super.onPause();
        doldur();
    }

    private void doldur()
    {
        Cursor cursor=vt.listele();
        cursor.moveToFirst();

        veri= new String[cursor.getCount()];
        id=new int[cursor.getCount()];

        for(int i=0;i<cursor.getCount();i++)
        {
            id[i]=cursor.getInt(0);
            veri[i]=(cursor.getString(1)+" - "+cursor.getString(2)+" dakika.");
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,veri);
        lv.setAdapter(adapter);
    }
}
