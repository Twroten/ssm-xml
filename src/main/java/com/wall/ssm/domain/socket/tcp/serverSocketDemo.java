package com.wall.ssm.domain.socket.tcp;

import lombok.Cleanup;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSocketDemo {

    @Test
    public void testServer() throws Exception {
        @Cleanup
        ServerSocket serverSocket = new ServerSocket(8090);
        System.out.println("等待客户端🔗️连接...");
        @Cleanup
        Socket accept = serverSocket.accept();
        @Cleanup
        InputStream inputStream = accept.getInputStream();
        @Cleanup
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        System.out.println(byteArrayOutputStream.toString());
        Thread.sleep(5000);
        System.out.println("接受完成！");
    }
}
