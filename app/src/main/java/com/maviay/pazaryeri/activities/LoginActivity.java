package com.maviay.pazaryeri.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;

import com.maviay.pazaryeri.Base.BaseActivity;
import com.maviay.pazaryeri.R;
import com.maviay.pazaryeri.Utils.Util;
import com.maviay.pazaryeri.databinding.ActivityLoginBinding;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
//import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    /*@BindView(R.id.txtSifremiUnuttum)
    TextView txtSifremiUnuttum;
    @BindView(R.id.edtPass)
    TextView edtPass;
    @BindView(R.id.edtPhone)
    TextView edtPhone;
    @BindView(R.id.btnGirisYap)
    TextView btnGirisYap;*/

    ActivityLoginBinding tasarimLogin;

    Context context = this;
    private static final String TAG = "LoginActivity";
    private UrunSorgulari urunSorgulari = UrunSorgulari.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); // Network hatasını bertaraf etmek için.

        super.onCreate(savedInstanceState);
        tasarimLogin = DataBindingUtil.setContentView(this,R.layout.activity_login);
        tasarimLogin.setLoginActivityNesnesi(this);
        //Hawk.init(this).build();

        tasarimLogin.editKullanici.setOnEditorActionListener(editorListener);
        tasarimLogin.editPass.setOnEditorActionListener(editorListener);

        tasarimLogin.btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kullanici = tasarimLogin.editKullanici.getText().toString().trim();
                String sifre = tasarimLogin.editPass.getText().toString().trim();

                if (kullanici.isEmpty()) {
                    Util.alertError(LoginActivity.this).setText("Lütfen kullanıcı adınızı giriniz.").show();
                    tasarimLogin.editKullanici.requestFocus();
                    return;
                }
                if (sifre.isEmpty()) {
                    Util.alertError(LoginActivity.this).setText("Lütfen şifrenizi giriniz.").show();
                    tasarimLogin.editPass.requestFocus();
                    return;
                }
                setLoading(true);
                login(kullanici, sifre);

            }
        });

        tasarimLogin.txtSifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.goIntent(LoginActivity.this, ForgetPasswordActivity.class);
            }
        });

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_NEXT:
                    break;
                case EditorInfo.IME_ACTION_DONE:
                    break;
            }
            return false;
        }
    };

    private void login(String kullanici, String sifre) {
        String jsonSatir = "";
        Map<String, String> map = new HashMap<>();
        map.put("username", kullanici);
        map.put("password", sifre);
        map.put("expiresInMins", urunSorgulari.GetExpires());
        JSONObject jo = new JSONObject(map);
        String jsonStr = jo.toJSONString();
        //System.out.println(jsonStr);
        String jsonAdres = urunSorgulari.GetWebSayfa() + "/auth/login";
        URL url;
        urunSorgulari.SetToken("");
        try {
            setLoading(false);
            url = new URL(jsonAdres);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");

            String data = jsonStr;
            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200) { // Bağlantı başarılı ise kod 200 olmalıdır.
                //throw new RuntimeException("HttpResponseCode: " +responsecode);
                setLoading(false);
                Util.alertError(LoginActivity.this)
                    .setText("HttpResponseCode: " +responsecode)
                    .show();
                return;
            }
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
            InputStream content = (InputStream)conn.getInputStream();
            BufferedReader in   = new BufferedReader (new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                jsonSatir += line;
            }
            //System.out.println(jsonSatir);
            JSONParser parse = new JSONParser();
            JSONObject jobj = (JSONObject)parse.parse(jsonSatir);


            if (jobj.get("token") != null) {
                Intent intent = new Intent(this, UserActivity.class);

                urunSorgulari.SetToken((String)jobj.get("token"));
                //intent.putExtra("token", (String)jobj.get("token"));
                if (jobj.get("id") != null) intent.putExtra("id", (long)jobj.get("id"));
                if (jobj.get("username") != null) intent.putExtra("username", (String)jobj.get("username"));
                if (jobj.get("email") != null) intent.putExtra("email", (String)jobj.get("email"));
                if (jobj.get("firstName") != null) intent.putExtra("firstName", (String)jobj.get("firstName"));
                if (jobj.get("lastName") != null) intent.putExtra("lastName", (String)jobj.get("lastName"));
                if (jobj.get("gender") != null) intent.putExtra("gender", (String)jobj.get("gender"));
                if (jobj.get("image") != null) intent.putExtra("image", (String)jobj.get("image"));
                startActivity(intent);
                //Util.goIntent(intent);
                //Util.goIntent(LoginActivity.this, UserActivity.class);
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
           setLoading(false);
        }
        if (urunSorgulari.GetToken().isEmpty()) {
            Util.alertError(LoginActivity.this)
                .setText("Giriş başarısız.")
                .show();
        } else {
            //Util.toast(LoginActivity.this, "Giriş Başarılı. Token: " + token);
            //Util.toast(LoginActivity.this, "Giriş Başarılı.");
            //Util.goIntent(LoginActivity.this, UserActivity.class);
        }
    }

   public void buttonGirisYapTikla(){
       String kullanici = tasarimLogin.editKullanici.getText().toString().trim();
       String sifre = tasarimLogin.editPass.getText().toString().trim();

       if (kullanici.isEmpty()) {
           Util.alertError(LoginActivity.this).setText("Lütfen kullanıcı adınızı giriniz.").show();
           tasarimLogin.editKullanici.requestFocus();
           return;
       }
       if (sifre.isEmpty()) {
           Util.alertError(LoginActivity.this).setText("Lütfen şifrenizi giriniz.").show();
           tasarimLogin.editPass.requestFocus();
           return;
       }
       setLoading(true);
       login(kullanici, sifre);


    }

    public void txtSifremiUnuttumTikla(){
       // Util.goIntent(LoginActivity.this, ForgetPasswordActivity.class);
    }
}