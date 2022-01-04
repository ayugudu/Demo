package com.wfg.bio_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunnableTarget implements  Runnable {
    private Socket socket;

    public ServerRunnableTarget(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
    // 处理接受到的socket
        //3 获取字节输入流
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            // 包装成缓冲字符输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String msg ;
            while((msg= br.readLine())!=null){
                System.out.println("服务器接受到：" + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
