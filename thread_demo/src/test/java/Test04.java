import lombok.extern.log4j.Log4j2;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Log4j2
public class Test04 {

    public static void main(String[] args) {
        GuardedObject<Integer> guardedObject = new GuardedObject();

        new Thread(()->{
          log.debug("等待结果");
          Integer a =guardedObject.get(2000);
          System.out.println(a);
        },"t1").start();

       new Thread(()->{
           log.debug("产生结果");
           try {
               Thread.sleep(5000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           guardedObject.complete(new Random().nextInt(10));
       },"t2").start();

    }


}

class MailBoxes{


    private static Map<Integer,GuardedObject> boxes = new Hashtable<>();


    private static int id =1;

    //产生唯一id
    private static synchronized int generateId(){
        return  id++;
    }


    public static  GuardedObject createGuardedObject(){
        GuardedObject<String> go = new GuardedObject<String>(generateId());
         boxes.put(go.getId(), go) ;
         return  go;
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }
    public static GuardedObject getGuardedObject(Integer id){
        return  boxes.get(id);
    }
}


class GuardedObject <T>{
  private int id;

    public int getId() {
        return id;
    }

    public GuardedObject(int id) {
        this.id = id;
    }

    public GuardedObject() {
    }

    private T response;
    // 获取结果
    public T get(){
        synchronized (this){
            while(response==null){
                try{
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return  response;
    }


    //获取结果，超时 自动放弃
    public T get(long time) {
        synchronized (this){
            //获取当前时间
           long begin = System.currentTimeMillis();
           long tmp=0;
            while(response==null){
                // 判断是否超时
                if(tmp>=time){
                   break;
                }
                try {
                    this.wait(time-tmp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tmp = System.currentTimeMillis()-begin;
            }
         }
         return  response;


    }

    // 产生结果
    public void complete(T response){
         synchronized(this){
             //给结果赋值
             this.response=response;
             this.notifyAll();
         }

    }



}
