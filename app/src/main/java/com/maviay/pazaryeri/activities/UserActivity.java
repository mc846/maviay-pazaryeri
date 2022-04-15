package com.maviay.pazaryeri.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import com.maviay.pazaryeri.Base.BaseActivity;
import com.maviay.pazaryeri.R;
import com.maviay.pazaryeri.Utils.Util;
import com.maviay.pazaryeri.databinding.ActivityUserBinding;

import java.io.InputStream;

public class UserActivity extends BaseActivity {

  ActivityUserBinding tasarimUser;
  Context context = this;
  private UrunSorgulari urunSorgulari = UrunSorgulari.getInstance();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy); // Network hatasını bertaraf etmek için.

    super.onCreate(savedInstanceState);
    tasarimUser = DataBindingUtil.setContentView(this,R.layout.activity_user);
    tasarimUser.setUserActivityNesnesi(this);

    Intent intent = getIntent();
    tasarimUser.editKullanici.setText(intent.getStringExtra("username"));
    tasarimUser.editEPosta.setText(intent.getStringExtra("email"));
    tasarimUser.editAdiSoyadi.setText(intent.getStringExtra("firstName") + " " + intent.getStringExtra("lastName"));
    String resimAdres = intent.getStringExtra("image");
    new  DownloadImageTask((ImageView) findViewById(R.id.imageKullanici)).execute(resimAdres);
  }
  private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
      this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
      String urldisplay = urls[0];
      Bitmap mIcon11 = null;
      try {
        InputStream in = new java.net.URL(urldisplay).openStream();
        mIcon11 = BitmapFactory.decodeStream(in);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
      bmImage.setImageBitmap(result);
    }
  }
  public void buttonButunUrunlerTikla(){
    //Util.toast(UserActivity.this, "Bütün Ürünler.");
    if (urunSorgulari.GetToken().isEmpty()) {
      Util.alertError(UserActivity.this)
          .setText("Giriş başarısız.")
          .show();
      return;
    }
    Intent intent = new Intent(this, ButunUrunlerActivity.class);
    //intent.putExtra("token", token);
    startActivity(intent);
  }
  public void buttonSecilenUrunlerTikla(){
    //Util.toast(UserActivity.this, "Seçilen Ürünler.");
    if (urunSorgulari.GetToken().isEmpty()) {
      Util.alertError(UserActivity.this)
          .setText("Giriş başarısız.")
          .show();
      return;
    }
    Intent intent = new Intent(this, SecilenUrunlerActivity.class);
    startActivity(intent);
  }
  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_user;
  }
}