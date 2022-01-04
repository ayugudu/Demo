package com.wfg.bio_4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 开发实现伪异步 通信架构
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(9999);
            //定义循环来接受客户端的socket编程请求
            //初始化一个线程池对象
            HandlerSocketServerPool socketServerPool = new HandlerSocketServerPool(10,6);
            while (true){
                Socket socket =ss.accept();
              // 把socket对象交给一个线程池进行处理
                //把socket封装成一个任务对象交给线程处理
                Runnable target = new ServerRunnableTarget(socket);
                socketServerPool.execute(target);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
