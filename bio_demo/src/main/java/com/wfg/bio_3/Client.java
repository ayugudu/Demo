package com.wfg.bio_3;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端逻辑
 */
public class Client {
    public static void main(String[] args) {

        try {
            //请求于服务器的socket对象连接
            Socket socket = new Socket("127.0.0.1",9999);
            //2 得到一个打印流
            PrintStream ps = new PrintStream(socket.getOutputStream());
            Scanner sc = new Scanner( System.in);
            while(sc.hasNext()){
                String msg= sc.nextLine();
                ps.println(msg);
                ps.flush();
            }

        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
