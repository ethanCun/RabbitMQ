package com.ethan.rabbitmq.rabbitmq.workqueues;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
//@RabbitListener(queuesToDeclare = {@Queue("workqueues")})
public class Consumer02 {

//    @RabbitHandler
    public void receive(String msg){

        System.out.println("workqueues consumer2：" + msg);
    }
}