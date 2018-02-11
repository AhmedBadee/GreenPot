package com.mahmoud.greenpot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignIn extends AppCompatActivity {

    @BindView(R.id.tv_username)
    TextView tvUsername;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.tv_password)
    TextView tvPassword;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }
}
