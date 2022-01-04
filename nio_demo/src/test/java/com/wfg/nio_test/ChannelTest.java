package com.wfg.nio_test;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {
    @Test
   public void read() throws IOException {
       //定义一个文件输入流与源文件接同
       FileInputStream fis = new FileInputStream("F:\\360MoveData\\Users\\DELL\\Desktop\\server\\新建 文本文档.txt");
       //需要得到文件字节输入流的文件通道
       FileChannel channel = fis.getChannel();
       //定义一个缓冲区
       ByteBuffer buffer = ByteBuffer.allocate(1024);
       //读取数据到缓冲区
       channel.read(buffer);
       
       buffer.flip();
       String rs = new String(buffer.array(),0,buffer.remaining());
       System.out.println(rs);
   }

    @Test
    public void write(){
        try {
            // 字节输出流通向目标文件
            FileOutputStream fos = new FileOutputStream("F:\\360MoveData\\Users\\DELL\\Desktop\\server\\新建 文本文档.txt");
           //2 得到字节输出流对应的通道 channel
            FileChannel channel = fos.getChannel();
            //3 分配缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("hello".getBytes());
            //4把缓冲区切换成写出模式
            buffer.flip();
            channel.write(buffer);
            channel.close();
            System.out.println("数据写出文件");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void copy() throws IOException {
       // 定义文件位置
        File srcFile = new File("F:\\360MoveData\\Users\\DELL\\Desktop\\下载.png");
        File copyFile = new File("F:\\360MoveData\\Users\\DELL\\Desktop\\下载_copy.png");
       //获取文件输入流
        FileInputStream fis = new FileInputStream(srcFile);
        //获取文件输出流
        FileOutputStream fos = new FileOutputStream(copyFile);
        //获取文件通道
        FileChannel fileChannel = fis.getChannel();
        FileChannel  copyChannel =  fos.getChannel();
        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);


        while(true){
           // 必需先清空缓冲区
            buffer.clear();
            //写入数据到缓冲区
           int len= fileChannel.read(buffer);
           if(len<0){
             break;
           }
           //切换模式为可读模式
            buffer.flip();
            copyChannel.write(buffer);
        }
           fileChannel.close();
           copyChannel.close();
           System.out.println("复制文件成功！");

   }

   @Test
    public void copy_trans() throws IOException {
       // 定义源文件位置
       FileInputStream srcFile = new FileInputStream("F:\\360MoveData\\Users\\DELL\\Desktop\\下载.png");
       FileChannel channel=srcFile.getChannel();
       FileOutputStream copyFile = new FileOutputStream("F:\\360MoveData\\Users\\DELL\\Desktop\\下载_copy.png");
       FileChannel copyChannel = copyFile.getChannel();
       //复制文件
       // copyChannel.transferFrom(channel,channel.position(),channel.size());
        channel.transferTo(channel.position(),channel.size(),copyChannel);
       // 关闭原通道
       channel.close();
       //关闭现通道
       copyChannel.close();;




   }
}
