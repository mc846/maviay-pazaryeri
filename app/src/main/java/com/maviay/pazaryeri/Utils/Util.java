package com.maviay.pazaryeri.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.maviay.pazaryeri.activities.LoginActivity;
import com.maviay.pazaryeri.Enum.INPUT;
import com.maviay.pazaryeri.R;
import com.tapadoo.alerter.Alerter;

public class Util {

    public static void goMain(Context from) {
        from.startActivity(new Intent(from, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public static void goIntent(Context a,Class b){
        Intent intent = new Intent(a,b);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivity(intent);
    }

    public static boolean inputFilter(EditText editText, INPUT type) {

        if (editText == null) {
            return false;
        }

        String s = editText.getText().toString().trim();

        if (s.length() <= 0) {
            return false;
        }

        if (type == INPUT.PHONE) {
            s = s.replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
            if (s.length() != 10) {
                return false;
            }
            if (s.charAt(0) != '5') {
                return false;
            }
        }

        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void toast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void uyari(Context context, String title) {
        AlertDialog.Builder alertDialogPassword = new AlertDialog.Builder(context);
        alertDialogPassword.setTitle("UYARI!")
                .setMessage(title)
                .setCancelable(false)
                .setIcon(R.drawable.warning)
                .setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static Alerter alertError(Activity activity){
        return Alerter.create(activity)
                .setBackgroundColorRes(R.color.colorRed)
                .setIcon(R.drawable.logo_icon_png)
                .setTitle("Hata")
                .setText("Giriş Başarısız");
    }

}