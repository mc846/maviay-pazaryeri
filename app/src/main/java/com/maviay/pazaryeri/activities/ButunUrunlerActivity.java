package com.maviay.pazaryeri.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.maviay.pazaryeri.Base.BaseActivity;
import com.maviay.pazaryeri.R;
import com.maviay.pazaryeri.Utils.Util;
import com.maviay.pazaryeri.databinding.ActivityButunUrunlerBinding;

import java.util.ArrayList;

public class ButunUrunlerActivity extends BaseActivity {
  ActivityButunUrunlerBinding tasarimButunUrunler;
  Context context = this;
  private UrunSorgulari urunSorgulari = UrunSorgulari.getInstance();
  private UrunAdapter urunAdapter;
  private ListView butunUrunlerList;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy); // Network hatasını bertaraf etmek için.

    super.onCreate(savedInstanceState);
    tasarimButunUrunler = DataBindingUtil.setContentView(this,R.layout.activity_butun_urunler);
    tasarimButunUrunler.setButunUrunlerActivityNesnesi(this);
    Intent intent = getIntent();
    //token = intent.getStringExtra("token");
    if (urunSorgulari.GetToken().isEmpty()) {
      Util.alertError(ButunUrunlerActivity.this)
          .setText("Bağlantı başarısız.")
          .show();
      return;
    }
    urunAdapter = new UrunAdapter(this);
    butunUrunlerList = (ListView)findViewById(R.id.butun_urunler_list);
    butunUrunlerList.setAdapter(urunAdapter);
    ArrayList<Urun> urunler = urunSorgulari.products("", "id,title,description,brand,category,price,thumbnail", 0, 0, Long.valueOf(0));
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < urunler.size(); i++) {
          urunAdapter.add(urunler.get(i));
        }
        butunUrunlerList.setSelection(0 /*butunUrunlerList.getCount() - 1*/);
      }
    });
  }
  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_butun_urunler;
  }

}
