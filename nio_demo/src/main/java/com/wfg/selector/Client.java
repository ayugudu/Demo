package com.wfg.selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        //1 获取通道
        SocketChannel socketChannel =SocketChannel.open();
        //切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //分配缓冲区大小
        ByteBuffer buffer =ByteBuffer.allocate(1024);
        Scanner sc = new Scanner(System.in);
        while(true){
           String msg = sc.nextLine();
            buffer.put(("你好"+msg).getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
    }
}
