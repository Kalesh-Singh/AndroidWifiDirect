package com.techexchange.mobileapps.wifidirectlab;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    private void initializeComponents() {
        onOffButton = findViewById(R.id.onOff);
        discoverButton = findViewById(R.id.discover);
        sendButton = findViewById(R.id.sendButton);
        listView = findViewById(R.id.peerListView);
        readMsgTextView = findViewById(R.id.readMsg);
        connectionStatusTextView = findViewById(R.id.connectionStatus);
        writeMsgEditText = findViewById(R.id.writeMsg);
    }
}
