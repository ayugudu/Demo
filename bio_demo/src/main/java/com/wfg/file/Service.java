package com.wfg.file;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Service {
    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(8888);
             while(true){
                 Socket socket =ss.accept();
                new ServerReaderThread(socket).start();
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
