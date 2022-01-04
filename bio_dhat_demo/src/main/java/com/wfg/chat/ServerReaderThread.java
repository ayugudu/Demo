package com.wfg.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerReaderThread extends  Thread{
   private Socket socket;

   public ServerReaderThread(Socket socket){
       this.socket=socket;
   }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             String  msg =null;
             while((msg=br.readLine())!=null){
                 // 给所有客户端发送消息
               sendMsgToAllClient(msg);
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把当前客户端的消息推送给全部在线的socket
     */
    private void sendMsgToAllClient(String msg){
      for(Socket s :Server.allSocket){
          try {
              PrintStream ps = new PrintStream(s.getOutputStream());
              ps.println(msg);
              ps.flush();
          } catch (IOException e) {
              System.out.println("当前有人下线");
              Server.allSocket.remove(s);
          }
      }
    }
}

