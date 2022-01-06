
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Log4j2
public class Test01 {

@Test
    public void t() throws ExecutionException, InterruptedException {

    //需要创建 Callable 实现里面的call方法
    FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
            log.debug("running....");
            Thread.sleep(1000);
            return 1000;
        }
    });

    // 创建线程： FutureTask实现了runnable
    Thread t1 = new Thread(task,"t1");
    //启动线程
    t1.start();

    //异步获取线程结果
    log.debug("{}",task.get());
}

@Test
    public void t2() throws InterruptedException {
    Thread t2= new Thread(()->{
         try {
             log.debug("睡眠一秒");
             Thread.sleep(2000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     },"t2");

    t2.start();

   Thread.sleep(1000);
   log.debug("interrupt");
   t2.interrupt();

}







}
