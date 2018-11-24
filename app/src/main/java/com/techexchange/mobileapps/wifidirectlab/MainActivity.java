package com.techexchange.mobileapps.wifidirectlab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
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
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;

    BroadcastReceiver receiver;
    IntentFilter intentFilter;

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

    @Override
    protected void onResume() {
        super.onResume();

        // Register the broadcast receiver
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the broadcast receiver
        unregisterReceiver(receiver);
    }

    private void initializeComponents() {
        onOffButton = findViewById(R.id.onOff);
        discoverButton = findViewById(R.id.discover);
        sendButton = findViewById(R.id.sendButton);
        listView = findViewById(R.id.peerListView);
        readMsgTextView = findViewById(R.id.readMsg);
        connectionStatusTextView = findViewById(R.id.connectionStatus);
        writeMsgEditText = findViewById(R.id.writeMsg);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            onOffButton.setText("Wifi off");
        }

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(wifiP2pManager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }
}
