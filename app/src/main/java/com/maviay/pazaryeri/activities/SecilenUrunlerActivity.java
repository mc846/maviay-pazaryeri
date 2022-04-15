package com.maviay.pazaryeri.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.databinding.DataBindingUtil;

import com.maviay.pazaryeri.Base.BaseActivity;
import com.maviay.pazaryeri.R;
import com.maviay.pazaryeri.Utils.Util;
import com.maviay.pazaryeri.databinding.ActivitySecilenUrunlerBinding;

import java.util.ArrayList;

public class SecilenUrunlerActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
  ActivitySecilenUrunlerBinding tasarimSecilenUrunler;
  Context context = this;
  private UrunSorgulari urunSorgulari = UrunSorgulari.getInstance();
  private UrunAdapter urunAdapter;
  private ListView secilenUrunlerList;
  private Spinner spinner;
  private static final String[] paths = {"10", "25", "50", "100"};
  private int sayfadaUrunAdet = 10, urunAdet = 0, urunSayfa = 0, sayfaAdet = 0;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy); // Network hatasını bertaraf etmek için.

    super.onCreate(savedInstanceState);
    tasarimSecilenUrunler = DataBindingUtil.setContentView(this, R.layout.activity_secilen_urunler);
    tasarimSecilenUrunler.setSecilenUrunlerActivityNesnesi(this);
    Intent intent = getIntent();
    //token = intent.getStringExtra("token");
    ArrayList<Urun> urunler = new ArrayList<Urun>();
    //Urun urun = null;
    //urun = new Urun(Long.valueOf(0), "fgsfgsf", "", Long.valueOf(0), Double.valueOf(0), Double.valueOf(0), Double.valueOf(0), "", "", "");
    //urunler.add(urun);

    spinner = (Spinner)findViewById(R.id.spinnerAdet);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SecilenUrunlerActivity.this,
            android.R.layout.simple_spinner_item,paths);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);
    secilenUrunlerList = (ListView)findViewById(R.id.secilen_urunler_list);

    Button buttonListele, buttonIlkSayfa, buttonOncekiSayfa, buttonSonrakiSayfa, buttonSonSayfa;

    buttonListele = (Button)findViewById(R.id.buttonListele);
    buttonListele.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (TokenKontrol() == false) return;
        UrunListele(1);
      }
    });

    buttonIlkSayfa = (Button)findViewById(R.id.buttonIlkSayfa);
    buttonIlkSayfa.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (TokenKontrol() == false) return;
        UrunListele(1);
      }
    });

    buttonOncekiSayfa = (Button)findViewById(R.id.buttonOncekiSayfa);
    buttonOncekiSayfa.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (TokenKontrol() == false) return;
        UrunListele(urunSayfa - 1);
      }
    });

    buttonSonrakiSayfa = (Button)findViewById(R.id.buttonSonrakiSayfa);
    buttonSonrakiSayfa.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (TokenKontrol() == false) return;
        UrunListele(urunSayfa + 1);
      }
    });
    buttonSonSayfa = (Button)findViewById(R.id.buttonSonSayfa);
    buttonSonSayfa.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (TokenKontrol() == false) return;
        UrunListele(sayfaAdet);
      }
    });
  }
  private boolean TokenKontrol () {
    if (urunSorgulari.GetToken().isEmpty()) {
      Util.alertError(SecilenUrunlerActivity.this)
              .setText("Bağlantı başarısız.")
              .show();
      return false;
    }
    return true;
  }
  private void UrunListele (int sayfa) { // Paginator için.
    int skip = 0;
    urunAdapter = new UrunAdapter(context);
    secilenUrunlerList.setAdapter(null);
    secilenUrunlerList.setAdapter(urunAdapter);
    if (sayfa <= 0) sayfa = 1;
    if (sayfadaUrunAdet == 0) {
      urunSayfa = 1;
    } else {
      urunSayfa = sayfa;
    }
    ArrayList<Urun> urunler1 = urunSorgulari.products(tasarimSecilenUrunler.editAranacakUrun.getText().toString(), "id,title", 0, 0, Long.valueOf(0));
    urunAdet = urunler1.size();
    if (urunAdet == 0) {
      secilenUrunlerList.setAdapter(null);
      return;
    }
    sayfaAdet = urunAdet / sayfadaUrunAdet;
    if (sayfaAdet * sayfadaUrunAdet < urunAdet) sayfaAdet ++;
    if (urunSayfa > sayfaAdet) urunSayfa = sayfaAdet;
    skip = sayfadaUrunAdet * (urunSayfa - 1);

    ArrayList<Urun> urunler2 =  urunSorgulari.products(tasarimSecilenUrunler.editAranacakUrun.getText().toString(), "id,title,description,brand,category,price,thumbnail", sayfadaUrunAdet, skip, Long.valueOf(0));
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < urunler2.size(); i++) {
          urunAdapter.add(urunler2.get(i));
        }
        secilenUrunlerList.setSelection(0 /*secilenUrunlerList.getCount() - 1*/);
      }
    });

    tasarimSecilenUrunler.textSayfalar.setText("Sayfa: " + urunSayfa + "/" + sayfaAdet);
  }
  @Override
  public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

    switch (position) {
      case 0:
        sayfadaUrunAdet = 10;
        break;
      case 1:
        sayfadaUrunAdet = 25;
        break;
      case 2:
        sayfadaUrunAdet = 50;
        break;
      case 3:
        sayfadaUrunAdet = 100;
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_secilen_urunler;
  }
}
