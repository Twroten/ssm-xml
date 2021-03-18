package com.wall.ssm.domain.url;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLDemo {

    @Test
    public void testURL() throws MalformedURLException {

        URL url = new URL("http://linux.vbird.org/linux_basic/0330regularex/regular_express.txt");
        System.out.println("File==" + url.getFile());
        System.out.println("Host==" + url.getHost());
        System.out.println("Path==" + url.getPath());
        System.out.println("Port==" + url.getPort());
        System.out.println("Protocol==" + url.getProtocol());
        System.out.println("UserInfo==" + url.getUserInfo());
        System.out.println("Authority==" + url.getAuthority());
        System.out.println("Query==" + url.getQuery());

    }

    @Test
    public void URLDownloadDemo() throws Exception {
        URL url = new URL("https://m701.music.126.net/20210306191113/3d4046d49a75efc3bf2c9439381ef99d/jdyyaac/obj/w5rDlsOJwrLDjj7CmsOj/5546003514/e409/aed6/f93a/e0f802f6dbe22026d98c0129ec474324.m4a");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(new File("src/main/resources/documents/literature/错位时空.m4a"));
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
    }

    @Test
    public void testURLDownload() throws Exception {
        URL url = new URL("https://gitmind.cn/app/doc/89b654870");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(new File("src/main/resources/documents/JavaSource/Java开发路线.xmind"));
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        System.out.println("Download Complete!");

    }
}
