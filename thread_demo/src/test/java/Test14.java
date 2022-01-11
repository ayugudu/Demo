import lombok.extern.log4j.Log4j2;

/**
 * 两阶段终止
 */
@Log4j2
public class Test14 {

    //终止标记 注意要保证变量的 可见性（volatile）
    private volatile  static  boolean stop= false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            while(true){
                if(stop){
                  log.debug("料理后事");
                  break;
                }
                /**
                 * 其余操作
                 */
               log.debug("正在运行");

            }
        }).start();

        //其他线程 开始打断
        Test14 t14 = new Test14();
        Thread.sleep(1000);
        t14.stop();
    }
    public  void stop(){
        stop =true;
    }
}
