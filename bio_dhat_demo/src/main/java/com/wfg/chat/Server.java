package com.wfg.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现的需求
 * 1 注册端口
 * 2 接受客户端的socket连接，交给一个独立的线程处理
 * 3 把当前连接的客户端socket存入到在线socket集合中播哦村
 * 4 接收客户端消息，推送给当前所有在线的socket接受
 */
public class Server {
 // 定义一个静态集合
    public static List<Socket>  allSocket = new ArrayList<>();
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(9999);
            while(true){
                Socket socket = ss.accept();
                //把登录的客户端socket 存入到集合中
                allSocket.add(socket);
                //为当前登录的socket分配一个独立的线程来处理通信
               new ServerReaderThread(socket).start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
