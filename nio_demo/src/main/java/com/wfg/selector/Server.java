package com.wfg.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    public static void main(String[] args) throws IOException {
        //1 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //2 切换为非阻塞模式
        ssChannel.configureBlocking(false);
        //3 绑定连接的端口
         ssChannel.bind(new InetSocketAddress(9999));

         //4 获取选择器Selector
        Selector selector =Selector.open();
        //5 将通道注册到选择器上 ，并开始指定接受事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6 使用selector 选择器轮询已经就绪好的事件
        while(selector.select()>0){
            //7 获取选择器中所有注册通道中已经就绪好的事件
            Iterator<SelectionKey> it =  selector.selectedKeys().iterator();
            //8 开始便利就绪好的事件
            while(it.hasNext()){
                //提取当前这个事件
                SelectionKey sk  = it.next();
                //判断这个事件是什么
                if(sk.isAcceptable()){
                // 直接获取当前接入的客户端通道
                    SocketChannel socketChannel = ssChannel.accept();
                    // 切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    //注册到选择器中
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else if(sk.isReadable()){
                    // 获取当前选择器上的读选择器就绪事件
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    //读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len=0;
                    while((len=socketChannel.read(buf))>0){
                        buf.flip();
                        System.out.println(new String(buf.array(),0,len));
                        buf.clear();
                    }
                }
                it.remove();//移除当前事件
            }
        }

    }
}
