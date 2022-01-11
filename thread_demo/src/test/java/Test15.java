import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 原子引用 ABA 问题解决
 */
@Log4j2
public class Test15 {

    static AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("A",0);

    public static void main(String[] args) throws InterruptedException {

        //获取值
        String prev=atomicStampedReference.getReference();
        //获取版本号
         int stamp =atomicStampedReference.getStamp();
        //其余线程更新版本号:A->B->A
         other();
        Thread.sleep(1000);
        //尝试更新:比较 版本/值 是否相同，相同则更新
        log.debug("change A->C {}",atomicStampedReference.compareAndSet(prev,"c",stamp,stamp+1));


    }
    //其余线程更新为 A-B-A
    public static void other(){
        new Thread(()->{
            // 获取版本号
            int stamp = atomicStampedReference.getStamp();
            log.debug("change A->B {}",atomicStampedReference.compareAndSet(atomicStampedReference.getReference(),"B",stamp,stamp+1));
        }).start();
        new Thread(()->{
            //获取版本号
            int stamp =atomicStampedReference.getStamp();
            log.debug("change B->C{}",atomicStampedReference.compareAndSet(atomicStampedReference.getReference(),"C",stamp,stamp+1));
        }).start();
    }

}
