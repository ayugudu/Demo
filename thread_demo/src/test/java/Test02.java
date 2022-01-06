
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

@Log4j2
public class Test02 {

    Thread monitor;

    //启动监控线程
    public void start(){
        monitor = new Thread(() -> {
            while(true){
                //判断当前线程是否被打断
                Thread currentThread  = Thread.currentThread();
                if(currentThread.isInterrupted()){
                    // 释放资源 结束线程
                    log.debug("有其余线程通知你要结束了....请释放资源");
                    break;
                }
                try {
                    Thread.sleep(5000);
                    log.debug("阻塞中被打断了.....");
                } catch (InterruptedException e) {
                   //因为sleep出现异常后，会清楚打断标记
                    //需要重新设置打断标记
                  currentThread.interrupt();
                }

            }

        });
        //启动线程
        monitor.start();

    }


    //停止监控线程
    public void stop(){
     //打断线程
      monitor.interrupt();
    }

  @Test
    public void t1() throws InterruptedException {
        start();
        Thread.sleep(1000);
        stop();
  }


  @Test
    public void t2(){

        // TIMED_WAITING状态
      Thread t1=  new Thread(()->{

          synchronized (Test02.class){
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }

        },"t1");

      t1.start();


        // WAITING状态
     Thread t2= new Thread(()->{
          try {
              //等待 t1 线程执行完毕
              t1.join();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      },"t2");
     t2.start();


      //BLOCKED状态
     Thread t3= new Thread(()->{
          //被t1加锁了，拿不到锁，进入blocked
          synchronized (Test02.class){

          }
      },"t3");
     t3.start();


      log.debug("t1-----{}",t1.getState());
      log.debug("t2-----{}",t2.getState());
      log.debug("t3-----{}",t3.getState());
  }
}
