import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Hashtable;

@Log4j2
public class Test03 {

    int num=0;

    public void add(){
        //临界区
        num++;

    }
    public void t(){
        // get()与put方法都是原子性的，但是放在一起并不是线程安全的
        Hashtable<String,String> table = new Hashtable();
        if (table.get("key")==null){
            table.put("key","value");
        }
    }
  int i=0;
    // jit 即使编译器
   public void t1(){
      Object o = new Object();
      synchronized (o){
          i++;
      }
   }

}
