package com.wall.ssm.domain.socket.tcp;

import lombok.Cleanup;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer {

    @Test
    public void testFileTransServer() throws Exception {

        File file = new File("src/main/resources/documents/literature/regular_express.txt");
        @Cleanup
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        @Cleanup
        ServerSocket serverSocket = new ServerSocket(8090);
        @Cleanup
        Socket accept = serverSocket.accept();
        @Cleanup
        InputStream inputStream = accept.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        System.out.println("服务端接收成功！");

    }
}
