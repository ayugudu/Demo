import javafx.concurrent.Worker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/***
 * 自定义线程池
 */

public class TestPool<T> {


}

@FunctionalInterface // 拒绝策略
interface  RejectPolicy<T>{
    void reject(BlockingQueue<T> queue,T task);
}
class ThreadPool{
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private int coreSize;

    //获取任务的超时时间
    private long timeOut;

    private TimeUnit timeUnit;

    private  RejectPolicy rejectPolicy;

    //执行任务
    public void execute(Runnable task){
            synchronized (workers){
                // 当任务没有超过coreSize时，直接交给worker对象执行
                // 如果任务数 超过coreSize时，加入任务队列暂存
                if(workers.size()<coreSize){
                    Worker worker = new Worker(task);
                    workers.add(worker);
                    worker.start();
                }else{
                    taskQueue.put(task);
                    //1 死等
                    //2 超时等待
                    //3 让调用者放弃任务执行
                    //4 让调用者抛出异常
                    //5 让调用者自己执行任务
                 taskQueue.tryPut(rejectPolicy,task);
                }
            }
    }

    public ThreadPool(int coreSize, long timeOut, TimeUnit timeUnit,int queueCapcity ,RejectPolicy rejectPolicy)
    {
        this.coreSize = coreSize;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
        this.taskQueue=new BlockingQueue<>(queueCapcity);
        this.rejectPolicy=rejectPolicy;
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            //1 当task不为空,执行任务
            //2 当task执行完毕
            while(task!=null||((task=taskQueue.poll(timeOut,timeUnit))!=null)){
                try{
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task=null;
                }
            }
            synchronized (workers){
                workers.remove(this);
            }
        }
    }

}

class BlockingQueue<T>{
    //1 任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //2 锁
    private ReentrantLock lock = new ReentrantLock();

    // 3生产者条件变量
    private Condition  fullWaitSet = lock.newCondition();

    //4 消费者条件变量
    private Condition emptyWaitSet =lock.newCondition();

    // 5 容量
    private int capcity;

    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    // 带超时的阻塞获取
    public T poll(long timeOut, TimeUnit unit){
        lock.lock();
        try{
            // 将 timout 统一转换为 纳秒
            long nanos = unit.toNanos(timeOut);
            while(queue.isEmpty()){
                try {

                    if(nanos<=0){
                        return null;
                    }
                    // 返回都是剩余时间
                    nanos =emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t= queue.removeFirst();
            fullWaitSet.signal();
            return  t;
        }finally {
            lock.unlock();
        }


    }

    // 阻塞获取
    public T take(){
        lock.lock();
        try{
            while(queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t=  queue.removeFirst();
            fullWaitSet.signal();
            return  t;
        }finally {
            lock.unlock();
        }
    }

    //阻塞添加
    public void put(T t){
        lock.lock();
        try{
            while(queue.size()==capcity){
                fullWaitSet.await();
            }
            queue.addLast(t);
            emptyWaitSet.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    // 超时阻塞添加
    public boolean offer(T t ,long timeout,TimeUnit timeUnit){
        lock.lock();
        try{
            long nanos= timeUnit.toNanos(timeout);
            while(queue.size()==capcity){
                fullWaitSet.await();
                if(nanos<=0){
                    return  false;
                }
                nanos = fullWaitSet.awaitNanos(nanos);
            }
            queue.addLast(t);
            emptyWaitSet.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return  true;
    }

    // 获取大小
    public int size(){
        lock.lock();
        try{
            return  queue.size();
        }finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
      lock.lock();
      try{
          // 判断队列是否已满
          if(queue.size()==capcity){
              rejectPolicy.reject(this,task);
          }else{
              queue.addLast(task);
              emptyWaitSet.signal();
          }
      }finally {
          lock.unlock();
      }

    }
}
