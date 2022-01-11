import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


@Log4j2
public class Test07 {
    // 创建锁资源
    ReentrantLock lock = new ReentrantLock();

    // 可重入测试
    @Test
    public void t() {
        //获取锁
        lock.lock();
        try {
            log.debug("entry t");
            t1();
        } finally {
            //释放锁
            lock.unlock();
        }


    }

    public void t1() {
        // 获取锁
        lock.lock();
        try {
            log.debug("entry t1");
        } finally {
            //释放锁
            lock.unlock();
        }

    }

    @Test
    // 可打断测试
    public void t2() {
        val thread = new Thread(() -> {
            try {
                //如果没有竞争，那么此方法就会获取lock对象锁
                //如果有竞争就会进入阻塞队列，可以被其他线程用interrupt方法打断
                log.debug("尝试获取锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                log.debug("没有获取锁");
                e.printStackTrace();
                return;
            }

            try {
                log.debug("获取锁");
            } finally {
                //释放锁
                lock.unlock();
            }
        }, "t2");

        thread.start();

        // 主线程获取锁
        lock.lock();

        log.debug("打断锁t2");
        thread.interrupt();

    }

    //锁超时测试
    public static void main(String[] args) {
        Test07 test07 = new Test07();
        try {
            test07.t3();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    //锁超时测试
    public void t3() throws InterruptedException {
        val thread = new Thread(() -> {
            log.debug("尝试获得锁");
            try {
                // 尝试获取锁，等待获取锁三秒钟，获取不到 则释放
                if (!lock.tryLock(3, TimeUnit.SECONDS)) {
                    log.debug("获取不到锁");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                //临界区
                log.debug("获得锁");
            } finally {
                lock.unlock();
            }
        });

        lock.lock();
        Thread.sleep(2000);
        thread.start();
        lock.unlock();
    }

    // 测试条件变量
    @Test
    public void t4() throws InterruptedException {
        // 创建一个新的条件变量（休息室）
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        //加锁
        lock.lock();
        //进入条件变量
        condition1.await();

        //唤醒
        condition1.signal();
    }

}
