package com.wfg.bio_2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端 多发模式
 */
public class Client {
    public static void main(String[] args) throws IOException {
        //1创建 socket 对象请求服务端连接
        Socket socket = new Socket("127.0.0.1",9999);
        //2 从SOcket 对象中获取一个字节输出流
        OutputStream os = socket.getOutputStream();
        // 3 把字节输出流包装成一个打印流
        PrintStream ps = new PrintStream(os);
        Scanner  sc = new Scanner(System.in);
        while (true){
            String msg = sc.nextLine();
            ps.println(msg);
            ps.flush();
        }

    }
}
