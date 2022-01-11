import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 用于测试线程间的执行顺序
 */
@Log4j2
public class Test10 {

    public static void main(String[] args){
     // 测试 同步（wait）
        Test10  test10= new Test10();
//        test10.t();
        test10.t1();
    }

    // 固定运行顺序执行 （wait ， notify）
    //先执行线程二 在执行线程一
    public void t(){
        // 用于同步的对象
        Object o= new Object();
        AtomicBoolean isT2 = new AtomicBoolean(false);
        Thread t1= new Thread(()->{
            synchronized (o){
                // 线程二还未执行 陷入wait
                while(!isT2.get()){
                    log.debug("t1等待t2执行完....");
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("执行线程t1");
            }
        },"t1");


        Thread t2 = new Thread(()->{
            synchronized (o){
                isT2.set(true);
                log.debug("执行线程二");
                // 唤醒线程一
                o.notifyAll();
            }

        },"t2");


        t1.start();
        t2.start();

    }


    // 固定运行顺序 （park，unpark版）
    public void t1(){

         Thread t1 = new Thread(() -> {
             //当前线程暂停
             LockSupport.park();
             log.debug("t1线程运行");
        },"t1");

         t1.start();

          new Thread(()->{
              //唤醒t1 线程
              log.debug("t2线程执行");
              LockSupport.unpark(t1);
          },"t2").start();
    }
}
