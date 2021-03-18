package com.wall.ssm.domain.socket.udp;

import lombok.Cleanup;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RecvUDPDemo {
    @Test
    public void testRecvUDP() throws Exception {

        @Cleanup
        DatagramSocket datagramSocket = new DatagramSocket(9090);
        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
            datagramSocket.receive(datagramPacket);
            byte[] data = datagramPacket.getData();
            String msg = new String(data, 0, datagramPacket.getLength());
            if ("bye".equals(msg)) {
                System.out.println(msg);
                break;
            }
            System.out.println(msg);
        }
        datagramSocket.close();

    }
}
