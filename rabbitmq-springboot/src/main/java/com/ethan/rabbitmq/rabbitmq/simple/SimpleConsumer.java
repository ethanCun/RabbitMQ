package com.ethan.rabbitmq.rabbitmq.simple;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//Hello world模型
@Component
//queuesToDeclare:表示监听hello队列
//@Queue默认是持久化， 不独占， 不自动删除
@RabbitListener(queuesToDeclare = {@Queue(value = "hello",durable = "true",
declare = "true", autoDelete = "false")})
public class SimpleConsumer {

    @RabbitHandler
    public void simple(String message){

        System.out.println("简单消费： " + message);

    }
}
