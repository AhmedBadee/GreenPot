package com.mahmoud.greenpot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MqttConnect extends AppCompatActivity {

    @BindView(R.id.et_host)
    EditText etHost;

    @BindView(R.id.et_server_username)
    EditText etServerUsername;

    @BindView(R.id.et_server_password)
    EditText etServerPassword;

    private static String MQTTHOST;
    private static String USERNAME;
    private static String PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_connect);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_mqtt_connect)
    public void mqttConnect() {
        MQTTHOST = etHost.getText().toString();
        USERNAME = etServerUsername.getText().toString();
        PASSWORD = etServerPassword.getText().toString();

        if (MQTTHOST == null) {
            Toast.makeText(MqttConnect.this, "Please Enter the server URL", Toast.LENGTH_LONG).show();
        } else if (USERNAME == null) {
            USERNAME = "";
            PASSWORD = "";
        }

        Log.e("HOST", MQTTHOST);

        if (MQTTHOST != null || USERNAME != null || PASSWORD != null) {
            goToPlugActivity();
        }
    }

    private void goToPlugActivity() {
        Intent intent = new Intent(MqttConnect.this, Plug.class);
        intent.putExtra("MQTTHOST", MQTTHOST);
        intent.putExtra("USERNAME", USERNAME);
        intent.putExtra("PASSWORD", PASSWORD);
        startActivity(intent);
        finish();
    }
}
