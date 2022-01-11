import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程交替执行
 */
public class Test12 {
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(()->{
           awaitSignal.print("a",a,b);
        }).start();

        new Thread(()->{
            awaitSignal.print("b",b,c);
        }).start();

        new Thread(()->{
            awaitSignal.print("c",c,a);
        }).start();


        //主线程首先唤醒 a线程
        awaitSignal.lock();
        try{
            System.out.println("开始");
            //唤醒a线程
            a.signal();
        }finally {
            awaitSignal.unlock();
        }
    }


}
class AwaitSignal extends ReentrantLock{
    private int loopNumber;
    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    //参数1 打印内容 ：参数2 进入那一间休息室， 参数3 下一间休息室
    public void print(String str, Condition current,Condition next){
        for(int i=0;i<loopNumber;i++){
            lock();
            try{
                //进入休息室等待被唤醒
                current.await();
                System.out.println(str);
                //唤醒下面要执行的线程
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }


}