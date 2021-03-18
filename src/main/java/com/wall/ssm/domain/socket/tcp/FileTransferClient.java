package com.wall.ssm.domain.socket.tcp;

import lombok.Cleanup;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FileTransferClient {

    @Test
    public void testFileTransferClient() throws Exception {

        File file = new File("/home/wall/Documents/regular_express.txt");
        @Cleanup
        FileInputStream fileInputStream = new FileInputStream(file);
        @Cleanup
        Socket socket = new Socket("127.0.0.1", 8090);
        @Cleanup
        OutputStream outputStream = socket.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        System.out.println("发送成功！");
    }

}
