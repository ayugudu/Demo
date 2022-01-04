package com.wfg.file;

import com.wfg.bio_3.ServerThreadReader;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class ServerReaderThread extends  Thread{
 private Socket socket;

 public ServerReaderThread(Socket socket){
     this.socket =socket;
 }

    @Override
    public void run() {

        try {
            //1 得到一个数据输入流获取客户端发过来的数据
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            //2 读取客户端发送过来的文件类型
            String suffix =dis.readUTF();
            // 定义一个字节输出管道负责把客户端发来的文件数据写出去
            OutputStream os = new FileOutputStream("F:\\360MoveData\\Users\\DELL\\Desktop\\server\\"+ UUID.randomUUID().toString()+suffix);

            byte[] bytes = new byte[1024];
            int len=0;
            while((len=dis.read(bytes))>0){
                 os.write(bytes,0,len);
            }
            os.flush();
            os.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
