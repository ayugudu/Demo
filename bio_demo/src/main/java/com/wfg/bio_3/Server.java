package com.wfg.bio_3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同时接受多个客户端socket通信请求
 * 接受一个客户但socket请求对象后，交给一个独立
 */
public class Server {


    public static void main(String[] args) {
        ServerSocket ss;
        {
            try {
                // 注册端口
                ss = new ServerSocket(9999);
                while(true){
                    Socket socket =ss.accept();
                    // 创建一个独立的线程来处理与客户端连接的请求
                    new ServerThreadReader(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
