package com.ethan.rabbitmq.rabbitmq.workqueues;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer01 {

    @RabbitListener(queuesToDeclare = {@Queue("work")})
    public void receive1(Object msg){

        System.out.println("队列消费 consumer1：" + msg);
    }

    @RabbitListener(queuesToDeclare = {@Queue("work")})
    public void receive2(Object msg){

        System.out.println("队列消费 consumer2：" + msg);
    }
}
