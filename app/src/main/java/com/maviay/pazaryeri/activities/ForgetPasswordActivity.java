package com.maviay.pazaryeri.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import com.maviay.pazaryeri.Base.BaseActivity;
import com.maviay.pazaryeri.R;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.edtPhoneForgetPass)
    MaskedEditText edtPhoneForgetPass;
    @BindView (R.id.btnSifreGonder)
    Button btnSifreGonder;
    @BindView(R.id.txtYadaGirisYap)
    TextView txtYadaGirisYap;
    Context context = this;
    private static final String TAG = "ForgetPasswordActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        edtPhoneForgetPass.setOnEditorActionListener(editorListener);
    }

    protected int getLayoutResourceId(){
        return R.layout.activity_forget_password;
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo
                        .IME_ACTION_DONE:
                    break;
            }
            return false;
        }
    };
}