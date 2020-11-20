package com.ethan.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

//演示队列消费者
@Component
public class ConsumerDelay {

    //注意消费是在死信队列里面进行消费， 生产是在延时队列里面生产
    @RabbitListener(queuesToDeclare = @Queue("my-dlx-queue"))
    public void delayReceive(Object msg){

        System.out.println("延时队列： " + msg);
        System.out.println("接受成功： " + new Date());
    }
}
