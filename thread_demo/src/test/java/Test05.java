import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;

public class Test05 {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);

        for(int i =0;i<3;i++){
            int id =i;
            new Thread(()->{
                queue.put(new Message(id,"值"+id));
            },"生产者"+id).start();
        }
        new Thread(()->{
            while(true){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.get();
            }

        },"消费者").start();
    }



}
@Log4j2
//线程之间的消息队列：生产者/消费者
class MessageQueue{
    //队列容量
    int capacity;
    private LinkedList<Message> queue = new LinkedList<>();

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // 获取消息
    public Message get(){
        synchronized (queue){
        //当队列不为空时可以消费,为空时则进入等待阻塞，等生产者将其唤醒
        while(queue.isEmpty()){

                try {
                    log.debug("队列为空，等待生产");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //从队列的头部获取消息并返回
            Message message= queue.removeFirst();
            log.debug("已消费message{}",message);
            // 唤醒生产者
            queue.notifyAll();
            return  message;

        }

    }

    // 生产消息
    public void put(Message message){
       synchronized (queue){
           //当队列满时则进入阻塞状态
           while(queue.size()==capacity){
               try {
                   log.debug("队列已满，等待消费");
                   queue.wait();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
           log.debug("已生产消息{}",message);
           // 生产消息
           queue.addLast(message);
           //唤醒消费者
           queue.notifyAll();
       }
    }
}




class Message{
 int id;
 String value;

    public Message(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}