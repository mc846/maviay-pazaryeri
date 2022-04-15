package com.maviay.pazaryeri.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.maviay.pazaryeri.Base.BaseActivity;
import com.maviay.pazaryeri.R;
import com.maviay.pazaryeri.databinding.ActivityUrunBinding;

import java.util.ArrayList;

public class UrunActivity  extends BaseActivity {
    ActivityUrunBinding tasarimUrun;
    Context context = this;
    private UrunSorgulari urunSorgulari = UrunSorgulari.getInstance();
    Long id = Long.valueOf(0);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); // Network hatasını bertaraf etmek için.

        super.onCreate(savedInstanceState);
        tasarimUrun = DataBindingUtil.setContentView(this, R.layout.activity_urun);
        tasarimUrun.setUrunActivityNesnesi(this);

        Intent intent = getIntent();
        tasarimUrun.tvSatir9a.setText("Id:");
        id = intent.getLongExtra("id", 0);
        tasarimUrun.tvSatir9b.setText(id.toString());
        if (id > Long.valueOf(0)) {
            ArrayList<Urun> urunler =  urunSorgulari.products("", "", 0, 0, id);
            if (urunler.size() > 0) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (urunler.size() > 0) {
                            tasarimUrun.tvSatir1a.setText("Title:");
                            tasarimUrun.tvSatir1b.setText(urunler.get(0).getTitle());
                            tasarimUrun.tvSatir2a.setText("Description:");
                            tasarimUrun.tvSatir2b.setText(urunler.get(0).getDescription());
                            tasarimUrun.tvSatir3a.setText("Brand:");
                            tasarimUrun.tvSatir3b.setText(urunler.get(0).getBrand());
                            tasarimUrun.tvSatir4a.setText("Category:");
                            tasarimUrun.tvSatir4b.setText(urunler.get(0).getCategory());
                            tasarimUrun.tvSatir5a.setText("Price:");
                            tasarimUrun.tvSatir5b.setText(urunler.get(0).getPrice().toString());
                            tasarimUrun.tvSatir6a.setText("Discount Percentage:");
                            tasarimUrun.tvSatir6b.setText(urunler.get(0).getDiscountPercentage().toString());
                            tasarimUrun.tvSatir7a.setText("Rating:");
                            tasarimUrun.tvSatir7b.setText(urunler.get(0).getRating().toString());
                            tasarimUrun.tvSatir8a.setText("Stock");
                            tasarimUrun.tvSatir8b.setText(urunler.get(0).getStock().toString());
                            String resimAdres = urunler.get(0).getThumbnail();
                            Glide.with(context).load(resimAdres).into((ImageView) findViewById(R.id.ivUrunSol2));


                            ImageView iv;
                            LinearLayout linear;
                            linear = new LinearLayout(context);
                            linear = (LinearLayout) findViewById(R.id.LLScroll);
                            ArrayList<Resim> resimler = urunler.get(0).getResimler();
                            for (int i = 0; i < resimler.size(); i++) {
                                iv = new ImageView(context);
                                iv.setPadding(0, 10, 0, 0);
                                linear.addView(iv);
                                Glide.with(context).load(resimler.get(i).getAdres()).into((ImageView) iv);
                            }

                        }
                    }
                });
            }
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_urun;
    }

}
