package com.ethan.rabbitmq.rabbitmq.topic;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component("topic")
public class Consumer01 {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue, //临时队列
                    exchange = @Exchange( //队列绑定交换机
                            value = "topic",
                            type = "topic"
                    ),
                    key = {"user1.*.*"} //路由key

            )
    )
    public void receive01(Object msg){

        System.out.println("topic consumer01: " + msg);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue, //临时队列
                    exchange = @Exchange( //队列绑定交换机
                            value = "topic",
                            type = "topic"
                    ),
                    key = {"user.#"} //路由key

            )
    )
    public void receive02(Object msg){

        System.out.println("topic consumer02: " + msg);
    }
}
