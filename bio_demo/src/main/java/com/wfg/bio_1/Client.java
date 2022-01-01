package com.wfg.bio_1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //1创建 socket 对象请求服务端连接
        Socket socket = new Socket("127.0.0.1",9999);
        //2 从SOcket 对象中获取一个字节输出流
        OutputStream os = socket.getOutputStream();
        // 3 把字节输出流包装成一个打印流
        PrintStream ps = new PrintStream(os);
        ps.println("hello 服务端 ");
        ps.flush();
    }
}
