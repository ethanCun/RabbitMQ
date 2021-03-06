package com.ethan.rabbitmq.rabbitmq.fanout;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component("fanout-consumer02")
@RabbitListener(bindings = {@QueueBinding(
        value = @Queue, //临时队列
        exchange = @Exchange(value = "logs", type = "fanout") //绑定交换机
)})
public class Consumer02 {

    @RabbitHandler
    public void receive(String msg){

        System.out.println("fanout consumer02: " + msg);
    }
}