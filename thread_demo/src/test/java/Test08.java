import lombok.extern.log4j.Log4j2;

/**
 * 哲学家就餐问题
 */
public class Test08 {

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }


}

//就餐所使用的筷子
class Chopstick{
    String name;

    public Chopstick(String name) {
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
class Philosopher extends  Thread{
    Chopstick left;
    Chopstick right;

    public Philosopher(String name,Chopstick left, Chopstick right) {
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
            // 获得右手筷子
            synchronized (right){
                // 获得左手筷子
                synchronized (left){
                    //就餐
                    eat();
                }
                //放下左手筷子
            }
            // 放下右手筷子
        }
    }
}