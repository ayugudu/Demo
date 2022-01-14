import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Log4j2
public class Test18 {
    public static void main(String[] args) {
        MyLock lock = new MyLock();

        new Thread(()->{
          lock.lock();
          try {
              log.debug("加锁....");
              Thread.sleep(1000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          } finally {
              log.debug("解锁....");
              lock.unlock();
          }
        },"t1").start();

        new Thread(()->{
            lock.lock();
            try {
                log.debug("加锁....");
            }finally {
                log.debug("解锁....");
                lock.unlock();
            }
        },"t2").start();
    }

}

/**
 * 自定义锁 不可重入锁
 */
class MyLock implements Lock {

    // 独占锁 同步器类
    class MySync extends AbstractQueuedSynchronizer{

        @Override
        protected boolean tryAcquire(int arg) {
           if(compareAndSetState(0,1)){
               // 加上了锁,并设置owner为当前线程
               setExclusiveOwnerThread(Thread.currentThread());
               return  true;
           }
           return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            // state 是volitate 放在后面 加写屏障
            setState(0);
            return  true;
        }

        @Override // 是否持有独占锁
        protected boolean isHeldExclusively() {
            return getState()==1;
        }

        public Condition newCondition(){
            return  new ConditionObject();
        }
    }


     private MySync sync = new MySync();

    @Override // 加锁（不成功则进行等待）
    public void lock() {
        sync.acquire(1);
    }

    @Override  // 加锁，可打断
    public void lockInterruptibly() throws InterruptedException {
         sync.acquireInterruptibly(1);
    }

    @Override  // 尝试加锁 （一次）
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override // 尝试加锁，带超时
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override // 解锁
    public void unlock() {
          sync.release(1);
    }

    @Override //创建条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }
}