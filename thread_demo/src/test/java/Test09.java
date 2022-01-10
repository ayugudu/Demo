import lombok.extern.log4j.Log4j2;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家就餐问题
 */
public class Test09 {

    public static void main(String[] args) {
        Chopstick2 c1 = new Chopstick2("1");
        Chopstick2 c2 = new Chopstick2("2");
        Chopstick2 c3 = new Chopstick2("3");
        Chopstick2 c4 = new Chopstick2("4");
        Chopstick2 c5 = new Chopstick2("5");
        new Philosopher2("苏格拉底", c1, c2).start();
        new Philosopher2("柏拉图", c2, c3).start();
        new Philosopher2("亚里士多德", c3, c4).start();
        new Philosopher2("赫拉克利特", c4, c5).start();
        new Philosopher2("阿基米德", c5, c1).start();
    }


}

//就餐所使用的筷子
class Chopstick2 extends ReentrantLock {
    String name;

    public Chopstick2(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}

@Log4j2
//哲学家
class Philosopher2 extends  Thread{
    Chopstick2 left;
    Chopstick2 right;

    public Philosopher2(String name,Chopstick2 left, Chopstick2 right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    private void eat(){
        log.debug("eat ting");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            //  尝试获取左手筷子
            if(left.tryLock()){
                //加锁成功
                try{
                    //临界区
                    //尝试获取右手筷子
                    if(right.tryLock()){
                        try{
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }

                }finally {
                    left.unlock();
                }

            }
        }
    }
}