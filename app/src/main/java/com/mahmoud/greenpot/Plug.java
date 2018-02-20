package com.mahmoud.greenpot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class Plug extends AppCompatActivity {

    private static String MQTTHOST /* = "tcp://m23.cloudmqtt.com:13207" */;
    private static String USERNAME /* = "ruafzbed" */;
    private static String PASSWORD /* = "QK7Hde2Drz2r" */;

    private MqttAndroidClient client;

    @BindView(R.id.tv_consumed_power_value)
    TextView tvConsumedPowerValue;

    @BindView(R.id.switch_first)
    Switch switchFirst;

    @BindView(R.id.switch_second)
    Switch switchSecond;

    @BindView(R.id.switch_third)
    Switch switchThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        MQTTHOST = intent.getStringExtra("MQTTHOST");
        USERNAME = intent.getStringExtra("USERNAME");
        PASSWORD = intent.getStringExtra("PASSWORD");

        String clientId = MqttClient.generateClientId();

        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://" + MQTTHOST, clientId);

        IMqttToken iMqttToken;
        try {
            if (USERNAME.compareTo("") == 0 || PASSWORD.compareTo("") == 0) {
                iMqttToken = client.connect();
            } else {
                MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                mqttConnectOptions.setUserName(USERNAME);
                mqttConnectOptions.setPassword(PASSWORD.toCharArray());

                iMqttToken = client.connect(mqttConnectOptions);
            }
            iMqttToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Plug.this, "CONNECTED", Toast.LENGTH_LONG).show();
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(Plug.this, "FAILED!!!", Toast.LENGTH_LONG).show();

                    Intent intent1 = new Intent(Plug.this, MqttConnect.class);
                    startActivity(intent1);
                }
            });

        } catch (MqttException exception) {
            exception.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String power = new String(message.getPayload());
                if (power.substring(0, 4).equalsIgnoreCase("Power"))
                    tvConsumedPowerValue.setText(power.substring(5));

                if (new String(message.getPayload()).equals("1"))
                    switchFirst.setChecked(true);
                else
                    switchFirst.setChecked(false);

                if (new String(message.getPayload()).equals("2"))
                    switchSecond.setChecked(true);
                else
                    switchSecond.setChecked(false);

                if (new String(message.getPayload()).equals("3"))
                    switchThird.setChecked(true);
                else
                    switchThird.setChecked(false);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // disconnect();
    }

    private void publish(String message) {
        String topic = "Plug";

        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribe() {
        String topic = "PlugPub";

        try {
            client.subscribe(topic, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            IMqttToken disconnectToken = client.disconnect();
            disconnectToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Plug.this, "DISCONNECTED", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @OnCheckedChanged(R.id.switch_first)
    public void onCheckedChanged1(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            publish("1");
        } else {
            publish("1");
        }
    }

    @OnCheckedChanged(R.id.switch_second)
    public void onCheckedChanged2(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            publish("2");
        } else {
            publish("2");
        }
    }

    @OnCheckedChanged(R.id.switch_third)
    public void onCheckedChanged3(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            publish("3");
        } else {
            publish("3");
        }
    }
}
