import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import static java.lang.Thread.sleep;

@Log4j2
public class Test06 {
    // 用于测试活锁
   static volatile int   count=10;
    public static void main(String[] args) {
        Test06 t = new Test06();
        t.t3();
    }
   // 死锁测试
    public void t(){
      Object a = new Object();
      Object b = new Object();

    // 线程1 需要获取多把锁
     Thread t1= new Thread(()->{
          synchronized (a){
              log.debug("获得了A锁");
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              synchronized (b){
                  log.debug("获得了B锁");
              }
          }
      });

    // 线程2 需要获取多把锁
      Thread t2=  new Thread(()->{
            synchronized (b){
                log.debug("获得了b锁");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (a){
                    log.debug("获得了a锁");
                }
            }
        });

      t1.start();
      t2.start();
    }
  // 死锁测试
    public void t2(){
        Object a = new Object();
        Object b = new Object();
        // 线程1 需要获取多把锁
        new Thread(()->{
            synchronized (a){
                log.debug("获得了a锁");
                synchronized (b){
                    log.debug("获得了b锁");
                }
            }
        }).start();

        // 线程2 需要获取多把锁
        new Thread(()->{
            synchronized (b){
                log.debug("获得了b锁");
                synchronized (a){
                    log.debug("获得了a锁");
                }
            }
        }).start();
    }

   // 活锁测试
    @Test
   public void t3(){
     new Thread(()->{
         //期望小于等于0结束线程
         while(count>0){
             try {
                 sleep(2);
                 count--;
                 log.debug("count{}",count);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }).start();


       new Thread(()->{
           // 期望超过20结束线程
           while(count<20){
               try {
                   sleep(2);
                   count++;
                   log.debug("count{}",count);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }).start();

   }

}


