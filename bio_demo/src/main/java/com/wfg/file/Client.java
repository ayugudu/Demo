package com.wfg.file;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        try{
            //请求于服务端的socket连接
           Socket socket = new Socket("127.0.0.1",8888);
           //将字节输出流包装成一个数据输出流
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            //上传后缀
            dataOutputStream.writeUTF(".png");
            //把文件数据发送给服务端进行接收
            InputStream is = new FileInputStream("F:\\360MoveData\\Users\\DELL\\Desktop\\下载.png");
            byte[] bs = new byte[1024];
            int len =0;
            while((len= is.read(bs))>0){
                dataOutputStream.write(bs,0,len);
            }
            dataOutputStream.flush();
            is.close();
            socket.shutdownOutput();//通知服务器这边数据发送完毕

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
