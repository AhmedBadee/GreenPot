package com.mahmoud.greenpot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import butterknife.OnLongClick;

public class Plug extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
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

    @BindView(R.id.device_first)
    TextView deviceFirst;

    @BindView(R.id.device_second)
    TextView deviceSecond;

    @BindView(R.id.device_third)
    TextView deviceThird;

    private EditText newDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        MQTTHOST = intent.getStringExtra("MQTTHOST");
        USERNAME = intent.getStringExtra("USERNAME");
        PASSWORD = intent.getStringExtra("PASSWORD");

        String clientId = MqttClient.generateClientId();

        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://" + MQTTHOST, clientId);

        new MqttConnectTask(Plug.this).execute();
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

        if (client.isConnected()) {
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
    }

    @OnCheckedChanged(R.id.switch_first)
    public void onCheckedChanged1(boolean isChecked) {
        if (isChecked) {
            publish("One is on");
        } else {
            publish("One is off");
        }
    }

    @OnCheckedChanged(R.id.switch_second)
    public void onCheckedChanged2(boolean isChecked) {
        if (isChecked) {
            publish("Two is on");
        } else {
            publish("Two is off");
        }
    }

    @OnCheckedChanged(R.id.switch_third)
    public void onCheckedChanged3(boolean isChecked) {
        if (isChecked) {
            publish("Three is on");
        } else {
            publish("Three is off");
        }
    }

    @OnLongClick(R.id.device_first)
    public boolean onLongClickDeviceFirst() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.device_change_dialog, null);

        dialogBuilder.setTitle("Enter New Device Name: ");
        dialogBuilder.setView(dialogView);
        newDevice = dialogView.findViewById(R.id.new_device);
        dialogBuilder.setCancelable(false);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deviceFirst.setText(newDevice.getText().toString());
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return false;
    }

    @OnLongClick(R.id.device_second)
    public boolean onLongClickDeviceSecond() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.device_change_dialog, null);

        dialogBuilder.setTitle("Enter New Device Name: ");
        dialogBuilder.setView(dialogView);
        newDevice = dialogView.findViewById(R.id.new_device);
        dialogBuilder.setCancelable(false);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deviceSecond.setText(newDevice.getText().toString());
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return true;
    }

    @OnLongClick(R.id.device_third)
    public boolean onLongClickDeviceThird() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.device_change_dialog, null);

        dialogBuilder.setTitle("Enter New Device Name: ");
        dialogBuilder.setView(dialogView);
        newDevice = dialogView.findViewById(R.id.new_device);
        dialogBuilder.setCancelable(false);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deviceThird.setText(newDevice.getText().toString());
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class MqttConnectTask extends AsyncTask<Void, Boolean, Boolean> {

        private AlertDialog.Builder alertDialogBuilder;
        private AlertDialog alertDialog;

        private Context context;

        private MqttConnectTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Connecting...");
            alertDialogBuilder.setCancelable(false);

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            final Boolean[] connected = new Boolean[1];

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
                        connected[0] = true;
                        publishProgress(connected);
                        Toast.makeText(Plug.this, "CONNECTED", Toast.LENGTH_LONG).show();
                        subscribe();

                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable cause) {

                            }

                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                String power = new String(message.getPayload());
                                if (power.substring(0, 5).equals("Power"))
                                    tvConsumedPowerValue.setText(power.substring(5));

                                if (new String(message.getPayload()).equalsIgnoreCase("First is connected"))
                                    switchFirst.setChecked(true);
                                else if (new String(message.getPayload()).equalsIgnoreCase("First is disconnected"))
                                    switchFirst.setChecked(false);

                                if (new String(message.getPayload()).equalsIgnoreCase("Second is connected"))
                                    switchSecond.setChecked(true);
                                else if (new String(message.getPayload()).equalsIgnoreCase("Second is disconnected"))
                                    switchSecond.setChecked(false);

                                if (new String(message.getPayload()).equalsIgnoreCase("Third is connected"))
                                    switchThird.setChecked(true);
                                else if (new String(message.getPayload()).equalsIgnoreCase("Third is disconnected"))
                                    switchThird.setChecked(false);
                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(Plug.this, "FAILED!!!", Toast.LENGTH_LONG).show();

                        Intent intent1 = new Intent(Plug.this, MqttConnect.class);
                        startActivity(intent1);
                        finish();
                    }
                });

            } catch (MqttException exception) {
                exception.printStackTrace();
            }

            return connected[0];
        }

        @Override
        protected void onProgressUpdate(Boolean... connected) {
            if (connected[0]) {
                setContentView(R.layout.activity_plug);
                ButterKnife.bind((Activity) context);
            }
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            alertDialog.dismiss();
        }
    }
}
