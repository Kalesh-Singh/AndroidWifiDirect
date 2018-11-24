package com.techexchange.mobileapps.wifidirectlab;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button onOffButton;
    Button discoverButton;
    Button sendButton;
    ListView listView;
    TextView readMsgTextView;
    TextView connectionStatusTextView;
    EditText writeMsgEditText;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        executeListener();
    }

    private void executeListener() {
        onOffButton.setOnClickListener(v -> {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
                onOffButton.setText("Wifi On");
            } else {
                wifiManager.setWifiEnabled(true);
                onOffButton.setText("Wifi off");
            }
        });
    }

    private void initializeComponents() {
        onOffButton = findViewById(R.id.onOff);
        discoverButton = findViewById(R.id.discover);
        sendButton = findViewById(R.id.sendButton);
        listView = findViewById(R.id.peerListView);
        readMsgTextView = findViewById(R.id.readMsg);
        connectionStatusTextView = findViewById(R.id.connectionStatus);
        writeMsgEditText = findViewById(R.id.writeMsg);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            onOffButton.setText("Wifi off");
        }
    }
}
