package com.wall.ssm.domain.socket.udp;

import lombok.Cleanup;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class SendUDPDemo {

    @Test
    public void testSendUdp() throws Exception {

        @Cleanup
        DatagramSocket datagramSocket = new DatagramSocket(6060);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String msg = bufferedReader.readLine();
            byte[] data = msg.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(data, 0, data.length,
                    new InetSocketAddress("127.0.0.1", 9090));
            datagramSocket.send(datagramPacket);
            if ("bye".equals(msg)) {
                break;
            }
        }
        datagramSocket.close();
    }
}
