package com.wfg.t;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Scanner;

public class Test01 {

    @Test
    public void t(){
        //分配一个缓冲区，容量设置为10
        ByteBuffer buffer =ByteBuffer.allocate(10);
        //获取位置
        System.out.println(buffer.position());
        //获取界限位置
        System.out.println(buffer.limit());
        //获取缓冲容量
        System.out.println(buffer.capacity());
        //向缓冲区中添加数据
        buffer.put("wfg".getBytes());

        //切换可读数据
        buffer.flip();
        //获取位置
        System.out.println(buffer.position());
        //获取界限位置
        System.out.println(buffer.limit());
        //获取缓冲容量
        System.out.println(buffer.capacity());
    }


    @Test
    public void test(){
        Scanner sc = new Scanner(System.in);
        int a =sc.nextInt();
        int b= sc.nextInt();
        System.out.println(a+b);
    }
}
