package com.techexchange.mobileapps.wifidirectlab;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientThread extends Thread {
    Socket socket;
    String hostAddress;

    public ClientThread(InetAddress hostAddress) {
        this.hostAddress = hostAddress.getHostAddress();
        socket = new Socket();
    }

    @Override
    public void run() {
        try {
            socket.connect(new InetSocketAddress(hostAddress, 8888), 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
