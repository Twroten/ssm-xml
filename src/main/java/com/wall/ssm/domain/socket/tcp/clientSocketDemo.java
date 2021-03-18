package com.wall.ssm.domain.socket.tcp;

import lombok.Cleanup;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class clientSocketDemo {

    @Test
    public void testClient() throws IOException {
        @Cleanup
        Socket clientSocket = new Socket("127.0.0.1", 8090);
        @Cleanup
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write("Hello 世界!".getBytes(StandardCharsets.UTF_8));
        System.out.println("传输完成！");

    }
}
