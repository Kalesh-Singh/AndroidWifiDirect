package com.techexchange.mobileapps.wifidirectlab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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

    List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

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

        discoverButton.setOnClickListener(v -> {
            wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Successfully started discovering
                    connectionStatusTextView.setText("Discovery Started");
                }

                @Override
                public void onFailure(int reason) {
                    // Failed to start discovering
                    connectionStatusTextView.setText("Discovery failed to Start");
                }
            });
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final WifiP2pDevice device = deviceArray[position];
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;

            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(getApplicationContext(),
                            "Connection failed", Toast.LENGTH_SHORT).show();
                }
            });
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

    WifiP2pManager.PeerListListener peerListListener = peerList -> {
        if (!peerList.getDeviceList().equals(peers)) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            deviceNameArray = new String[peers.size()];
            deviceArray = new WifiP2pDevice[peers.size()];

            int index = 0;
            for (WifiP2pDevice device : peers) {
                deviceNameArray[index] = device.deviceName;
                deviceArray[index] = device;
                index++;
            }

            ArrayAdapter<String> arrayAdapter
                    = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, deviceNameArray);

            listView.setAdapter(arrayAdapter);

            if (peers.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        "No Device Found!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                connectionStatusTextView.setText("Host");
            } else if (info.groupFormed) {
                connectionStatusTextView.setText("Client");
            }
        }
    };

}
