package com.wfg.bio_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端反复接受消息
 */
public class Server {
    public static void main(String[] args) {
        try{
            //1 定义一个server socket 对象进行服务端的端口注册
            ServerSocket ss = new ServerSocket(9999);
            //2 监听客户端socket请求
            Socket socket = ss.accept();
            //3 获取字节输入流
            InputStream inputStream = socket.getInputStream();
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
